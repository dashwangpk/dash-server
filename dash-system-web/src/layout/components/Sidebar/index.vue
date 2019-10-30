<template>
  <div :class="{'has-logo':showLogo}">
    <logo v-if="showLogo" :collapse="isCollapse" />
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <el-menu
        :default-active="$route.path"
        :collapse="isCollapse"
        :background-color="variables.menuBg"
        :text-color="variables.menuText"
        :active-text-color="variables.menuActiveText"
        :collapse-transition="false"
        mode="vertical"
      >
        <app-link to="home">
          <el-menu-item index="/home" :class="{'submenu-title-noDropdown':true}">
            <item icon="home" title="主页" />
          </el-menu-item>
        </app-link>
        <!-- <sidebar-item v-for="route in permission_routes" :key="route.path" :item="route" :base-path="route.path" /> -->
        <sidebar-item v-for="menuItem in menuList" :key="menuItem.menuId" :item="menuItem" />
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Logo from './Logo'
import SidebarItem from './SidebarItem'
import variables from '@/styles/variables.scss'
import Item from './Item'
import AppLink from './Link'

export default {
  components: { Item, AppLink, SidebarItem, Logo },
  computed: {
    ...mapGetters([
      'permission_routes',
      'menuList',
      'sidebar'
    ]),
    showLogo() {
      return this.$store.state.settings.sidebarLogo
    },
    variables() {
      return variables
    },
    isCollapse() {
      return !this.sidebar.opened
    }
  }
}
</script>
