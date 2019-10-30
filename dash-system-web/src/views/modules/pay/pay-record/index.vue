<template>
  <div class="app-container">
    <el-card>
      <el-form ref="listQuery" :model="listQuery" label-width="130px" style="padding:0,margin:0">
        <el-row>
          <el-col :span="6">
            <el-form-item label="订单号：">
              <el-input v-model="listQuery.outTradeNo" clearable @keyup.enter.native="getList()" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="就诊卡：">
              <el-input v-model="listQuery.hisSn" clearable @keyup.enter.native="getList()" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="渠道类型：">
              <el-select v-model="listQuery.payChannel" style="width:100%" clearable>
                <el-option
                  v-for="item in payChannelOptions"
                  :key="item.key"
                  :label="item.label"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item>
              <el-button
                type="primary"
                icon="iconfont iconchaxun"
                style="right:5px"
                @click="getList()"
              >查询</el-button>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="6">
            <el-form-item label="费用类型：">
              <el-select v-model="listQuery.feeType" style="width:100%" clearable>
                <el-option
                  v-for="item in feeTypeOptions"
                  :key="item.key"
                  :label="item.label"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="订单状态：">
              <el-select v-model="listQuery.tradeStatus" style="width:100%" clearable>
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
    >
      <el-table-column label="订单ID" align="center" show-overflow-tooltip>
        <template slot-scope="scope">
          <span>{{ scope.row.outTradeNo }}</span>
        </template>
      </el-table-column>
      <el-table-column label="就诊号" align="center" show-overflow-tooltip>
        <template slot-scope="scope">
          <span>{{ scope.row.hisSn }}</span>
        </template>
      </el-table-column>
      <el-table-column label="患者ID" align="center" show-overflow-tooltip>
        <template slot-scope="scope">
          <span>{{ scope.row.patientId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="姓名" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.patientName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="费用类型" align="center">
        <template slot-scope="scope">
          <span>{{ filterFeeType[scope.row.feeType] }}</span>
        </template>
      </el-table-column>
      <el-table-column label="订单金额" header-align="center" align="right">
        <template slot-scope="scope">
          <span>{{ scope.row.totalFee }}</span>
        </template>
      </el-table-column>
      <el-table-column label="自付金额" header-align="center" align="right">
        <template slot-scope="scope">
          <span>{{ scope.row.selfFee }}</span>
        </template>
      </el-table-column>
      <el-table-column label="统筹金额" header-align="center" align="right">
        <template slot-scope="scope">
          <span>{{ scope.row.siaFee }}</span>
        </template>
      </el-table-column>
      <el-table-column label="订单状态" align="center">
        <template slot-scope="scope">
          <span>{{ filterTradeStatus[scope.row.tradeStatus] }}</span>
        </template>
      </el-table-column>
      <el-table-column label="订单日期" align="center" show-overflow-tooltip>
        <template slot-scope="scope">
          <span>{{ scope.row.startTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="订单渠道" align="center">
        <template slot-scope="scope">
          <span>{{ filterPayChannel[scope.row.payChannel] }}</span>
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
  components: {},
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
      }
    }
  },
  created() {
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
      if (this.listQuery.dataValue.length > 0) {
        Object.assign(fetchData, {
          startDate: this.listQuery.dataValue[0],
          endDate: this.listQuery.dataValue[1]
        })
      }

      api.payRefundTrade.listPayRecordInfo({ data: fetchData }).then(res => {
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

