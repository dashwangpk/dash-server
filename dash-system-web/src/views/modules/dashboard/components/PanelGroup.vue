<template>
  <el-row :gutter="10" class="panel-group">
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-icon-wrapper icon-income">
          <svg-icon icon-class="incomeyuan" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            总支付
          </div>
          <count-to :decimals="2" :start-val="0" :end-val="panelData.incomeFee" :duration="2000" class="card-panel-num" />
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-icon-wrapper icon-incomecount">
          <svg-icon icon-class="incomebi" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            总支付(笔)
          </div>
          <count-to :start-val="0" :end-val="panelData.incomeCount" :duration="2000" class="card-panel-num" />
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-icon-wrapper icon-refund">
          <svg-icon icon-class="refundyuan" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            总退款
          </div>
          <count-to :decimals="2" :start-val="0" :end-val="panelData.refundFee" :duration="2000" class="card-panel-num" />
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-icon-wrapper icon-refundcount">
          <svg-icon icon-class="refundbi" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            总退款(笔)
          </div>
          <count-to :start-val="0" :end-val="panelData.refundCount" :duration="2000" class="card-panel-num" />
        </div>
      </div>
    </el-col>
  </el-row>
</template>

<script>
import CountTo from 'vue-count-to'

export default {
  components: {
    CountTo
  },
  props: {
    panelData: {
      type: Object,
      required: true
    }
  },
  methods: {

  }
}
</script>

<style lang="scss" scoped>
.panel-group {
  margin-top: 18px;
  .card-panel-col{
    margin-bottom: 32px;
  }
  .card-panel {
    height: 112px;
    cursor: pointer;
    font-size: 12px;
    position: relative;
    overflow: hidden;
    color: #666;
    background: #fff;
    box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
    border-color: rgba(0, 0, 0, .05);
    // &:hover {
    //   .card-panel-icon-wrapper {
    //     color: #fff;
    //   }
    //   .icon-income {
    //      background: #f4516c;
    //   }
    //   .icon-incomecount {
    //     background: #ffb980;
    //   }
    //   .icon-refund {
    //     background: #34bfa3;
    //   }
    //   .icon-refundcount {
    //     background: #b6a2de
    //   }
    // }
    .icon-income {
      color: #f4516c;
    }
    .icon-incomecount {
      color: #ffb980;
    }
    .icon-refund {
      color: #34bfa3;
    }
    .icon-refundcount {
      color: #b6a2de
    }
    .card-panel-icon-wrapper {
      float: left;
      margin: 14px 0 0 14px;
      padding: 16px;
      transition: all 0.38s ease-out;
      border-radius: 6px;
    }
    .card-panel-icon {
      float: left;
      font-size: 48px;
    }
    .card-panel-description {
      float: right;
      font-weight: bold;
      margin: 26px;
      margin-left: 0px;
      .card-panel-text {
        line-height: 18px;
        color: rgba(0, 0, 0, 0.45);
        font-size: 16px;
        margin-bottom: 12px;
      }
      .card-panel-num {
        font-size: 20px;
      }
    }
  }
}
</style>
