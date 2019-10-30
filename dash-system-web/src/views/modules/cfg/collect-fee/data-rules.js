import api from '@/api'

const dataRules = {
  hospitalId: [
    { required: true, message: '医院不能为空', trigger: 'blur' }
  ],
  type: [
    { required: true, message: '类型不能为空', trigger: 'blur' }
  ],
  collectFeeSn: [
    { required: true, message: '收费员编号不能为空', trigger: 'blur' },
    { type: 'string', pattern: /^.{1,50}$/, message: '收费员编号不能超过50字符', trigger: 'blur' },
    {
      validator: function(rule, value, callback) {
        const fetchData = { collectFeeId: dataRules.collectFeeId, collectFeeSn: value }
        api.cfgCollectfee.validatorRepeat({ data: fetchData }).then(res => {
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
    { required: true, message: '收费员名称不能为空', trigger: 'blur' },
    { type: 'string', pattern: /^.{1,50}$/, message: '收费员名称不能超过50字符', trigger: 'blur' }
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
