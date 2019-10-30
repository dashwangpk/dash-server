
import Layout from '@/layout'
import { isExternal } from '@/utils/validate'

const tempAsyncRoutes = {
  path: '/',
  component: Layout,
  name: 'main-dynamic',
  children: []
}

function genRouters(children = [], asyncRoutes = []) {
  for (let index = 0; index < children.length; index++) {
    const menuItem = children[index]
    if (menuItem.type === 0) {
      if (menuItem.children && menuItem.children.length > 0) {
        genRouters(menuItem.children, asyncRoutes)
      }
    } else {
      if (!isExternal(menuItem.url)) {
        const menu = {}
        menuItem.url = menuItem.url.replace(/^\//, '')
        menu.path = menuItem.url.replace(/\//g, '-')
        menuItem.path = menu.path
        menu.name = menuItem.componentName
        if (!menu.name) {
          menu.name = menuItem.name
        }
        menu.meta = {}
        menu.meta.title = menuItem.name
        menu.meta.icon = menuItem.icon
        menu.meta.noCache = (menuItem.noCache === 1)
        try {
          menu.component = require('@/views/modules/' + menuItem.url + '.vue').default
        } catch (e) {
          menu.component = null
        }
        asyncRoutes.push(menu)
      }
    }
  }
}

export function generateRouters(userMenuList) {
  var asyncRoutes = []
  for (let index = 0; index < userMenuList.length; index++) {
    const menuItem = userMenuList[index]
    if (menuItem.type === 0) {
      if (menuItem.children && menuItem.children.length > 0) {
        genRouters(menuItem.children, asyncRoutes)
      }
    } else {
      if (!isExternal(menuItem.url)) {
        const menu = { }
        menuItem.url = menuItem.url.replace(/^\//, '')
        menu.path = menuItem.url.replace(/\//g, '-')
        menuItem.path = menu.path
        menu.name = menuItem.componentName
        if (!menu.name) {
          menu.name = menuItem.name
        }
        menu.meta = {}
        menu.meta.title = menuItem.name
        menu.meta.icon = menuItem.icon
        menu.meta.noCache = (menuItem.noCache === 1)
        try {
          menu.component = require('@/views/modules/' + menuItem.url + '.vue').default
        } catch (e) {
          menu.component = null
        }
        asyncRoutes.push(menu)
      }
    }
  }

  tempAsyncRoutes.children = asyncRoutes
  return [tempAsyncRoutes, { path: '*', redirect: '/404', hidden: true }]
}

