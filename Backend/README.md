#  服务端
端口20187


## 启动
>使用Maven工具对项目进行打包，获取 summarization-0.0.1-SNAPSHOT.jar 文件
```
java -jar summarization-0.0.1-SNAPSHOT.jar
```

## 核心类图
![img.png](fig%2Fimg.png)


## 一些细节
一、 配置端口号、MySQL、Redis、Kafka：src\main\resources\application.yml

二、 用户在使用插件前需要先使用激活码来激活插件，否则请求会被拦截
- com/summarization/interceptor/ActivationCodeInterceptor.java
- com/summarization/config/WebMvcConfiguration.java