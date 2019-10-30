<template>
  <el-dialog
    title="更改密码"
    :visible.sync="visible"
    :append-to-body="true"
  >
    <el-card>
      <el-form ref="dataForm" :model="dataForm" :rules="dataRule" label-width="80px" @keyup.enter.native="dataFormSubmit()">
        <el-form-item label="姓名">
          <span>{{ name }}</span>
        </el-form-item>
        <el-form-item label="用户名">
          <span>{{ userName }}</span>
        </el-form-item>
        <el-form-item label="原密码" prop="password">
          <el-input v-model="dataForm.password" type="password" />
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="dataForm.newPassword" type="password" />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="dataForm.confirmPassword" type="password" />
        </el-form-item>
      </el-form>
    </el-card>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
import api from '@/api'
import { mapGetters } from 'vuex'
import { removeToken } from '@/utils/auth'

export default {
  data() {
    var validateConfirmPassword = (rule, value, callback) => {
      if (this.dataForm.newPassword !== value) {
        callback(new Error('确认密码与新密码不一致'))
      } else {
        callback()
      }
    }
    return {
      visible: false,
      dataForm: {
        password: '',
        newPassword: '',
        confirmPassword: ''
      },
      dataRule: {
        password: [
          { required: true, message: '原密码不能为空', trigger: 'blur' }
        ],
        newPassword: [
          { required: true, message: '新密码不能为空', trigger: 'blur' },
          {
            type: 'string',
            pattern: /^(?![a-zA-z]+$)(?!\d+$)(?![!@#$%^&*]+$)(?![a-zA-z\d]+$)(?![a-zA-z!@#$%^&*]+$)(?![\d!@#$%^&*]+$)[a-zA-Z\d!@#$%^&*]+$/,
            message: '密码必须包含:字母/数字/特殊字符',
            trigger: 'blur'
          },
          {
            type: 'string',
            pattern: /^.{6,20}$/,
            message: '密码长度在6到20字符之间',
            trigger: 'blur'
          }
        ],
        confirmPassword: [
          { required: true, message: '确认密码不能为空', trigger: 'blur' },
          { validator: validateConfirmPassword, trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'name',
      'userName',
      'avatar',
      'device'
    ])
  },
  methods: {
    // 初始化
    init() {
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
      })
    },
    // 表单提交
    dataFormSubmit() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const passwordForm = {
            'password': this.dataForm.password,
            'newPassword': this.dataForm.newPassword
          }

          api.sysUser.passwordUpdate({ data: passwordForm }).then(res => {
            if (res && res.data && res.data.code === 0) {
              this.visible = false
              this.$nextTick(() => {
                removeToken()
                this.$router.replace({ name: 'login' })
              })
            } else {
              this.$message.error(res.data.msg)
            }
          })
        }
      })
    }
  }
}
</script>

