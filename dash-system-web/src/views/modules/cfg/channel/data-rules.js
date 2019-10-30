import api from '@/api'

const dataRules = {
  channelSn: [
    { required: true, message: '场景名称不能为空', trigger: 'blur' },
    { type: 'string', pattern: /^.{1,50}$/, message: '场景名称不能超过50字符', trigger: 'blur' },
    {
      validator: function(rule, value, callback) {
        const fetchData = { channelId: dataRules.channelId, channelSn: value }
        api.cfgChannel.validatorRepeat({ data: fetchData }).then(res => {
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
    { required: true, message: '支付方式不能为空', trigger: 'blur' },
    { type: 'string', pattern: /^.{1,50}$/, message: '渠道名称不能超过50字符', trigger: 'blur' }
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

export default dataRules
