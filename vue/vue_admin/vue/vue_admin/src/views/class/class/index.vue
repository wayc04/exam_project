<template>
    <div class="button">
      <el-button type="primary" :icon="Plus" @click="addClassDialogVisible=true">添加班级</el-button>
    </div>
  <!--添加班级dialog-->
  <el-dialog v-model="addClassDialogVisible" title="添加班级" width="500">
    <el-form :model="form">
      <el-form-item label="班级名" :label-width="formLabelWidth">
        <el-input v-model="form.className" autocomplete="off" />
      </el-form-item>

    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="addClassDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="addNewClass">确认</el-button>
      </div>
    </template>
  </el-dialog>


  <el-table :data="tableData" border style="width: 100%">
    <el-table-column fixed prop="classname" label="班级名" width="300"></el-table-column>
    <el-table-column prop="classid" label="班级号" width="120"></el-table-column>
    <el-table-column fixed="right" label="操作" width="150">
      <template #default="{row}">
        <el-button @click="getStudentInClass(row)" type="text" size="small">查看班级学生</el-button>
      </template>
    </el-table-column>
  </el-table>
  <!--学生列表详情页-->
  <el-dialog v-model="getStudentInClassVisible" title="学生列表" width="500">
    <el-table :data="studentsList">
      <el-table-column prop="userid" label="用户ID" width="120" />
      <el-table-column prop="truename" label="姓名" width="120" />
      <el-table-column prop="username" label="用户名" width="120" />
    </el-table>
  </el-dialog>
</template>

<script setup>
    import {Plus} from '@element-plus/icons-vue'
    import { ref, reactive } from 'vue'
    import { getClassList,addClass,getStudentsByClass} from '../../../api/tea'
    const tableData = ref([])
    getClassList().then(({data}) => {
        console.log(data.data)
        tableData.value = data.data
    })

    let addClassDialogVisible = ref(false);
    const form = reactive({className:''})
    const formLabelWidth = '140px'

    const addNewClass = () =>{
      addClass({className:form.className}).then(({data})=>{
        console.log(data.data)
      });
      location.reload();
    }

    let getStudentInClassVisible = ref(false)
    let studentsList = ref([])
    const getStudentInClass = (row) => {
        console.log(row)
        getStudentsByClass({classid:row.classid}).then(({data})=>{
          //console.log(data.data)
          studentsList.value = data.data
        })
      getStudentInClassVisible.value = true
    }

</script>

<style lang="less" scoped>
    .button {
      margin-bottom: 20px;
    }
</style>