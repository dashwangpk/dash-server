<template>
  <el-dialog
    :title="!dataForm.menuId ? '添加菜单' : '修改菜单'"
    :close-on-click-modal="false"
    :visible.sync="visible"
  >
    <div style="height:300px;" class="menuSetting">
      <el-scrollbar wrap-class="scrollbar-wrapper" style="height:100%;">
        <el-form
          ref="dataForm"
          :model="dataForm"
          :rules="menuDataRules"
          label-width="80px"
          @keyup.enter.native="dataFormSubmit()"
        >
          <el-form-item label="类型" prop="status">
            <el-radio-group v-model="dataForm.type">
              <el-radio v-for="(typeRadio, itemIndex) in typeRadioItems" :key="itemIndex" :label="typeRadio.typeValue" @change="typeChange(value)">{{ typeRadio.typeLable }}</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="名称" prop="name">
            <el-input v-model="dataForm.name" placeholder="菜单名称" />
          </el-form-item>
          <el-form-item label="上级菜单" prop="parentName">
            <el-input v-model="dataForm.parentName" v-popover:popoverMenuParent :readonly="true" placeholder="上级菜单" />
            <el-popover
              ref="popoverMenuParent"
              placement="bottom-start"
              trigger="click"
            >
              <div style="height:220px;" class="menuPopverDiv">
                <el-scrollbar wrap-class="scrollbar-wrapper" style="height:100%;">
                  <el-tree
                    ref="dirListTree"
                    :data="dirList"
                    :props="menuListTreeProps"
                    node-key="menuId"
                    :default-expand-all="false"
                    @current-change="dirListTreeCurrentChangeHandle"
                  />
                </el-scrollbar>
              </div>
            </el-popover>
          </el-form-item>
          <el-form-item v-if="dataForm.type === 1" label="路由" prop="url">
            <el-input v-model="dataForm.url" placeholder="路由" />
          </el-form-item>
          <el-form-item v-if="dataForm.type === 1" label="组件名" prop="componentName">
            <el-input v-model="dataForm.componentName" placeholder="组件名" />
          </el-form-item>
          <el-form-item v-if="dataForm.type === 1" label="前端缓存" prop="status">
            <el-radio-group v-model="dataForm.noCache">
              <el-radio v-for="(typeRadio, itemIndex) in noCacheRadioItems" :key="itemIndex" :label="typeRadio.typeValue">{{ typeRadio.typeLable }}</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="排序号" prop="orderNum">
            <el-input-number v-model="dataForm.orderNum" :min="0" width="100%" placeholder="排序号" />
          </el-form-item>
          <el-form-item label="图标" prop="icon">
            <el-popover
              ref="popoverMenuIcon"
              width="390"
              placement="top"
              trigger="click"
            >
              <div style="height:220px;" class="menuPopverDiv">
                <el-scrollbar wrap-class="scrollbar-wrapper" style="height:100%;">
                  <el-row :span="22">
                    <el-button
                      v-for="(icon, index) in icons"
                      :key="index"
                      style="padding-left:10px;padding-right: 10px;vertical-align:middle;height:36px;width:36px;margin-left: 5px;margin-bottom: 5px;"
                      :label="icon"
                      @click="iconActiveHandle(icon)"
                    >
                      <svg-icon :icon-class="icon || ''" class-name="disabled" />
                    </el-button>
                  </el-row>
                </el-scrollbar>
              </div>
            </el-popover>
            <el-input v-model="dataForm.icon" v-popover:popoverMenuIcon placeholder="图标" />
          </el-form-item>
        </el-form>
      </el-scrollbar>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button type="danger" @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
import api from '@/api'
import icons from './requireIcons.js'
import menuDataRules from './menu-rules.js'

export default {
  data() {
    return {
      visible: false,
      icons: icons,
      dataForm: {
        menuId: null,
        type: 0,
        name: null,
        url: null,
        orderNum: 0,
        icon: null,
        parentName: '一级目录',
        parentId: 0,
        noCache: 1,
        componentName: null
      },
      menuListTreeProps: {
        label: 'name',
        children: 'children'
      },
      typeRadioItems: [
        { typeLable: '目录', typeValue: 0 },
        { typeLable: '菜单', typeValue: 1 }
      ],
      noCacheRadioItems: [
        { typeLable: '开启', typeValue: 0 },
        { typeLable: '关闭', typeValue: 1 }
      ],
      menuDataRules: menuDataRules,
      dirList: []
    }
  },
  methods: {
    init(menuId) {
      this.dataForm.menuId = menuId
      this.dataForm.type = 0
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        api.sysAppMenu.dirTree().then(res => {
          if (res && res.data && res.data.code === 0) {
            this.dirList = res.data.dirTree
          } else {
            this.$message({ showClose: true, message: res.data.msg, type: 'error' })
          }
        })
        if (this.dataForm.menuId || this.dataForm.menuId === 0) {
          const fetchData = { menuId: this.dataForm.menuId }
          api.sysAppMenu.infos({ data: fetchData }).then(res => {
            if (res && res.data && res.data.code === 0) {
              this.dataForm = res.data.sysMenu
            } else {
              this.$message({ showClose: true, message: res.data.msg, type: 'error' })
            }
          })
        }
      })
    },
    dirListTreeCurrentChangeHandle(data, node) {
      this.dataForm.parentId = data.menuId
      this.dataForm.parentName = data.name
    },
    iconActiveHandle(icon) {
      this.dataForm.icon = icon
    },
    typeChange(value) {
      this.dataForm.url = null
      this.noCache = 1
      this.componentName = null
    },
    // 表单提交
    dataFormSubmit() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const fetchData = this.dataForm
          api.sysAppMenu.saveUpdate({ data: fetchData }).then(res => {
            if (res && res.data && res.data.code === 0) {
              this.$message({ message: '保存成功', type: 'success', duration: 1500,
                onClose: () => {
                  this.visible = false
                  this.$emit('refreshDataList')
                }
              })
            } else {
              this.$message({ showClose: true, message: res.data.msg, type: 'error' })
            }
          })
        }
      })
    }
  }
}
</script>

<style>
  .menuSetting .el-scrollbar .el-scrollbar__wrap {overflow-x: hidden;}
  .menuPopverDiv .el-scrollbar .el-scrollbar__wrap {overflow-x: hidden;}
</style>
