<template>
  <div class="app-container">
    <el-card>
      <el-form ref="listQuery" :model="listQuery" label-width="130px">
        <el-row>
          <el-col :span="6">
            <el-form-item label="支付方式：">
              <el-select v-model="listQuery.payMode">
                <el-option
                  v-for="item in payPatternOptions"
                  :key="item.key"
                  :label="item.label"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="4" :offset="12">
            <el-form-item>
              <el-button type="primary" icon="iconfont iconchaxun" @click="getBillAccountList()">查询</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>
    <div class="bill-account">
      <el-table
        v-loading="dataListLoading"
        :data="billAccountList"
        style="width: 100%;"
        border
      >
        <el-table-column
          header-align="center"
          :show-overflow-tooltip="true"
          label="账单日期"
          align="center"
          width="120"
        >
          <template slot-scope="scope">
            {{ scope.row.billDate }}
          </template>
        </el-table-column>
        <el-table-column
          header-align="center"
          align="left"
          :show-overflow-tooltip="true"
          label="支付方式"
          min-width="80"
        >
          <template slot-scope="scope">
            {{ filterPayModeType[scope.row.payMode] }}
          </template>
        </el-table-column>
        <el-table-column
          align="right"
          header-align="center"
          :show-overflow-tooltip="true"
          label="上期结余"
          min-width="80"
        >
          <template slot-scope="scope">
            <!-- <template v-if="scope.$index == 0 && pageIndex == 1 && checkEdit ">
              <el-form :rules="priorFeeRules">
                <el-form-item prop="priorFee">
                  <el-input v-model="scope.row.priorFee" class="edit-input" size="small" />
                </el-form-item>
              </el-form>
            </template> -->
            <!-- <span v-else>{{ scope.row.priorFee }}</span> -->
            {{ scope.row.priorFee }}
          </template>
        </el-table-column>
        <el-table-column
          align="right"
          header-align="center"
          :show-overflow-tooltip="true"
          label="本期收入"
          min-width="80"
        >
          <template slot-scope="scope">
            <!-- <template v-if="scope.$index == 0 && pageIndex == 1 && checkEdit ">
              <el-form :rules="dayFeeRules">
                <el-form-item prop="dayFee">
                  <el-input v-model="scope.row.dayFee" class="edit-input" size="small" />
                </el-form-item>
              </el-form>
            </template>
            <span v-else>{{ scope.row.dayFee }}</span> -->
            {{ scope.row.dayFee }}
          </template>
        </el-table-column>
        <el-table-column
          align="right"
          header-align="center"
          :show-overflow-tooltip="true"
          label="本期结余"
          min-width="80"
        >
          <template slot-scope="scope">
            <template v-if="scope.$index == 0 && pageIndex == 1 && checkEdit ">
              <!-- <el-input v-model="scope.row.currentFee" class="edit-input" size="small" /> -->
              <el-form ref="currentFeeRef" :model="currentFeeForm" :rules="currentFeeRules">
                <el-form-item prop="currentFee">
                  <el-input v-model="scope.row.currentFee" class="edit-input" size="small" />
                </el-form-item>
              </el-form>
            </template>
            <span v-else>{{ scope.row.currentFee }}</span>
          </template>
        </el-table-column>
        <el-table-column
          header-align="center"
          align="left"
          :show-overflow-tooltip="true"
          label="核对人"
          width="100"
        >
          <template slot-scope="scope">
            {{ filterUser[scope.row.userId] }}
          </template>
        </el-table-column>
        <el-table-column
          header-align="center"
          align="center"
          :show-overflow-tooltip="true"
          label="核对时间"
          min-width="120"
        >
          <template slot-scope="scope">
            {{ scope.row.checkTime }}
          </template>
        </el-table-column>
        <el-table-column
          header-align="center"
          align="center"
          label="操作"
          min-width="120"
        >
          <template slot-scope="scope">
            <template v-if="scope.$index == 0 && pageIndex == 1">
              <el-button v-if="!checkEdit" plain type="primary" size="mini" @click="handleUpdate(scope)">核对</el-button>
              <el-button v-if="checkEdit" plain type="primary" size="mini" @click="saveUpdate(scope)">保存</el-button>
              <el-button v-if="checkEdit" plain type="danger" size="mini" @click="cancelUpdate(scope)">取消</el-button>
            </template>
            <template v-else>
              <!-- <el-button :disabled="true" plain type="primary" size="mini">核对</el-button> -->
            </template>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-pagination
      :current-page="pageIndex"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="pageSize"
      :total="totalPage"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="sizeChangeHandle"
      @current-change="currentChangeHandle"
    />
  </div>

</template>

<script>

import { mapGetters } from 'vuex'
import api from '@/api'

var rowList = {}

var priorFeeValidator = (rule, value, callback) => {
  var temp = /^\d+\.?\d{0,2}$/
  if (!rowList.priorFee || rowList.priorFee.length <= 0) {
    callback(new Error('金额不能为空'))
    return
  }
  if (temp.test(rowList.priorFee)) {
    callback()
  } else {
    callback(new Error('输入的金额不是合规数字'))
  }
}

var dayFeeValidator = (rule, value, callback) => {
  var temp = /^\d+\.?\d{0,2}$/
  if (!rowList.dayFee || rowList.dayFee.length <= 0) {
    callback(new Error('金额不能为空'))
    return
  }
  if (temp.test(rowList.dayFee)) {
    callback()
  } else {
    callback(new Error('输入的金额不是合规数字'))
  }
}

var currentFeeValidator = (rule, value, callback) => {
  var temp = /^\d+\.?\d{0,2}$/
  if (!rowList.currentFee || rowList.currentFee.length <= 0) {
    callback(new Error('金额不能为空'))
    return
  }
  if (temp.test(rowList.currentFee)) {
    callback()
  } else {
    callback(new Error('输入的金额不是合规数字'))
  }
}

export default {
  name: 'BillAccount',
  components: {
  },
  data() {
    return {
      selectUser: {
        username: null,
        userId: null
      },
      listQuery: {
        payMode: ''
      },
      currentFeeForm: {},
      billAccountList: [],
      payPatternOptions: [],
      filterPayModeType: {},
      filterUser: {},
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: [],
      addOrUpdateVisible: false,
      resetPasswordVisible: false,
      deleteVisible: false,
      userRolesVisible: false,
      checkEdit: false,
      cacheRow: {},
      priorFeeRules: {
        priorFee: [
          {
            validator: priorFeeValidator,
            trigger: ['blur', 'change']
          }
        ]
      },
      dayFeeRules: {
        dayFee: [
          {
            validator: dayFeeValidator,
            trigger: ['blur', 'change']
          }
        ]
      },
      currentFeeRules: {
        currentFee: [
          {
            validator: currentFeeValidator,
            trigger: ['blur', 'change']
          }
        ]
      }
    }
  },
  computed: {
    ...mapGetters([
      'userId'
    ])
  },
  created() {
    this.getPayModeType()
    this.getUserType()
  },
  methods: {
    async getBillAccountList() {
      this.dataListLoading = true
      this.checkEdit = false
      const fetchData = {
        'params': {
          'page': '' + this.pageIndex,
          'limit': '' + this.pageSize
        },
        payMode: this.listQuery.payMode
      }
      api.payBillAccount.list({ data: fetchData }).then(res => {
        this.dataListLoading = false
        if (res && res.data && res.data.success) {
          this.billAccountList = res.data.body.list
          if (res.data.body.list.length > 0) {
            rowList = res.data.body.list[0]
          }
          this.totalPage = res.data.body.totalCount
        } else {
          this.$message({ showClose: true, message: res.data.msg, type: 'error' })
        }
      })
    },
    getPayModeType() {
      api.cfgAa10.wholeoptions({ data: { aaa100: 'pay_mode' }}).then(res => {
        if (res && res.data && res.data.code === 0) {
          this.payPatternOptions = []
          const tempTypeOptions = {}
          res.data.wholeoptions.forEach((item, index, array) => {
            if (item.aaa104 === '1') {
              this.payPatternOptions.push({
                label: item.aaa103,
                key: item.aaa102
              })
            }
            tempTypeOptions[item.aaa102] = item.aaa103
          })
          this.filterPayModeType = tempTypeOptions
          this.listQuery.payMode = this.payPatternOptions[0].key
          this.getBillAccountList()
        }
      })
    },
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
    currentChangeHandle(val) {
      this.pageIndex = val
      this.getBillAccountList()
    },
    sizeChangeHandle(val) {
      this.pageSize = val
      this.pageIndex = 1
      this.getBillAccountList()
    },
    handleUpdate(scope) {
      this.checkEdit = true
      this.cacheRow = {}
      this.cacheRow.currentFee = scope.row.currentFee
      this.cacheRow.dayFee = scope.row.dayFee
      this.cacheRow.priorFee = scope.row.priorFee
    },
    saveUpdate(scope) {
      const fectData = scope.row
      fectData.userId = this.userId

      this.$refs['currentFeeRef'].validate((valid) => {
        if (valid) {
          api.payBillAccount.update({ data: scope.row }).then(res => {
            if (res && res.data && res.data.success) {
              this.checkEdit = false
              this.getBillAccountList()
              this.$message({
                message: '保存成功',
                type: 'success'
              })
            } else {
              this.$message({ showClose: true, message: res.data.msg, type: 'error' })
            }
          })
        }
      })
    },
    cancelUpdate(scope) {
      scope.row.currentFee = this.cacheRow.currentFee
      scope.row.dayFee = this.cacheRow.dayFee
      scope.row.priorFee = this.cacheRow.priorFee
      this.cacheRow = {}
      this.checkEdit = false
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
/* .bill-account .el-form-item--mini.el-form-item {
    margin-bottom: 0px;
} */
</style>
