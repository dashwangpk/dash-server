<template>
  <el-dialog
    title="角色"
    :before-close="onClose"
    :close-on-click-modal="false"
    :visible.sync="visible"
  >
    <div style="height:300px;margin-left: 8%;margin-right: 8%;">
      <el-transfer
        v-model="userRoles"
        :props="roleProps"
        :titles="['未分配角色', '已分配角色']"
        :format="{
          noChecked: '${total}',
          hasChecked: '${checked}/${total}' }"
        :data="roleData"
        @change="ruleChange"
      />
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button type="primary" @click="onClose">关闭</el-button>
    </span>
  </el-dialog>
</template>

<script>
import api from '@/api'

export default {
  components: {
  },
  data() {
    return {
      visible: false,
      roleId: null,
      roleProps: {
        key: 'roleId',
        label: 'roleName'
      },
      roleData: [],
      userRoles: [],
      user: {
        username: null,
        userId: null
      }
    }
  },
  methods: {
    init(user) {
      this.user = user
      this.roleData = []
      this.userRoles = []
      this.visible = true
      this.$nextTick(() => {
        const fetchData = { userId: this.user.userId }
        api.sysRole.enableList({}).then(res => {
          if (res && res.data && res.data.code === 0) {
            this.roleData = res.data.roles
            api.sysRole.userRoleIds({ data: fetchData }).then(res => {
              if (res && res.data && res.data.code === 0) {
                this.userRoles = res.data.userRoleIds
              } else {
                this.$message({ showClose: true, message: res.data.msg, type: 'error' })
              }
            })
          } else {
            this.$message({ showClose: true, message: res.data.msg, type: 'error' })
          }
        })
      })
    },
    ruleChange(roleIds, direction) {
      const ungrantRoleIds = []

      this.roleData.forEach(function(value, i) {
        ungrantRoleIds.push(value.roleId)
      })

      const fetchData = {
        userId: this.user.userId,
        grantRoleIds: this.userRoles
      }

      api.sysRole.grantUserRoles({ data: fetchData }).then(res => {
        if (!res || !res.data || res.data.code !== 0) {
          this.$message({ showClose: true, message: res.data.msg, type: 'error' })
        }
      })
    },
    onClose() {
      this.visible = false
      this.$emit('refreshDataList')
    }
  }
}
</script>
<style >
  .el-transfer-panel{
    width: 180px;
  }
</style>
