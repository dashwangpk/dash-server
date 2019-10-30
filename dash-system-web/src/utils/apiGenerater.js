import axios from 'axios'
import qs from 'qs'
import _ from 'lodash'
import { getToken, removeToken } from '@/utils/auth'
import router from '@/router'

const http = axios.create({
  timeout: 1000 * 30,
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json; charset=utf-8'
  }
})

/**
 * 请求拦截
 */
http.interceptors.request.use(config => {
  config.headers['token'] = getToken() // 请求头带上token
  return config
}, error => {
  return Promise.reject(error)
})

/**
 * 响应拦截
 */
http.interceptors.response.use(response => {
  if (response.data && response.data.code === 401) { // 401, token失效
    removeToken()
    router.push({ path: '/login' })
  }
  return response
}, error => {
  return Promise.reject(error)
})

/**
 * 请求地址处理
 * @param {*} actionName action方法名称
 */
http.adornUrl = (actionName) => {
  // 非生产环境 && 开启代理, 接口前缀统一使用[/proxyApi/]前缀做代理拦截!
  return process.env.VUE_APP_BASE_API + actionName
}

/**
 * get请求参数处理
 * @param {*} params 参数对象
 * @param {*} openDefultParams 是否开启默认参数?
 */
http.adornParams = (params = {}, openDefultParams = true) => {
  var defaults = {
    't': new Date().getTime()
  }
  return openDefultParams ? _.merge(defaults, params) : params
}

/**
 * post请求数据处理
 * @param {*} data 数据对象
 * @param {*} openDefultdata 是否开启默认数据?
 * @param {*} contentType 数据格式
 *  json: 'application/json; charset=utf-8'
 *  form: 'application/x-www-form-urlencoded; charset=utf-8'
 */
http.adornData = (data = {}, openDefultdata = true, contentType = 'json') => {
  var defaults = {
    't': new Date().getTime()
  }
  data = openDefultdata ? _.merge(defaults, data) : data
  return contentType === 'json' ? JSON.stringify(data) : qs.stringify(data)
}

// export default http

const getMethodType = function(api) {
  if (api.method && ['GET', 'POST', 'PUT', 'DELETE'].includes(api.method)) {
    return api.method
  } else {
    return 'GET'
  }
}

const getPathParams = function(url) {
  const regex = /{([^}]+)}/g
  const pathParams = []
  let currentMatch
  while ((currentMatch = regex.exec(url))) {
    pathParams.push(currentMatch[1])
  }
  return pathParams
}

const prepareAjaxMethod = (api) => (options = {}) => {
  const method = getMethodType(api)
  const pathParamNames = getPathParams(api.url)
  const pathParamUrl = pathParamNames.reduce((url, pathParamName) => {
    const paramVal = options.pathParams[pathParamName]
    if (paramVal) {
      return url.replace(`{${pathParamName}}`, paramVal)
    } else {
      throw new Error(`${url} has invalid path params : [${pathParamName}].`)
    }
  }, api.url)

  let newUrl = http.adornUrl(pathParamUrl)
  if (!_.isEmpty(options.queryParams)) {
    newUrl = `${newUrl}?${qs.stringify(options.queryParams)}`
  }

  // config:其他参数配置配置
  if (!api.config && (api.config = {})) {
    options.config = api.config
  }

  options = _.merge({
    url: newUrl,
    method
  }, options)

  Reflect.deleteProperty(options, 'pathParams')
  Reflect.deleteProperty(options, 'queryParams')

  return http(options)
}

const generate = function(apiObj) {
  _.forIn(apiObj, (v, k) => {
    const newVal = {}
    v.forEach((api) => {
      newVal[api.name] = prepareAjaxMethod(api)
    })
    apiObj[k] = newVal
  })
  return apiObj
}

export default generate

