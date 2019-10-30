<template>
  <el-container :style="styleObject">
    <el-aside width="200px" style="border: 1px solid #eee;background-color: #f7f3f3;">
      <el-menu>
        <div class="typeLable">
          <font>代码类型</font>
        </div>
        <el-menu-item
          v-for="(aa00Data, index) in aa00DataList"
          :key="index"
          :index="''+index"
          :label="aa00Data.aaa100"
          @click="displayItems(aa00Data)"
        >
          <span slot="title">{{ aa00Data.aaa101 }}</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-main>
      <el-button type="primary" :disabled="!this.seletAa00Data" @click="handleAdd">
        添加
      </el-button>
      <el-table
        v-loading="dataListLoading"
        :data="dataList"
        border
        style="width: 100%;"
      >
        <el-table-column
          min-width="80"
          :show-overflow-tooltip="true"
          prop="aaa102"
          header-align="center"
          align="left"
          label="代码"
        >
          <template slot-scope="scope">
            <template v-if="scope.row.aaa105 == 1 && scope.row.edit ">
              <el-input v-model="scope.row.aaa102" class="edit-input" size="small" />
            </template>
            <span v-else>{{ scope.row.aaa102 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          min-width="80"
          :show-overflow-tooltip="true"
          prop="aaa103"
          header-align="center"
          align="left"
          label="代码名称"
        >
          <template slot-scope="scope">
            <template v-if="scope.row.aaa105 == 1 && scope.row.edit ">
              <el-input v-model="scope.row.aaa103" class="edit-input" size="small" />
            </template>
            <span v-else>{{ scope.row.aaa103 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          width="88"
          :show-overflow-tooltip="true"
          prop="aaa106"
          header-align="center"
          align="center"
          label="排序号"
        >
          <template slot-scope="scope">
            <template v-if="scope.row.aaa105 == 1 && scope.row.edit ">
              <el-input v-model="scope.row.aaa106" type="number" min="0" class="edit-input" size="small" />
            </template>
            <span v-else>{{ scope.row.aaa106 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          width="70"
          :show-overflow-tooltip="true"
          prop="aaa104"
          header-align="center"
          align="center"
          label="启用"
        >
          <template slot-scope="scope">
            <template v-if="scope.row.aaa105 == 1">
              <el-switch
                v-model="scope.row.aaa104"
                active-value="1"
                inactive-value="0"
                @change="aa10StatusChange(scope)"
              />
            </template>
            <template v-else>
              <el-switch
                v-model="scope.row.aaa104"
                disabled
                active-value="1"
                inactive-value="0"
              />
            </template>
          </template>
        </el-table-column>
        <el-table-column
          min-width="140"
          :show-overflow-tooltip="true"
          prop="aae013"
          header-align="center"
          align="left"
          label="备注"
        >
          <template slot-scope="scope">
            <template v-if="scope.row.aaa105 == 1 && scope.row.edit ">
              <el-input v-model="scope.row.aae013" class="edit-input" size="small" />
            </template>
            <span v-else>{{ scope.row.aae013 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          fixed="right"
          header-align="center"
          align="center"
          min-width="160"
          label="操作"
        >
          <template slot-scope="scope">
            <template v-if="scope.row.aaa105 == 1">
              <el-button v-if="!scope.row.edit" plain type="primary" size="mini" @click="handleUpdate(scope)">修改</el-button>
              <el-button v-if="scope.row.edit" plain type="primary" size="mini" @click="saveUpdate(scope)">保存</el-button>
              <el-button v-if="scope.row.edit" plain type="primary" size="mini" @click="cancelUpdate(scope)">取消</el-button>
              <el-button v-if="!scope.row.edit" plain type="danger" size="mini" @click="deleteHandle(scope)">删除</el-button>
            </template>
            <template v-else>
              系统内置
            </template>
          </template>
        </el-table-column>
      </el-table>
    </el-main>
    <el-dialog
      title="删除代码值"
      :visible.sync="deleteVisible"
      width="30%"
    >
      <span>“{{ this.selectDict.aaa102 }}”代码值删除后无法找回！</span>
      <span slot="footer" class="dialog-footer">
        <el-button type="danger" @click="deleteVisible = false">取 消</el-button>
        <el-button type="primary" @click="deleteRoleYes">确 定</el-button>
      </span>
    </el-dialog>
  </el-container>
</template>

<script>
import api from '@/api'

export default {
  name: 'UAa10',
  data() {
    return {
      selectScope: { row: {}},
      selectDict: {
        aaa100: null,
        aaa101: null
      },
      aa00DataList: {
      },
      styleObject: {
        height: '480px',
        marginTop: '10px'
      },
      seletAa00Data: null,
      dataList: [],
      dataListLoading: false,
      deleteVisible: false
    }
  },
  computed: {
  },
  created() {
    this.displayList()
  },
  mounted() {
    this.styleObject.height = (window.innerHeight - 94) + 'px'
  },
  methods: {
    displayList() {
      api.cfgAa10.displayList().then(res => {
        if (res && res.data && res.data.code === 0) {
          this.aa00DataList = res.data.displayList
        } else {
          this.$message.error(res.data.msg)
        }
      })
    },
    displayItems(aa00Data) {
      this.seletAa00Data = aa00Data
      const fetchData = {
        aaa100: aa00Data.aaa100,
        aaa101: aa00Data.aaa100
      }
      api.cfgAa10.displayItems({ data: fetchData }).then(res => {
        if (res && res.data && res.data.code === 0) {
          this.dataList = res.data.displayItems.map(aa00Data => {
            aa00Data.originalAaa103 = aa00Data.aaa103
            aa00Data.originalAaa102 = aa00Data.aaa102
            aa00Data.originalAae013 = aa00Data.aae013
            aa00Data.edit = 0
            return aa00Data
          })
        } else {
          this.$message.error(res.data.msg)
        }
      })
    },
    // 新增
    handleAdd() {
      if (!this.seletAa00Data) {
        this.$message.error('请选择代码类型')
      } else {
        this.dataList.push({ aaa100: this.seletAa00Data.aaa100, aaa101: this.seletAa00Data.aaa101,
          aaa102: '', aaa103: '', edit: 1,
          aae013: '', aaa104: '1', aaa105: '1', aaa106: '0' })
      }
    },
    //  修改
    handleUpdate(scope) {
      this.selectScope.row = { ... scope.row }
      scope.row.edit = 1
    },
    // 修改状态
    aa10StatusChange(scope) {
      const fectData = scope.row
      api.cfgAa10.itemStatusUpdate({ data: fectData }).then(res => {
        if (res && res.data && res.data.code === 0) {
          this.$message({
            message: '修改成功',
            type: 'success'
          })
        } else {
          this.$message.error(res.data.msg)
        }
      })
    },
    saveUpdate(scope) {
      const fectData = scope.row
      api.cfgAa10.itemSaveUpdate({ data: fectData }).then(res => {
        if (res && res.data && res.data.code === 0) {
          this.$message({
            message: '保存成功',
            type: 'success'
          })
          scope.row.originalAaa102 = scope.row.aaa102
          scope.row.edit = 0
        } else {
          this.$message.error(res.data.msg)
        }
      })
    },
    cancelUpdate(scope) {
      scope.row.edit = 0
      if (!this.selectScope.row.originalAaa102) {
        this.dataList.splice(scope.$index, 1)
      } else {
        scope.row.aaa103 = this.selectScope.row.aaa103
        scope.row.aaa102 = this.selectScope.row.aaa102
        scope.row.aae013 = this.selectScope.row.aae013
        this.selectScope.row = {}
      }
    },
    deleteRoleYes() {
      const fetchData = { aaa100: this.selectDict.aaa100, aaa102: this.selectDict.aaa102 }
      api.cfgAa10.itemDelete({ data: fetchData }).then(res => {
        this.deleteVisible = false
        if (res && res.data && res.data.code === 0) {
          this.$message({
            message: '删除成功',
            type: 'success',
            duration: 1500,
            onClose: () => {
              this.displayItems(this.seletAa00Data)
            }
          })
        } else {
          this.$message.error(res.data.msg)
        }
      })
    },
    // 删除
    deleteHandle(scope) {
      this.selectDict.aaa100 = scope.row.aaa100
      this.selectDict.aaa102 = scope.row.aaa102
      this.deleteVisible = true
    }
  }
}
</script>

<style lang="scss" scoped>

.main-container .app-main .el-container .el-aside .el-menu .el-menu-item {
    height: 36px;
    line-height: 36px;
    background-color: #f7f3f3;
}
.main-container .app-main .el-container .el-aside .el-menu .el-menu-item:hover, .el-menu-item:focus {
    outline: none;
    background-color: rgb(198, 218, 241)!important;
}
.typeLable{
    color: rgb(250, 250, 250);
    font-size: 14px;
    font-weight: 700;
    text-align: left;
    background-color:#717273;
    height: 36px;
    padding-top: 10px;
    text-align: center;
  }
</style>

