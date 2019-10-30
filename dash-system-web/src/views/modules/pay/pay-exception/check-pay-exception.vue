<template>
  <el-dialog
    title="支付异常核对"
    :close-on-click-modal="false"
    :visible.sync="visible"
    class="payCheck"
    width="75%"
  >
    <el-card>
      <div slot="header">
        <span>支付信息</span>
      </div>

      <el-table
        :data="hisOrder"
        border
        fit
        highlight-current-row
        :header-cell-style="{background: '#FFFFFF'}"
      >
        <el-table-column label="患者姓名" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.patientName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="就诊号/住院号" align="center" min-width="120px" show-overflow-tooltip>
          <template slot-scope="scope">
            <span>{{ scope.row.hisSn }}</span>
          </template>
        </el-table-column>
        <el-table-column label="支付方式" align="center">
          <template slot-scope="scope">
            <span>{{ filterPayMode[scope.row.payMode] }}</span>
          </template>
        </el-table-column>
        <el-table-column label="订单号" align="center" show-overflow-tooltip>
          <template slot-scope="scope">
            <span>{{ scope.row.outTradeNo }}</span>
          </template>
        </el-table-column>
        <el-table-column label="交易金额" header-align="center" align="right" show-overflow-tooltip>
          <template slot-scope="scope">
            <span>{{ scope.row.totalFee }}</span>
          </template>
        </el-table-column>
        <el-table-column label="交易号" header-align="center" align="right" width="140" :show-overflow-tooltip="true">
          <template slot-scope="scope">
            <!-- <template v-if="scope.row.tradeStatus == 2">
                <el-input ref="tradeStatusInput" v-model="scope.row.transactionId" class="edit-input" size="small" />
              </template> -->
            <span>{{ scope.row.thirdTransactionId }}</span>
          </template>
        </el-table-column>
        <el-table-column label="交易状态" align="center" width="120">
          <template slot-scope="scope">
            <el-tag>{{ filterTradeStatus[scope.row.tradeStatus] }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="交易时间" align="center" width="140" show-overflow-tooltip>
          <template slot-scope="scope">
            <span>{{ scope.row.startTime }}</span>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-card>
      <div slot="header">
        <span>参数信息</span>
      </div>
      <div style="height:160px;" class="paylogs">
        <el-scrollbar wrap-class="scrollbar-wrapper" style="height:100%;">
          <el-table :data="payLogs" border>
            <el-table-column label="参数名称" width="140">
              <template slot-scope="scope">
                {{ scope.row.paramName }}
              </template>
            </el-table-column>
            <el-table-column label="参数内容" min-width="200">
              <template slot-scope="scope">
                {{ scope.row.paramValue }}
              </template>
            </el-table-column>
          </el-table>
        </el-scrollbar>
      </div>
    </el-card>
    <span slot="footer" class="dialog-footer">
      <el-button type="danger" @click="visible = false">取消</el-button>
      <el-button type="primary" @click="checkSuccess()">核对成功</el-button>
    </span>
  </el-dialog>
</template>

<script>
import api from '@/api'

export default {
  components: {
  },
  data() {
    return {
      visible: false,
      dataForm: {
        userId: null,
        username: null,
        mobile: null,
        status: 0
      },
      hisOrder: [],
      payLogs: [],
      statusRadioItems: [
        { statusLable: '正常', statusType: 1 },
        { statusLable: '冻结', statusType: 0 }
      ],
      filterTradeStatus: {},
      filterPayMode: {}
    }
  },
  methods: {
    init(order, filterTradeStatus, filterPayMode) {
      this.dataForm.outTradeNo = order.out_trade_no
      this.filterTradeStatus = filterTradeStatus
      this.filterPayMode = filterPayMode
      this.hisOrder = []
      this.payLogs = []

      this.visible = true
      this.$nextTick(() => {
        if (this.dataForm.outTradeNo) {
          const fetchData = { outTradeNo: this.dataForm.outTradeNo }
          api.payRefundTrade.payLogs({ data: fetchData }).then(res => {
            // console.log(res)
            if (res && res.data && res.data.success) {
              if (res.data.body && res.data.body.length > 0) {
                for (var index in res.data.body) {
                  const log = res.data.body[index]
                  if (log['accountApp'] === 'his') {
                    this.payLogs.push({ paramName: 'his输入参数', paramValue: log['inParam'] })
                    this.payLogs.push({ paramName: 'his输出参数', paramValue: log['outParam'] })
                  } else if (log['accountApp'] === 'thrid') {
                    this.payLogs.push({ paramName: '第三方输入参数', paramValue: log['inParam'] })
                    this.payLogs.push({ paramName: '第三方输出参数', paramValue: log['outParam'] })
                  } else {
                    this.payLogs.push({ paramName: '输入参数', paramValue: log['inParam'] })
                    this.payLogs.push({ paramName: '输出参数', paramValue: log['outParam'] })
                  }
                }
              }
            } else {
              this.$message({ showClose: true, message: res.data.msg, type: 'error' })
            }
          })

          api.payRefundTrade.hisOrder({ data: fetchData }).then(res => {
            console.log(res)
            if (res && res.data && res.data.success) {
              this.hisOrder.push(res.data.body)
            } else {
              this.$message({ showClose: true, message: res.data.msg, type: 'error' })
            }
          })
        }
      })
    },
    tradeStatusChange(tradeStatus) {
      // tradeStatus == 2 支付成功状态
      if (tradeStatus === 2) {
        this.$message({ showClose: true, message: '请填写HIS交易号', type: 'success' })
        this.$nextTick(() => {
          this.$refs.tradeStatusInput.focus()
        })
      } else {
        this.hisOrder[0].transactionId = 0
      }
    },
    // 核对成功
    checkSuccess() {
      const fetchData = {
        outTradeNo: this.hisOrder[0].outTradeNo
      }

      api.payRefundTrade.hisOrderCheckSuccess({ data: fetchData }).then(res => {
        if (res && res.data && res.data.success) {
          this.$message({
            message: '操作成功',
            type: 'success',
            duration: 1500,
            onClose: () => {
              this.visible = false
              this.$emit('refreshDataList')
            }
          })
        } else {
          this.$message({ showClose: true, message: res.data.msg, type: 'error' })
        }
      })
    },
    // 表单提交
    dataFormSubmit() {
      if (this.hisOrder[0].tradeStatus === 2 && this.hisOrder[0].transactionId === 0) {
        this.$message({ showClose: true, message: "支持成功状态下HIS交易号不能为默认值'0'", type: 'error' })
        this.$nextTick(() => {
          this.$refs.tradeStatusInput.focus()
        })
        return
      }

      const fetchData = {
        outTradeNo: this.hisOrder[0].outTradeNo,
        transactionId: this.hisOrder[0].transactionId,
        tradeStatus: this.hisOrder[0].tradeStatus
      }

      api.payRefundTrade.updateHisOrderTradeStatus({ data: fetchData }).then(res => {
        if (res && res.data && res.data.success) {
          this.$message({
            message: '保存成功',
            type: 'success',
            duration: 1500,
            onClose: () => {
              this.visible = false
              this.$emit('refreshDataList')
            }
          })
        } else {
          this.$message({ showClose: true, message: res.data.msg, type: 'error' })
        }
      })
    }
  }
}
</script>
<style>
  .paylogs .el-scrollbar .el-scrollbar__wrap {overflow-x: hidden;}
  .payCheck .el-card__body{padding-top:3px; }
  .payCheck .el-dialog__body{padding-top:0px;}
  .payCheck .el-card__header {padding: 8px 20px;}
</style>
