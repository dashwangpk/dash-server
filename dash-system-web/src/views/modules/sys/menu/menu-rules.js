import api from '@/api'

const menuDataRules = {

  name: [
    { required: true, message: '菜单名称不能为空', trigger: 'blur' },
    {
      type: 'string',
      pattern: /^.{1,40}$/,
      message: '菜单名称不能超过40字符',
      trigger: 'blur'
    }
  ],
  url: [
    { required: true, message: '路由不能为空', trigger: 'blur' },
    {
      type: 'string',
      pattern: /^.{1,200}$/,
      message: '路由不能超过200字符',
      trigger: 'blur'
    }
  ]
}

export default menuDataRules
