<template>
  <div class="app-container">
    <el-button type="primary" @click="handleAdd">
      添加
    </el-button>

    <el-table
      v-loading="dataListLoading"
      :data="dataList"
      border
      style="width: 100%;"
    >
      <el-table-column
        :show-overflow-tooltip="true"
        min-width="80"
        prop="aaa101"
        header-align="center"
        align="left"
        label="类型名称"
      />
      <el-table-column
        :show-overflow-tooltip="true"
        align="left"
        min-width="80"
        prop="aaa100"
        header-align="center"
        label="代码类型"
      />
      <el-table-column
        :show-overflow-tooltip="true"
        align="center"
        width="80"
        prop="aaa106"
        header-align="center"
        label="排序号"
      />
      <el-table-column
        min-width="120"
        :show-overflow-tooltip="true"
        prop="items"
        header-align="center"
        align="left"
        label="代码值信息"
      />
      <el-table-column
        min-width="120"
        :show-overflow-tooltip="true"
        prop="aae013"
        header-align="center"
        align="left"
        label="备注"
      />
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        min-width="160"
        label="操作"
      >
        <template slot-scope="scope">
          <el-button plain type="primary" size="small" @click="handleUpdate(scope)">修改</el-button>
          <el-button plain type="danger" size="small" @click="deleteHandle(scope)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      :current-page="pageIndex"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="pageSize"
      :total="totalPage"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="sizeChangeHandle"
      @current-change="currentChangeHandle"
    />
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList" />
    <el-dialog
      title="删除字典"
      :visible.sync="deleteVisible"
      width="30%"
    >
      <span>“{{ this.selectDict.aaa101 }}”字典删除后无法找回！</span>
      <span slot="footer" class="dialog-footer">
        <el-button type="danger" @click="deleteVisible = false">取 消</el-button>
        <el-button type="primary" @click="deleteRoleYes">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import api from '@/api'
import AddOrUpdate from './aa10-add-or-update'

export default {
  name: 'Aa10',
  components: {
    AddOrUpdate
  },
  data() {
    return {
      dataForm: {
        aaa101: ''
      },
      selectDict: {
        aaa100: null,
        aaa101: null
      },
      appTreeData: null,
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: [],
      addOrUpdateVisible: false,
      deleteVisible: false
    }
  },
  computed: {
  },
  created() {
    this.getDataList()
  },
  mounted() {
  },
  methods: {
    loadSysAppDictList(data, node, component) {
      this.currentAppData = data
      this.dataForm.paramName = ''
      this.pageIndex = 1
      this.pageSize = 10
      this.totalPage = 0
      this.getDataList()
    },
    loadSysAppMenuList(data, node, component) {
      this.currentAppData = data
      this.dataForm.aaa101 = ''
      this.pageIndex = 1
      this.pageSize = 10
      this.totalPage = 0
      this.getDataList()
    },
    // 获取数据列表
    getDataList() {
      this.dataListLoading = true
      const fetchData = {
        'params': {
          'page': '' + this.pageIndex,
          'limit': '' + this.pageSize
        }
      }
      api.cfgAa10.dictList({ data: fetchData }).then(res => {
        if (res && res.data && res.data.code === 0) {
          this.dataList = res.data.dictList.list
          this.totalPage = res.data.dictList.totalCount
        } else {
          this.$message.error(res.data.msg)
        }
        this.dataListLoading = false
      })
    },
    // 每页数
    sizeChangeHandle(val) {
      this.pageSize = val
      this.pageIndex = 1
      this.getDataList()
    },
    // 当前页
    currentChangeHandle(val) {
      this.pageIndex = val
      this.getDataList()
    },
    // 新增
    handleAdd() {
      this.addOrUpdateVisible = true
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init()
      })
    },
    //  修改
    handleUpdate(scope) {
      this.addOrUpdateVisible = true
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(scope.row.aaa100)
      })
    },
    deleteRoleYes() {
      const fetchData = { aaa100s: [this.selectDict.aaa100] }
      api.cfgAa10.delete({ data: fetchData }).then(res => {
        this.deleteVisible = false
        if (res && res.data && res.data.code === 0) {
          this.$message({
            message: '删除成功',
            type: 'success',
            duration: 1500,
            onClose: () => {
              this.getDataList()
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
      this.selectDict.aaa101 = scope.row.aaa101
      this.deleteVisible = true
    }
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  .roles-table {
    margin-top: 30px;
  }
  .permission-tree {
    margin-bottom: 30px;
  }
}
</style>

