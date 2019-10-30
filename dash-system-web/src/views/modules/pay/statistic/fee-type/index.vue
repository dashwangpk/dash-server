<template>
  <div class="app-container">
    <el-card>
      <el-date-picker
        v-model="dateRange"
        type="daterange"
        align="left"
        value-format="yyyy-MM-dd"
        unlink-panels
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        :picker-options="pickerOptions"
      />
      <el-button type="primary" icon="el-icon-search" @click="getStatisticChannel">查询</el-button>
    </el-card>
    <el-table
      :data="dataList"
      style="width: 100%"
      :header-cell-style="{background: '#FFFFFF'}"
    >
      <el-table-column
        prop="feeType"
        header-align="center"
        align="left"
        label="统计类型"
        min-width="120"
      >
        <template slot-scope="scope">
          {{ feeTypeOptions[scope.row.feeType] ? feeTypeOptions[scope.row.feeType] : scope.row.feeType }}
        </template>
      </el-table-column>
      <el-table-column header-align="center" label="收入">
        <el-table-column
          prop="incomeCount"
          header-align="center"
          align="right"
          label="总笔数"
          min-width="120"
        />
        <el-table-column
          prop="incomeFee"
          header-align="center"
          align="right"
          label="总金额(元)"
          min-width="120"
        />
      </el-table-column>
      <el-table-column header-align="center" label="支出">
        <el-table-column
          prop="refundCount"
          header-align="center"
          align="right"
          label="总笔数"
          min-width="120"
        />
        <el-table-column
          prop="refundFee"
          header-align="center"
          align="right"
          label="总金额(元)"
          min-width="120"
        />
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import api from '@/api'

export default {
  name: 'StatisticFeeType',
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
      feeTypeOptions: {},
      dateRange: '',
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false
    }
  },
  created() {
    this.getFeeType()
  },
  methods: {
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
    getStatisticChannel() {
      this.dataListLoading = true
      const fetchData = {
        'beginTime': this.dateRange[0],
        'endTime': this.dateRange[1]
      }

      api.payStatistic.feeType({ data: fetchData }).then(res => {
        if (res && res.data && res.data.success) {
          this.dataList = res.data.body
          // this.totalPage = res.data.body.totalCount
        } else {
          this.$message.error(res.data.msg)
        }
        this.dataListLoading = false
      })
    }
  }
}
</script>
<style lang="scss" scoped>
.app-container {
  .roles-table {
    margin-top: 30px;
  }
  .permission-tree {
    margin-bottom: 30px;
  }
}
</style>
<style>
.el-card__body .el-date-editor .el-range-separator{
    width:12px;
    padding:0px;
}
.el-card__body .el-date-editor{
    width: 240px;
}
</style>
