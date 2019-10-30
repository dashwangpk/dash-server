<template>
  <el-dialog
    title="菜单授权"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <div style="height:300px;">
      <el-row :gutter="20">
        <el-col :span="5">
          <div> 
            <label class="treelable">选择菜单</label>
          </div>
        </el-col>
        <el-col :span="19">
          <div class="treediv" style="height:300px;" >
            <el-scrollbar wrap-class="scrollbar-wrapper" style="height:100%;">
              <el-tree
                :data="menuList"
                :props="menuListTreeProps"
                node-key="menuId"
                ref="menuListTree"
                :default-expand-all="true"
                show-checkbox>
              </el-tree>
            </el-scrollbar>      
          </div>
        </el-col>
      </el-row>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button type="danger" @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
import api from '@/api'

  export default {
    data () {
      return {
        visible: false,
        menuList: [],
        memuIds:[],
        roleId:null,
        menuListTreeProps: {
          label: 'name',
          children: 'children'
        }
      }
    },
    components: {
    },
    methods: {
      init (roleId) {
        this.roleId = roleId
        this.visible = true
        this.$nextTick(() => {
            let fetchData = {roleId:this.roleId}
            api.sysAppMenu.tree().then(res => {
                if(res && res.data && res.data.code == 0){
                  this.menuList = res.data.menuTree
                  api.sysRole.memuIds({data:fetchData}).then(res => {
                      if(res && res.data && res.data.code == 0){
                        var idx = res.data.memuIds.indexOf(999999999999)
                        if (idx !== -1) {
                          res.data.memuIds.splice(idx, res.data.memuIds.length - idx)
                        }
                        this.memuIds = res.data.memuIds
                        this.$refs.menuListTree.setCheckedKeys(this.memuIds)
                      }else{
                        this.$message({showClose: true, message: res.data.msg,type: 'error'})
                      }    
                  })                    
                }else{
                  this.$message({showClose: true, message: res.data.msg,type: 'error'})
                }    
            })   

        })
      },
      // 表单提交
      dataFormSubmit () {
                
       let checkedMenuIds = this.$refs.menuListTree.getCheckedKeys()
       let halfCheckedMenuIds = this.$refs.menuListTree.getHalfCheckedKeys()
       let menuIds = [].concat(checkedMenuIds, [999999999999], halfCheckedMenuIds)
       let fetchData = {roleId:this.roleId, menuIds : menuIds}   
          api.sysRole.saveRoleMenuIds({data:fetchData}).then(res => {
            if(res && res.data && res.data.code == 0){
              this.visible = false
              this.$message({message: '保存成功', type: 'success', duration: 1500})             
            }else{
              this.$message({showClose: true, message: res.data.msg,type: 'error'})
            }    
          })   

      }
    }
  }
</script>
<style>
  .treediv .el-scrollbar .el-scrollbar__wrap {overflow-x: hidden;}
  .treelable{
    box-sizing: border-box;
    color: rgb(96, 98, 102);
    cursor: default;
    display: block;
    float: left;
    font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", Arial, sans-serif;
    font-size: 14px;
    font-weight: 700;
    text-align: left;
    margin-left: 30px;
  }
</style>  