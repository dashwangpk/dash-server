<template>
  <el-dialog
    title="应用场景"
    :close-on-click-modal="false"
    :visible.sync="visible"
  >
    <el-radio-group v-model="appScene.sceneId" style="width: 100%;">
      <el-row>
        <el-col v-for="(sceneOption, index) in sceneOptions" :key="index" :span="6" :label="sceneOption.sceneId">
          <el-radio style="margin-bottom: 8px;" :disabled="sceneOption.enable == 0" :label="sceneOption.sceneId">{{ sceneOption.sceneName }}</el-radio>
        </el-col>
      </el-row>
    </el-radio-group>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
import api from '@/api'

export default {
  data() {
    return {
      visible: false,
      appScene: {
        appId: null,
        sceneId: null
      },
      sceneOptions: []
    }
  },
  created() {
    this.getAppScene()
  },
  methods: {
    init(appId) {
      this.appScene.appId = appId
      const fectData = { appId: this.appScene.appId }
      api.cfgApp.info({ data: fectData }).then(res => {
        if (res && res.data && res.data.code === 0) {
          this.appScene = res.data.app
        } else {
          this.$message.error(res.data.msg)
        }
      })
      this.visible = true
    },
    getAppScene() {
      api.cfgScene.wholelist({}).then(res => {
        if (res && res.data && res.data.code === 0) {
          this.sceneOptions = res.data.wholelist
        } else {
          this.$message.error(res.data.msg)
        }
      })
    },
    // 表单提交
    dataFormSubmit() {
      const fectData = this.appScene
      api.cfgApp.saveUpdateScene({ data: fectData }).then(res => {
        if (res && res.data && res.data.code === 0) {
          this.$message({
            message: '保存成功',
            type: 'success',
            duration: 1500,
            onClose: () => {
              this.visible = false
              this.$emit('refreshDataList')
            }
          })
        } else {
          this.$message.error(res.data.msg)
        }
      })
    }
  }
}
</script>
