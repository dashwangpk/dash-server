<template>
  <div class="app-container">
    <el-card>
      <el-button type="primary" icon="iconfont iconchaxun" @click="getList()">查询</el-button>
    </el-card>

    <el-table
      :data="list"
      :cell-class-name="changeCellStyle"
      border
      fit
      highlight-current-row
    >
      <el-table-column label="订单ID" min-width="120" align="center" show-overflow-tooltip>
        <template slot-scope="scope">
          <span>{{ scope.row.outTradeNo }}</span>
        </template>
      </el-table-column>
      <el-table-column label="商品名称" min-width="120" align="center" show-overflow-tooltip>
        <template slot-scope="scope">
          <span>{{ scope.row.deviceInfo }}</span>
        </template>
      </el-table-column>
      <el-table-column label="支付金额" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.totalFee }}</span>
        </template>
      </el-table-column>
      <el-table-column label="支付方式" width="80" align="center" show-overflow-tooltip>
        <template slot-scope="scope">
          <span>{{ filterPayMode[scope.row.payMode] }}</span>
        </template>
      </el-table-column>
      <el-table-column label="用户ID" min-width="120" align="center" show-overflow-tooltip>
        <template slot-scope="scope">
          <span>{{ scope.row.buyerId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="订单状态" align="center">
        <template slot-scope="scope">
          <span>{{ filterTradeStatus[scope.row.tradeStatus] }}</span>
        </template>
      </el-table-column>
      <el-table-column label="支付开始时间" min-width="110" align="center" show-overflow-tooltip>
        <template slot-scope="scope">
          <span>{{ scope.row.timeStart }}</span>
        </template>
      </el-table-column>
      <el-table-column label="支付结束时间" min-width="110" align="center" show-overflow-tooltip>
        <template slot-scope="scope">
          <span>{{ scope.row.timeEnd }}</span>
        </template>
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        label="退款"
        min-width="120"
      >
        <template slot-scope="scope">
          <el-button v-if="scope.row.tradeStatus == 2" plain type="primary" size="mini" @click="handleUpdate(scope)">退款</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      :current-page="listQuery.params.pageIndex"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="listQuery.params.pageSize"
      :total="listQuery.params.totalPage"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="sizeChangeHandle"
      @current-change="currentChangeHandle"
    />
  </div>
</template>

<script>
import api from '@/api'
import { parseTime } from '@/utils'

export default {
  name: 'PayRecord',
  data() {
    return {
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
      listQuery: {
        dataValue: [
          parseTime(new Date().getTime() - 3600 * 1000 * 24 * 7),
          parseTime(new Date().getTime() - 3600 * 1000 * 24)
        ],
        payChannel: '',
        feeType: '',
        tradeStatus: '',
        outTradeNo: '',
        hisSn: '',
        params: {
          pageIndex: 1,
          pageSize: 10,
          totalPage: 0
        }
      },
      filterPayMode: {}
    }
  },
  created() {
    this.getPayModeType()
    this.getParams()
    this.getPayChannelType()
    this.getFeeTypeType()
    this.getTradeStatusType()
  },
  activated() {
    if (this.$route.params.data && !this.$route.meta.noCache) {
      this.getParams()
    }
  },
  methods: {
    handleUpdate(scope) {
      const fectData = {
        'outTradeNo': scope.row.outTradeNo,
        'refundFee': scope.row.totalFee,
        'refundDesc': '退款操作'
      }

      api.payPayOrder.refund({ data: fectData }).then(
        res => {
          if (res && res.data && res.data.code === 0) {
            this.$message({
              message: '退款成功',
              type: 'sucess'
            })
            this.getList()
          }
        },
        err => {
          this.$message({
            message: err,
            type: 'error'
          })
        }
      )
    },
    getPayModeType() {
      api.cfgAa10.wholeoptions({ data: { aaa100: 'pay_mode' }}).then(res => {
        if (res && res.data && res.data.code === 0) {
          const tempTypeOptions = {}
          res.data.wholeoptions.forEach((item, index, array) => {
            tempTypeOptions[item.aaa102] = item.aaa103
          })
          this.filterPayMode = tempTypeOptions
        }
      })
    },

    getParams() {
      if (this.$route.params.data) {
        this.listQuery.dataValue = [
          this.$route.params.data.billDate,
          this.$route.params.data.billDate
        ]
      }

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

    changeCellStyle({ row, column, rowIndex, columnIndex }) {
      // if (columnIndex === 7 || columnIndex === 9) {
      //   return "cellColor";
      // }
    },

    sizeChangeHandle(val) {
      this.listQuery.params.pageSize = val
      this.listQuery.params.pageIndex = 1
      this.getList()
    },

    currentChangeHandle(val) {
      this.listQuery.params.pageIndex = val
      this.getList()
    },

    getList() {
      const fetchData = this.listQuery
      // if (this.listQuery.dataValue.length > 0) {
      //   Object.assign(fetchData, {
      //     startDate: this.listQuery.dataValue[0],
      //     endDate: this.listQuery.dataValue[1]
      //   })
      // }

      if (this.listQuery.dataValue.length > 0) {
        Object.assign(fetchData, {
          startDate: null,
          endDate: null
        })
      }

      api.payPatternPlatform.platformOrderList({ data: fetchData }).then(res => {
        console.log(res)
        if (res.data && res.data.body) {
          this.list = res.data.body.list
          this.listQuery.params.totalPage = res.data.body.totalCount
        } else {
          this.$message({
            message: res.data.message,
            type: 'error'
          })
        }
      })
    },

    handleDownload() {
      this.downloadLoading = true
      import('@/vendor/Export2Excel').then(excel => {
        const tHeader = ['timestamp', 'title', 'type', 'importance', 'status']
        const filterVal = [
          'timestamp',
          'title',
          'type',
          'importance',
          'status'
        ]
        const data = this.formatJson(filterVal, this.list)
        excel.export_json_to_excel({
          header: tHeader,
          data,
          filename: 'table-list'
        })
        this.downloadLoading = false
      })
    },
    formatJson(filterVal, jsonData) {
      return jsonData.map(v =>
        filterVal.map(j => {
          if (j === 'timestamp') {
            // return parseTime(v[j])
          } else {
            return v[j]
          }
        })
      )
    }
  }
}
</script>

