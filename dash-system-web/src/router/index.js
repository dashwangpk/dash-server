import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/* Router Modules */
// import componentsRouter from './modules/components'
// import chartsRouter from './modules/charts'
// import tableRouter from './modules/table'
// import nestedRouter from './modules/nested'

/** note: sub-menu only appear when children.length>=1
 *  detail see  https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 **/

/**
* hidden: true                   if `hidden:true` will not show in the sidebar(default is false)
* alwaysShow: true               if set true, will always show the root menu, whatever its child routes length
*                                if not set alwaysShow, only more than one route under the children
*                                it will becomes nested mode, otherwise not show the root menu
* redirect: noredirect           if `redirect:noredirect` will no redirect in the breadcrumb
* name:'router-name'             the name is used by <keep-alive> (must set!!!)
* meta : {
    roles: ['admin','editor']    will control the page roles (you can set multiple roles)
    title: 'title'               the name show in sub-menu and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar
    noCache: true                if true, the page will no be cached(default is false)
    breadcrumb: false            if false, the item will hidden in breadcrumb(default is true)
    affix: true                  if true, the tag will affix in the tags-view
  }
**/

/**
 * 全局路由，无需布局，全屏显示
 */
const globalRoutes = [
  {
    path: '/404',
    component: () => import('@/views/modules/errorPage/404'),
    meta: { title: '404未找到' }
  },
  {
    path: '/login',
    component: () => import('@/views/modules/login/index'),
    meta: { title: '登录' }
  },
  {
    path: '/401',
    component: () => import('@/views/modules/errorPage/401'),
    meta: { title: '401登录失效' }
  },
  // pay-info.vue
  {
    path: '/clientPayInfo',
    component: () => import('@/views/modules/client/pay-info'),
    name: 'clientPayInfo',
    meta: { title: 'clientPayInfo', noCache: true }
  },
  // pay-success
  {
    path: '/paySuccess',
    component: () => import('@/views/modules/client/pay-success'),
    name: 'paySuccess',
    meta: { title: 'paySuccess', noCache: true }
  }

]

/**
 * 全局路由，带布局
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 * */
export const constantRoutes = [
  {
    path: '/',
    component: Layout,
    name: 'main',
    // redirect: 'dashboard',
    children: [
      {
        path: 'home',
        component: () => import('@/views/modules/dashboard/index'),
        name: 'Home',
        meta: { title: '主页', icon: 'home', noCache: false, affix: true }
      },
      // {
      //   path: 'documentation',
      //   component: () => import('@/views/modules/documentation/index'),
      //   name: 'Documentation',
      //   meta: { title: 'documentation', icon: 'documentation', affix: true }
      // },
      {
        path: 'page404',
        component: () => import('@/views/modules/errorPage/404'),
        name: 'Page404',
        meta: { title: 'page404', noCache: true }
      }
    ]
  }

]

/**
 * asyncRoutes
 * the routes that need to be dynamically loaded based on user roles
*/
export const asyncRoutes = []

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: globalRoutes.concat(constantRoutes) // 显示全局路由 包含全屏路由与布局路由
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
