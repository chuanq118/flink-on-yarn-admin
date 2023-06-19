
import axios from "axios"
import {ElNotification} from "element-plus"
import type {YarnApplication} from "@/domain"


const axIns = axios.create({
    baseURL: import.meta.env.VITE_BASE_URL,
    timeout: 10000
});

axIns.interceptors.request.use(function (config) {
    // 在发送请求之前做些什么
    const secret = localStorage.getItem('secret')
    config.headers.Authorization = `Bearer ${secret == null ? '' : secret}`
    return config;
}, function (error) {
    // 对请求错误做些什么
    return Promise.reject(error);
});

// 添加响应拦截器
axIns.interceptors.response.use(function (response) {
    // 2xx 范围内的状态码都会触发该函数。
    // 对响应数据做点什么
    return response;
}, function (error) {
    if (error.response) {
        // 请求成功发出且服务器也响应了状态码，但状态代码超出了 2xx 的范围
        if (error.response.status === 401) {

            return
        }
        ElNotification({
            title: 'flink-yarn Server 返回了错误的响应码',
            message: `${error.response.status}, ${error.response.data}`,
            type: 'error',
        })
    } else if (error.request) {
        // 请求已经成功发起，但没有收到响应
        // `error.request` 在浏览器中是 XMLHttpRequest 的实例，
        // 而在node.js中是 http.ClientRequest 的实例
        ElNotification({
            title: 'Server 没有响应请求',
            message: `${JSON.stringify(error.request)}`,
            type: 'error',
        })
    } else {
        // 发送请求时出了点问题
        console.log('Error', error.message);
    }
    return Promise.reject(error);
});

function isSuccessStatusCode(code: number) :boolean{
  return code >= 200 && code < 300;
}

export async function listFlinkYarnApplications(): Promise<null | Array<YarnApplication>> {
  const response = await axIns.get('/api/flink/applications', {
    responseType: "json"
  })
  if (isSuccessStatusCode(response.status)) {
    return response.data
  }
  return null
}

export async function cancelFlinkYarnApplication(timestamp: bigint, id: number): Promise<true | string> {
  const response = await axIns.get(`/api/flink/application/${id}/${timestamp}`)
  if (isSuccessStatusCode(response.status)) {
    return true
  }
  return <string>response.data
}

