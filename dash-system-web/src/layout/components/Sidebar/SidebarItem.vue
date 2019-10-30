<template>
  <div v-if="!item.hidden" class="menu-wrapper">

    <el-submenu v-if="item.type == 0" ref="subMenu" :index="'index'+item.menuId" popper-append-to-body>
      <template slot="title">
        <item :icon="item.icon || '' " :title="generateTitle(item.name)" />
      </template>
      <sidebar-item
        v-for="childItem in item.children"
        :key="childItem.menuId"
        :is-nest="true"
        :item="childItem"
        class="nest-menu"
      />
    </el-submenu>

    <template v-else>
      <app-link :to="resolvePath(item.url)">
        <el-menu-item :index="'/' + resolvePath(item.url)" :class="{'submenu-title-noDropdown':!isNest}">
          <item :icon="item.icon || '' " :title="generateTitle(item.name)" />
        </el-menu-item>
      </app-link>
    </template>

  </div>
</template>

<script>
import { generateTitle } from '@/utils/i18n'
import { isExternal } from '@/utils/validate'
import Item from './Item'
import AppLink from './Link'
import FixiOSBug from './FixiOSBug'

export default {
  name: 'SidebarItem',
  components: { Item, AppLink },
  mixins: [FixiOSBug],
  props: {
    // route object
    item: {
      type: Object,
      required: true
    },
    isNest: {
      type: Boolean,
      default: false
    },
    basePath: {
      type: String,
      default: ''
    }
  },
  data() {
    this.onlyOneChild = null
    return {}
  },
  methods: {
    hasOneShowingChild(children = [], parent) {
      const showingChildren = children.filter(item => {
        if (item.hidden) {
          return false
        } else {
          // Temp set(will be used if only has one showing child)
          this.onlyOneChild = item
          return true
        }
      })

      // When there is only one child router, the child router is displayed by default
      if (showingChildren.length === 1) {
        return true
      }

      // Show parent if there are no child router to display
      if (showingChildren.length === 0) {
        this.onlyOneChild = { ... parent, path: '', noShowingChildren: true }
        return true
      }

      return false
    },
    resolvePath(routePath) {
      if (isExternal(routePath)) {
        return routePath
      }
      return routePath.replace(/\//g, '-')
    },
    // resolvePath(routePath) {
    //   if (isExternal(routePath)) {
    //     return routePath
    //   }
    //   return path.resolve(this.basePath, routePath)
    // },

    generateTitle
  }
}
</script>
