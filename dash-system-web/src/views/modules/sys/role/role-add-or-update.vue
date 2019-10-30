<template>
  <el-dialog
    :title="!dataForm.roleId || dataForm.roleId === 0 ? '添加角色信息' : '修改角色信息'"
    :close-on-click-modal="false"
    :visible.sync="visible"
  >
    <el-form
      ref="dataForm"
      :model="dataForm"
      :rules="roleDataRules"
      label-width="80px"
      @keyup.enter.native="dataFormSubmit()"
    >
      <el-form-item label="角色名" prop="roleName">
        <el-input v-model="dataForm.roleName" placeholder="角色名" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="dataForm.status">
          <el-radio v-for="(statusRadio, itemIndex) in statusRadioItems" :key="itemIndex" :label="statusRadio.statusType">{{ statusRadio.statusLable }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="描述" prop="remark">
        <el-input
          v-model="dataForm.remark"
          type="textarea"
          :autosize="{ minRows: 2, maxRows: 4}"
          placeholder="描述"
        />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button type="danger" @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
import api from '@/api'
import roleDataRules from './role-rules.js'

export default {
  components: {
  },
  data() {
    return {
      visible: false,
      dataForm: {
        roleId: null,
        roleName: null,
        remark: null,
        status: 0
      },
      statusRadioItems: [
        { statusLable: '启用', statusType: 1 },
        { statusLable: '禁用', statusType: 0 }
      ],
      roleDataRules: roleDataRules
    }
  },
  methods: {
    init(roleId) {
      this.dataForm.roleId = roleId
      roleDataRules.roleId = roleId
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.roleId || this.dataForm.roleId === 0) {
          const fetchData = { roleId: this.dataForm.roleId }
          api.sysRole.infos({ data: fetchData }).then(res => {
            if (res && res.data && res.data.code === 0) {
              this.dataForm = res.data.role
            } else {
              this.$message({ showClose: true, message: res.data.msg, type: 'error' })
            }
          })
        }
      })
    },
    // 表单提交
    dataFormSubmit() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const fetchData = this.dataForm
          api.sysRole.saveUpdate({ data: fetchData }).then(res => {
            if (res && res.data && res.data.code === 0) {
              this.$message({ message: '保存成功', type: 'success', duration: 1500,
                onClose: () => {
                  this.visible = false
                  this.$emit('refreshDataList')
                }
              })
            } else {
              this.$message({ showClose: true, message: res.data.msg, type: 'error' })
            }
          })
        }
      })
    }
  }
}
</script>
