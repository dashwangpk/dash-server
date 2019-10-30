import api from '@/api'

const hospitalDataRules = {
  hospitalSn: [
    { required: true, message: '机构编号不能为空', trigger: 'blur' },
    { type: 'string', pattern: /^.{1,50}$/, message: '机构编号不能超过50字符', trigger: 'blur' },
    {
      validator: function(rule, value, callback) {
        const fetchData = { hospitalId: hospitalDataRules.hospitalId, hospitalSn: value }
        api.cfgHospital.validatorRepeat({ data: fetchData }).then(res => {
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
    { required: true, message: '机构名称不能为空', trigger: 'blur' },
    { type: 'string', pattern: /^.{1,50}$/, message: '机构名称不能超过50字符', trigger: 'blur' }
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

export default hospitalDataRules
