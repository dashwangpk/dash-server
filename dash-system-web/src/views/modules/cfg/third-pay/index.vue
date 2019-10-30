<template>
  <div class="tab-container">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane
        v-for="item in tabMapOptions"
        :key="item.key"
        :label="item.label"
        :name="item.key"
      >
        <el-button
          v-if="activeName === 'PAY_100'"
          type="primary"
          @click="addNewCfg('PAY_100',tempWechat,'add')"
        >新增</el-button>
        <el-table
          v-if="activeName === 'PAY_100'"
          :data="wechatList"
          style="width: 100%;"
          border
        >
          <el-table-column align="center" :show-overflow-tooltip="true" label="名称">
            <template slot-scope="scope">{{ scope.row.alias }}</template>
          </el-table-column>
          <el-table-column align="center" :show-overflow-tooltip="true" label="APPID">
            <template slot-scope="scope">{{ scope.row.appid }}</template>
          </el-table-column>
          <el-table-column align="center" label="商户号" :show-overflow-tooltip="true">
            <template slot-scope="scope">{{ scope.row.mchId }}</template>
          </el-table-column>
          <el-table-column align="center" label="api密钥" :show-overflow-tooltip="true">
            <template slot-scope="scope">{{ scope.row.apiKey }}</template>
          </el-table-column>
          <el-table-column align="center" label="启用状态">
            <!-- <template slot-scope="scope">{{ scope.row.enable }}</template> -->
            <template slot-scope="scope">
              <el-tag v-if="scope.row.enable === 0 " type="success">启用</el-tag>
              <el-tag v-else type="danger">禁用</el-tag>
            </template>
          </el-table-column>
          <el-table-column align="center" label="操作">
            <template slot-scope="scope">
              <el-button
                type="primary"
                size="small"
                plain
                @click="addNewCfg('PAY_100',scope.row,'update')"
              >修改</el-button>
              <el-button plain type="danger" size="small" @click="deleteDetailWX(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-button
          v-if="activeName === 'PAY_200'"
          type="primary"
          @click="addNewCfg('PAY_200',tempAlipay,'add')"
        >新增</el-button>
        <el-table
          v-if="activeName === 'PAY_200'"
          :data="alipayList"
          style="width: 100%;"
          border
        >
          <el-table-column align="center" :show-overflow-tooltip="true" label="名称">
            <template slot-scope="scope">{{ scope.row.alias }}</template>
          </el-table-column>
          <el-table-column align="center" :show-overflow-tooltip="true" label="APPID">
            <template slot-scope="scope">{{ scope.row.appid }}</template>
          </el-table-column>
          <el-table-column align="center" label="私钥" :show-overflow-tooltip="true">
            <template slot-scope="scope">{{ scope.row.privateKey }}</template>
          </el-table-column>
          <el-table-column align="center" label="公钥" :show-overflow-tooltip="true">
            <template slot-scope="scope">{{ scope.row.publicKey }}</template>
          </el-table-column>
          <el-table-column align="center" label="签名类型">
            <template slot-scope="scope">{{ scope.row.signType }}</template>
          </el-table-column>
          <el-table-column align="center" label="启用状态">
            <template slot-scope="scope">
              <el-tag v-if="scope.row.enable === 0 " type="success">启用</el-tag>
              <el-tag v-else type="danger">禁用</el-tag>
            </template>
          </el-table-column>
          <el-table-column align="center" label="操作">
            <template slot-scope="scope">
              <el-button
                type="primary"
                size="small"
                plain
                @click="addNewCfg('PAY_200',scope.row,'update')"
              >修改</el-button>
              <el-button plain type="danger" size="small" @click="deleteDetailZFB(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-button
          v-if="activeName === 'PAY_500'"
          type="primary"
          @click="addNewCfg('PAY_500',tempUnionpay,'add')"
        >新增</el-button>
        <el-table
          v-if="activeName === 'PAY_500'"
          :data="unionpayList"
          style="width: 100%;"
          border
        >
          <el-table-column align="center" :show-overflow-tooltip="true" min-width="100" label="名称">
            <template slot-scope="scope">{{ scope.row.alias }}</template>
          </el-table-column>
          <el-table-column align="center" :show-overflow-tooltip="true" min-width="100" label="商户代码">
            <template slot-scope="scope">{{ scope.row.merId }}</template>
          </el-table-column>

          <el-table-column align="center" :show-overflow-tooltip="true" min-width="100" label="敏感加密证书">
            <template slot-scope="scope">{{ scope.row.encCer }}</template>
          </el-table-column>
          <el-table-column align="center" :show-overflow-tooltip="true" min-width="100" label="中级证书">
            <template slot-scope="scope">{{ scope.row.middleCer }}</template>
          </el-table-column>
          <el-table-column align="center" :show-overflow-tooltip="true" min-width="100" label="根证书">
            <template slot-scope="scope">{{ scope.row.rootCer }}</template>
          </el-table-column>
          <el-table-column align="center" :show-overflow-tooltip="true" min-width="100" label="私钥证书">
            <template slot-scope="scope">{{ scope.row.sign }}</template>
          </el-table-column>

          <el-table-column align="center" label="启用状态" width="80">
            <template slot-scope="scope">
              <el-tag v-if="scope.row.enable === 0 " type="success">启用</el-tag>
              <el-tag v-else type="danger">禁用</el-tag>
            </template>
          </el-table-column>
          <el-table-column align="center" label="操作" fixed="right" width="160">
            <template slot-scope="scope">
              <el-button
                type="primary"
                size="small"
                plain
                @click="addNewCfg('PAY_500',scope.row,'update')"
              >修改</el-button>
              <el-button plain type="danger" size="small" @click="deleteDetailYL(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <medical-ins v-if="activeName === 'PAY_300'" />
      </el-tab-pane>
    </el-tabs>

    <el-dialog :title="textMap[activeName]" :visible.sync="wechatVisible">
      <el-form
        ref="dataFormWechat"
        :rules="ruleValidate"
        :model="tempWechat"
        label-width="100px"
        destroy-on-close
      >
        <el-form-item label="别名" prop="alias">
          <el-input v-model="tempWechat.alias" />
        </el-form-item>
        <el-form-item label="APPID" prop="appid">
          <el-input v-model="tempWechat.appid" />
        </el-form-item>
        <el-form-item label="商户号" prop="mchId">
          <el-input v-model="tempWechat.mchId" />
        </el-form-item>
        <el-form-item label="API密钥" prop="apiKey">
          <el-input v-model="tempWechat.apiKey" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="启用标志" prop="enable">

          <el-radio-group v-model="tempWechat.enable">
            <el-radio v-for="(statusRadio, itemIndex) in statusRadioItems" :key="itemIndex" :label="statusRadio.statusType">{{ statusRadio.statusLable }}</el-radio>
          </el-radio-group>

        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="wechatVisible = false">取消</el-button>
        <el-button v-if="tempWechat.typeF === 'add'" type="primary" @click="saveDetailWX()">保存</el-button>
        <el-button v-else type="primary" @click="updateDetailWX()">保存</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="textMap[activeName]" :visible.sync="alipayVisible">
      <el-form
        ref="dataFormAlipay"
        :rules="ruleValidate"
        :model="tempAlipay"
        label-width="100px"
      >
        <el-form-item label="别名" prop="alias">
          <el-input v-model="tempAlipay.alias" />
        </el-form-item>
        <el-form-item label="APPID" prop="appid">
          <el-input v-model="tempAlipay.appid" />
        </el-form-item>
        <el-form-item label="私钥" prop="privateKey">
          <el-input v-model="tempAlipay.privateKey" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="公钥" prop="publicKey">
          <el-input v-model="tempAlipay.publicKey" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="签名类型" prop="signType">
          <el-select v-model="tempAlipay.signType" class="filter-item">
            <el-option
              v-for="item in signOptions"
              :key="item.key"
              :label="item.display_name"
              :value="item.key"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="启用标志" prop="enable">
          <el-radio-group v-model="tempAlipay.enable">
            <el-radio v-for="(statusRadio, itemIndex) in statusRadioItems" :key="itemIndex" :label="statusRadio.statusType">{{ statusRadio.statusLable }}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="alipayVisible = false">取消</el-button>
        <el-button v-if="tempAlipay.typeF === 'add'" type="primary" @click="saveDetailZFB()">保存</el-button>
        <el-button v-else type="primary" @click="updateDetailZFB()">保存</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="textMap[activeName]" :visible.sync="unionpayVisible">
      <el-form
        ref="dataFormUnionpay"
        :rules="ruleValidate"
        :model="tempUnionpay"
        label-width="100px"
      >
        <el-form-item label="别名" prop="alias">
          <el-input v-model="tempUnionpay.alias" />
        </el-form-item>
        <el-form-item label="商户代码" prop="merId">
          <el-input v-model="tempUnionpay.merId" />
        </el-form-item>
        <el-form-item label="敏感加密证书" prop="encCer">
          <div @click="selectEnCerFile">
            <el-input v-model="tempUnionpay.encCer" :readonly="true" placeholder="请选择敏感加密证书" />
          </div>
          <input id="encCerFileId" ref="encCerUpload" style="display:none;" type="file" @change="getEnCerFile">
        </el-form-item>

        <el-form-item label="中级证书" prop="middleCer">
          <div @click="selectMiddleCerFile">
            <el-input v-model="tempUnionpay.middleCer" :readonly="true" placeholder="请选择中级证书" />
          </div>
          <input id="middleCerFileId" ref="middleCerFileUpload" style="display:none;" type="file" @change="getMiddleCerFile">
        </el-form-item>

        <el-form-item label="根证书" prop="rootCer">
          <div @click="selectRootCerFile">
            <el-input v-model="tempUnionpay.rootCer" :readonly="true" placeholder="请选择根证书" />
          </div>
          <input id="rootCerFileId" ref="rootCerFileUpload" style="display:none;" type="file" @change="getRootCerFile">
        </el-form-item>

        <el-form-item label="私钥证书" prop="sign">
          <div @click="selectSignFile">
            <el-input v-model="tempUnionpay.sign" :readonly="true" placeholder="请选择私钥证书" />
          </div>
          <input id="signFileId" ref="signFileUpload" style="display:none;" type="file" @change="getSignFile">
        </el-form-item>

        <el-form-item label="启用标志" prop="enable">
          <el-radio-group v-model="tempUnionpay.enable">
            <el-radio v-for="(statusRadio, itemIndex) in statusRadioItems" :key="itemIndex" :label="statusRadio.statusType">{{ statusRadio.statusLable }}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="unionpayVisible = false">取消</el-button>
        <el-button v-if="tempUnionpay.typeF === 'add'" type="primary" @click="saveDetailYL()">保存</el-button>
        <el-button v-else type="primary" @click="updateDetailYL()">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import ruleValidate from './rules.js'
import api from '@/api'
import medicalIns from '../medical-insurance/index'

export default {
  name: 'PayConfigs',
  components: { medicalIns },
  data() {
    return {
      statusRadioItems: [
        { statusLable: '启用', statusType: 0 },
        { statusLable: '禁用', statusType: 1 }
      ],
      tabMapOptions: [
        { label: '微信', key: 'PAY_100' },
        { label: '支付宝', key: 'PAY_200' },
        { label: '银联', key: 'PAY_500' }
        // { label: '医保', key: 'PAY_300' }
      ],
      textMap: {
        PAY_100: '微信',
        PAY_200: '支付宝',
        PAY_500: '银联'
      },
      enableOptions: [
        { key: 0, display_name: '启用' },
        { key: 1, display_name: '禁用' }
      ],
      signOptions: [
        { key: 'RSA2', display_name: 'RSA2' },
        { key: 'RSA', display_name: 'RSA' }
      ],
      wechatVisible: false,
      alipayVisible: false,
      unionpayVisible: false,
      wechatList: [],
      alipayList: [],
      unionpayList: [],
      tempWechat: { enable: '0' },
      tempAlipay: { signType: 'RSA2', enable: '0', oldAppid: null },
      tempUnionpay: {
        enable: '0',
        alias: null,
        merId: null,
        encCer: null,
        middleCer: null,
        rootCer: null,
        sign: null
      },
      addDetail: {},
      ruleValidate: ruleValidate,
      activeName: 'PAY_100',
      encCerfileList: []
    }
  },
  mounted() {
    this.handleClick({ name: 'PAY_100' })
  },
  methods: {
    selectSignFile() {
      this.$refs.signFileUpload.dispatchEvent(new MouseEvent('click'))
    },
    getSignFile() {
      const inputFiles = this.$refs.signFileUpload.files
      if (inputFiles && inputFiles.length > 0) {
        const file = inputFiles[0]
        this.tempUnionpay.sign = file.name
      }
    },
    selectRootCerFile() {
      this.$refs.rootCerFileUpload.dispatchEvent(new MouseEvent('click'))
    },
    getRootCerFile() {
      const inputFiles = this.$refs.rootCerFileUpload.files
      if (inputFiles && inputFiles.length > 0) {
        const file = inputFiles[0]
        this.tempUnionpay.rootCer = file.name
      }
    },
    selectMiddleCerFile() {
      this.$refs.middleCerFileUpload.dispatchEvent(new MouseEvent('click'))
    },
    getMiddleCerFile() {
      const inputFiles = this.$refs.middleCerFileUpload.files
      if (inputFiles && inputFiles.length > 0) {
        const file = inputFiles[0]
        this.tempUnionpay.middleCer = file.name
      }
    },
    selectEnCerFile() {
      this.$refs.encCerUpload.dispatchEvent(new MouseEvent('click'))
    },
    getEnCerFile() {
      const inputFiles = this.$refs.encCerUpload.files
      if (inputFiles && inputFiles.length > 0) {
        const file = inputFiles[0]
        this.tempUnionpay.encCer = file.name
      }
    },
    addNewCfg(cfg, datas, typeF) {
      // this.closeModel();
      if (cfg === 'PAY_100') {
        this.wechatVisible = true
        this.alipayVisible = false
        this.unionpayVisible = false
        this.tempWechat = datas
        this.tempWechat.oldAppid = datas.appid
        Object.assign(this.tempWechat, { typeF: typeF })
      } else if (cfg === 'PAY_200') {
        this.wechatVisible = false
        this.alipayVisible = true
        this.unionpayVisible = false
        this.tempAlipay = datas
        this.tempAlipay.oldAppid = datas.appid
        Object.assign(this.tempAlipay, { typeF: typeF })
      } else {
        this.wechatVisible = false
        this.alipayVisible = false
        this.unionpayVisible = true
        this.$nextTick(() => {
          this.$refs['dataFormUnionpay'].resetFields()
          this.$refs.middleCerFileUpload.value = ''
          this.$refs.encCerUpload.value = ''
          this.$refs.rootCerFileUpload.value = ''
          this.$refs.signFileUpload.value = ''

          if (typeF === 'add') {
            this.ruleValidate.oldMerId = null
            this.tempUnionpay.enable = 0
            this.tempUnionpay.alias = null
            this.tempUnionpay.merId = null
            this.tempUnionpay.encCer = null
            this.tempUnionpay.middleCer = null
            this.tempUnionpay.rootCer = null
            this.tempUnionpay.sign = null
            this.tempUnionpay.oldAppid = null
            this.tempUnionpay.typeF = 'add'
          } else {

            this.tempUnionpay.enable = datas.enable
            this.tempUnionpay.alias = datas.alias
            this.tempUnionpay.merId = datas.merId
            this.tempUnionpay.encCer = datas.encCer
            this.tempUnionpay.middleCer = datas.middleCer
            this.tempUnionpay.rootCer = datas.rootCer
            this.tempUnionpay.sign = datas.sign
            this.tempUnionpay.oldAppid = datas.merId
            this.tempUnionpay.typeF = 'update'
            this.ruleValidate.oldMerId = datas.merId
          }
        })
      }
    },
    // 微信保存
    saveDetailWX() {
      this.$refs['dataFormWechat'].validate(valid => {
        if (valid) {
          const reqData = this.tempWechat
          api.payConfig.insertWechat({ data: reqData }).then(
            res => {
              this.HandleResult(res.data, 'PAY_100')
            },
            err => {
              this.$message({
                message: err,
                type: 'error'
              })
            }
          )
        }
      })
    },
    // 微信修改
    updateDetailWX() {
      this.$refs['dataFormWechat'].validate(valid => {
        if (valid) {
          const reqData = this.tempWechat

          api.payConfig.updateWechat({ data: reqData }).then(
            res => {
              if (res.data.success) {
                this.$message({
                  message: res.data.message,
                  type: 'success'
                })
                this.wechatVisible = false
                this.handleClick({ name: this.activeName })
              } else {
                this.$message({
                  message: res.data.message,
                  type: 'error'
                })
              }
            },
            err => {
              this.$message({
                message: err,
                type: 'error'
              })
            }
          )
        }
      })
    },
    // 微信删除
    deleteDetailWX(row) {
      this.$refs['dataFormWechat'].validate(valid => {
        if (valid) {
          const reqData = row
          api.payConfig.deleteWechat({ data: reqData }).then(
            res => {
              if (res.data.success) {
                this.$message({
                  message: res.data.message,
                  type: 'success'
                })
                this.handleClick({ name: this.activeName })
              } else {
                this.$message({
                  message: res.data.message,
                  type: 'error'
                })
              }
            },
            err => {
              this.$message({
                message: err,
                type: 'error'
              })
            }
          )
        }
      })
    },
    // 支付宝保存
    saveDetailZFB() {
      this.$refs['dataFormAlipay'].validate(valid => {
        if (valid) {
          const reqData = this.tempAlipay
          api.payConfig.insertAlipay({ data: reqData }).then(
            res => {
              this.HandleResult(res.data, 'PAY_200')
            },
            err => {
              this.$message({
                message: err,
                type: 'error'
              })
            }
          )
        }
      })
    },
    // 支付宝修改
    updateDetailZFB() {
      this.$refs['dataFormAlipay'].validate(valid => {
        if (valid) {
          const reqData = this.tempAlipay
          api.payConfig.updateAlipay({ data: reqData }).then(
            res => {
              if (res.data.success) {
                this.$message({
                  message: res.data.message,
                  type: 'success'
                })
                this.alipayVisible = false
                this.handleClick({ name: this.activeName })
              } else {
                this.$message({
                  message: res.data.message,
                  type: 'error'
                })
              }
            },
            err => {
              this.$message({
                message: err,
                type: 'error'
              })
            }
          )
        }
      })
    },
    // 支付宝删除
    deleteDetailZFB(row) {
      this.$refs['dataFormAlipay'].validate(valid => {
        if (valid) {
          const reqData = row
          api.payConfig.deleteAlipay({ data: reqData }).then(
            res => {
              if (res.data.success) {
                this.$message({
                  message: res.data.message,
                  type: 'success'
                })
                this.handleClick({ name: this.activeName })
              } else {
                this.$message({
                  message: res.data.message,
                  type: 'error'
                })
              }
            },
            err => {
              this.$message({
                message: err,
                type: 'error'
              })
            }
          )
        }
      })
    },
    saveCerFile() {
      const fileFormData = new window.FormData()
      fileFormData.append('merId', this.tempUnionpay.merId)

      const encCerFiles = this.$refs.encCerUpload.files
      if (encCerFiles && encCerFiles.length > 0) {
        const encCerFile = encCerFiles[0]
        fileFormData.append('encCerFile', encCerFile)
      } else {
        fileFormData.append('encCerFile', null)
      }

      const middleCerFiles = this.$refs.middleCerFileUpload.files
      if (middleCerFiles && middleCerFiles.length > 0) {
        const middleCerFile = middleCerFiles[0]
        fileFormData.append('middleCerFile', middleCerFile)
      } else {
        fileFormData.append('middleCerFile', null)
      }

      const rootCerFiles = this.$refs.rootCerFileUpload.files
      if (rootCerFiles && rootCerFiles.length > 0) {
        const rootCerFile = rootCerFiles[0]
        fileFormData.append('rootCerFile', rootCerFile)
      } else {
        fileFormData.append('rootCerFile', null)
      }

      const signFiles = this.$refs.signFileUpload.files
      if (signFiles && signFiles.length > 0) {
        const signFile = signFiles[0]
        fileFormData.append('signFile', signFile)
      } else {
        fileFormData.append('signFile', null)
      }

      api.payConfig.uploadCerFile({ data: fileFormData }).then(
        res => {
          this.$message({
            message: '保存成功',
            type: 'success'
          })
        },
        err => {
          this.$message({
            message: err,
            type: 'error'
          })
        }
      )
    },
    // 银联保存
    saveDetailYL() {
      this.$refs['dataFormUnionpay'].validate(valid => {
        if (valid) {
          const reqData = this.tempUnionpay
          api.payConfig.insertUnionpay({ data: reqData }).then(
            res => {
              this.saveCerFile()
              this.unionpayVisible = false
            },
            err => {
              this.$message({
                message: err,
                type: 'error'
              })
            }
          )
        }
      })
    },
    // 银联修改
    updateDetailYL() {
      this.$refs['dataFormUnionpay'].validate(valid => {
        if (valid) {
          const reqData = this.tempUnionpay
          api.payConfig.updateUnionpay({ data: reqData }).then(
            res => {
              if (res.data.success) {
                this.saveCerFile()
                this.unionpayVisible = false
                this.handleClick({ name: this.activeName })
              } else {
                this.$message({
                  message: res.data.message,
                  type: 'error'
                })
              }
            },
            err => {
              this.$message({
                message: err,
                type: 'error'
              })
            }
          )
        }
      })
    },
    // 银联删除
    deleteDetailYL(row) {
      this.$refs['dataFormUnionpay'].validate(valid => {
        if (valid) {
          const reqData = row
          api.payConfig.deleteUnionpay({ data: reqData }).then(
            res => {
              if (res.data.success) {
                this.$message({
                  message: res.data.message,
                  type: 'success'
                })
                this.handleClick({ name: this.activeName })
              } else {
                this.$message({
                  message: res.data.message,
                  type: 'error'
                })
              }
            },
            err => {
              this.$message({
                message: err,
                type: 'error'
              })
            }
          )
        }
      })
    },

    HandleResult(data, v_type) {
      if (data.success) {
        if (data.body === 1) {
          this.$message({
            message: data.message,
            type: 'success'
          })

          if (v_type === 'PAY_100') {
            this.wechatVisible = false
          } else if (v_type === 'PAY_200') {
            this.alipayVisible = false
          } else {
            this.unionpayVisible = false
          }

          this.handleClick({ name: this.activeName })
        } else if (data.body === 10) {
          this.$message({
            message: '别名重复',
            type: 'error'
          })
        } else {
          this.$message({
            message: '商户已有',
            type: 'error'
          })
        }
      } else {
        this.$message({
          message: data.message,
          type: 'error'
        })
      }
    },

    handleClick(tab, event) {
      if (tab.name === 'PAY_100') {
        api.payConfig.findWechat({ data: {}}).then(res => {
          if (res.data && res.data.body) {
            this.wechatList = res.data.body
          }
        })
      } else if (tab.name === 'PAY_200') {
        api.payConfig.findAlipay({ data: {}}).then(res => {
          if (res.data && res.data.body) {
            this.alipayList = res.data.body
          }
        })
      } else if (tab.name === 'PAY_500') {
        api.payConfig.findUnionpay({ data: {}}).then(res => {
          if (res.data && res.data.body) {
            this.unionpayList = res.data.body
          }
        })
      }
    },

    closeModel() {
      this.tempWechat = {}
      this.tempAlipay = {}
      this.tempUnionpay = {}
    }
  }
}
</script>

<style scoped>
.tab-container {
  margin: 30px;
}
</style>
