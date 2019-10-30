<template>
  <div class="app-container">

    <el-table
      v-loading="dataListLoading"
      :data="dataList"
      row-key="hospitalId"
      style="width: 100%;"
      border
    >
      <el-table-column
        prop="name"
        header-align="center"
        align="left"
        :show-overflow-tooltip="true"
        min-width="80"
        label="机构名称"
      />
      <el-table-column
        prop="hospitalSn"
        header-align="center"
        align="left"
        :show-overflow-tooltip="true"
        min-width="80"
        label="机构编号"
      />
      <el-table-column
        prop="enable"
        header-align="center"
        align="center"
        min-width="70"
        label="启用状态"
      >
        <template slot-scope="scope">
          <el-tag v-if="scope.row.enable === 1 " type="success">启用</el-tag>
          <el-tag v-else type="danger">禁用</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="remark"
        header-align="center"
        align="left"
        min-width="160"
        :show-overflow-tooltip="true"
        label="备注"
      />
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        min-width="180"
        label="操作"
      >
        <template slot-scope="scope">
          <el-button plain type="primary" size="mini" @click="addOrUpdateHandle(null, scope.row)">添加</el-button>
          <el-button plain type="primary" size="mini" @click="addOrUpdateHandle(scope.row.hospitalId)">修改</el-button>
          <el-button :disabled="scope.row.children.length > 0 || scope.row.parentId === 0 " plain type="danger" size="mini" @click="deleteHandle(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataTree" />
    <el-dialog
      title="删除医院"
      :visible.sync="deleteVisible"
      width="30%"
    >
      <span>“{{ this.selectHospital.name }}”医院删除后无法找回！</span>
      <span slot="footer" class="dialog-footer">
        <el-button type="danger" @click="deleteVisible = false">取 消</el-button>
        <el-button type="primary" @click="deleteRoleYes()">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import AddOrUpdate from './hospital-add-or-update'
import api from '@/api'

export default {
  name: 'Hospital',
  components: {
    AddOrUpdate
  },
  data() {
    return {
      selectHospital: {
        name: null
      },
      dataForm: {
        key: ''
      },
      dataList: [],
      dataListLoading: false,
      dataListSelections: [],
      addOrUpdateVisible: false,
      deleteVisible: false
    }
  },
  created() {
    this.getDataTree()
  },
  mounted() {
  },
  methods: {
    getDataTree() {
      this.dataListLoading = true
      api.cfgHospital.tree({}).then(res => {
        if (res && res.data && res.data.code === 0) {
          this.dataListLoading = false
          this.dataList = res.data.hospitalTree
        } else {
          this.$message.error(res.data.msg)
        }
      })
    },
    // 获取数据列表
    getDataList() {
      const fectData = {
        params: {
          'page': '' + this.pageIndex,
          'limit': '' + this.pageSize
        }
      }
      this.dataListLoading = true

      api.cfgHospital.list({ data: fectData }).then(res => {
        if (res && res.data && res.data.code === 0) {
          this.dataListLoading = false
          this.dataList = res.data.page.list
          this.totalPage = res.data.page.totalCount
        } else {
          this.$message.error(res.data.msg)
        }
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
    // 多选
    selectionChangeHandle(val) {
      this.dataListSelections = val
    },
    // 新增 / 修改
    addOrUpdateHandle(id, parent) {
      this.addOrUpdateVisible = true
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(id, parent)
      })
    },
    // 删除
    deleteHandle(row) {
      this.selectHospital = row
      this.deleteVisible = true
    },
    deleteRoleYes() {
      const fetchData = { hospitalId: this.selectHospital.hospitalId }
      api.cfgHospital.delete({ data: fetchData }).then(res => {
        this.deleteVisible = false
        if (res && res.data && res.data.code === 0) {
          this.getDataTree()
          this.$message({ message: '删除医院成功', type: 'success' })
        } else {
          this.$message({ showClose: true, message: res.data.msg, type: 'error' })
        }
      })
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

