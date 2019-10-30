<template>
  <div class="app-container">
    <el-card>
      <el-form ref="listQuery" :model="listQuery" label-width="130px" style="padding:0,margin:0">
        <el-row>
          <el-col :span="6">
            <el-form-item label="支付方式：">
              <el-select v-model="listQuery.payMode" clearable>
                <el-option
                  v-for="item in payPatternOptions"
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
                v-model="listQuery.billDate"
                type="date"
                align="left"
                value-format="yyyy-MM-dd"
                placeholder="日期"
                style="width:181px"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8" :offset="2">
            <el-form-item>
              <el-button type="primary" icon="iconfont iconchaxun" @click="getList()">查询</el-button>
              <el-button type="primary" icon="iconfont iconchaxun" @click="handleDownload">导出</el-button>
              <el-button v-print="'#printTab'" type="primary" icon="iconfont iconchaxun">打印</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <el-table
      id="printTab"
      :data="list"
      :cell-class-name="changeCellStyle"
      border
      fit
      highlight-current-row
      show-summary
      :header-cell-style="{background: '#FFFFFF'}"
    >
      <el-table-column label="支付方式" header-align="center" align="left" width="100">
        <template slot-scope="scope">
          <span>{{ filterPayMode[scope.row.pay_mode] }}</span>
        </template>
      </el-table-column>
      <!-- 支付 n-->
      <el-table-column label="支付" align="center">
        <el-table-column label="HIS" align="center">
          <el-table-column
            label="笔数（笔）"
            header-align="center"
            align="right"
            prop="p1_income_count"
            width="90"
          >
            <template slot-scope="scope">
              <span @click="turnTradePayDetail(scope.row)">{{ scope.row.p1_income_count }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="金额（元）"
            header-align="center"
            align="right"
            prop="p1_income_fee"
            width="90"
          >
            <template slot-scope="scope">
              <span @click="turnTradePayDetail(scope.row)">{{ scope.row.p1_income_fee }}</span>
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column label="支付方" align="center">
          <el-table-column
            label="笔数（笔）"
            header-align="center"
            align="right"
            prop="p2_income_count"
            width="90"
          >
            <template slot-scope="scope">
              <span @click="turnTradePayDetail(scope.row)">{{ scope.row.p2_income_count }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="金额（元）"
            header-align="center"
            align="right"
            prop="p2_income_fee"
            width="90"
          >
            <template slot-scope="scope">
              <span @click="turnTradePayDetail(scope.row)">{{ scope.row.p2_income_fee }}</span>
            </template>
          </el-table-column>
        </el-table-column>
        <!-- 差额 -->
        <el-table-column label="差额" align="center">
          <el-table-column
            label="笔数（笔）"
            header-align="center"
            align="right"
            prop="income_count"
            width="90"
          >
            <template slot-scope="scope">
              <template v-if="scope.row.income_count === 0">
                <span style="cursor: auto;">{{ scope.row.income_count }}</span>
              </template>
              <template v-else>
                <span style="color:red;" @click="turnTradePayDetail(scope.row)">{{ scope.row.income_count }}</span>
              </template>
            </template>

          </el-table-column>
          <el-table-column
            label="金额（元）"
            header-align="center"
            align="right"
            prop="income_fee"
            width="90"
          >
            <template slot-scope="scope">
              <template v-if="scope.row.income_fee === 0">
                <span style="cursor: auto;">{{ scope.row.income_fee }}</span>
              </template>
              <template v-else>
                <span style="color:red;" @click="turnTradePayDetail(scope.row)">{{ scope.row.income_fee }}</span>
              </template>
            </template>
          </el-table-column>
        </el-table-column>
      </el-table-column>
      <!-- 退款 n -->
      <el-table-column label="退款" align="center">
        <el-table-column label="HIS" header-align="center" align="right">
          <el-table-column
            label="笔数（笔）"
            header-align="center"
            align="right"
            prop="p1_refund_count"
            width="90"
          >
            <template slot-scope="scope">
              <span @click="turnTradeRefundDetail(scope.row)">{{ scope.row.p1_refund_count }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="金额（元）"
            header-align="center"
            align="right"
            prop="p1_refund_fee"
            width="90"
          >
            <template slot-scope="scope">
              <span @click="turnTradeRefundDetail(scope.row)">{{ scope.row.p1_refund_fee }}</span>
            </template>
          </el-table-column>

        </el-table-column>
        <el-table-column label="支付方" align="center">
          <el-table-column
            label="笔数（笔）"
            header-align="center"
            align="right"
            prop="p2_refund_count"
            width="90"
          >
            <template slot-scope="scope">
              <span @click="turnTradeRefundDetail(scope.row)">{{ scope.row.p2_refund_count }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="金额（元）"
            header-align="center"
            align="right"
            prop="p2_refund_fee"
            width="90"
          >
            <template slot-scope="scope">
              <span @click="turnTradeRefundDetail(scope.row)">{{ scope.row.p2_refund_fee }}</span>
            </template>
          </el-table-column>

        </el-table-column>
        <!-- 差额 -->
        <el-table-column label="差额" align="center">
          <el-table-column
            label="笔数（笔）"
            header-align="center"
            align="right"
            prop="refund_count"
            width="90"
          >
            <template slot-scope="scope">
              <template v-if="scope.row.refund_count === 0">
                <span style="cursor: auto;">{{ scope.row.refund_count }}</span>
              </template>
              <template v-else>
                <span style="color:red;" @click="turnTradeRefundDetail(scope.row)">{{ scope.row.refund_count }}</span>
              </template>
            </template>
          </el-table-column>
          <el-table-column
            label="金额（元）"
            header-align="center"
            align="right"
            prop="refund_fee"
            width="90"
          >
            <template slot-scope="scope">
              <template v-if="scope.row.refund_fee === 0">
                <span style="cursor: auto;">{{ scope.row.refund_fee }}</span>
              </template>
              <template v-else>
                <span style="color:red;" @click="turnTradeRefundDetail(scope.row)">{{ scope.row.refund_fee }}</span>
              </template>
            </template>
          </el-table-column>
        </el-table-column>
      </el-table-column>

      <el-table-column label="应收金额(元)" align="center">
        <el-table-column
          :label="accountType.app1"
          header-align="center"
          align="right"
          prop="p1_receivable_fee"
        >
          <template slot-scope="scope">
            <span @click="turnTradeDetail(scope.row)">{{ scope.row.p1_receivable_fee }}</span>
          </template>
        </el-table-column>
        <el-table-column
          :label="accountType.app2"
          header-align="center"
          align="right"
          prop="p2_receivable_fee"
        >
          <template slot-scope="scope">
            <span @click="turnTradeDetail(scope.row)">{{ scope.row.p2_receivable_fee }}</span>
          </template>
        </el-table-column>
        <el-table-column label="差额(元)" header-align="center" align="right" prop="receivable_fee">
          <template slot-scope="scope">
            <span @click="turnTradeDetail(scope.row)">{{ scope.row.receivable_fee }}</span>
          </template>
        </el-table-column>
      </el-table-column>
    </el-table>

    <el-form
      v-if="auditVisible"
      ref="listAudit"
      :model="listAudit"
      label-width="130px"
      style="margin-top:10px"
    >
      <el-row>
        <el-col :span="8">
          <el-form-item label="核对时间：" prop="akc190">
            <input
              v-model="listAudit.checkTime"
              type="text"
              style="outline:none;border-style: none;"
            >
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="核对人：" prop="akc172">
            <span>{{ auditName }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="核对状态：" prop="akc172">
            <span v-if="listAudit.checkStatus == '1'">已核对</span>
            <span v-else>未核对</span>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="核对备注：">
            <el-input
              v-if="dialogFormVisible"
              v-model="listAudit.checkRemark"
              type="textarea"
              rows="2"
              disabled
            />
            <el-input v-else v-model="listAudit.checkRemark" type="textarea" rows="2" clearable />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <div slot="footer" style="text-align:center;" class="dialog-footer">
      <el-button
        v-if="!dialogFormVisible"
        type="primary"
        style="margin-left: 30px"
        @click="auditInfo"
      >核对</el-button>
      <el-button
        v-if="dialogFormVisible"
        type="primary"
        style="margin-left: 10px"
        @click="dialogFormVisible = false"
      >编辑</el-button>
      <el-button
        v-if="!dialogFormVisible"
        type="primary"
        style="margin-left: 10px"
        @click="dialogFormVisible = true"
      >取消</el-button>
    </div>
  </div>
</template>

<script>
import api from '@/api'
import { parseTime } from '@/utils'

export default {
  name: 'MedicalInsBill',
  components: {},
  data() {
    return {
      payPatternOptions: [],
      tableKey: 0,
      accountType: { app1: 'HIS', app2: '支付方' },
      list: [],
      listLoading: true,
      listQuery: {
        checkAccount: '0',
        payMode: '',
        billDate: parseTime(new Date().getTime() - 3600 * 1000 * 24)
      },
      filterPayMode: {},
      dialogFormVisible: true,
      dialogStatus: '',
      textMap: {
        update: 'Edit',
        create: 'Create'
      },
      auditName: null,
      downloadLoading: false,
      listAudit: { userId: '', checkTime: '', userName: null },
      auditVisible: false
    }
  },
  created() {
    this.getParams()
    this.getPayModeType()
    this.getAppIdType()
  },
  activated() {
    if (this.$route.params.data && !this.$route.meta.noCache) {
      this.getParams()
    }
  },
  methods: {
    getParams() {
      if (this.$route.params.data) {
        this.listQuery.billDate = this.$route.params.data.date
      }

      this.getList()
    },

    turnTradePayDetail(row) {
      const datas = this.listQuery
      if (row.income_count !== 0 || row.income_fee !== 0) {
        this.$router.push({
          name: 'PayException',
          params: { data: datas }
        })
      } else {
        this.$router.push({
          name: 'PayRecord',
          params: { data: datas }
        })
      }
    },

    turnTradeRefundDetail(row) {
      const datas = this.listQuery
      if (row.refund_count !== 0 || row.refund_fee !== 0) {
        this.$router.push({
          name: 'RefundException',
          params: { data: datas }
        })
      } else {
        this.$router.push({
          name: 'RefundRecord',
          params: { data: datas }
        })
      }
    },

    turnTradeDetail(row) {
      const datas = this.listQuery
      this.$router.push({
        name: row.receivable_fee !== 0 ? 'PayException' : 'PayRecord',
        params: { data: datas }
      })
    },

    getPayModeType() {
      api.cfgAa10.wholeoptions({ data: { aaa100: 'pay_mode' }}).then(res => {
        if (res && res.data && res.data.code === 0) {
          this.payPatternOptions = []
          res.data.wholeoptions.forEach((item, index, array) => {
            this.payPatternOptions.push({
              label: item.aaa103,
              key: item.aaa102
            })
          })
        }
      })
    },

    getAppIdType() {
      api.cfgVpaymode.list({ data: {}}).then(res => {
        if (res && res.data && res.data.code === 0) {
          console.log(res)

          const tempTypeOptions = {}
          res.data.payModeList.forEach((item, index, array) => {
            tempTypeOptions[item.payMode] = item.alias
          })
          this.filterPayMode = tempTypeOptions
        }
      })
    },

    changeCellStyle({ row, column, rowIndex, columnIndex }) {
      if (columnIndex > 0) {
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
      const fetchData = {
        payMode: this.listQuery.payMode,
        billDate: this.listQuery.billDate,
        accountApp1: 'his',
        accountApp2: 'third'
      }

      api.payPatternPlatform.billCheckInfo({ data: fetchData }).then(res => {
        if (res.data && res.data.body) {
          this.list = res.data.body
          if (this.list.length > 0) {
            this.auditVisible = true
          } else {
            this.auditVisible = false
          }
          this.getAudit()
        } else {
          this.$message({
            message: res.data.message,
            type: 'error'
          })
        }
      })
    },

    getAudit() {
      const fetchData = {
        billDate: this.listQuery.billDate,
        accountApp1: 'his',
        accountApp2: 'third'
      }

      api.payPatternPlatform.findAuditInfo({ data: fetchData }).then(res => {
        if (res.data && res.data.body && res.data.body.length > 0) {
          const items = res.data.body
          this.listAudit = items[0]
          this.auditName = ''
          this.listAudit.userId =
            this.listAudit.userId && this.listAudit.userId !== ''
              ? this.listAudit.userId
              : '1'
          this.listAudit.checkTime =
            this.listAudit.checkTime && this.listAudit.checkTime !== ''
              ? this.listAudit.checkTime
              : parseTime(new Date())
          this.dialogFormVisible = true

          if (this.listAudit.checkStatus === '0') {
            this.dialogFormVisible = false
          }

          const fetchData = { userId: this.listAudit.userId }
          api.sysUser.infos({ data: fetchData }).then(res => {
            if (res && res.data && res.data.code === 0) {
              this.auditName = res.data.user.name
            }
          })
        }
      })
    },

    auditInfo() {
      const fetchData = {
        billDate: this.listQuery.billDate,
        checkRemark: this.listAudit.checkRemark,
        checkStatus: '1',
        userId: '1',
        checkTime: parseTime(new Date())
      }
      api.payPatternPlatform.updateCheckInfo({ data: fetchData }).then(res => {
        if (res.data && res.data.success) {
          this.$message({
            message: res.data.message,
            type: 'error'
          })
          this.getAudit()
        } else {
          this.$message({
            message: res.data.message,
            type: 'error'
          })
        }
      })
    },

    getSummaries(param) {
      const { columns, data } = param
      const sums = []
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = '合计'
          return
        }
        const values = data.map(item => Number(item[column.property]))
        if (!values.every(value => isNaN(value))) {
          sums[index] = values.reduce((prev, curr) => {
            const value = Number(curr)
            if (!isNaN(value)) {
              return prev + curr
            } else {
              return prev
            }
          }, 0)
          sums[index] += ' 元'
        } else {
          sums[index] = 'N/A'
        }
      })

      return sums
    },

    handleDownload() {
      this.downloadLoading = true
      import('@/vendor/Export2Excel').then(excel => {
        const tHeader = [['pbAppid'], 'pbIncomeCount', 'tbIncomeCount']
        const filterVal = ['pbAppid', 'pbIncomeCount', 'tbIncomeCount']
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
            return parseTime(v[j])
          } else {
            return v[j]
          }
        })
      )
    }
  }
}
</script>
