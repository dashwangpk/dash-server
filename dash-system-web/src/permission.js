import router from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import { getToken } from '@/utils/auth' // get token from cookie
import { generateRouters } from '@/utils/routerUtils' // get token from cookie
import api from '@/api'

NProgress.configure({ showSpinner: false }) // NProgress Configuration

const whiteList = ['/login', '/clientPayInfo', '/paySuccess'] // no redirect whitelist

router.beforeEach(async(to, from, next) => {
  // start progress bar
  NProgress.start()

  // determine whether the user has logged in
  const hasToken = getToken()
  if (hasToken) {
    if (to.path === '/login' || to.path === '/') {
      // if is logged in, redirect to the home page
      next({ path: '/home' })
      NProgress.done()
    } else {
      // determine whether the user has obtained his permission roles through getInfo menuList
      // const hasRoles = store.getters.roles && store.getters.roles.length > 0
      // if (hasRoles) {
      const hasDynamicRoutes = store.getters.hasDynamicRoutes

      if (hasDynamicRoutes) {
        next()
      } else {
        try {
          // get user info
          // note: roles must be a object array! such as: ['admin'] or ,['developer','editor']
          await api.sysUser.info({ data: {}}).then(res => {
            if (res && res.data && res.data.code === 0) {
              store.dispatch('user/setUserInfo', res.data.user)
              return
            } else {
              NProgress.done()
              return
            }
          })

          await api.sysAppMenu.userRoleMenuList({}).then(res => {
            if (res && res.data && res.data.code === 0) {
              sessionStorage.setItem('permissions', JSON.stringify(res.data.permissions || '[]'))
              store.dispatch('user/setMenuList', res.data.userRoleMenuList)
              store.dispatch('permission/setDynamicRoutes', true)
            } else {
              NProgress.done()
              return
            }
          })

          const userMenuList = store.getters.menuList
          // console.log('userMenuList')
          const asyncRoutes = generateRouters(userMenuList)
          // console.log('asyncRoutes')
          router.addRoutes(asyncRoutes)
          // console.log('router.addRoutes(asyncRoutes)')

          next({ ...to, replace: true })
        } catch (error) {
          // remove token and go to login page to re-login
          console.log('// remove token and go to login page to re-login')

          await store.dispatch('user/resetToken')
          Message.error(error || 'Has Error')
          next(`/login?redirect=${to.path}`)
          NProgress.done()
        }
      }
    }
  } else {
    /* has no token*/

    if (whiteList.indexOf(to.path) !== -1) {
      // in the free login whitelist, go directly
      next()
    } else {
      // other pages that do not have permission to access are redirected to the login page.
      next(`/login?redirect=${to.path}`)
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  // finish progress bar
  NProgress.done()
})

