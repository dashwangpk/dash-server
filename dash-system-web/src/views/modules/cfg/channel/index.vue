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
        prop="channelSn"
        min-width="80"
        :show-overflow-tooltip="true"
        header-align="center"
        align="left"
        label="渠道编号"
      />
      <el-table-column
        prop="name"
        min-width="80"
        :show-overflow-tooltip="true"
        header-align="center"
        align="left"
        label="渠道名称"
      />
      <el-table-column
        min-width="70"
        prop="enable"
        header-align="center"
        align="center"
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
        min-width="160"
        :show-overflow-tooltip="true"
        align="left"
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
          <el-button type="primary" plain size="mini" @click="addOrUpdateHandle(scope.row.channelId)">修改</el-button>
          <!-- <el-button type="danger" size="mini" @click="deleteHandle(scope.row)">删除</el-button> -->
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

import AddOrUpdate from './add-or-update'
import api from '@/api'

export default {
  name: 'Channel',
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

      api.cfgChannel.list({ data: fectData }).then(res => {
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
