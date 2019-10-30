<template>
  <div class="app-container">
    <el-card>
      <el-form ref="queryForm" :model="queryForm" label-width="120px" style="padding:0,margin:0">
        <el-row>
          <el-col :span="6">
            <el-form-item label="订单ID：">
              <el-input v-model="queryForm.outTradeNo" clearable />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="应用：">
              <el-select v-model="queryForm.appId" style="width:100%" clearable>
                <el-option
                  v-for="item in appInfos"
                  :key="item.key"
                  :label="item.label"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="支付状态：">
              <el-select v-model="queryForm.tradeStatus" style="width:100%" clearable>
                <el-option
                  v-for="item in tradeStatusOptions"
                  :key="item.key"
                  :label="item.label"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <div style="margin-left: 20px;">
              <el-button type="primary" icon="iconfont iconchaxun" @click="getList()">查询</el-button>
              <el-button type="info" icon="iconfont iconchaxun" @click="cancelParams()">取消</el-button>
            </div>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6">
            <el-form-item label="支付方式：">
              <el-select v-model="queryForm.payMode" style="width:100%" clearable>
                <el-option
                  v-for="item in payModes"
                  :key="item.key"
                  :label="item.label"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="时间：">
              <el-date-picker
                v-model="queryForm.timeStartInterval"
                type="daterange"
                align="left"
                value-format="yyyy-MM-dd"
                unlink-panels
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                :picker-options="pickerOptions"
              />
            </el-form-item>
          </el-col>
          <el-col :span="6" />
          <el-col :span="6" />
        </el-row>
      </el-form>
    </el-card>

    <el-table
      :data="list"
      border
      fit
      highlight-current-row
    >
      <el-table-column label="订单ID" min-width="120" align="center" show-overflow-tooltip>
        <template slot-scope="scope">
          <template v-if="scope.row.refunded == 0">
            <span>{{ scope.row.outTradeNo }}</span>
          </template>
          <template v-else>
            <span style="color:red;">{{ scope.row.outTradeNo }}</span>
          </template>
        </template>
      </el-table-column>
      <el-table-column label="应用ID" min-width="120" align="center" show-overflow-tooltip>
        <template slot-scope="scope">
          <template v-if="scope.row.refunded == 0">
            <span>{{ scope.row.appId }}</span>
          </template>
          <template v-else>
            <span style="color:red;">{{ scope.row.appId }}</span>
          </template>
        </template>
      </el-table-column>
      <el-table-column label="商户号" min-width="120" align="center" show-overflow-tooltip>
        <template slot-scope="scope">
          <template v-if="scope.row.refunded == 0">
            <span>{{ scope.row.mchId }}</span>
          </template>
          <template v-else>
            <span style="color:red;">{{ scope.row.mchId }}</span>
          </template>
        </template>
      </el-table-column>
      <el-table-column label="订单金额" align="center">
        <template slot-scope="scope">
          <template v-if="scope.row.refunded == 0">
            <span>{{ scope.row.totalFee }}</span>
          </template>
          <template v-else>
            <span style="color:red;">{{ scope.row.totalFee }}</span>
          </template>
        </template>
      </el-table-column>
      <el-table-column label="支付状态" align="center">
        <template slot-scope="scope">
          <template v-if="scope.row.refunded == 0">
            <span>{{ filterTradeStatus[scope.row.tradeStatus] }}</span>
          </template>
          <template v-else>
            <span style="color:red;">{{ filterTradeStatus[scope.row.tradeStatus] }}</span>
          </template>
        </template>
      </el-table-column>
      <el-table-column label="支付方式" width="80" align="center" show-overflow-tooltip>
        <template slot-scope="scope">
          <template v-if="scope.row.refunded == 0">
            <span>{{ filterPayMode[scope.row.payMode] }}</span>
          </template>
          <template v-else>
            <span style="color:red;">{{ filterPayMode[scope.row.payMode] }}</span>
          </template>
        </template>
      </el-table-column>
      <el-table-column label="买家ID" min-width="100" align="center" show-overflow-tooltip>
        <template slot-scope="scope">
          <template v-if="scope.row.refunded == 0">
            <span>{{ scope.row.buyerId }}</span>
          </template>
          <template v-else>
            <span style="color:red;">{{ scope.row.buyerId }}</span>
          </template>
        </template>
      </el-table-column>
      <el-table-column label="支付发起时间" min-width="110" align="center" show-overflow-tooltip>
        <template slot-scope="scope">
          <template v-if="scope.row.refunded == 0">
            <span>{{ scope.row.timeStart }}</span>
          </template>
          <template v-else>
            <span style="color:red;">{{ scope.row.timeStart }}</span>
          </template>
        </template>
      </el-table-column>
      <el-table-column label="支付完成时间" min-width="110" align="center" show-overflow-tooltip>
        <template slot-scope="scope">
          <template v-if="scope.row.refunded == 0">
            <span>{{ scope.row.timeEnd }}</span>
          </template>
          <template v-else>
            <span style="color:red;">{{ scope.row.timeEnd }}</span>
          </template>
        </template>
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        label="是否有退款"
        min-width="80"
      >
        <template slot-scope="scope">
          <template v-if="scope.row.refunded == 0">
            <span> 否</span>
          </template>
          <template v-else>
            <span style="color:red;cursor:pointer;" @click="handleToRefund(scope.row)">是</span>
          </template>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      :current-page="params.pageIndex"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="params.pageSize"
      :total="params.totalPage"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="sizeChangeHandle"
      @current-change="currentChangeHandle"
    />
  </div>
</template>

<script>
import api from '@/api'

export default {
  name: 'PayRecord',
  data() {
    return {
      queryForm: {
        timeStartInterval: [],
        payMode: null,
        outTradeNo: null,
        appId: null,
        tradeStatus: null
      },
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
      payChannelOptions: [],
      feeTypeOptions: [],
      tradeStatusOptions: [],
      filterPayChannel: {},
      filterFeeType: {},
      filterTradeStatus: {},
      tableKey: 0,
      list: [],
      listLoading: true,
      params: {
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0
      },
      filterPayMode: {},
      payModes: [],
      tradeStatus: [],
      appInfos: []
    }
  },
  created() {
    this.getAppInfos()
    this.getPayModeType()
    this.getParams()
    this.getPayChannelType()
    this.getFeeTypeType()
    this.getTradeStatusType()
  },
  activated() {

  },
  methods: {
    handleToRefund(row) {
      const datas = { outTradeNo: row.outTradeNo }
      this.$router.push({
        name: 'RefundRecord',
        params: { data: datas }
      })
    },
    getAppInfos() {
      api.cfgVpaymode.list({ data: { }}).then(res => {
        if (res && res.data && res.data.code === 0) {
          this.appInfos = []
          res.data.payModeList.forEach((item, index, array) => {
            this.appInfos.push({
              label: item.alias,
              key: item.appid
            })
          })
        }
      })
    },
    getPayModeType() {
      api.cfgAa10.wholeoptions({ data: { aaa100: 'pay_mode' }}).then(res => {
        if (res && res.data && res.data.code === 0) {
          this.payModes = []
          const tempTypeOptions = {}
          res.data.wholeoptions.forEach((item, index, array) => {
            this.payModes.push({
              label: item.aaa103,
              key: item.aaa102
            })
            tempTypeOptions[item.aaa102] = item.aaa103
          })
          this.filterPayMode = tempTypeOptions
        }
      })
    },
    cancelParams() {
      this.queryForm = {
        timeStartInterval: [],
        payMode: null,
        outTradeNo: null,
        appId: null,
        tradeStatus: null
      }
    },
    getParams() {
      this.getList()
    },
    getPayChannelType() {
      api.cfgChannel.wholeList({}).then(res => {
        if (res && res.data && res.data.code === 0) {
          this.payChannelOptions = []
          const tempTypeOptions = {}
          res.data.wholeList.forEach((item, index, array) => {
            this.payChannelOptions.push({
              label: item.name,
              key: item.channelSn
            })
            tempTypeOptions[item.channelSn] = item.name
          })
          this.filterPayChannel = tempTypeOptions
        }
      })
    },
    getFeeTypeType() {
      api.cfgAa10.wholeoptions({ data: { aaa100: 'fee_type' }}).then(res => {
        if (res && res.data && res.data.code === 0) {
          this.feeTypeOptions = []
          const tempTypeOptions = {}
          res.data.wholeoptions.forEach((item, index, array) => {
            if (item.aaa104 === '1') {
              this.feeTypeOptions.push({
                label: item.aaa103,
                key: item.aaa102
              })
            }
            tempTypeOptions[item.aaa102] = item.aaa103
          })
          this.filterFeeType = tempTypeOptions
        }
      })
    },
    getTradeStatusType() {
      api.cfgAa10
        .wholeoptions({ data: { aaa100: 'trade_status' }})
        .then(res => {
          if (res && res.data && res.data.code === 0) {
            this.tradeStatusOptions = []
            const tempTypeOptions = {}
            res.data.wholeoptions.forEach((item, index, array) => {
              if (item.aaa104 === '1') {
                this.tradeStatusOptions.push({
                  label: item.aaa103,
                  key: item.aaa102
                })
              }
              tempTypeOptions[item.aaa102] = item.aaa103
            })
            this.filterTradeStatus = tempTypeOptions
          }
        })
    },
    sizeChangeHandle(val) {
      this.params.pageSize = val
      this.params.pageIndex = 1
      this.getList()
    },
    currentChangeHandle(val) {
      this.params.pageIndex = val
      this.getList()
    },
    getList() {
      const fetchData = this.queryForm
      if (this.queryForm.timeStartInterval && this.queryForm.timeStartInterval.length > 0) {
        Object.assign(fetchData, {
          startDate: this.queryForm.timeStartInterval[0],
          endDate: this.queryForm.timeStartInterval[1]
        })
      } else {
        Object.assign(fetchData, {
          startDate: null,
          endDate: null
        })
      }
      fetchData.params = this.params

      api.payPatternPlatform.platformOrderList({ data: fetchData }).then(res => {
        if (res.data && res.data.body) {
          this.list = res.data.body.list
          this.params.totalPage = res.data.body.totalCount
        } else {
          this.$message({
            message: res.data.message,
            type: 'error'
          })
        }
      })
    }
  }
}
</script>

