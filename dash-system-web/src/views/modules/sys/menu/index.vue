<template>
  <div class="app-container">
    <el-button type="primary" @click="handleAdd">
      添加
    </el-button>

    <el-table
      v-loading="dataListLoading"
      row-key="menuId"
      :data="sysMenuList"
      style="width: 100%;"
      border
    >
      <el-table-column
        header-align="center"
        align="left"
        :show-overflow-tooltip="true"
        label="菜单名称"
        width="220"
      >
        <template slot-scope="scope">
          {{ scope.row.name }}
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        :show-overflow-tooltip="true"
        label="图标"
        width="80"
      >
        <template slot-scope="scope">
          <svg-icon :icon-class="scope.row.icon || ''" class-name="disabled" />
        </template>
      </el-table-column>
      <el-table-column
        width="80"
        header-align="center"
        align="center"
        label="类型"
      >
        <template slot-scope="scope">
          <el-tag v-if="scope.row.type === 0" type="info">目录</el-tag>
          <el-tag v-else-if="scope.row.type === 1" type="success">菜单</el-tag>
          <el-tag v-else-if="scope.row.type === 2" type="info">按钮</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        width="80"
        align="center"
        label="排序号"
      >
        <template slot-scope="scope">
          {{ scope.row.orderNum }}
        </template>
      </el-table-column>
      <el-table-column
        min-width="120"
        align="center"
        :show-overflow-tooltip="true"
        label="路由"
      >
        <template slot-scope="scope">
          {{ scope.row.url }}
        </template>
      </el-table-column>
      <el-table-column
        min-width="120"
        align="center"
        :show-overflow-tooltip="true"
        label="组件名"
      >
        <template slot-scope="scope">
          {{ scope.row.componentName }}
        </template>
      </el-table-column>
      <el-table-column
        width="80"
        align="center"
        :show-overflow-tooltip="true"
        label="前端缓存"
      >
        <template slot-scope="scope">
          <el-tag v-if="scope.row.noCache === 0" type="success">开启</el-tag>
          <el-tag v-else-if="scope.row.noCache === 1" type="warning">关闭</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="200"
        label="操作"
      >
        <template slot-scope="scope">
          <el-button plain type="primary" size="small" @click="handleUpdate(scope)">修改</el-button>
          <el-button plain type="danger" size="small" @click="deleteHandle(scope)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getSysMenuList" />
    <el-dialog
      title="删除菜单"
      :visible.sync="deleteVisible"
      width="30%"
    >
      <span>“{{ this.selectMenu.name }}”菜单删除后无法找回！</span>
      <span slot="footer" class="dialog-footer">
        <el-button type="danger" @click="deleteVisible = false">取 消</el-button>
        <el-button type="primary" @click="deleteRoleYes">确 定</el-button>
      </span>
    </el-dialog>
  </div>

</template>

<script>
import AddOrUpdate from './menu-add-or-update'
import api from '@/api'

export default {
  name: 'MenuManager',
  components: {
    AddOrUpdate
  },
  data() {
    return {
      sysMenuList: [],
      dataListLoading: false,
      addOrUpdateVisible: false,
      deleteVisible: false,
      selectMenu: { name: null, menuId: null }
    }
  },
  computed: {
  },
  created() {
    this.getSysMenuList()
  },
  methods: {
    async getSysMenuList() {
      this.dataListLoading = true
      api.sysAppMenu.tree().then(res => {
        this.dataListLoading = false
        if (res && res.data && res.data.code === 0) {
          this.sysMenuList = res.data.menuTree
        } else {
          this.$message({ showClose: true, message: res.data.msg, type: 'error' })
        }
      })
    },
    handleAdd() {
      this.addOrUpdateVisible = true
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init()
      })
    },
    handleUpdate(scope) {
      this.addOrUpdateVisible = true
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(scope.row.menuId)
      })
    },
    deleteRoleYes() {
      const fectData = this.selectMenu
      api.sysAppMenu.delete({ data: fectData }).then(res => {
        this.deleteVisible = false
        if (res && res.data && res.data.code === 0) {
          this.getSysMenuList()
          this.$message({ message: '删除角色成功', type: 'success' })
        } else {
          this.$message({ showClose: true, message: res.data.msg, type: 'error' })
        }
      })
    },
    deleteHandle(scope) {
      this.selectMenu = scope.row
      this.deleteVisible = true
    }

  }
}
</script>

<style lang="scss" scoped>
.app-container {
  .roles-table {
    margin-top: 30px;
  }
  .permission-tree {
    margin-bottom: 30px;
  }
}
</style>
