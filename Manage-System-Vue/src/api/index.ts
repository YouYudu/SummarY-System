import request from '../utils/request';

// const rootUrl:string = 'http://127.0.0.1:20187/admin/'
const rootUrl:string = 'http://193.112.94.119:20187/admin/'

export const fetchData = () => {
    return request({
        url: './table.json',
        method: 'get'
    });
};


export const fetchActivation = (query: any) => {
    return request({
        url: rootUrl+'fetchActivation',
        method: 'post',
        params: query
    },);
};


export const updateActivation = (params: any) => {
    return request({
        url: rootUrl+'updateActivation',
        method: 'post',
        data: {...params}
    },);
};


export const deleteActivation = (params: any) => {
    return request({
        url: rootUrl+'deleteActivation',
        method: 'post',
        data: {...params}
    },);
};


export const addActivation = (params: any) => {
    return request({
        url: rootUrl+'addActivation',
        method: 'get',
        params: {...params}
    },);
};


export const fetchPluginFeedback = (query: any) => {
    return request({
        url: rootUrl+'fetchPluginFeedback',
        method: 'get',
        params: query
    },);
};


export const fetchCommentFeedback = (query: any) => {
    return request({
        url: rootUrl+'fetchCommentFeedback',
        method: 'get',
        params: query
    },);
};

export const adminLogin = (query: any) => {
    return request({
        url: rootUrl+'login',
        method: 'post',
        params: query
    },);
};

export const adminEdit = (query: any) => {
    return request({
        url: rootUrl+'adminEdit',
        method: 'post',
        params: query
    },);
};