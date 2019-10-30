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
              <el-select v-model="queryForm.appid" style="width:100%" clearable>
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
            <el-form-item label="退款状态：">
              <el-select v-model="queryForm.refundStatus" style="width:100%" clearable>
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
      <el-table-column label="退款ID" min-width="120" align="center" show-overflow-tooltip>
        <template slot-scope="scope">
          <span>{{ scope.row.outRefundNo }}</span>
        </template>
      </el-table-column>
      <el-table-column label="订单ID" min-width="120" align="center" show-overflow-tooltip>
        <template slot-scope="scope">
          <span>{{ scope.row.outTradeNo }}</span>
        </template>
      </el-table-column>
      <el-table-column label="应用ID" min-width="120" align="center" show-overflow-tooltip>
        <template slot-scope="scope">
          <span>{{ scope.row.appid }}</span>
        </template>
      </el-table-column>
      <el-table-column label="商户号" min-width="120" align="center" show-overflow-tooltip>
        <template slot-scope="scope">
          <span>{{ scope.row.mchId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="退款金额" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.totalFee }}</span>
        </template>
      </el-table-column>
      <el-table-column label="退款原因" align="center" min-width="100" show-overflow-tooltip>
        <template slot-scope="scope">
          <span>{{ scope.row.refundDesc }}</span>
        </template>
      </el-table-column>
      <el-table-column label="退款状态" align="center">
        <template slot-scope="scope">
          <span>{{ filterTradeStatus[scope.row.refundStatus] }}</span>
        </template>
      </el-table-column>
      <el-table-column label="退款方式" width="80" align="center" show-overflow-tooltip>
        <template slot-scope="scope">
          <span>{{ filterPayMode[scope.row.payMode] }}</span>
        </template>
      </el-table-column>
      <el-table-column label="买家ID" min-width="100" align="center" show-overflow-tooltip>
        <template slot-scope="scope">
          <span>{{ scope.row.buyerId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="退款发起时间" min-width="110" align="center" show-overflow-tooltip>
        <template slot-scope="scope">
          <span>{{ scope.row.timeStart }}</span>
        </template>
      </el-table-column>
      <el-table-column label="退款完成时间" min-width="110" align="center" show-overflow-tooltip>
        <template slot-scope="scope">
          <span>{{ scope.row.timeEnd }}</span>
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
  name: 'RefundRecord',
  data() {
    return {
      queryForm: {
        timeStartInterval: [],
        payMode: null,
        outTradeNo: null,
        appid: null,
        refundStatus: null
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
    this.getParams()
  },
  methods: {
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
        refundStatus: null
      }
    },
    getParams() {
      if (this.$route.params.data && this.$route.params.data.outTradeNo) {
        this.cancelParams()
        this.queryForm.outTradeNo = this.$route.params.data.outTradeNo
        this.getList()
      }
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
        .wholeoptions({ data: { aaa100: 'refund_status' }})
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

      api.payPatternPlatform.platformRefundList({ data: fetchData }).then(res => {
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

