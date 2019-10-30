import api from '@/api'

const userDataRules = {
  name: [
    { required: true, message: '姓名不能为空', trigger: 'blur' },
    {
      type: 'string',
      pattern: /^.{1,40}$/,
      message: '姓名不能超过40字符',
      trigger: 'blur'
    },
    {
      validator: function(rule, value, callback) {
        const fetchData = { name: value, userId: userDataRules.userId }
        api.sysUser.validatorRepeat({ data: fetchData }).then(res => {
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
  username: [
    { required: true, message: '用户名不能为空', trigger: 'blur' },
    {
      type: 'string',
      pattern: /^.{1,40}$/,
      message: '用户名不能超过40字符',
      trigger: 'blur'
    },
    {
      validator: function(rule, value, callback) {
        const fetchData = { username: value, userId: userDataRules.userId }
        api.sysUser.validatorRepeat({ data: fetchData }).then(res => {
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
  mobile: [
    { required: true, message: '手机号不能为空', trigger: 'blur' },
    {
      type: 'string',
      pattern: /^1(3|4|5|7|8)\d{9}$/,
      message: '手机格式有误',
      trigger: 'blur'
    },
    {
      validator: function(rule, value, callback) {
        const fetchData = { mobile: value, userId: userDataRules.userId }
        api.sysUser.validatorRepeat({ data: fetchData }).then(res => {
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
  ]
}

export default userDataRules
