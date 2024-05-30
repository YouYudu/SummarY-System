# 后台管理页面

基于 Vue3 + pinia + Element Plus 的后台管理页面。

主要负责激活码管理、用户反馈查询。

使用前，管理员需要先登录。



## 安装步骤
> 因为使用vite3，node版本需要 14.18+

```
git clone https://github.com/lin-xin/vue-manage-system.git      // 把模板下载到本地
cd vue-manage-system    // 进入模板目录
npm install         // 安装项目依赖，等待安装完成之后，安装失败可用 cnpm 或 yarn

// 运行
npm run dev
```


## 一些细节
发送请求的接口在 `src/api/index.ts`