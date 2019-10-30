import Vue from 'vue'
import Vuex from 'vuex'
import app from './modules/app'
import errorLog from './modules/errorLog'
import permission from './modules/permission'
import tagsView from './modules/tagsView'
import settings from './modules/settings'
import user from './modules/user'
import getters from './getters'

// const handleStore = store => {
//   if (localStorage.store) store.replaceState(JSON.parse(localStorage.store))  // 初始化store
//   store.subscribe((mutation, state) => {
//       localStorage.setItem("store",  JSON.stringify(state))
//   })
// }

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    app,
    errorLog,
    permission,
    tagsView,
    settings,
    user
  },
  getters
  // plugins: [handleStore]
})

export default store
