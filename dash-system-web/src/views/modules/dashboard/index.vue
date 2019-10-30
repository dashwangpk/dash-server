<template>
  <div class="app-container" :style="{height:height}">
    <num-group :panel-data="numData" />
    <div style="position:relative;">
      <div style="width:100%;height:0px;">
        <div style="position: absolute;right: 0px;top:6px;z-index:10;">
          <el-date-picker
            v-model="pieDateRange"
            type="daterange"
            align="left"
            value-format="yyyy-MM-dd"
            unlink-panels
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :picker-options="pickerOptions"
            @change="pieStatistic()"
          />
        </div>
      </div>
    </div>
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="支付方式" name="second">
        <el-row v-if=" tabName ='second'" :gutter="18">
          <el-col :xs="24" :sm="24" :lg="12">
            <div class="chart-wrapper">
              <pie-chart :pie-data="payModeData" />
            </div>
          </el-col>
          <el-col :xs="24" :sm="24" :lg="12">
            <div class="chart-wrapper">
              <pie-chart :pie-data="payModeCountData" />
            </div>
          </el-col>
        </el-row>
      </el-tab-pane>
      <el-tab-pane label="费用类型" name="third">
        <el-row v-if=" tabName ='third'" :gutter="18">
          <el-col :xs="24" :sm="24" :lg="12">
            <div class="chart-wrapper">
              <pie-chart :pie-data="feeTypeData" />
            </div>
          </el-col>
          <el-col :xs="24" :sm="24" :lg="12">
            <div class="chart-wrapper">
              <pie-chart :pie-data="feeTypeCountData" />
            </div>
          </el-col>
        </el-row>
      </el-tab-pane>
      <el-tab-pane label="支付渠道" name="first">
        <el-row v-if="tabName ='first'" :gutter="18">
          <el-col :xs="24" :sm="24" :lg="12">
            <div class="chart-wrapper">
              <pie-chart :pie-data="payChannelData" />
            </div>
          </el-col>
          <el-col :xs="24" :sm="24" :lg="12">
            <div class="chart-wrapper">
              <pie-chart :pie-data="payChannelCountData" />
            </div>
          </el-col>
        </el-row>
      </el-tab-pane>
    </el-tabs>

  </div>
</template>

<script>
// import LineChart from './components/LineChart'
import PieChart from './components/BPieChart'
// import PanelGroup from './components/PanelGroup'
import NumGroup from './components/NumGroup'
import api from '@/api'
import { parseTime } from '@/utils'

export default {
  name: 'Home',
  components: { PieChart, NumGroup },
  data() {
    return {
      tabName: 'second',
      activeName: 'second',
      pickerOptions: {
        shortcuts: [
          {
            text: '最近一周',
            onClick(picker) {
              const end = new Date()
              const start = new Date()
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
              picker.$emit('pick', [start, end])
            }
          },
          {
            text: '最近一个月',
            onClick(picker) {
              const end = new Date()
              const start = new Date()
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
              picker.$emit('pick', [start, end])
            }
          },
          {
            text: '最近三个月',
            onClick(picker) {
              const end = new Date()
              const start = new Date()
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
              picker.$emit('pick', [start, end])
            }
          }
        ]
      },
      linechartTypeOptions: [
        { label: '支付渠道', value: 'pay_channel' },
        { label: '支付方式', value: 'pay_mode' },
        { label: '费用类型', value: 'fee_type' }
      ],
      payChannelData: { nameData: [], seriesData: [] },
      feeTypeData: { nameData: [], seriesData: [] },
      payModeData: { nameData: [], seriesData: [] },
      payChannelCountData: { nameData: [], seriesData: [] },
      feeTypeCountData: { nameData: [], seriesData: [] },
      payModeCountData: { nameData: [], seriesData: [] },
      dateRange: [parseTime(new Date().getTime() - 3600 * 1000 * 24 * 7), parseTime(new Date().getTime() - 3600 * 1000 * 24)],
      pieDateRange: [parseTime(new Date().getTime() - 3600 * 1000 * 24), parseTime(new Date().getTime() - 3600 * 1000 * 24)],
      linechartType: 'pay_channel',
      lineChartData: {
        nameData: [],
        chartData: {},
        rangeDate: []
      },
      panelData: {
        incomeFee: 0,
        incomeCount: 0,
        refundFee: 0,
        refundCount: 0
      },
      numData: {
        medicalInsFee: 0,
        chinaPayFee: 0,
        aliFee: 0,
        wxFee: 0,
        TotalFee: 0
      },
      emptyPanelData: {
        incomeFee: 0,
        incomeCount: 0,
        refundFee: 0,
        refundCount: 0
      },
      channelTypeOptions: {},
      feeTypeOptions: {},
      payModeTypeOptions: {},
      height: '720px'
    }
  },
  computed: {
  },
  created() {
    this.initTypeOptions()
  },
  mounted() {
    this.accountSum()
    this.pieStatistic()
    this.linechartIncomeFee()
    if (window.screen.height > 900) {
      this.height = window.screen.height - 80 + 'px'
    }
  },
  methods: {
    handleClick(tab, event) {
      this.tabName = tab.name
    },
    initTypeOptions() {
      this.getChannelType()
      this.getFeeType()
      this.getPayModeType()
    },
    getChannelType() {
      api.cfgChannel.wholeList({}).then(res => {
        if (res && res.data && res.data.code === 0) {
          const tempTypeOptions = {}
          res.data.wholeList.forEach((item, index, array) => {
            tempTypeOptions[item.channelSn] = item.name
          })
          this.channelTypeOptions = tempTypeOptions
        }
      })
    },
    getFeeType() {
      api.cfgAa10.wholeoptions({ data: { aaa100: 'fee_type' }}).then(res => {
        if (res && res.data && res.data.code === 0) {
          const tempTypeOptions = {}
          res.data.wholeoptions.forEach((item, index, array) => {
            tempTypeOptions[item.aaa102] = item.aaa103
          })
          this.feeTypeOptions = tempTypeOptions
        }
      })
    },
    getPayModeType() {
      api.cfgAa10.wholeoptions({ data: { aaa100: 'pay_mode' }}).then(res => {
        if (res && res.data && res.data.code === 0) {
          const tempTypeOptions = {}
          res.data.wholeoptions.forEach((item, index, array) => {
            tempTypeOptions[item.aaa102] = item.aaa103
          })
          this.payModeTypeOptions = tempTypeOptions
        }
      })
    },
    pieStatistic() {
      this.billSum()
      this.getStatisticChannel()
      this.getStatisticFeeType()
      this.getStatisticPayMode()
    },
    accountSum() {
      api.payBillAccount.accountSum({ data: {}}).then(res => {
        if (res && res.data && res.data.success) {
          const sums = res.data.body
          let totalFee = 0

          for (let i = 0; i < sums.length; i++) {
            const sum = sums[i]
            if (sum.pay_mode === 'alipay') {
              this.numData.aliFee = sum.current_fee
            } else if (sum.pay_mode === 'unionpay') {
              this.numData.chinaPayFee = sum.current_fee
            } else if (sum.pay_mode === 'wechat') {
              this.numData.wxFee = sum.current_fee
            }
            totalFee = totalFee + sum.current_fee
          }

          this.numData.TotalFee = totalFee
        } else {
          this.$message.error(res.data.msg)
        }
      })
    },
    getStatisticChannel() {
      const fetchData = {
        'beginTime': this.pieDateRange[0],
        'endTime': this.pieDateRange[1]
      }
      api.payStatistic.payChannel({ data: fetchData }).then(res => {
        if (res && res.data && res.data.success) {
          this.payChannelData = {}
          this.payChannelData.nameData = []
          this.payChannelData.seriesData = []
          this.payChannelData.title = '支付渠道-金额'

          this.payChannelCountData = {}
          this.payChannelCountData.nameData = []
          this.payChannelCountData.seriesData = []
          this.payChannelCountData.title = '支付渠道-笔数'

          const channelDataArray = res.data.body
          for (let i = 0; i < channelDataArray.length; i++) {
            const payChannel = channelDataArray[i].payChannel
            const keyName = this.channelTypeOptions[payChannel] ? this.channelTypeOptions[payChannel] : payChannel
            const incomeFee = channelDataArray[i].incomeFee
            const incomeCount = channelDataArray[i].incomeCount

            this.payChannelData.nameData.push(keyName)
            this.payChannelData.seriesData.push({ value: incomeFee, name: keyName })

            this.payChannelCountData.nameData.push(keyName)
            this.payChannelCountData.seriesData.push({ value: incomeCount, name: keyName })
          }
        } else {
          this.$message.error(res.data.msg)
        }
      })
    },
    getStatisticFeeType() {
      const fetchData = {
        'beginTime': this.pieDateRange[0],
        'endTime': this.pieDateRange[1]
      }
      api.payStatistic.feeType({ data: fetchData }).then(res => {
        if (res && res.data && res.data.success) {
          this.feeTypeData = {}
          this.feeTypeData.nameData = []
          this.feeTypeData.seriesData = []
          this.feeTypeData.title = '费用类型-金额'

          this.feeTypeCountData = {}
          this.feeTypeCountData.nameData = []
          this.feeTypeCountData.seriesData = []
          this.feeTypeCountData.title = '费用类型-笔数'

          const feeDataArray = res.data.body
          for (let i = 0; i < feeDataArray.length; i++) {
            const feeType = feeDataArray[i].feeType
            const keyName = this.feeTypeOptions[feeType] ? this.feeTypeOptions[feeType] : feeType
            const incomeFee = feeDataArray[i].incomeFee
            const incomeCount = feeDataArray[i].incomeCount

            this.feeTypeData.nameData.push(keyName)
            this.feeTypeData.seriesData.push({ value: incomeFee, name: keyName })

            this.feeTypeCountData.nameData.push(keyName)
            this.feeTypeCountData.seriesData.push({ value: incomeCount, name: keyName })
          }
        } else {
          this.$message.error(res.data.msg)
        }
      })
    },
    getStatisticPayMode() {
      const fetchData = {
        'beginTime': this.pieDateRange[0],
        'endTime': this.pieDateRange[1]
      }
      api.payStatistic.payMode({ data: fetchData }).then(res => {
        if (res && res.data && res.data.success) {
          this.payModeData = {}
          this.payModeData.nameData = []
          this.payModeData.seriesData = []
          this.payModeData.title = '支付方式-金额'

          this.payModeCountData = {}
          this.payModeCountData.nameData = []
          this.payModeCountData.seriesData = []
          this.payModeCountData.title = '支付方式-笔数'

          const payDataArray = res.data.body
          for (let i = 0; i < payDataArray.length; i++) {
            const payMode = payDataArray[i].payMode
            const keyName = this.payModeTypeOptions[payMode] ? this.payModeTypeOptions[payMode] : payMode
            const incomeFee = payDataArray[i].incomeFee
            const incomeCount = payDataArray[i].incomeCount

            this.payModeData.nameData.push(keyName)
            this.payModeData.seriesData.push({ value: incomeFee, name: keyName })

            this.payModeCountData.nameData.push(keyName)
            this.payModeCountData.seriesData.push({ value: incomeCount, name: keyName })
          }
        } else {
          this.$message.error(res.data.msg)
        }
      })
    },
    billSum() {
      const fetchData = {
        beginTime: this.pieDateRange[0],
        endTime: this.pieDateRange[1]
      }
      api.payStatistic.billSum({ data: fetchData }).then(res => {
        if (res && res.data && res.data.success) {
          if (res.data.body) {
            this.panelData = res.data.body
          } else {
            this.panelData = this.emptyPanelData
          }
        } else {
          this.$message.error(res.data.msg)
        }
      })
    },
    linechartIncomeFee() {
      // linechartType : pay_channel,pay_mode, fee_type
      const fectData = {
        linechartType: this.linechartType,
        beginTime: this.dateRange[0],
        endTime: this.dateRange[1]
      }
      this.lineChartData = {
        nameData: [],
        chartData: {},
        rangeDate: []
      }
      const tempLineChartData = {}
      api.payStatistic.linechartIncomeFee({ data: fectData }).then(res => {
        if (res && res.data && res.data.success) {
          tempLineChartData.chartData = {}
          tempLineChartData.rangeDate = res.data.body.rangeDateList
          tempLineChartData.nameData = []
          const tempChartData = res.data.body.lineCharData
          const tempNameData = Object.keys(res.data.body.lineCharData)

          // { label: '支付渠道', value: 'pay_channel' },
          // { label: '支付方式', value: 'pay_mode' },
          // { label: '费用类型', value: 'fee_type' }

          if (this.linechartType === 'pay_channel') {
            tempLineChartData.title = '支付渠道'
            for (let i = 0; i < tempNameData.length; i++) {
              const keyName = this.channelTypeOptions[tempNameData[i]] ? this.channelTypeOptions[tempNameData[i]] : tempNameData[i]
              tempLineChartData.chartData[keyName] = tempChartData[tempNameData[i]]
              tempLineChartData.nameData.push(keyName)
            }
          }

          if (this.linechartType === 'pay_mode') {
            tempLineChartData.title = '支付方式'
            for (let i = 0; i < tempNameData.length; i++) {
              const keyName = this.payModeTypeOptions[tempNameData[i]] ? this.payModeTypeOptions[tempNameData[i]] : tempNameData[i]
              tempLineChartData.chartData[keyName] = tempChartData[tempNameData[i]]
              tempLineChartData.nameData.push(keyName)
            }
          }

          if (this.linechartType === 'fee_type') {
            tempLineChartData.title = '费用类型'
            for (let i = 0; i < tempNameData.length; i++) {
              const keyName = this.feeTypeOptions[tempNameData[i]] ? this.feeTypeOptions[tempNameData[i]] : tempNameData[i]
              tempLineChartData.chartData[keyName] = tempChartData[tempNameData[i]]
              tempLineChartData.nameData.push(keyName)
            }
          }
          this.lineChartData = tempLineChartData
        } else {
          this.$message.error(res.data.msg)
        }
      })
    }
  }
}
</script>
<style lang="scss" scoped>
.dashboard-editor-container {
  padding: 32px;
  background-color: rgb(240, 242, 245);
  .chart-wrapper {
    background: #fff;
    padding: 12px 12px 0;
    margin-bottom: 32px;
  }
}
.search-container{
  margin-bottom: 0px;
  margin-top: 0px;
  float:right;
}
.app-container {
   height: 100%;
   background-color: rgb(240, 242, 245);
   padding: 16px;
  .roles-table {
    margin-top: 30px;
  }
  .permission-tree {
    margin-bottom: 30px;
  }
  .chart-wrapper {
    background: #fff;
    padding: 12px 12px 12px 12px;
    margin-bottom: 32px;
  }
  .el-card.is-always-shadow {
    -webkit-box-shadow: 0 0px 0px 0 rgba(0, 0, 0, 0.1);
    box-shadow: 0 0px 0px 0 rgba(0, 0, 0, 0.1);
  }
  .el-card {
    border: 1px solid #d9dee7;
  }
  .el-card box-card is-always-shadow{
    .el-card__header {
      border-bottom: 1px solid #d9dee7;
    }
  }
}
.box-card {
  background-color: rgb(240, 242, 245);
}
.box-cardl {
  background-color: rgb(240, 242, 245);
  margin-top: 30px;
}
</style>
<style>
.filter-container .el-date-editor .el-range-separator{
    width:12px;
    padding:0px;
}
.filter-container .el-date-editor{
    width: 240px;
}
.search-container .el-date-editor .el-range-separator{
    width:12px;
    padding:0px;
}
.search-container .el-date-editor{
    width: 240px;
    top: 1px;
}
</style>

