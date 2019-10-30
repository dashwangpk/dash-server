<template>
  <div class="login-body">
    <div class="sysleft">
      <div style="font-size:30px;color:#1d83d7;margin-top: 18px;margin-bottom: 10px;">统一支付系统</div>
    </div>
    <div class="login-b">
      <div class="login-container">
        <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" auto-complete="on" label-position="left">
          <div class="title-container">
            <h3 class="title">
              账号登录
            </h3>
            <img src="/login-title.png" width="100%">
          </div>

          <el-form-item prop="username" style="background-color:#fff;">
            <span class="svg-container">
              <img src="/userid.png">
            </span>
            <el-input
              ref="username"
              v-model="loginForm.username"
              style="background-color:#fff;"
              placeholder="用户名"
              name="username"
              type="text"
              auto-complete="on"
            />
          </el-form-item>

          <el-form-item prop="password" style="background-color:#fff;">
            <span class="svg-container">
              <img src="/password.png">
            </span>
            <el-input
              :key="passwordType"
              ref="password"
              v-model="loginForm.password"
              :type="passwordType"
              placeholder="密码"
              name="password"
              auto-complete="on"
              @keyup.enter.native="handleLogin"
            />
          </el-form-item>

          <el-button
            size="medium"
            :loading="loading"
            type="primary"
            style="background: url(/loginbtn-bg.png);
          background-position:50% 40%;
          width:100%;
          border-color: #fff;
          height:48px;
          margin-bottom:30px;
          background-size:120%;
          background-repeat:no-repeat;
          padding:0px;"
            @click.native.prevent="handleLogin"
          >
            登录
          </el-button>
        </el-form>
      </div>
    </div>
    <div class="syscenter">
      <span style="font-size:14px;color:#7f7f7f;">长天科技有限公司技术支持</span>
    </div>
  </div>
</template>

<script>
import api from '@/api'

export default {
  name: 'Login',
  data() {
    return {
      loginForm: {
        username: null,
        password: null
      },
      loginRules: {
        username: [{ required: true, trigger: 'blur', message: '用户名不能为空' }],
        password: [{ required: true, trigger: 'blur', message: '密码不能为空' }]
      },
      passwordType: 'password',
      loading: false,
      showDialog: false,
      redirect: undefined
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  created() {
    // window.addEventListener('storage', this.afterQRScan)
  },
  mounted() {
    if (this.loginForm.username === '') {
      this.$refs.username.focus()
    } else if (this.loginForm.password === '') {
      this.$refs.password.focus()
    }
  },
  destroyed() {
    // window.removeEventListener('storage', this.afterQRScan)
  },
  methods: {
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.password.focus()
      })
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          const fectData = this.loginForm
          api.sysUser.userLogin({ data: fectData }).then(res => {
            if (res && res.data && res.data.code === 0) {
              this.$store.dispatch('user/setToken', res.data)
              this.$router.push({ path: 'home' })
            } else {
              this.$message.error(res.data.msg)
            }
            this.loading = false
          }).catch(reason => {
            this.loading = false
          })
        } else {
          // console.log('error submit!!')
          return false
        }
      })
    }

  }
}
</script>

<style lang="scss">
/* 修复input 背景不协调 和光标变色 */
/* Detail see https://github.com/PanJiaChen/vue-element-admin/pull/927 */

$bg:#283443;
$light_gray:#fff;
$cursor: #fff;

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
  .login-container .el-input input {
    color: $cursor;
  }
}

/* reset element-ui css */
.login-body{
  height: 100%;
  .sysleft{
    position: relative;
    width: 230px;
    max-width: 100%;
    margin: 0 0 0 24%;
    overflow: hidden;
  }
  .syscenter{
    position: relative;
    width: 170px;
    max-width: 100%;
    margin: 0 auto;
    padding-top: 10px;
    overflow: hidden;
  }
}
.login-b{
  height: 78%;
  background-color:#733ae9;
}

.login-container {
  .el-input {
    display: inline-block;
    height: 47px;
    width: 90%;
    border-radius: 5px;

    input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      border-radius: 0px;
      padding: 12px 5px 12px 15px;
      color: #303133;
      height: 47px;
      caret-color: #303133;

      &:-webkit-autofill {
        // box-shadow: 0 0 0px 1000px #00ffff inset !important;
        -webkit-text-fill-color: #454545 !important;
      }
    }
  }

  .el-form-item {
    border: 1px solid rgba(255, 255, 255, 0.1);
    // background: rgba(0, 0, 0, 0.1);
    border-color: #dddddd;
    border-radius: 5px;
    color: #454545;
  }
}
loginBtn{
  background: url(/loginbtn-bg.png);
  background-size:100%;
  background-repeat:no-repeat;
  width:100%;
  margin-bottom:30px;
}

</style>

<style lang="scss" scoped>
$bg:#2d3a4b;
$dark_gray:#ffffff;
$light_gray:rgb(255, 255, 255);

.login-container {
  height: 100%;
  width: 85%;
  max-width: 100%;
  margin: 0 auto;
  background-color: $bg;
  overflow: hidden;
  // background: url(/login_bg.jpg);
  background: url(/loginpanel.png);
  background-size:100%;
  background-repeat:no-repeat;
  background-color:#733ae9;

  .titleName{
    position: relative;
    width: 230px;
    max-width: 100%;
    margin: 0 auto;
    overflow: hidden;
    margin-top: 100px;
  }

  .login-form {
    position: relative;
    width: 31%;
    max-width: 100%;
    // padding: 160px 35px 0;
    border-radius: 10px;
    background-color: #ffffff;
    padding-right: 10px;
    padding-left: 10px;
    // padding-top: 20px;
    margin: 9% 9% 0 59%;
    // margin-top: 80px;
    overflow: hidden;
  }

  .tips {
    font-size: 14px;
    color: #fff;
    margin-bottom: 10px;

    span {
      &:first-of-type {
        margin-right: 16px;
      }
    }
  }

  .svg-container {
    padding: 6px 5px 0px 5px;
    color: $dark_gray;
    vertical-align: middle;
    width: 8%;
    display: inline-block;
  }

  .title-container {
    position: relative;
    margin-bottom: 18px;

    .title {
      font-size: 16px;
      color:#1d83d7;
      margin: 0px auto 0px auto;
      // text-align: center;
      font-weight: bold;
      font-family: sans-serif;
    }

    // .set-language {
    //   color: #fff;
    //   position: absolute;
    //   top: 3px;
    //   font-size: 18px;
    //   right: 0px;
    //   cursor: pointer;
    // }
  }

  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: $dark_gray;
    cursor: pointer;
    user-select: none;
  }

  .thirdparty-button {
    position: absolute;
    right: 0;
    bottom: 6px;
  }

  @media only screen and (max-width: 470px) {
    .thirdparty-button {
      display: none;
    }
  }
}
</style>
