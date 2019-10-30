import { asyncRoutes, constantRoutes } from '@/router'

/**
 * 通过meta.role判断是否与当前用户权限匹配
 * @param roles
 * @param route
 */
function hasPermission(roles, route) {
  if (route.meta && route.meta.roles) {
    return roles.some(role => route.meta.roles.includes(role))
  } else {
    return true
  }
}

/**
 * 递归过滤异步路由表，返回符合用户角色权限的路由表
 * @param routes asyncRoutes
 * @param roles
 */
export function filterAsyncRoutes(routes, roles) {
  const res = []

  routes.forEach(route => {
    const tmp = { ...route }
    if (hasPermission(roles, tmp)) {
      if (tmp.children) {
        tmp.children = filterAsyncRoutes(tmp.children, roles)
      }
      res.push(tmp)
    }
  })

  return res
}

const state = {
  routes: [],
  addRoutes: [],
  hasDynamicRoutes:false
}

const mutations = {
  SET_ROUTES: (state, routes) => {
    state.addRoutes = routes
    state.routes = constantRoutes.concat(routes)
  },
  SET_HASDYNAMICROUTES: (state, hasDynamicRoutes) => {
    state.hasDynamicRoutes = hasDynamicRoutes
  }
}

const actions = {
  generateRoutes({ commit }, roles) {
    return new Promise(resolve => {
      let accessedRoutes
      accessedRoutes = asyncRoutes
      // if (roles.includes('admin')) {
      //   accessedRoutes = asyncRoutes
      // } else {
      //   accessedRoutes = filterAsyncRoutes(asyncRoutes, roles)
      // }
      // commit('SET_ROUTES', accessedRoutes)
      resolve(accessedRoutes)
    })
  },
  sysAsyncRoutes({ commit },){
    return new Promise(resolve => {
      commit('SET_ROUTES', asyncRoutes)
      resolve(asyncRoutes)
    }) 
  },
  asyncRoutes({ commit }, asyncRoutes) {
    return new Promise(resolve => {
      commit('SET_ROUTES', asyncRoutes)
      resolve(asyncRoutes)
    })
  },
  setDynamicRoutes({ commit }, hasDynamicRoutes) {
    return new Promise(resolve => {
      commit('SET_HASDYNAMICROUTES', hasDynamicRoutes)
      resolve(asyncRoutes)
    })
  }

}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
