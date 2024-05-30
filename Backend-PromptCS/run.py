# coding=utf-8

from __future__ import absolute_import
import os
import torch
import json
import random
import logging
import argparse
import numpy as np
from io import open
import time
from model import PromptCS
import redis
from kafka import KafkaConsumer
from kafka import TopicPartition

logging.basicConfig(format = '%(asctime)s - %(levelname)s - %(name)s -   %(message)s',
                    datefmt = '%m/%d/%Y %H:%M:%S',
                    level = logging.INFO)
logger = logging.getLogger(__name__)


# os.environ['CUDA_VISIBLE_DEVICES'] = '0'
import setproctitle
proc_title = "yyd can share but dont kill"
setproctitle.setproctitle(proc_title)

def set_seed(seed=42):
    random.seed(seed)
    os.environ['PYHTONHASHSEED'] = str(seed)
    np.random.seed(seed)
    torch.manual_seed(seed)
    torch.cuda.manual_seed(seed)
    torch.backends.cudnn.deterministic = True


def arguments():
    parser = argparse.ArgumentParser()

    # Connection related
    parser.add_argument("--kafka_servers", default='["193.112.94.119:9092"]', type=str,
                        help="Connecting kafka servers")
    parser.add_argument("--kafka_topic", default='test', type=str,
                        help="Topic to consume")
    parser.add_argument("--redis_host", default='193.112.94.119', type=str,
                        help="Host used to connect redis server")
    parser.add_argument("--redis_port", default='6379', type=str,
                        help="Port used to connect redis server")
    parser.add_argument("--redis_pwd", default='yyd123456', type=str,
                        help="Password used to connect redis server")

    # Model related
    parser.add_argument("--model_name_or_path", default='bigcode/starcoderbase-1b', type=str,
                        help="Path to pre-trained model")
    parser.add_argument("--output_dir", default='./saved_models', type=str,
                        help="The output directory where the model predictions and checkpoints will be written.")
    parser.add_argument("--load_model_path", default='./saved_models/checkpoint-best-bleu/pytorch_model.bin', type=str,
                        help="Path to trained model: Should contain the .bin files")
    parser.add_argument("--mode", default='PromptCS', type=str,
                        choices=["PromptCS", "finetune"],
                        help="Operational mode.")
    parser.add_argument("--template", type=str, default="[0, 100]",
                        help="The concatenation method of pseudo tokens and code snippet.")
    parser.add_argument("--prompt_encoder_type", default='lstm', type=str,
                        choices=["lstm", "transformer"],
                        help="Architecture of prompt encoder.")
    parser.add_argument("--max_code_length", default=300, type=int,
                        help="The maximum total target sequence length after tokenization. Sequences longer "
                             "than this will be truncated, sequences shorter will be padded.")
    parser.add_argument("--max_target_length", default=30, type=int,
                        help="The maximum total target sequence length after tokenization. Sequences longer "
                             "than this will be truncated, sequences shorter will be padded.")
    parser.add_argument("--batch_size", default=16, type=int,
                        help="Batch size.")


    parser.add_argument("--no_cuda", action='store_true',
                        help="Avoid using CUDA when available")
    parser.add_argument('--seed', type=int, default=2023,
                        help="random seed for initialization")

    # print arguments
    args = parser.parse_args()

    return args


def prepare(args):
    logger.info(args)

    # Setup CUDA, GPU
    device = torch.device("cuda" if torch.cuda.is_available() and not args.no_cuda else "cpu")
    args.n_gpu = torch.cuda.device_count()

    logger.warning("device: %s, n_gpu: %s", device, args.n_gpu)

    assert args.n_gpu == 1, ("This version of PromptCS can only run on a single GPU \n"
                             "Please set the GPU you want to use and run again. For example: CUDA_VISIBLE_DEVICES=0 python run.py \n"
                             "If you need multi-GPU training, please check out the DeepSpeed version of PromptCS")

    logger.warning("model: %s, prompt_encoder: %s, len: %s",
                   args.model_name_or_path, args.prompt_encoder_type, args.template)

    args.device = device
    args.template = eval(args.template)
    set_seed(args.seed)

    model = PromptCS(args=args, device=device, template=args.template)
    logger.info("reload model from {}".format(args.load_model_path))
    model.load_state_dict(torch.load(args.load_model_path))

    model.to(device)

    return model


def get_redis_client(args):
    return redis.Redis(host=args.redis_host, port=args.redis_port, password=args.redis_pwd, db=0)


def get_kafka_consumer(args):
    bootstrap_servers = eval(args.kafka_servers)
    consumer = KafkaConsumer(
        bootstrap_servers=bootstrap_servers,
        consumer_timeout_ms=1000,
        group_id="test-consumer-group",
        enable_auto_commit=False
    )

    consumer.assign([TopicPartition(args.kafka_topic, 0)])

    return consumer


def generate_comment(model, codes):
    preds = model(x_hs=codes)

    return preds


def info_handler(info, pattern):
    left = info.find(pattern)
    right = left + len(pattern)

    key = info[:left]
    value = info[right:]

    return key, value

def deploy():
    args = arguments()
    model = prepare(args)
    kafka_consumer = get_kafka_consumer(args)
    redis_client = get_redis_client(args)

    msgs = kafka_consumer.poll(timeout_ms=1000)
    kafka_consumer.commit()

    while True:
        try:
            msgs = kafka_consumer.poll(max_records=args.batch_size, timeout_ms=100)
            if not msgs:
                continue

            keys, codes = [], []
            for tp, msgs_list in msgs.items():
                for msg in msgs_list:
                    info = str(msg.value, encoding="utf-8")
                    key, code = info_handler(info, "<SEP>")
                    keys.append(key)
                    codes.append(code)

            preds = generate_comment(model, codes)

            for i in range(len(preds)):
                redis_client.set(keys[i], preds[i], nx=True, ex=600)
                print(keys[i])

            kafka_consumer.commit()
        except ValueError as e:
            print("捕获到异常:", e)
            time.sleep(5)


if __name__ == "__main__":
    deploy()







