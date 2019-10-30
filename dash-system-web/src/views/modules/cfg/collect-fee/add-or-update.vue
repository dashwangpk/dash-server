<template>
  <el-dialog
    :title="!dataForm.collectFeeId ? '新增收费员' : '修改收费员'"
    :close-on-click-modal="false"
    :visible.sync="visible"
  >
    <el-form ref="dataForm" :model="dataForm" :rules="dataRules" label-width="100px" @keyup.enter.native="dataFormSubmit()">
      <el-form-item label="收费员编号" prop="collectFeeSn">
        <el-input v-model="dataForm.collectFeeSn" :readonly="!!dataForm.collectFeeId " placeholder="收费员编号" />
      </el-form-item>
      <el-form-item label="收费员名称" prop="name">
        <el-input v-model="dataForm.name" placeholder="收费员名称" />
      </el-form-item>
      <el-form-item label="医院" prop="hospitalId">
        <el-select v-model="dataForm.hospitalId" placeholder="请选医院">
          <el-option
            v-for="hospital in hospitalList"
            :key="hospital.hospitalId"
            :disabled="hospital.enable == 0"
            :label="hospital.name"
            :value="hospital.hospitalId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="类型" prop="type">
        <el-select v-model="dataForm.type" placeholder="请选类型">
          <el-option
            v-for="type in typeList"
            :key="type.aaa102"
            :disabled="type.aaa104 == '0'"
            :label="type.aaa103"
            :value="type.aaa102"
          />
        </el-select>
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
      hospitalList: [],
      typeList: [],
      enableRadioItems: [
        { statusLable: '启用', statusType: 1 },
        { statusLable: '禁用', statusType: 0 }
      ],
      visible: false,
      dataForm: {
        collectFeeId: 0,
        hospitalId: '',
        collectFeeSn: '',
        name: '',
        type: '',
        enable: 0,
        remark: ''
      },
      dataRules: dataRules
    }
  },
  mounted() {
    this.getHospitalList()
    this.getCollectFeeTypeList()
  },
  methods: {
    init(id) {
      this.dataForm.collectFeeId = id
      this.dataRules.collectFeeId = id
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.collectFeeId) {
          const fectData = { collectFeeId: this.dataForm.collectFeeId }
          api.cfgCollectfee.info({ data: fectData }).then(res => {
            if (res && res.data && res.data.code === 0) {
              this.dataForm = res.data.collectFee
            } else {
              this.$message.error(res.data.msg)
            }
          })
        }
      })
    },
    getHospitalList() {
      api.cfgHospital.wholelist({ data: { }}).then(res => {
        if (res && res.data && res.data.code === 0) {
          this.hospitalList = res.data.wholelist
        } else {
          this.$message.error(res.data.msg)
        }
      })
    },
    getCollectFeeTypeList() {
      // "sfylx" 为收费员类型 暂为前端传入
      api.cfgAa10.options({ data: { aaa100: 'sfylx' }}).then(res => {
        if (res && res.data && res.data.code === 0) {
          this.typeList = res.data.options
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
          api.cfgCollectfee.saveUpdate({ data: fectData }).then(res => {
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
