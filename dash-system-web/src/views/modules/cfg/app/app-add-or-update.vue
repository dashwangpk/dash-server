<template>
  <el-dialog
    :title="!dataForm.appId ? '新增应用' : '修改应用'"
    :close-on-click-modal="false"
    :visible.sync="visible"
  >
    <el-form ref="dataForm" :model="dataForm" :rules="appDataRules" label-width="80px" @keyup.enter.native="dataFormSubmit()">
      <el-form-item label="应用编号" prop="appSn">
        <el-input v-model="dataForm.appSn" placeholder="应用编号" :readonly="!!dataForm.appId " />
      </el-form-item>
      <el-form-item label="应用名称" prop="name">
        <el-input v-model="dataForm.name" placeholder="应用名称" />
      </el-form-item>
      <el-form-item label="是否启用" prop="enable">
        <el-radio-group v-model="dataForm.enable">
          <el-radio v-for="(enableRadio, itemIndex) in enableRadioItems" :key="itemIndex" :label="enableRadio.statusType">{{ enableRadio.statusLable }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input
          v-model="dataForm.remark"
          type="textarea"
          :autosize="{ minRows: 2, maxRows: 4}"
          placeholder="备注"
        />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
import api from '@/api'
import appDataRules from './app-data-rules.js'

export default {
  data() {
    return {
      enableRadioItems: [
        { statusLable: '启用', statusType: 1 },
        { statusLable: '禁用', statusType: 0 }
      ],
      visible: false,
      dataForm: {
        appId: null,
        appSn: '',
        name: '',
        enable: 0,
        remark: ''
      },
      appDataRules: appDataRules
    }
  },
  methods: {
    init(id) {
      this.dataForm.appId = id
      this.appDataRules.appId = id
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.appId || this.dataForm.appId === 0) {
          const fectData = { appId: this.dataForm.appId }
          api.cfgApp.info({ data: fectData }).then(res => {
            if (res && res.data && res.data.code === 0) {
              this.dataForm = res.data.app
            } else {
              this.$message.error(res.data.msg)
            }
          })
        }
      })
    },
    // 表单提交
    dataFormSubmit() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const fectData = this.dataForm
          api.cfgApp.saveUpdate({ data: fectData }).then(res => {
            if (res && res.data && res.data.code === 0) {
              this.$message({
                message: '保存成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.visible = false
                  this.$emit('refreshDataList')
                }
              })
            } else {
              this.$message.error(res.data.msg)
            }
          })
        }
      })
    }
  }
}
</script>
