
const payRefundTrade = [
  {
    name: 'listPayRecordInfo',
    url: '/pay/payRefundException/listPayRecordInfo',
    method: 'POST'
  },
  {
    name: 'listRefundRecordInfo',
    url: '/pay/payRefundException/listRefundRecordInfo',
    method: 'POST'
  },
  {
    name: 'listPayExceptionInfo',
    url: '/pay/payRefundException/listPayExceptionInfo',
    method: 'POST'
  },
  {
    name: 'listRefundExceptionInfo',
    url: '/pay/payRefundException/listRefundExceptionInfo',
    method: 'POST'
  },
  {
    name: 'payLogs',
    url: '/pay/payRefundException/pay/logs',
    method: 'POST'
  },
  {
    name: 'hisOrder',
    url: '/pay/payRefundException/hisOrder',
    method: 'POST'
  },
  {
    name: 'updateHisOrderTradeStatus',
    url: '/pay/payRefundException/hisOrder/updateTradeStatus',
    method: 'POST'
  }, {
    name: 'hisOrderCheckSuccess',
    url: '/pay/payRefundException/hisOrder/check/success',
    method: 'POST'
  },
  {
    name: 'hisRefund',
    url: '/pay/payRefundException/hisRefund',
    method: 'POST'
  },
  {
    name: 'hisRefundCheckSuccess',
    url: '/pay/payRefundException/hisRefund/check/success',
    method: 'POST'
  }
]

export default payRefundTrade
