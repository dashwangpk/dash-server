<template>
  <el-dialog
    :title="!dataForm.userId ? '添加用户信息' : '修改用户信息'"
    :close-on-click-modal="false"
    :visible.sync="visible"
  >
    <el-form
      ref="dataForm"
      :model="dataForm"
      :rules="userDataRules"
      label-width="80px"
      @keyup.enter.native="dataFormSubmit()"
    >
      <el-form-item label="姓名" prop="name">
        <el-input v-model="dataForm.name" placeholder="姓名" />
      </el-form-item>
      <el-form-item label="用户名" prop="username">
        <el-input v-model="dataForm.username" placeholder="用户名" />
      </el-form-item>
      <el-form-item label="手机号" prop="mobile">
        <el-input v-model="dataForm.mobile" placeholder="电话" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="dataForm.status">
          <el-radio v-for="(statusRadio, itemIndex) in statusRadioItems" :key="itemIndex" :label="statusRadio.statusType">{{ statusRadio.statusLable }}</el-radio>
        </el-radio-group>
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
import userDataRules from './user-rules.js'

export default {
  components: {
  },
  data() {
    return {
      visible: false,
      dataForm: {
        userId: null,
        username: null,
        mobile: null,
        status: 0
      },
      statusRadioItems: [
        { statusLable: '正常', statusType: 1 },
        { statusLable: '禁用', statusType: 0 }
      ],
      userDataRules: userDataRules
    }
  },
  methods: {
    init(userId) {
      this.dataForm.userId = userId || null
      userDataRules.userId = userId || null
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.userId) {
          const fetchData = { userId: this.dataForm.userId }
          api.sysUser.infos({ data: fetchData }).then(res => {
            if (res && res.data && res.data.code === 0) {
              this.dataForm = res.data.user
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
          api.sysUser.saveUpdate({ data: fetchData }).then(res => {
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
