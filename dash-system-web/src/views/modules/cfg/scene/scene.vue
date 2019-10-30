<template>
  <div class="app-container">
    <el-button type="primary" @click="addOrUpdateHandle()">添加</el-button>
    <el-table
      v-loading="dataListLoading"
      :data="dataList"
      border
      style="width: 100%;"
    >
      <el-table-column
        prop="sceneName"
        min-width="80"
        :show-overflow-tooltip="true"
        header-align="center"
        align="center"
        label="支付场景名称"
      />
      <el-table-column
        prop="payMode"
        min-width="80"
        :show-overflow-tooltip="true"
        header-align="center"
        align="center"
        label="支付方式"
      >
        <template slot-scope="scope">
          {{ payModeOPtion[scope.row.payMode] ? payModeOPtion[scope.row.payMode] : scope.row.payMode }}
        </template>
      </el-table-column>
      <el-table-column
        prop="appid"
        min-width="80"
        :show-overflow-tooltip="true"
        header-align="center"
        align="center"
        label="支付方式编号"
      />
      <el-table-column
        min-width="70"
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
        prop="remark"
        header-align="center"
        min-width="160"
        :show-overflow-tooltip="true"
        align="center"
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
          <el-button plain type="primary" size="mini" @click="addOrUpdateHandle(scope.row.sceneId)">修改</el-button>
          <el-button plain type="danger" size="mini" @click="deleteHandle(scope.row)">删除</el-button>
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
  </div>
</template>

<script>

import AddOrUpdate from './scene-add-or-update'
import api from '@/api'

export default {
  name: 'Scene',
  components: {
    AddOrUpdate
  },
  data() {
    return {
      dataForm: {
        key: ''
      },
      dataList: [],
      payModeList: [],
      payModeOPtion: {},
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: [],
      addOrUpdateVisible: false
    }
  },
  created() {
    this.getPayModeList()
  },
  mounted() {
    this.getDataList()
  },
  methods: {
    // 支付方式
    getPayModeList() {
      api.cfgVpaymode.list({}).then(res => {
        if (res && res.data && res.data.code === 0) {
          this.payModeList = res.data.payModeList
          const tempPayModeOPtion = []
          this.payModeList.forEach((item, index, array) => {
            tempPayModeOPtion[item.payMode] = item.alias
          })
          this.payModeOPtion = tempPayModeOPtion
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

      api.cfgScene.list({ data: fectData }).then(res => {
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
    // 删除
    deleteHandle(id) {
      var ids = id ? [id] : this.dataListSelections.map(item => {
        return item.sceneId
      })
      this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: this.$http.adornUrl('/scene/scene/delete'),
          method: 'post',
          data: this.$http.adornData(ids, false)
        }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.getDataList()
              }
            })
          } else {
            this.$message.error(data.msg)
          }
        })
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
