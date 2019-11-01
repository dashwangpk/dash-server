import api from '@/api'

const appDataRules = {
  mchSn: [
    { required: true, message: '商户编码不能为空', trigger: 'blur' },
    { type: 'string', pattern: /^.{1,50}$/, message: '商户编码不能超过50字符', trigger: 'blur' },
    {
      validator: function(rule, value, callback) {
        const fetchData = { mchId: appDataRules.mchId, mchSn: value }
        api.cfgMch.validatorRepeat({ data: fetchData }).then(res => {
          if (res && res.data && res.data.code === 0) {
            if (res.data.isRepeat) {
              callback(new Error(res.data.repeatMessage))
            }
          }
          callback()
        })
      },
      trigger: 'blur'
    }
  ],
  name: [
    { required: true, message: '商户名称不能为空', trigger: 'blur' },
    { type: 'string', pattern: /^.{1,50}$/, message: '商户名称不能超过50字符', trigger: 'blur' },
    {
      validator: function(rule, value, callback) {
        const fetchData = { mchId: appDataRules.mchId, name: value }
        api.cfgMch.validatorRepeat({ data: fetchData }).then(res => {
          if (res && res.data && res.data.code === 0) {
            if (res.data.isRepeat) {
              callback(new Error(res.data.repeatMessage))
            }
          }
          callback()
        })
      },
      trigger: 'blur'
    }
  ],
  contacts: [
    { type: 'string', pattern: /^.{1,50}$/, message: '联系人不能超过50字符', trigger: 'blur' }
  ],
  phone: [
    {
      type: 'string',
      pattern: /^1(3|4|5|7|8)\d{9}$/,
      message: '手机格式有误',
      trigger: 'blur'
    }
  ],
  remark: [
    {
      type: 'string',
      pattern: /^.{1,100}$/,
      message: '备注不能超过100字符',
      trigger: 'blur'
    }
  ]
}

export default appDataRules
