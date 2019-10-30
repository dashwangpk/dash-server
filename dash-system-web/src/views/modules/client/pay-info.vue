<template>
  <div>
    <div class="app-title">
      <div class="title-font">长安国际地下停车场收费系统</div>
    </div>
    <div v-if="isShow" class="app-container">
      <div class="left" />
      <div class="app-div">
        <el-card>
          <el-form
            ref="payInfoForm"
            :model="payInfoForm"
            :rules="payInfoDataRules"
            label-width="100px"
          >

            <!-- <el-form-item label="姓名" prop="buyerUserName">
              <el-input v-model="payInfoForm.buyerUserName" placeholder="姓名" />
            </el-form-item> -->
            <!-- <el-form-item label="应用ID" prop="appid">
              <el-input v-model="payInfoForm.appid" placeholder="应用ID" />
            </el-form-item> -->

            <el-form-item label="商品名称" prop="deviceInfo">
              <el-input v-model="payInfoForm.deviceInfo" placeholder="商品名称" />
            </el-form-item>

            <el-form-item label="费用" prop="totalFee">
              <el-input v-model="payInfoForm.totalFee" placeholder="费用" />
            </el-form-item>

          </el-form>
        </el-card>
      </div>
      <div class="left" />
    </div>
    <div v-if="isShow" class="app-footer">
      <div class="footer-font">
        <el-button type="danger" size="medium" @click="dataFormSubmit()">确定</el-button>
      </div>
    </div>
    <div class="app-footer">
      <div class="footer-font">
        <img :src="payImg">
      </div>
    </div>
  </div>
</template>

<script>

// import AddOrUpdate from './pay-info-add-or-update'
import payInfoDataRules from './pay-info-data-rules'
import api from '@/api'

export default {
  name: 'ClientPayInfo',
  components: {
    // AddOrUpdate
  },
  data() {
    return {
      timeOut: null,
      timeInterval: null,
      payImg: null,
      payInfoDataRules: payInfoDataRules,
      dataForm: {
        sceneId: 0,
        sceneName: '',
        payMode: '',
        payModeItem: null,
        appid: '',
        enable: 0,
        remark: ''
      },
      dataList: [],
      payModeList: [],
      payModeOPtion: {},
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: [],
      addOrUpdateVisible: false,
      outTradeNo: null,
      isShow: true,
      payInfoForm: {
        outTradeNo: '1234567',
        appid: '',
        mchId: '777290058172927',
        deviceInfo: '这是一个测试',
        productId: '地下停车场',
        body: '这是一个测试',
        detail: '这是一个测试',
        totalFee: null,
        tradeType: '支付',
        tradeStatus: '0',
        buyerId: '',
        buyerUserName: '',
        transactionId: ''
      }
    }
  },
  created() {
    // this.getPayModeList()
  },
  mounted() {
    // this.getDataList()
  },
  methods: {
    // 支付方式
    getPayModeList() {
      api.cfgVpaymode.list({}).then(res => {
        if (res && res.data && res.data.code === 0) {
          this.payModeList = res.data.payModeList
          const tempPayModeOPtion = []
          this.payModeList.forEach((item, index, array) => {
            tempPayModeOPtion[item.payMode] = item.alias
          })
          this.payModeOPtion = tempPayModeOPtion
        } else {
          this.$message.error(res.data.msg)
        }
      })
    },
    // 获取数据列表
    getDataList() {
      const fectData = {
        params: {
          'page': '' + this.pageIndex,
          'limit': '' + this.pageSize
        }
      }
      this.dataListLoading = true

      api.cfgScene.list({ data: fectData }).then(res => {
        if (res && res.data && res.data.code === 0) {
          this.dataListLoading = false
          this.dataList = res.data.page.list
          this.totalPage = res.data.page.totalCount
        } else {
          this.$message.error(res.data.msg)
        }
      })
    },
    queryPayInfo() {
      const fectData = {
        outTradeNo: this.payInfoForm.outTradeNo,
        payMode: 'wechat' }

      api.payClientPayOrder.queryOrder({ data: fectData }).then(res => {
        console.log(res)
        if (res && res.data) {
          if (res.data.body.tradeStatus === '2') {
            this.$router.push({
              name: 'paySuccess',
              params: {}
            })
          }
        } else {
          this.$message.error(res.data.msg)
        }
      })
    },
    clearIntervals() {
      clearInterval(this.timeInterval)
    },
    // 表单提交
    dataFormSubmit() {
      this.$refs['payInfoForm'].validate((valid) => {
        if (valid) {
          const fectData = this.payInfoForm
          //, responseType: 'arraybuffer'
          api.payClientPayOrder.createAllQrCode({ data: fectData }).then(res => {
            console.log(res)
            console.log(res.data)

            if (res && res.data) {
              // const datas = res.data
              // this.payImg =
              //   'data:image/png;base64,' +
              //   btoa(
              //     new Uint8Array(datas).reduce(
              //       (data, byte) => data + String.fromCharCode(byte),
              //       ''))
              this.payImg = 'data:image/png;base64,' + res.data.bytes

              this.isShow = false
              this.payInfoForm.outTradeNo = res.data.outTradeNo
              this.timeInterval = setInterval(this.queryPayInfo, 3000)
              this.timeOut = setTimeout(this.clearIntervals, 3 * 60 * 1000)
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
<style lang="scss" scoped>
.app-footer{
  width: 100%;
  padding-top: 10px;
  display: table;
  .footer-font{
    width: 100%;
    font-size: 32px;
    text-align: center;
    display: table-cell;
    vertical-align: middle;
  }
}
.app-title{
  width: 100%;
  padding-top: 40px;
  display: table;
  .title-font{
    color: blue;
    width: 100%;
    font-size: 32px;
    text-align: center;
    display: table-cell;
    vertical-align: middle;
  }
}
.app-container {
  display: table;
  width: 100%;
  margin: 10px  auto;
  padding: 10px;
  .left{
    display: table-cell;
    width: 25%;
  }
  .right{
    display: table-cell;
    width: 25%;
  }
  .app-div{
    margin-top: 30px;
    // width:420px;
  }
  .roles-table {
    margin-top: 330px;
  }
  .permission-tree {
    margin-bottom: 30px;
  }
}
</style>
