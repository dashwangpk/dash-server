
const payConfig = [
  // 支付宝配置url
  {
    name: 'insertAlipay',
    url: '/cfg/alipaycfg/insertInfo',
    method: 'POST'
  },
  {
    name: 'findAlipay',
    url: '/cfg/alipaycfg/findInfo',
    method: 'POST'
  },
  {
    name: 'updateAlipay',
    url: '/cfg/alipaycfg/updateInfo',
    method: 'POST'
  },
  {
    name: 'deleteAlipay',
    url: '/cfg/alipaycfg/deleteInfo',
    method: 'POST'
  },
  // 微信配置url
  {
    name: 'insertWechat',
    url: '/cfg/wechatcfg/insertInfo',
    method: 'POST'
  },
  {
    name: 'findWechat',
    url: '/cfg/wechatcfg/findInfo',
    method: 'POST'
  },
  {
    name: 'updateWechat',
    url: '/cfg/wechatcfg/updateInfo',
    method: 'POST'
  },
  {
    name: 'deleteWechat',
    url: '/cfg/wechatcfg/deleteInfo',
    method: 'POST'
  },
  // 银联配置url
  {
    name: 'insertUnionpay',
    url: '/cfg/unionpaycfg/insertInfo',
    method: 'POST'
  },
  {
    name: 'findUnionpay',
    url: '/cfg/unionpaycfg/findInfo',
    method: 'POST'
  },
  {
    name: 'updateUnionpay',
    url: '/cfg/unionpaycfg/updateInfo',
    method: 'POST'
  },
  {
    name: 'deleteUnionpay',
    url: '/cfg/unionpaycfg/deleteInfo',
    method: 'POST'
  },
  // 医保配置url
  {
    name: 'insertMedIns',
    url: '/cfg/medicalinsurancecfg/insertInfo',
    method: 'POST'
  },
  {
    name: 'findMedIns',
    url: '/cfg/medicalinsurancecfg/findInfo',
    method: 'POST'
  },
  {
    name: 'updateMedIns',
    url: '/cfg/medicalinsurancecfg/updateInfo',
    method: 'POST'
  },
  {
    name: 'deleteMedIns',
    url: '/cfg/medicalinsurancecfg/deleteInfo',
    method: 'POST'
  },
  // 商保配置url
  {
    name: 'insertComIns',
    url: '/cfg/commercialinsurancecfg/insertInfo',
    method: 'POST'
  },
  {
    name: 'findComIns',
    url: '/cfg/commercialinsurancecfg/findInfo',
    method: 'GET'
  },
  {
    name: 'updateComIns',
    url: '/cfg/commercialinsurancecfg/updateInfo',
    method: 'POST'
  },
  {
    name: 'deleteComIns',
    url: '/cfg/commercialinsurancecfg/deleteInfo',
    method: 'POST'
  },
  {
    name: 'uploadCerFile',
    url: '/cfg/unionpaycfg/uploadCerFile',
    method: 'POST'
  },
  {
    name: 'validateRepeat',
    url: '/cfg/unionpaycfg/validateRepeat',
    method: 'POST'
  }

]

export default payConfig
