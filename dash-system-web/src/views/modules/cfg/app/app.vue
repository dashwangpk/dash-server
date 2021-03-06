<template>
  <div class="app-container">
    <el-button type="primary" @click="addOrUpdateHandle()">添加</el-button>

    <el-table
      v-loading="dataListLoading"
      :data="dataList"
      style="width: 100%;"
      border
    >
      <el-table-column
        min-width="80"
        :show-overflow-tooltip="true"
        prop="appSn"
        header-align="center"
        align="left"
        label="应用编号"
      />
      <el-table-column
        min-width="80"
        :show-overflow-tooltip="true"
        prop="name"
        header-align="center"
        align="left"
        label="应用名称"
      />
      <el-table-column
        width="70"
        prop="enable"
        header-align="center"
        align="center"
        label="启用"
      >
        <template slot-scope="scope">
          <el-tag v-if="scope.row.enable === 1 " type="success">启用</el-tag>
          <el-tag v-else type="danger">禁用</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        min-width="160"
        :show-overflow-tooltip="true"
        prop="remark"
        header-align="center"
        align="left"
        label="备注"
      />
      <el-table-column
        min-width="180"
        fixed="right"
        header-align="center"
        align="center"
        label="操作"
      >
        <template slot-scope="scope">
          <el-button type="primary" plain size="mini" @click="sceneHandle(scope.row.appId)">场景</el-button>
          <el-button type="primary" plain size="mini" @click="addOrUpdateHandle(scope.row.appId)">修改</el-button>
          <el-button type="danger" plain size="mini" @click="deleteHandle(scope.row)">删除</el-button>
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
    <scene-add-or-update v-if="sceneAddOrUpdateVisible" ref="sceneAddOrUpdate" @refreshDataList="getDataList" />
    <el-dialog
      title="删除应用"
      :visible.sync="deleteVisible"
      width="30%"
    >
      <span>“{{ this.selectApp.name }}”应用删除后无法找回！</span>
      <span slot="footer" class="dialog-footer">
        <el-button type="danger" @click="deleteVisible = false">取 消</el-button>
        <el-button type="primary" @click="deleteRoleYes">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import AddOrUpdate from './app-add-or-update'
import SceneAddOrUpdate from './scene-add-or-update'
import api from '@/api'

export default {
  name: 'App',
  components: {
    AddOrUpdate,
    SceneAddOrUpdate
  },
  data() {
    return {
      selectApp: {
        name: null
      },
      dataForm: {
        key: ''
      },
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: [],
      addOrUpdateVisible: false,
      deleteVisible: false,
      sceneAddOrUpdateVisible: false
    }
  },
  created() {
    this.getDataList()
  },
  methods: {
    // 获取数据列表
    getDataList() {
      const fectData = {
        params: {
          'page': '' + this.pageIndex,
          'limit': '' + this.pageSize
        }
      }
      this.dataListLoading = true

      api.cfgApp.list({ data: fectData }).then(res => {
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
    // 新增 / 修改
    addOrUpdateHandle(id) {
      this.addOrUpdateVisible = true
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(id)
      })
    },
    // 场景
    sceneHandle(appId) {
      this.sceneAddOrUpdateVisible = true
      this.$nextTick(() => {
        this.$refs.sceneAddOrUpdate.init(appId)
      })
    },
    // 删除
    deleteHandle(row) {
      this.selectApp = row
      this.deleteVisible = true
    },
    deleteRoleYes() {
      const fetchData = { appId: this.selectApp.appId }
      api.cfgApp.delete({ data: fetchData }).then(res => {
        this.deleteVisible = false
        if (res && res.data && res.data.code === 0) {
          this.getDataList()
          this.$message({ message: '删除应用成功', type: 'success' })
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
