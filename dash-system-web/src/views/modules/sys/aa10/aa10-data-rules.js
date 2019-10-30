import api from '@/api'
const aa10DataRules = {
  aaa100: [
    { required: true, message: '字典编码不能为空', trigger: 'blur' },
    { type: 'string', pattern: /^.{1,32}$/, message: '行政区划编码不能超过32字符', trigger: 'blur' },
    {
      validator: function(rule, value, callback) {
        const fetchData = { appId: aa10DataRules.appId, aaa100: value }
        if (aa10DataRules.isAddDict) {
          api.cfgAa10.validatorRepeat({ data: fetchData }).then(res => {
            // eslint-disable-next-line eqeqeq
            if (res && res.data && res.data.code === 0) {
              if (res.data.isRepeat) {
                callback(new Error(res.data.repeatMessage))
              }
            }
            callback()
          })
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  aaa101: [
    { required: true, message: '字典名称不能为空', trigger: 'blur' },
    { type: 'string', pattern: /^.{1,50}$/, message: '行政区划名称不能超过50字符', trigger: 'blur' }
  ],
  aae013: [
    {
      type: 'string',
      pattern: /^.{1,120}$/,
      message: '备注不能超过120字符',
      trigger: 'blur'
    }
  ]
}

export default aa10DataRules
