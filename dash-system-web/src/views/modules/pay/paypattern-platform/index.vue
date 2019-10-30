<template>
  <div class="app-container">
    <el-card>
      <el-form ref="listQuery" :model="listQuery" label-width="130px" style="padding:0,margin:0">
        <el-row>
          <el-col :span="6">
            <el-form-item label="对账结果：">
              <el-select v-model="listQuery.checkResult">
                <el-option
                  v-for="item in sortOptions"
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
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                :picker-options="pickerOptions"
              />
            </el-form-item>
          </el-col>
          <el-col :span="6" :offset="4">
            <el-form-item>
              <el-button type="primary" icon="iconfont iconchaxun" @click="getList()">查询</el-button>
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
      :span-method="objectSpanMethod"
    >
      <el-table-column label="账期" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.billDate">{{ scope.row.billDate.toString().substring(0,10) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="对账方" align="center">
        <template slot-scope="scope">
          <span>{{ filterAccountApp[scope.row.accountApp] }}</span>
        </template>
      </el-table-column>
      <el-table-column label="收入笔数" header-align="center" align="right">
        <template slot-scope="scope">
          <span>{{ scope.row.incomeCount }}</span>
        </template>
      </el-table-column>
      <el-table-column label="收入金额(元)" header-align="center" align="right">
        <template slot-scope="scope">
          <span>{{ scope.row.incomeFee }}</span>
        </template>
      </el-table-column>
      <el-table-column label="支出笔数" header-align="center" align="right">
        <template slot-scope="scope">
          <span>{{ scope.row.refundCount }}</span>
        </template>
      </el-table-column>
      <el-table-column label="支出金额(元)" header-align="center" align="right">
        <template slot-scope="scope">
          <span>{{ scope.row.refundFee }}</span>
        </template>
      </el-table-column>
      <el-table-column label="应收金额(元)" header-align="center" align="right">
        <template slot-scope="scope">
          <span>{{ scope.row.totalFee }}</span>
        </template>
      </el-table-column>
      <el-table-column label="对账结果" align="center">
        <template slot-scope="scope">
          <span
            v-if="scope.row.checkResult == '1'"
            style="color:red;"
            @click="handleUpdate(scope.row)"
          >落账</span>
          <span v-else @click="handleUpdate(scope.row)">平账</span>
        </template>
      </el-table-column>
      <el-table-column label="核对状态" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.checkStatus == '0'" style="color:rgb(2, 174, 255);">未核对</span>
          <span v-else style="color:rgb(19, 206, 102);">已核对</span>
        </template>
      </el-table-column>
      <el-table-column label="核账时间" align="center" width="140" show-overflow-tooltip>
        <template slot-scope="scope">
          <span>{{ scope.row.checkTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="核对人" align="center">
        <template slot-scope="scope">
          <span>{{ filterUser[scope.row.userId] }}</span>
        </template>
      </el-table-column>
    </el-table>

    <!-- <el-pagination
      :current-page="listQuery.params.pageIndex"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="listQuery.params.pageSize"
      :total="listQuery.params.totalPage"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="sizeChangeHandle"
      @current-change="currentChangeHandle"
    />-->
  </div>
</template>

<script>
import api from '@/api'
import { parseTime } from '@/utils'

export default {
  name: 'PlatformThird',
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
          }
        ]
      },
      sortOptions: [{ label: '落账', key: '0' }, { label: '平账', key: '1' }],
      dataValue: '',
      tableKey: 0,
      list: [],
      listLoading: true,
      listQuery: {
        dataValue: [parseTime(new Date().getTime() - 3600 * 1000 * 24 * 7), parseTime(new Date().getTime() - 3600 * 1000 * 24)],
        checkResult: '',
        accountApp1: 'his',
        accountApp2: 'third',
        params: {
          pageIndex: 1,
          pageSize: 10,
          totalPage: 0
        }
      },
      filterUser: {},
      filterAccountApp: {},
      // parseTime(new Date().getTime() - 3600 * 1000 * 24),parseTime(new Date().getTime() - 3600 * 1000 * 24)
      dialogAuditVisible: false,
      dialogAuditDetail: [],
      checkRemark: '',
      downloadLoading: false,
      sureVisible: false
    }
  },
  created() {
    this.getAccountAppType()
    this.getUserType()
  },
  methods: {
    getUserType() {
      api.sysUser.wholeOptions().then(res => {
        if (res && res.data && res.data.code === 0) {
          const tempTypeOptions = {}
          res.data.wholeOptions.forEach((item, index, array) => {
            tempTypeOptions[item.userId] = item.name
          })
          this.filterUser = tempTypeOptions
        }
      })
    },
    getAccountAppType() {
      api.cfgAa10.wholeoptions({ data: { aaa100: 'account_app' }}).then(res => {
        if (res && res.data && res.data.code === 0) {
          const tempTypeOptions = {}
          res.data.wholeoptions.forEach((item, index, array) => {
            tempTypeOptions[item.aaa102] = item.aaa103
          })
          this.filterAccountApp = tempTypeOptions
        }
      })
    },

    objectSpanMethod({ row, column, rowIndex, columnIndex }) {
      if (columnIndex === 0 || columnIndex >= 7) {
        if (rowIndex % 2 === 0) {
          return {
            rowspan: 2,
            colspan: 1
          }
        } else {
          return {
            rowspan: 0,
            colspan: 0
          }
        }
      }
    },
    changeCellStyle({ row, column, rowIndex, columnIndex }) {
      if (columnIndex === 7) {
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
          startDate: this.listQuery.dataValue[0].toString().substring(0, 10),
          endDate: this.listQuery.dataValue[1].toString().substring(0, 10)
        })
      }

      api.payPatternPlatform.listInfo({ data: fetchData }).then(res => {
        if (res.data && res.data.body) {
          this.list = res.data.body.list
        } else {
          this.$message({
            message: res.data.message,
            type: 'error'
          })
        }
      })
    },

    handleUpdate(row) {
      const datas = { date: row.billDate.toString().substring(0, 10), app: '0' }
      this.$router.push({
        name: 'BillDetail',
        params: { data: datas }
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
