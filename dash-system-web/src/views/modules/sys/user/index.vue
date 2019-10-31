<template>
  <div class="app-container">
    <el-button v-if="isAuth('sys:user:list')" type="primary" @click="getSysUserList">
      查询
    </el-button>
    <el-button v-if="isAuth('sys:user:add')" type="primary" @click="handleAddUser">
      添加
    </el-button>

    <el-table
      v-loading="dataListLoading"
      :data="sysUserList"
      style="width: 100%;"
      border
    >
      <el-table-column
        header-align="center"
        align="left"
        :show-overflow-tooltip="true"
        label="用户名"
        min-width="100"
      >
        <template slot-scope="scope">
          {{ scope.row.username }}
        </template>
      </el-table-column>
      <el-table-column
        header-align="center"
        align="left"
        :show-overflow-tooltip="true"
        label="姓名"
        min-width="100"
      >
        <template slot-scope="scope">
          {{ scope.row.name }}
        </template>
      </el-table-column>
      <el-table-column
        align="left"
        header-align="center"
        label="手机号"
        min-width="100"
      >
        <template slot-scope="scope">
          {{ scope.row.mobile }}
        </template>
      </el-table-column>
      <el-table-column
        header-align="center"
        align="left"
        :show-overflow-tooltip="true"
        label="角色"
        min-width="120"
      >
        <template slot-scope="scope">
          {{ scope.row.roleNames }}
        </template>
      </el-table-column>
      <el-table-column
        header-align="center"
        align="center"
        label="启用状态"
        width="80"
      >
        <template slot-scope="scope">
          {{ scope.row.status }}
        </template>
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 1 " type="success">启用</el-tag>
          <el-tag v-else type="danger">禁用</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        header-align="center"
        align="center"
        label="创建时间"
        width="180"
      >
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
          <el-button v-if="isAuth('sys:user:resetPassword')" type="primary" plain size="small" @click="handleResetPassword(scope)">
            重置密码
          </el-button>
          <el-button v-if="isAuth('sys:user:role')" type="primary" plain size="small" @click="handleRoles(scope)">
            角色
          </el-button>
          <el-button v-if="isAuth('sys:user:update')" type="primary" plain size="small" @click="handleUpdate(scope)">
            修改
          </el-button>
          <el-button v-if="isAuth('sys:user:delete')" type="danger" plain size="small" @click="handleDelete(scope)">
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
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getSysUserList" />
    <user-roles v-if="userRolesVisible" ref="userRoles" @refreshDataList="getSysUserList" />
    <el-dialog
      title="重置密码"
      :visible.sync="resetPasswordVisible"
      width="30%"
    >
      <span>重置后密码为“000000”！</span>
      <span slot="footer" class="dialog-footer">
        <el-button type="danger" @click="resetPasswordVisible = false">取 消</el-button>
        <el-button type="primary" @click="resetPasswordYes">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog
      title="删除用户"
      :visible.sync="deleteVisible"
      width="30%"
    >
      <span>“{{ this.selectUser.username }}”用户删除后无法找回！</span>
      <span slot="footer" class="dialog-footer">
        <el-button type="danger" @click="deleteVisible = false">取 消</el-button>
        <el-button type="primary" @click="deleteUserYes">确 定</el-button>
      </span>
    </el-dialog>
  </div>

</template>

<script>
import AddOrUpdate from './user-add-or-update'
import UserRoles from './user-roles'
import api from '@/api'

export default {
  name: 'UserManager',
  components: {
    AddOrUpdate,
    UserRoles
  },
  data() {
    return {
      selectUser: {
        username: null,
        userId: null
      },
      sysUserList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: [],
      addOrUpdateVisible: false,
      resetPasswordVisible: false,
      deleteVisible: false,
      userRolesVisible: false
    }
  },
  computed: {
  },
  created() {
    this.getSysUserList()
  },
  methods: {
    async getSysUserList() {
      this.dataListLoading = true
      const fetchData = {
        'params': {
          'page': '' + this.pageIndex,
          'limit': '' + this.pageSize
        }
      }
      api.sysUser.list({ data: fetchData }).then(res => {
        this.dataListLoading = false
        if (res && res.data && res.data.code === 0) {
          this.sysUserList = res.data.page.list
          this.totalPage = res.data.page.totalCount
        } else {
          this.$message({ showClose: true, message: res.data.msg, type: 'error' })
        }
      })
    },
    currentChangeHandle(val) {
      this.pageIndex = val
      this.getSysUserList()
    },
    sizeChangeHandle(val) {
      this.pageSize = val
      this.pageIndex = 1
      this.getSysUserList()
    },
    handleAddUser() {
      this.addOrUpdateVisible = true
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init()
      })
    },
    handleUpdate(scope) {
      this.addOrUpdateVisible = true
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(scope.row.userId)
      })
    },
    handleRoles(scope) {
      this.userRolesVisible = true
      this.$nextTick(() => {
        this.$refs.userRoles.init(scope.row)
      })
    },
    resetPasswordYes() {
      const fetchData = { userId: this.selectUser.userId }
      api.sysUser.resetPassword({ data: fetchData }).then(res => {
        this.resetPasswordVisible = false
        if (res && res.data && res.data.code === 0) {
          this.$message({ message: '重置密码成功', type: 'success' })
        } else {
          this.$message({ showClose: true, message: res.data.msg, type: 'error' })
        }
      })
    },
    deleteUserYes() {
      const fetchData = { userId: this.selectUser.userId }
      api.sysUser.delete({ data: fetchData }).then(res => {
        this.deleteVisible = false
        this.getSysUserList()
        if (res && res.data && res.data.code === 0) {
          this.$message({ message: '删除用户成功', type: 'success' })
        } else {
          this.$message({ showClose: true, message: res.data.msg, type: 'error' })
        }
      })
    },
    handleResetPassword(scope) {
      this.selectUser = scope.row
      this.resetPasswordVisible = true
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
