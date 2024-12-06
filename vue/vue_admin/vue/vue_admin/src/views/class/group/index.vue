<template>
    <div>
      <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
        <el-button type="primary" @click="openAddDialog">添加用户</el-button> <!-- 添加按钮 -->
        <div style="display: flex; align-items: center;">
          <el-input
            placeholder="搜索用户名"
            v-model="searchUsername"
            style="width: 200px; margin-right: 10px;"
          />
          <el-input
            placeholder="搜索真实姓名"
            v-model="searchTruename"
            style="width: 200px; margin-right: 10px;"
          />
          <el-select v-model="selectedClassId" placeholder="选择班级" style="width: 200px; margin-right: 10px;">
            <el-option label="所有班级" :value="undefined" />
            <el-option
              v-for="cls in classOptions"
              :key="cls.classid"
              :label="cls.classname"
              :value="cls.classid"
            />
          </el-select>
          <el-button type="primary" @click="searchStudents">搜索</el-button>
        </div>
      </div>
  
      <el-table :data="tableData" style="width: 100%">
        <el-table-column prop="username" label="用户名" width="150" />
        <el-table-column prop="truename" label="真实姓名" width="150" />
        <el-table-column prop="classid" label="班级" width="100">
          <template #default="{ row }">
            {{ getClassName(row.classid) }}
          </template>
        </el-table-column>
        <el-table-column label="密码" width="150">
          <template #default="{ row }">
            <div class="password-cell" @mouseover="row.showPassword = true" @mouseleave="row.showPassword = false">
              <span v-if="!row.showPassword">******</span>
              <span v-else>{{ row.userpwd }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="操作" min-width="120">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleDetail(row)">
              详情
            </el-button>
            <el-button link type="primary" size="small" @click="openEditDialog(row)">
              编辑
            </el-button>
          </template>
        </el-table-column>
      </el-table>
  
      <!-- 编辑对话框 -->
      <el-dialog title="编辑学生信息" v-model="editDialogVisible">
        <el-form :model="currentStudent">
          <el-form-item label="用户名" class="form-item" :label-width="formLabelWidth">
            <el-input v-model="currentStudent.username" />
          </el-form-item>
          <el-form-item label="真实姓名" class="form-item" :label-width="formLabelWidth">
            <el-input v-model="currentStudent.truename" />
          </el-form-item>
          <el-form-item label="班级" class="form-item" :label-width="formLabelWidth">
            <el-select v-model="currentStudent.classid" placeholder="选择班级">
              <el-option
                v-for="cls in classOptions"
                :key="cls.classid"
                :label="cls.classname"
                :value="cls.classid"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="密码" class="form-item" :label-width="formLabelWidth">
            <el-input
              v-model="currentStudent.userpwd"
              style="width: 240px"
              type="password"
              placeholder="请输入密码"
              show-password
            />
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="editDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="addOrUpdateStudent">确认</el-button>
        </span>
      </el-dialog>
  
      <!-- 添加用户对话框 -->
      <el-dialog title="添加学生" v-model="addDialogVisible">
        <el-form :model="newStudent">
          <el-form-item label="用户名" class="form-item" :label-width="formLabelWidth">
            <el-input v-model="newStudent.username" />
          </el-form-item>
          <el-form-item label="真实姓名" class="form-item" :label-width="formLabelWidth">
            <el-input v-model="newStudent.truename" />
          </el-form-item>
          <el-form-item label="班级" class="form-item" :label-width="formLabelWidth">
            <el-select v-model="newStudent.classid" placeholder="选择班级">
              <el-option
                v-for="cls in classOptions"
                :key="cls.classid"
                :label="cls.classname"
                :value="cls.classid"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="密码" class="form-item" :label-width="formLabelWidth">
            <el-input
              v-model="newStudent.userpwd"
              style="width: 240px"
              type="password"
              placeholder="请输入密码"
              show-password
            />
          </el-form-item>
          <el-form-item label="确认密码" class="form-item" :label-width="formLabelWidth">
            <el-input
              v-model="newStudent.confirmPassword"
              style="width: 240px"
              type="password"
              placeholder="请再次输入密码"
              show-password
            />
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="addDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="addStudent">确认</el-button>
        </span>
      </el-dialog>
    </div>
</template>
  
<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import { getStuList, findStu, getClassOptions, updateStu, register as addStu } from '../../../api/stu'; // 根据实际路径调整
  
const tableData = ref([]); // 学生列表数据
const searchUsername = ref(''); // 搜索用户名
const searchTruename = ref(''); // 搜索真实姓名
const selectedClassId = ref(undefined); // 选择的班级ID
const classOptions = ref([]); // 班级选项
const classMap = ref({}); // 班级 ID 到名称的映射
const formLabelWidth = ref('100px'); // 表单标签宽度
  
const editDialogVisible = ref(false); // 控制编辑对话框的显示
const addDialogVisible = ref(false); // 控制添加对话框的显示
const currentStudent = ref({}); // 当前编辑的学生信息
const newStudent = ref({ username: '', truename: '', classid: '', userpwd: '', confirmPassword: '' }); // 新学生信息
  
const fetchStudents = async () => {
    try {
      const response = await getStuList({});
      console.log('获取学生列表:', response);
      if (response.data.code === 1) {
        tableData.value = response.data.data.map(student => ({ ...student, showPassword: false }));
      } else {
        console.error('获取学生列表失败:', response.data.msg);
      }
    } catch (error) {
      console.error('请求失败:', error);
    }
};
  
// 获取班级选项
const fetchClassOptions = async () => {
    try {
      const response = await getClassOptions(); // 假设有获取班级选项的接口
      if (response.data.code === 1) {
        classOptions.value = response.data.data; // 保存班级选项
        classMap.value = response.data.data.reduce((map, cls) => {
          map[cls.classid] = cls.classname; // 创建班级 ID 到名称的映射
          return map;
        }, {});
      } else {
        console.error('获取班级选项失败:', response.data.msg);
      }
    } catch (error) {
      console.error('请求失败:', error);
    }
};
  
// 获取班级名称
const getClassName = (classId) => {
    return classMap.value[classId] || '未知班级'; // 如果没有找到，返回“未知班级”
};
  
// 搜索学生
const searchStudents = async () => {
    const params = {
      username: searchUsername.value || undefined,
      truename: searchTruename.value || undefined,
      classid: selectedClassId.value || undefined
    };
  
    try {
      const response = await findStu(params);
      console.log('搜索学生:', response);
      if (response.data.code === 1) {
        tableData.value = response.data.data.map(student => ({ ...student, showPassword: false }));
      } else {
        console.error('搜索失败:', response.data.msg);
      }
    } catch (error) {
      console.error('请求失败:', error);
    }
};
  
// 打开添加对话框
const openAddDialog = () => {
    newStudent.value = { username: '', truename: '', classid: '', userpwd: '', confirmPassword: '' };
    addDialogVisible.value = true; // 显示添加对话框
};
  
// 打开编辑对话框
const openEditDialog = (student) => {
    currentStudent.value = { ...student }; // 复制学生信息到当前学生
    editDialogVisible.value = true; // 显示对话框
};
  
// 添加新学生
const addStudent = async () => {
    if (newStudent.value.userpwd !== newStudent.value.confirmPassword) {
      alert('密码和确认密码不一致！');
      return;
    }
  
    try {
      const response = await addStu(newStudent.value);
      console.log('添加学生:', response);
      if (response.data.code === 1) {
        await fetchStudents(); // 更新成功后重新获取学生列表
        addDialogVisible.value = false; // 关闭对话框
      } else {
        console.error('添加失败:', response.data.msg);
      }
    } catch (error) {
      console.error('请求失败:', error);
    }
};
  
// 更新学生信息
const addOrUpdateStudent = async () => {
    try {
      const response = await updateStu(currentStudent.value) // 更新学生信息
  
      console.log('操作学生:', response);
      if (response.data.code === 1) {
        await fetchStudents(); // 更新成功后重新获取学生列表
        editDialogVisible.value = false; // 关闭对话框
      } else {
        console.error('操作失败:', response.data.msg);
      }
    } catch (error) {
      console.error('请求失败:', error);
    }
};
  
// 组件挂载时获取学生列表和班级选项
onMounted(() => {
    fetchStudents();
    fetchClassOptions();
});

// 操作处理函数
const handleDetail = (row) => {
    console.log('查看详情:', row);
};
  
const handleEdit = (row) => {
    console.log('编辑学生:', row);
};
</script>
  
<style scoped>
.password-cell {
    cursor: pointer; /* 使光标在悬停时变成手型 */
}
.password-cell:hover {
    color: #409EFF; /* 悬停时的颜色 */
}
.form-item label {
    width: 120px; /* 设置统一的标签宽度 */
    text-align: right; /* 标签右对齐 */
}
</style>