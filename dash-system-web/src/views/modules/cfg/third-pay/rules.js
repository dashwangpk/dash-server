import api from '@/api'

const rules = {
  alias: [
    {
      required: true,
      message: '别名不能为空',
      trigger: 'blur'
    }
  ],
  appid: [
    {
      required: true,
      message: 'APPID不能为空',
      trigger: 'blur'
    }
  ],
  mchId: [
    {
      required: true,
      message: '商户mch_id不能为空',
      trigger: 'blur'
    }
  ],
  apiKey: [
    {
      required: true,
      message: '商户秘钥不能为空',
      trigger: 'blur'
    }
  ],

  privateKey: [
    {
      required: true,
      message: '支付宝私钥不能为空',
      trigger: 'blur'
    }
  ],
  publicKey: [
    {
      required: true,
      message: '支付宝公钥不能为空',
      trigger: 'blur'
    }
  ],

  merId: [
    {
      required: true,
      message: '商户代码不能为空',
      trigger: 'blur'
    },
    {
      validator: function(rule, value, callback) {
        const fetchData = { merId: value, oldMerId: rules.oldMerId }
        api.payConfig.validateRepeat({ data: fetchData }).then(res => {
          console.log(res)
          if (res && res.data && res.data.success) {
            if (res.data.body.isRepeat) {
              callback(new Error(res.data.body.repeatMessage))
            }
          }
          callback()
        })
      },
      trigger: 'blur'
    }
  ]
}

export default rules
