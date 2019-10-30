
const payInfoDataRules = {
  buyerUserName: [
    { required: true, message: '用户名不能为空', trigger: 'blur' },
    { type: 'string', pattern: /^.{1,50}$/, message: '用户名不能超过50字符', trigger: 'blur' }
  ],
  buyerId: [
    { required: true, message: '身份证号不能为空', trigger: 'blur' },
    { type: 'string', pattern: /^[1-9]\d{5}(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/, message: '身份证号不正确', trigger: 'blur' }
  ],
  appid: [
    { required: true, message: '应用ID不能为空', trigger: 'blur' }
  ],
  deviceInfo: [
    { required: true, message: '商品不能为空', trigger: 'blur' }
  ],
  productId: [
    { required: true, message: '商品不能为空', trigger: 'blur' }
  ],
  totalFee: [
    { required: true, message: '费用不能为空', trigger: 'blur' },
    {
      type: 'string',
      pattern: /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/,
      message: '费用格式不对',
      trigger: 'blur'
    }
  ]
}

export default payInfoDataRules
