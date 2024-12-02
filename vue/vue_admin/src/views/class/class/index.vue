<template>
    <div class="button">
      <el-button type="primary" :icon="Plus">添加班级</el-button>
    </div>

     <el-table
    :data="tableData"
    border
    style="width: 100%">
    <el-table-column
    fixed
      prop="classname"
      label="班级名"
      width="300">
    </el-table-column>
    <el-table-column
      prop="classid"
      label="班级号"
      width="120">
    </el-table-column>
    <el-table-column
      fixed="right"
      label="操作"
      width="100">
      <template v-slot="scope">
        <el-button @click="handleClick(scope.row)" type="text" size="small">查看</el-button>
      </template>
    </el-table-column>
  </el-table>
</template>

<script setup>
    import {Plus} from '@element-plus/icons-vue'
    import { ref, reactive } from 'vue'
    import { getClassList} from '../../../api/tea'
    const tableData = ref([])
    getClassList().then(({data}) => {
        console.log(data.data)
        tableData.value = data.data
    })

    
   
  
    const handleClick = (item, index) => {
        console.log(item)
    }

</script>

<style lang="less" scoped>
    .button {
      margin-bottom: 20px;
    }
</style>