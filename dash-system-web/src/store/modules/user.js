import { getToken, setToken, removeToken } from '@/utils/auth'
import router, { resetRouter } from '@/router'

const state = {
  token: getToken(),
  name: '',
  userName: '',
  avatar: '',
  userId: '',
  introduction: '',
  roles: [],
  menuList: []
}

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_INTRODUCTION: (state, introduction) => {
    state.introduction = introduction
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_USERNAME: (state, userName) => {
    state.userName = userName
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_USERID: (state, userId) => {
    state.userId = userId
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles
  },
  SET_MENULIST: (state, menuList) => {
    state.menuList = menuList
  }
}

const actions = {
  // // user login
  // login({ commit }, userInfo) {
  //   const { username, password } = userInfo
  //   return new Promise((resolve, reject) => {
  //     login({ username: username.trim(), password: password }).then(response => {
  //       const { data } = response
  //       commit('SET_TOKEN', data.token)
  //       setToken(data.token)
  //       resolve()
  //     }).catch(error => {
  //       reject(error)
  //     })

  //   })
  // },
  // user login
  setToken({ commit }, tokenInfo) {
    const { token } = tokenInfo
    commit('SET_TOKEN', token)
    setToken(token)
  },

  // get user info
  setUserInfo({ commit }, userInfo) {
    const { username, name, userId, introduction } = userInfo
    commit('SET_NAME', name)
    commit('SET_USERNAME', username)
    commit('SET_AVATAR', '')
    commit('SET_USERID', userId)
    commit('SET_INTRODUCTION', introduction)
  },

  setUserRoles({ commit }, userRoles) {
    return new Promise((resolve, reject) => {
      const { roles } = userRoles
      commit('SET_ROLES', roles)
      resolve()
    })
  },

  setMenuList({ commit }, menuList) {
    commit('SET_MENULIST', menuList)
  },

  // user logout
  logout({ commit, state }) {
    return new Promise((resolve, reject) => {
      commit('SET_TOKEN', '')
      commit('SET_ROLES', [])
      commit('SET_MENULIST', [])
      removeToken()
      resetRouter()
      resolve()
    })
  },

  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      commit('SET_TOKEN', '')
      commit('SET_ROLES', [])
      removeToken()
      resolve()
    })
  },

  // Dynamically modify permissions
  changeRoles({ commit, dispatch }, role) {
    return new Promise(async resolve => {
      const token = role + '-token'

      commit('SET_TOKEN', token)
      setToken(token)

      const { roles } = await dispatch('getInfo')

      resetRouter()

      // generate accessible routes map based on roles
      const accessRoutes = await dispatch('permission/generateRoutes', roles, { root: true })

      // dynamically add accessible routes
      router.addRoutes(accessRoutes)

      resolve()
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
