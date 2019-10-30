<template>
  <el-dialog
    :title="!dataForm.aaa100 ? '新增字典' : '修改字典'"
    :close-on-click-modal="false"
    width="70%"
    :visible.sync="visible"
  >
    <el-form
      ref="dataForm"
      :model="dataForm"
      :rules="aa10DataRules"
      label-width="80px"
      size="mini"
      @keyup.enter.native="dataFormSubmit()"
    >
      <el-row>
        <el-col :span="5">
          <el-form-item label="代码类型" prop="aaa100">
            <el-input v-model="dataForm.aaa100" :disabled="isUpdate" placeholder="代码类型" />
          </el-form-item>
        </el-col>
        <el-col :span="5">
          <el-form-item label="类型名称" prop="aaa101">
            <el-input v-model="dataForm.aaa101" placeholder="类型名称" />
          </el-form-item>
        </el-col>
        <el-col :span="5">
          <el-form-item label="显示" prop="aaa108">
            <el-radio v-model="dataForm.aaa108" label="1">是</el-radio>
            <el-radio v-model="dataForm.aaa108" label="0">否</el-radio>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="排序号" prop="aaa106">
            <el-input-number v-model="dataForm.aaa106" :min="0" placeholder="排序号" />
          </el-form-item>
        </el-col>
        <el-col :span="1">
          <el-button type="primary" size="mini" title="增加代码值项" style="margin-left: 10px;" @click="addDictItemData()">增加值</el-button>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="23">
          <el-form-item label="备注" prop="aae013">
            <el-input v-model="dataForm.aae013" placeholder="备注" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <div class="itemDataListDiv">
      <el-scrollbar wrap-class="scrollbar-wrapper" style="height:100%;">
        <el-form ref="dictItemDataListForm" size="mini" :model="dictItemDataListForm" label-width="60px">
          <el-row v-for="(itemData, index) in dictItemDataList" :key="index" :label="itemData.dictItemName">
            <el-col :span="8">
              <el-form-item label="代码名" :prop="''+index" :rules="[{validator:dictItemNameValidator, trigger: 'blur'} ]">
                <el-input v-model="itemData.aaa103" placeholder="代码名" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="代码值" :prop="''+index" :rules="[{validator:dictItemValueValidator, trigger: 'blur'} ]">
                <el-input v-model="itemData.aaa102" placeholder="代码值" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="排序号" prop="aaa106">
                <el-input-number v-model="itemData.aaa106" :min="0" placeholder="排序号" />
              </el-form-item>
            </el-col>
            <el-col :offset="3" :span="6">
              <el-form-item label="可编辑" :prop="''+index">
                <el-radio v-model="itemData.aaa105" label="1">是</el-radio>
                <el-radio v-model="itemData.aaa105" label="0">否</el-radio>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="启用" :prop="''+index">
                <el-radio v-model="itemData.aaa104" label="1">是</el-radio>
                <el-radio v-model="itemData.aaa104" label="0">否</el-radio>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="显示" prop="aaa107">
                <el-radio v-model="itemData.aaa107" label="1">是</el-radio>
                <el-radio v-model="itemData.aaa107" label="0">否</el-radio>
              </el-form-item>
            </el-col>
            <el-col :span="3">
              <el-button size="mini" type="danger" style="margin-left: 10px;" @click="deleteDictItem(index)">删除</el-button>
            </el-col>
          </el-row>
        </el-form>
      </el-scrollbar>
    </div>

    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
import api from '@/api'
import aa10DataRules from './aa10-data-rules.js'

export default {
  data() {
    return {
      visible: false,
      dictItemDataListForm: {},
      dictItemDataList: [],
      isUpdate: false,
      dataForm: {
        aaa100: null,
        aaa101: '',
        aaa102: '',
        aaa103: '',
        aaa106: '0',
        aae013: '',
        aaa108: '1'
      },
      aa10DataRules: aa10DataRules
    }
  },
  methods: {
    init(aaa100) {
      this.dataForm.aaa100 = aaa100
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
        this.$refs['dictItemDataListForm'].clearValidate()

        if (this.dataForm.aaa100) {
          aa10DataRules.isAddDict = false
          this.isUpdate = true
          const fetchData = {
            'aaa100': this.dataForm.aaa100
          }
          api.cfgAa10.aa10Info({ data: fetchData }).then(res => {
            if (res && res.data && res.data.code === 0) {
              this.dataForm = res.data.aa10Info.aa00
              if (res.data.aa10Info.aa10s) {
                this.dictItemDataList = res.data.aa10Info.aa10s
              }
            } else {
              this.$message.error(res.data.msg)
            }
          })
        } else {
          aa10DataRules.isAddDict = true
          this.isUpdate = false
          this.dataForm.aaa100 = null
          this.dataForm.aaa101 = null
          this.dataForm.aae013 = null
          this.dataForm.aaa108 = '1'
          this.dictItemDataList = [{ aaa102: '', aaa103: '', aaa104: '1', aaa105: '0', aaa106: '0', aaa107: '1' }]
        }
      })
    },
    dictItemValueValidator(rule, value, callback) {
      if (this.dictItemDataList[rule.field].aaa102 === null ||
              this.dictItemDataList[rule.field].aaa102.length === 0) {
        callback(new Error('代码值不能为空'))
      } else {
        callback()
      }
    },
    dictItemNameValidator(rule, value, callback) {
      if (this.dictItemDataList[rule.field].aaa103 === null ||
              this.dictItemDataList[rule.field].aaa103.length === 0) {
        callback(new Error('代码名不能为空'))
      } else {
        callback()
      }
    },
    deleteDictItem(index) {
      this.dictItemDataList.splice(index, 1)
    },
    addDictItemData() {
      this.dictItemDataList.push({ aaa102: '', aaa103: '', aaa104: '1', aaa105: '0', aaa106: '0', aaa107: '1' })
    },
    // 表单提交
    dataFormSubmit() {
      this.$refs['dataForm'].validate((paramDataValid) => {
        if (paramDataValid) {
          const fetchData = { ...this.dataForm }
          this.$refs['dictItemDataListForm'].validate((itemDataValid) => {
            if (itemDataValid) {
              if (this.dictItemDataList === null || this.dictItemDataList.length === 0) {
                this.$message.error('必须增加值信息')
                return
              }

              const aaa100 = this.dataForm.aaa100
              const aaa101 = this.dataForm.aaa101
              const aae013 = this.dataForm.aae013

              const dictList = []
              this.dictItemDataList.forEach(function(currentValue, index, arr) {
                dictList.push({
                  aaa100: aaa100,
                  aaa101: aaa101,
                  aaa102: currentValue.aaa102,
                  aaa103: currentValue.aaa103,
                  aaa104: currentValue.aaa104,
                  aaa105: currentValue.aaa105,
                  aaa106: currentValue.aaa106,
                  aaa107: currentValue.aaa107,
                  aae013: aae013
                })
              })

              fetchData.dictList = dictList
              fetchData.aa00 = { ...this.dataForm }

              api.cfgAa10.saveUpdate({ data: fetchData }).then(res => {
                if (res && res.data && res.data.code === 0) {
                  this.$message({
                    message: '操作成功',
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
      })
    }
  }

}
</script>
<style>
.itemDataListDiv {
    height: 200px;
    overflow: auto;
    padding-top:10px;
    background-color: #fff;
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    box-sizing: border-box;
    position: relative;
    overflow: hidden;
}

div.itemDataListDiv:hover{
    border: 1px dashed rgb(98, 146, 201);
}
.itemDataListDiv .el-scrollbar .el-scrollbar__wrap {overflow-x: hidden;}
</style>

