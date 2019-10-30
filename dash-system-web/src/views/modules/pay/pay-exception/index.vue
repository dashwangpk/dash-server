<template>
  <div class="app-container">
    <el-card>
      <el-form ref="listQuery" :model="listQuery" label-width="130px">
        <el-row>
          <el-col :span="6">
            <el-form-item label="订单号：" prop="outTradeNo">
              <el-input
                v-model="listQuery.outTradeNo"
                clearable
                size="mini"
                @keyup.enter.native="getList()"
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="患者姓名：" prop="patientName">
              <el-input
                v-model="listQuery.patientName"
                clearable
                size="mini"
                @keyup.enter.native="getList()"
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="就诊卡号：" prop="hisSn">
              <el-input
                v-model="listQuery.hisSn"
                clearable
                size="mini"
                @keyup.enter.native="getList()"
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item>
              <el-button type="primary" icon="iconfont iconchaxun" @click="getList()">查询</el-button>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="6">
            <el-form-item label="日期：">
              <el-date-picker
                v-model="listQuery.dataValue"
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
        </el-row>
      </el-form>
    </el-card>

    <el-table
      :data="list"
      :cell-class-name="changeCellStyle"
      border
      fit
      highlight-current-row
      :header-cell-style="{background: '#FFFFFF'}"
    >
      <el-table-column label="基本信息" align="center">
        <el-table-column label="患者姓名" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.patient_name }}</span>
          </template>
        </el-table-column>
        <el-table-column label="就诊号/住院号" align="center" min-width="120px" show-overflow-tooltip>
          <template slot-scope="scope">
            <span>{{ scope.row.his_sn }}</span>
          </template>
        </el-table-column>
      </el-table-column>

      <el-table-column label="HIS方" align="center">
        <el-table-column label="支付方式" align="center">
          <template slot-scope="scope">
            <span>{{ filterPayMode[scope.row.pay_mode] }}</span>
          </template>
        </el-table-column>
        <el-table-column label="订单号" align="center" show-overflow-tooltip>
          <template slot-scope="scope">
            <span>{{ scope.row.out_trade_no }}</span>
          </template>
        </el-table-column>
        <el-table-column label="交易金额" header-align="center" align="right" show-overflow-tooltip>
          <template slot-scope="scope">
            <span>{{ scope.row.total_fee }}</span>
          </template>
        </el-table-column>
        <el-table-column label="交易状态" align="center">
          <template slot-scope="scope">
            <span>{{ filterTradeStatus[scope.row.trade_status] }}</span>
          </template>
        </el-table-column>
        <el-table-column label="交易时间" align="center" width="140" show-overflow-tooltip>
          <template slot-scope="scope">
            <span>{{ scope.row.start_time }}</span>
          </template>
        </el-table-column>
      </el-table-column>

      <el-table-column label="支付方" align="center">
        <el-table-column label="交易金额" header-align="center" align="right" show-overflow-tooltip>
          <template slot-scope="scope">
            <span>{{ scope.row.total_fee_third }}</span>
          </template>
        </el-table-column>
        <el-table-column label="交易状态" align="center">
          <template slot-scope="scope">
            <span>{{ filterTradeStatus[scope.row.trade_status_third] }}</span>
          </template>
        </el-table-column>
        <el-table-column label="交易号" align="center" show-overflow-tooltip>
          <template slot-scope="scope">
            <span>{{ scope.row.transaction_id_third }}</span>
          </template>
        </el-table-column>
        <el-table-column label="买家支付号" align="center" min-width="120px" show-overflow-tooltip>
          <template slot-scope="scope">
            <span>{{ scope.row.buyer_id }}</span>
          </template>
        </el-table-column>
        <el-table-column label="买家名称" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.buyer_user_name }}</span>
          </template>
        </el-table-column>
        <el-table-column label="交易时间" align="center" width="140" show-overflow-tooltip>
          <template slot-scope="scope">
            <span>{{ scope.row.end_time }}</span>
          </template>
        </el-table-column>
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        min-width="120"
        label="操作"
      >
        <template slot-scope="scope">
          <el-button plain type="primary" size="mini" @click="checkHandle(scope.row)">核对</el-button>
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
    <check-pay-exception v-if="checkPayExceptionVisible" ref="checkPayException" @refreshDataList="getList" />
  </div>
</template>

<script>
import api from '@/api'
import { parseTime } from '@/utils'
import CheckPayException from './check-pay-exception'

export default {
  name: 'PayException',
  components: {
    CheckPayException
  },
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
      checkPayExceptionVisible: false,
      tableKey: 0,
      list: [],
      listLoading: true,
      filterPayMode: {},
      filterTradeStatus: {},
      tradeStatusOptions: {},
      listQuery: {
        dataValue: [parseTime(new Date().getTime() - 3600 * 1000 * 24 * 7), parseTime(new Date().getTime() - 3600 * 1000 * 24)],
        params: {
          pageIndex: 1,
          pageSize: 10,
          totalPage: 0
        }
      }
    }
  },
  created() {
    this.getParams()
    this.getPayModeType()
    this.getTradeStatusType()
  },
  activated() {
    if (this.$route.params.data && !this.$route.meta.noCache) {
      this.getParams()
    }
  },
  methods: {
    checkHandle(order) {
      this.checkPayExceptionVisible = true
      this.$nextTick(() => {
        this.$refs.checkPayException.init(order, this.filterTradeStatus, this.filterPayMode)
      })
    },

    getParams() {
      if (this.$route.params.data) {
        this.listQuery.dataValue = [this.$route.params.data.billDate, this.$route.params.data.billDate]
      }
      this.getList()
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

    getTradeStatusType() {
      api.cfgAa10
        .wholeoptions({ data: { aaa100: 'trade_status' }})
        .then(res => {
          if (res && res.data && res.data.code === 0) {
            const tempTypeOptions = {}
            this.tradeStatusOptions = res.data.wholeoptions
            res.data.wholeoptions.forEach((item, index, array) => {
              tempTypeOptions[item.aaa102] = item.aaa103
            })
            this.filterTradeStatus = tempTypeOptions
          }
        })
    },

    changeCellStyle({ row, column, rowIndex, columnIndex }) {
      if (columnIndex === 7 || columnIndex === 9) {
        return 'cellColor'
      }
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
      if (this.listQuery.dataValue.length > 0) {
        Object.assign(fetchData, {
          startDate: this.listQuery.dataValue[0],
          endDate: this.listQuery.dataValue[1]
        })
      }

      api.payRefundTrade.listPayExceptionInfo({ data: fetchData }).then(res => {
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
