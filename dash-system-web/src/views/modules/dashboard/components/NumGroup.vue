<template>
  <div>
    <!-- <el-row :gutter="10" class="panel-group">
      <el-col :span="20" :xs="10" :sm="10" :lg="6" class="card-panel-col">
        <div class="card-panel">
          <div class="card-panel-icon-wrapper icon-wxpay">
            <svg-icon icon-class="total" class-name="card-panel-icon" />
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">
              总额
            </div>
            <count-to :decimals="2" :start-val="0" :end-val="panelData.TotalFee" :duration="2000" class="card-panel-num" />
          </div>
        </div>
      </el-col>
    </el-row> -->
    <el-row :gutter="10" class="panel-group">
      <!-- <el-col :span="20" :xs="10" :sm="10" :lg="6" class="card-panel-col">
        <div class="card-panel">
          <div class="card-panel-icon-wrapper icon-wxpay">
            <svg-icon icon-class="total" class-name="card-panel-icon" />
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">
              总额
            </div>
            <count-to :decimals="2" :start-val="0" :end-val="panelData.TotalFee" :duration="2000" class="card-panel-num" />
          </div>
        </div>
      </el-col> -->
      <el-col :span="20" :xs="10" :sm="10" :lg="8" class="card-panel-col">
        <div class="card-panel">
          <div class="card-panel-icon-wrapper icon-wxpay">
            <svg-icon icon-class="wxpay" class-name="card-panel-icon" />
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">
              微信账户余额
            </div>
            <count-to :decimals="2" :start-val="0" :end-val="panelData.wxFee" :duration="2000" class="card-panel-num" />
          </div>
        </div>
      </el-col>
      <el-col :span="20" :xs="10" :sm="10" :lg="8" class="card-panel-col">
        <div class="card-panel">
          <div class="card-panel-icon-wrapper icon-alipay">
            <svg-icon icon-class="alipay" class-name="card-panel-icon" />
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">
              支付宝账户余额
            </div>
            <count-to :decimals="2" :start-val="5" :end-val="panelData.aliFee" :duration="2000" class="card-panel-num" />
          </div>
        </div>
      </el-col>
      <el-col :span="20" :xs="10" :sm="10" :lg="8" class="card-panel-col">
        <div class="card-panel">
          <div class="card-panel-icon-wrapper icon-income">
            <svg-icon icon-class="unionpay" class-name="card-panel-icon" />
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">
              银联账户余额
            </div>
            <count-to :decimals="2" :start-val="0" :end-val="panelData.chinaPayFee" :duration="2000" class="card-panel-num" />
          </div>
        </div>
      </el-col>

      <!-- <el-col :span="20" :xs="10" :sm="10" :lg="6" class="card-panel-col">
        <div class="card-panel">
          <div class="card-panel-icon-wrapper icon-minspay">
            <svg-icon icon-class="minspay" class-name="card-panel-icon" />
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">
              医保
            </div>
            <count-to :decimals="2" :start-val="0" :end-val="panelData.medicalInsFee" :duration="2000" class="card-panel-num" />
          </div>
        </div>
      </el-col> -->
    </el-row>
  </div>
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
  margin-top: 0px;
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

    .icon-minspay {
      color: #da251c;
    }
    .icon-incomecount {
      color: #ffb980;
    }
    .icon-refund {
      color: #34bfa3;
    }
    .icon-refundcount {
      color: #b6a2de;
    }
    .icon-alipay{
      color:#06b4fd;
    }
    .icon-wxpay{
      color:#00c800;
    }
    .card-panel-icon-wrapper {
      float: left;
      margin: 14px 0 0 8px;
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
      margin-top: 28px;
      margin-right: 8px;
      margin-bottom: 26px;
      margin-left: 4px;
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
