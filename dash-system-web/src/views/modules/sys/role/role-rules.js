import api from '@/api'

const userDataRules =  {

    roleName: [
      { required: true, message: '角色名不能为空', trigger: 'blur' },
      {
        type: 'string',
        pattern: /^.{1,40}$/,
        message: '角色名不能超过40字符',
        trigger: 'blur'
      },
      {
        validator:function (rule, value, callback) {
          let fetchData = {roleName:value, roleId:userDataRules.roleId}
          api.sysRole.validatorRepeat({data:fetchData}).then(res => {
            if(res && res.data && res.data.code == 0 ){
                if(res.data.isRepeat){
                  callback(new Error(res.data.repeatMessage))
                }
            }      
            callback()
          })
        },
        trigger:'blur'
      }
    ],
    remark: [
      { type: 'string',  pattern: /^.{1,100}$/, message: '描述不能超过100字符',  trigger: 'blur' }
    ]
  }

export default userDataRules
