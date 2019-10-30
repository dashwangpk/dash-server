
const payPatternPlatform = [
  // 支付宝配置url
  {
    name: 'listInfo',
    url: '/pay/paypatternPlatform/listInfo',
    method: 'POST'
  },
  {
    name: 'findDetail',
    url: '/pay/paypatternPlatform/findDetail',
    method: 'POST'
  },
  {
    name: 'updateCheckInfo',
    url: '/pay/paypatternPlatform/updateInfo',
    method: 'POST'
  },
  {
    name: 'billCheckInfo',
    url: '/pay/paypatternPlatform/bill/check/info',
    method: 'POST'
  },
  {
    name: 'findAuditInfo',
    url: '/pay/paypatternPlatform/findAuditInfo',
    method: 'POST'
  },
  {
    name: 'platformOrderList',
    url: '/pay/paypatternPlatform/platformOrderList',
    method: 'POST'
  },
  {
    name: 'platformRefundList',
    url: '/pay/paypatternPlatform/platformRefundList',
    method: 'POST'
  }

]

export default payPatternPlatform
