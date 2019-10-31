<template>
  <div class="app-container">
    <el-button type="primary" @click="getSysRoleList">
      查询
    </el-button>
    <el-button type="primary" @click="handleAddRole">
      添加
    </el-button>

    <el-table
      v-loading="dataListLoading"
      :data="sysRoleList"
      style="width: 100%;"
      border
    >
      <el-table-column
        header-align="center"
        align="left"
        :show-overflow-tooltip="true"
        label="角色名称"
        width="180"
      >
        <template slot-scope="scope">
          {{ scope.row.roleName }}
        </template>
      </el-table-column>
      <el-table-column
        header-align="center"
        align="center"
        label="启用状态"
        width="80"
      >
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 1 " type="success">启用</el-tag>
          <el-tag v-else type="danger">禁用</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        header-align="center"
        align="left"
        :show-overflow-tooltip="true"
        label="描述"
      >
        <template slot-scope="scope">
          {{ scope.row.remark }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="创建时间" width="180">
        <template slot-scope="scope">
          {{ scope.row.createTime }}
        </template>
      </el-table-column>
      <el-table-column
        header-align="center"
        align="center"
        label="操作"
        width="320"
      >
        <template slot-scope="scope">
          <el-button type="primary" plain size="small" @click="handleRoleMenus(scope)">
            菜单授权
          </el-button>
          <el-button type="primary" plain size="small" @click="handleUpdate(scope)">
            修改
          </el-button>
          <el-button type="danger" plain size="small" @click="handleDelete(scope)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      :current-page="pageIndex"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="pageSize"
      :total="totalPage"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="sizeChangeHandle"
      @current-change="currentChangeHandle"
    />
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getSysRoleList" />
    <role-menus v-if="roleMenusVisible" ref="roleMenus" @refreshDataList="getSysRoleList" />
    <el-dialog
      title="删除角色"
      :visible.sync="deleteVisible"
      width="30%"
    >
      <span>“{{ this.selectRole.roleName }}”角色删除后无法找回！</span>
      <span slot="footer" class="dialog-footer">
        <el-button type="danger" @click="deleteVisible = false">取 消</el-button>
        <el-button type="primary" @click="deleteRoleYes">确 定</el-button>
      </span>
    </el-dialog>
  </div>

</template>

<script>
import AddOrUpdate from './role-add-or-update'
import RoleMenus from './role-menus'
import api from '@/api'

export default {
  name: 'RoleManager',
  components: {
    AddOrUpdate,
    RoleMenus
  },
  data() {
    return {
      selectRole: {
        username: null,
        userId: null
      },
      sysRoleList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: [],
      addOrUpdateVisible: false,
      roleMenusVisible: false,
      deleteVisible: false
    }
  },
  computed: {
  },
  created() {
    this.getSysRoleList()
  },
  methods: {
    async getSysRoleList() {
      this.dataListLoading = true
      const fetchData = {
        'params': {
          'page': '' + this.pageIndex,
          'limit': '' + this.pageSize
        }
      }

      api.sysRole.list({ data: fetchData }).then(res => {
        this.dataListLoading = false
        if (res && res.data && res.data.code === 0) {
          this.sysRoleList = res.data.page.list
          this.totalPage = res.data.page.totalCount
        } else {
          this.$message({ showClose: true, message: res.data.msg, type: 'error' })
        }
      })
    },
    currentChangeHandle(val) {
      this.pageIndex = val
      this.getSysRoleList()
    },
    sizeChangeHandle(val) {
      this.pageSize = val
      this.pageIndex = 1
      this.getSysRoleList()
    },
    handleAddRole() {
      this.addOrUpdateVisible = true
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init()
      })
    },
    handleRoleMenus(scope) {
      this.roleMenusVisible = true
      this.$nextTick(() => {
        this.$refs.roleMenus.init(scope.row.roleId)
      })
    },
    handleUpdate(scope) {
      this.addOrUpdateVisible = true
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(scope.row.roleId)
      })
    },
    deleteRoleYes() {
      const fetchData = { roleId: this.selectUser.roleId }
      api.sysRole.delete({ data: fetchData }).then(res => {
        this.deleteVisible = false
        if (res && res.data && res.data.code === 0) {
          this.getSysRoleList()
          this.$message({ message: '删除角色成功', type: 'success' })
        } else {
          this.$message({ showClose: true, message: res.data.msg, type: 'error' })
        }
      })
    },
    handleDelete(scope) {
      this.selectUser = scope.row
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
