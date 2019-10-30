<template>
  <div class="app-container">
    <el-table :data="list" border fit highlight-current-row style="width: 100%">
      <el-table-column align="center" label="经办机构名称" prop="name" />

      <el-table-column align="center" label="统筹区" prop="administrativeDivision" />

      <el-table-column label="医院编码" align="center">
        <template slot-scope="scope">
          <template v-if="scope.row.edit">
            <el-input v-model="scope.row.hospitalSn" />
          </template>
          <span v-else>{{ scope.row.hospitalSn }}</span>
        </template>
      </el-table-column>

      <el-table-column label="密码" align="center">
        <template slot-scope="scope">
          <template v-if="scope.row.edit">
            <el-input v-model="scope.row.hospitalPassword" style="width:100%" />
          </template>
          <span v-else>{{ scope.row.hospitalPassword }}</span>
        </template>
      </el-table-column>

      <el-table-column label="启用标志" align="center">
        <template slot-scope="scope">
          <template v-if="scope.row.edit">
            <el-select v-model="scope.row.enable" class="filter-item" placeholder="Please select">
              <el-option
                v-for="item in enableOptions"
                :key="item.key"
                :label="item.display_name"
                :value="item.key"
              />
            </el-select>
          </template>
          <span v-else-if="scope.row.enable == '0'">启用</span>
          <span v-else>禁用</span>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" min-width="120px">
        <template slot-scope="scope">
          <template v-if="scope.row.edit">
            <el-button
              plain
              type="success"
              icon="el-icon-circle-check-outline"
              @click="confirmEdit(scope.row)"
            >确定</el-button>
            <el-button icon="el-icon-refresh" type="warning" plain @click="cancelEdit(scope.row)">取消</el-button>
          </template>

          <template v-else>
            <el-button
              type="primary"
              icon="el-icon-edit"
              plain
              @click="scope.row.edit=!scope.row.edit"
            >编辑</el-button>
          </template>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import api from '@/api'

export default {
  name: 'MedicalIns',
  data() {
    return {
      enableOptions: [
        { key: '0', display_name: '启用' },
        { key: '1', display_name: '禁用' }
      ],
      list: [],
      listQuery: {
        page: 1,
        limit: 10
      }
    }
  },
  created() {},
  mounted() {
    this.getList()
  },
  methods: {
    getList() {
      api.payConfig.findMedIns({ data: {}}).then(res => {
        console.log(res)
        if (res.data && res.data.body) {
          const items = res.data.body
          this.list = items.map(v => {
            this.$set(v, 'edit', false) // https://vuejs.org/v2/guide/reactivity.html
            v.originalTitle = v.title //  will be used when user click the cancel botton
            return v
          })
        }
      })
    },
    cancelEdit(row) {
      row.title = row.originalTitle
      row.edit = false
    },
    confirmEdit(row) {
      row.edit = false
      row.originalTitle = row.title
      const reqData = row
      api.payConfig.insertMedIns({ data: reqData }).then(res => {
        if (res.data.success) {
          this.$message({
            message: res.data.message,
            type: 'success'
          })
        } else {
          this.$message({
            message: res.data.message,
            type: 'error'
          })
        }
      })
    }
  }
}
</script>

<style scoped>
.edit-input {
  padding-right: 100px;
}
.cancel-btn {
  position: absolute;
  right: 15px;
  top: 10px;
}
</style>
