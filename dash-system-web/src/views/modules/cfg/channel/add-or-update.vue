<template>
  <el-dialog
    :title="!dataForm.id ? '新增支付渠道' : '修改支付渠道'"
    :close-on-click-modal="false"
    :visible.sync="visible"
  >
    <el-form
      ref="dataForm"
      :model="dataForm"
      :rules="dataRules"
      label-width="80px"
      @keyup.enter.native="dataFormSubmit()"
    >
      <el-form-item label="渠道编号" prop="channelSn">
        <el-input v-model="dataForm.channelSn" placeholder="渠道编号" />
      </el-form-item>
      <el-form-item label="渠道名称" prop="name">
        <el-input v-model="dataForm.name" placeholder="渠道名称" />
      </el-form-item>
      <el-form-item label="启用" prop="enable">
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
import dataRules from './data-rules.js'

export default {
  data() {
    return {
      enableRadioItems: [
        { statusLable: '启用', statusType: 1 },
        { statusLable: '禁用', statusType: 0 }
      ],
      payModeList: [],
      visible: false,
      dataForm: {
        channelId: null,
        name: '',
        channelSn: '',
        enable: 0,
        remark: ''
      },
      dataRules: dataRules
    }
  },
  created() {
    // this.getPayModeList()
  },
  methods: {
    init(id) {
      this.dataForm.channelId = id
      dataRules.channelId = id
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.channelId) {
          const fectData = { channelId: this.dataForm.channelId }
          api.cfgChannel.info({ data: fectData }).then(res => {
            if (res && res.data && res.data.code === 0) {
              this.dataForm = res.data.channel
            } else {
              this.$message.error(res.data.msg)
            }
          })
        }
      })
    },
    payModeChange(payMode) {
      const values = this.payModeList.filter(payModeItem => payModeItem.payMode === payMode)
      this.dataForm.appid = values[0].appid
    },
    // 支付方式
    getPayModeList() {
      api.cfgVpaymode.list({}).then(res => {
        if (res && res.data && res.data.code === 0) {
          this.payModeList = res.data.payModeList
        } else {
          this.$message.error(res.data.msg)
        }
      })
    },
    // 表单提交
    dataFormSubmit() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const fectData = this.dataForm
          api.cfgChannel.saveUpdate({ data: fectData }).then(res => {
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
