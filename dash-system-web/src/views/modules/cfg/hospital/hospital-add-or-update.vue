<template>
  <el-dialog
    :title="!dataForm.hospitalId ? '新增机构' : '修改机构'"
    :close-on-click-modal="false"
    :visible.sync="visible"
  >
    <el-form ref="dataForm" :model="dataForm" :rules="hospitalDataRules" label-width="80px" @keyup.enter.native="dataFormSubmit()">
      <el-form-item label="机构编号" prop="hospitalSn">
        <el-input v-model="dataForm.hospitalSn" placeholder="机构编号" />
      </el-form-item>
      <el-form-item label="机构名称" prop="name">
        <el-input v-model="dataForm.name" placeholder="机构名称" />
      </el-form-item>
      <el-form-item label="上级机构" prop="parentName">
        <el-input v-model="dataForm.parentName" :readonly="true" placeholder="上级机构" />
      </el-form-item>

      <el-form-item label="启用状态" prop="enable">
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
import hospitalDataRules from './hospital-data-rules.js'

export default {
  data() {
    return {
      enableRadioItems: [
        { statusLable: '启用', statusType: 1 },
        { statusLable: '禁用', statusType: 0 }
      ],
      dirList: [],
      hospitalListTreeProps: {
        label: 'name',
        children: 'children'
      },
      visible: false,
      dataForm: {
        hospitalId: null,
        hospitalSn: '',
        name: '',
        enable: 0,
        remark: '',
        parentId: 0,
        parentName: '根机构'
      },
      hospitalDataRules: hospitalDataRules
    }
  },
  methods: {
    init(id, parent) {
      this.dataForm.hospitalId = id
      hospitalDataRules.hospitalId = id
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()

        if (!this.dataForm.hospitalId && parent) {
          this.dataForm.parentId = parent.hospitalId
          const fectData = { hospitalId: parent.hospitalId }
          api.cfgHospital.info({ data: fectData }).then(res => {
            if (res && res.data && res.data.code === 0) {
              this.dataForm.parentName = res.data.hospital.name
            } else {
              this.$message.error(res.data.msg)
            }
          })
        }

        if (this.dataForm.hospitalId || this.dataForm.hospitalId === 0) {
          const fectData = { hospitalId: this.dataForm.hospitalId }
          api.cfgHospital.info({ data: fectData }).then(res => {
            if (res && res.data && res.data.code === 0) {
              this.dataForm = res.data.hospital
            } else {
              this.$message.error(res.data.msg)
            }
          })
        }
      })
    },
    dirListTreeCurrentChangeHandle(data, node) {
      this.dataForm.parentId = data.hospitalId
      this.dataForm.parentName = data.name
    },
    // 表单提交
    dataFormSubmit() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const fectData = this.dataForm
          api.cfgHospital.saveUpdate({ data: fectData }).then(res => {
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
<style>
  .menuPopverDiv .el-scrollbar .el-scrollbar__wrap {overflow-x: hidden;}
</style>
