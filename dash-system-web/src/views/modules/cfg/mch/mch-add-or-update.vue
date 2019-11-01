<template>
  <el-dialog
    :title="!dataForm.mchId ? '新增商户' : '修改商户'"
    :close-on-click-modal="false"
    :visible.sync="visible"
  >
    <el-form ref="dataForm" :model="dataForm" :rules="appDataRules" label-width="80px" @keyup.enter.native="dataFormSubmit()">
      <el-form-item label="商家编号" prop="mchSn">
        <el-input v-model="dataForm.mchSn" placeholder="商家编号" :readonly="!!dataForm.mchId " />
      </el-form-item>
      <el-form-item label="商家名称" prop="name">
        <el-input v-model="dataForm.name" placeholder="商家名称" />
      </el-form-item>
      <el-form-item label="联系人" prop="contacts">
        <el-input v-model="dataForm.contacts" placeholder="联系人" />
      </el-form-item>
      <el-form-item label="联系电话" prop="phone">
        <el-input v-model="dataForm.phone" placeholder="联系电话" />
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
import appDataRules from './mch-data-rules.js'

export default {
  data() {
    return {
      enableRadioItems: [
        { statusLable: '启用', statusType: 1 },
        { statusLable: '禁用', statusType: 0 }
      ],
      visible: false,
      dataForm: {
        mchId: null,
        mchSn: '',
        name: '',
        contacts: '',
        phone: '',
        enable: 0,
        remark: ''
      },
      appDataRules: appDataRules
    }
  },
  methods: {
    init(id) {
      this.dataForm.mchId = id
      this.appDataRules.mchId = id
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.mchId || this.dataForm.mchId === 0) {
          const fectData = { mchId: this.dataForm.mchId }
          api.cfgMch.info({ data: fectData }).then(res => {
            if (res && res.data && res.data.code === 0) {
              this.dataForm = res.data.mch
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
          api.cfgMch.saveUpdate({ data: fectData }).then(res => {
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
