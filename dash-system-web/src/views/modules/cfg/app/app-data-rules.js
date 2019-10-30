import api from '@/api'

const appDataRules = {
  appSn: [
    { required: true, message: '应用编码不能为空', trigger: 'blur' },
    { type: 'string', pattern: /^.{1,50}$/, message: '应用编码不能超过50字符', trigger: 'blur' },
    {
      validator: function(rule, value, callback) {
        const fetchData = { appId: appDataRules.appId, appSn: value }
        api.cfgApp.validatorRepeat({ data: fetchData }).then(res => {
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
    { required: true, message: '应用名称不能为空', trigger: 'blur' },
    { type: 'string', pattern: /^.{1,50}$/, message: '应用名称不能超过50字符', trigger: 'blur' }
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
