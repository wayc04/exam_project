<template>
    <div>
      <el-button type="primary" @click="openAddQuestionDialog">添加编程题</el-button>
      <h1>编程题展示面板</h1>
      <el-table :data="tableData" style="width: 100%">
        <el-table-column label="题目" width="300">
          <template #default="{ row }">
            <div v-html="formatContent(row.scontent)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="difficulty" label="难度" width="100" />
        <el-table-column prop="timeout" label="时间限制 (秒)" width="150" />
        <el-table-column prop="memorylimit" label="内存限制 (MB)" width="150" />
        <el-table-column prop="cname" label="所属课程" width="150" />
        <el-table-column fixed="right" label="操作" min-width="220">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleDetail(row.sid)">
              测试点信息
            </el-button>
            <el-button type="danger" size="small" @click="deleteCodeQuestionSubmit(row.sid)">
              删除编程题
            </el-button>
            <el-button type="warning" size="small" @click="openEditQuestionDialog(row)">
              修改编程题
            </el-button>
          </template>
        </el-table-column>
      </el-table>
  
      <!-- 展现测试点信息 -->
      <el-dialog v-model="dialogVisible" title="测试点信息">
        <el-table :data="testPoints" style="width: 100%">
          <el-table-column prop="tid" label="测试点ID" width="100" />
          <el-table-column prop="t1" label="测试点" width="200">
            <template #default="{ row }">
              <div v-html="formatContent(row.t1)"></div>
            </template>
          </el-table-column>
          <el-table-column prop="answer1" label="答案" width="200">
            <template #default="{ row }">
              <div v-html="formatContent(row.answer1)"></div>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="180">
            <template #default="{ row }">
              <el-button type="danger" size="small" @click="deleteTestPointSubmit(row.tid)">
                删除
              </el-button>
              <el-button type="warning" size="small" @click="openEditTestPointDialog(row)">
                修改
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible = false">关闭</el-button>
          <el-button type="primary" @click="openAddTestPointDialog(currentSid)">添加测试点</el-button>
        </span>
      </el-dialog>
  
      <!-- 添加编程题 -->
      <el-dialog v-model="addQuestionDialogVisible" title="添加编程题">
        <el-form :model="newQuestion">
          <el-form-item label="题目内容" :label-width="formLabelWidth">
            <el-input type="textarea" v-model="newQuestion.scontent" />
          </el-form-item>
          <el-form-item label="所属课程" :label-width="formLabelWidth">
            <el-select v-model="newQuestion.cno" placeholder="选择课程">
              <el-option
                v-for="course in courses"
                :key="course.cno"
                :label="course.cname"
                :value="course.cno"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="难度" :label-width="formLabelWidth">
            <el-input v-model.number="newQuestion.difficulty" type="number" autocomplete="off" />
          </el-form-item>
          <el-form-item label="时间限制 (秒)" :label-width="formLabelWidth">
            <el-input v-model.number="newQuestion.timeout" type="number" autocomplete="off" />
          </el-form-item>
          <el-form-item label="内存限制 (MB)" :label-width="formLabelWidth">
            <el-input v-model.number="newQuestion.memorylimit" type="number" autocomplete="off" />
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="addQuestion">添加</el-button>
          <el-button @click="addQuestionDialogVisible = false">取消</el-button>
        </span>
      </el-dialog>
  
      <!-- 编辑编程题 -->
      <el-dialog v-model="editQuestionDialogVisible" title="修改编程题">
        <el-form :model="editQuestion">
          <el-form-item label="题目内容" :label-width="formLabelWidth">
            <el-input type="textarea" v-model="editQuestion.scontent" />
          </el-form-item>
          <el-form-item label="所属课程" :label-width="formLabelWidth">
            <el-select v-model="editQuestion.cno" placeholder="选择课程">
              <el-option
                v-for="course in courses"
                :key="course.cno"
                :label="course.cname"
                :value="course.cno"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="难度" :label-width="formLabelWidth">
            <el-input v-model.number="editQuestion.difficulty" type="number" autocomplete="off" />
          </el-form-item>
          <el-form-item label="时间限制 (秒)" :label-width="formLabelWidth">
            <el-input v-model.number="editQuestion.timeout" type="number" autocomplete="off" />
          </el-form-item>
          <el-form-item label="内存限制 (MB)" :label-width="formLabelWidth">
            <el-input v-model.number="editQuestion.memorylimit" type="number" autocomplete="off" />
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="updateCodeQuestionSubmit">保存</el-button>
          <el-button @click="editQuestionDialogVisible = false">取消</el-button>
        </span>
      </el-dialog>
  
      <!-- 添加测试点 -->
      <el-dialog v-model="addTestPointDialogVisible" title="添加测试点">
        <el-form :model="newTestPoint">
          <el-form-item label="测试点内容" :label-width="formLabelWidth">
            <el-input type="textarea" v-model="newTestPoint.t1" />
          </el-form-item>
          <el-form-item label="答案" :label-width="formLabelWidth">
            <el-input type="textarea" v-model="newTestPoint.answer1" />
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="addTestPointSubmit">添加</el-button>
          <el-button @click="addTestPointDialogVisible = false">取消</el-button>
        </span>
      </el-dialog>
  
      <!-- 修改测试点 -->
      <el-dialog v-model="editTestPointDialogVisible" title="修改测试点">
        <el-form :model="editTestPoint">
          <el-form-item label="测试点内容" :label-width="formLabelWidth">
            <el-input type="textarea" v-model="editTestPoint.t1" />
          </el-form-item>
          <el-form-item label="答案" :label-width="formLabelWidth">
            <el-input type="textarea" v-model="editTestPoint.answer1" />
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="updateTestPointSubmit">保存</el-button>
          <el-button @click="editTestPointDialogVisible = false">取消</el-button>
        </span>
      </el-dialog>
    </div>
  </template>
  
  <script lang="ts" setup>
  import { ref, onMounted } from 'vue';
  import { findCourseById, findSubjectByType, getTestPointBySid, addCodeQuestion, addTestPoint, deleteTestPoint, updateTestPoint, deleteCodeQuestion, updateCodeQuestion, findAllCourse as findAllCourses } from '../../../api/subject';
  
  const tableData = ref([]);
  const dialogVisible = ref(false);
  const addQuestionDialogVisible = ref(false);
  const addTestPointDialogVisible = ref(false);
  const editTestPointDialogVisible = ref(false);
  const editQuestionDialogVisible = ref(false);
  const testPoints = ref([]);
  const courses = ref([]); // 存储课程列表
  const formLabelWidth = '120px';
  const newQuestion = ref({
    scontent: '',
    difficulty: 1,
    timeout: 60,
    memorylimit: 256,
    cno: null, // 所属课程
  });
  const newTestPoint = ref({
    t1: '',
    answer1: ''
  });
  const editTestPoint = ref({
    tid: null,
    t1: '',
    answer1: ''
  });
  const editQuestion = ref({
    sid: null,
    scontent: '',
    difficulty: 1,
    timeout: 60,
    memorylimit: 256,
    cno: null, // 所属课程
  });
  let currentSid = ref(null); // 跟踪当前题目 ID
  
  const fetchProgrammingQuestions = async () => {
  try {
    const response = await findSubjectByType({ type: '编程题' });
    console.log('编程题:', response);
    if (response.data.code === 1) {
      const questions = response.data.data;

      // 使用 Promise.all 来并行查询每个课程的名称
      const coursePromises = questions.map(async (item) => {
        if (item.cno) {
          const courseResponse = await findCourseById({ cno: item.cno });
          if (courseResponse.data.code === 1) {
            item.cname = courseResponse.data.data.cname; // 假设返回的课程数据在 data.data 中
          } else {
            console.error('获取课程名称失败:', courseResponse.msg);
            item.cname = '未知课程'; // 设置默认值
          }
        } else {
          item.cname = '未知课程'; // 如果没有 cno，设置默认值
        }
        return item;
      });

      // 等待所有课程名称的查询完成
      tableData.value = await Promise.all(coursePromises);
      console.log('编程题数据:', tableData.value); // 打印检查
    } else {
      console.error('获取编程题失败:', response.msg);
    }
  } catch (error) {
    console.error('获取编程题失败:', error);
  }
};
  
  const fetchCourses = async () => {
    try {
      const response = await findAllCourses();
      if (response.data.code === 1) {
        courses.value = response.data.data; // 假设返回的课程数据在 data.data 中
      } else {
        console.error('获取课程列表失败:', response.msg);
      }
    } catch (error) {
      console.error('获取课程列表失败:', error);
    }
  };
  
  const formatContent = (content) => {
    return content.replace(/\n/g, '<br>'); // 将换行符替换为 <br>
  };
  
  const handleDetail = async (sid) => {
    currentSid.value = sid; // 设置当前题目 ID
    try {
      const response = await getTestPointBySid({ sid });
      console.log('测试点信息:', response);
      if (response.data.code === 1) {
        testPoints.value = response.data.data; // 保存测试点信息
        dialogVisible.value = true; // 显示对话框
      } else {
        console.error('获取测试点失败:', response.msg);
      }
    } catch (error) {
      console.error('获取测试点失败:', error);
    }
  };
  
  const deleteTestPointSubmit = async (tid) => {
    try {
      const response = await deleteTestPoint({ tid });
      console.log('删除测试点:', response);
      if (response.data.code === 1) {
        // 重新获取测试点信息以更新列表
        await handleDetail(currentSid.value);
      } else {
        console.error('删除测试点失败:', response.msg);
      }
    } catch (error) {
      console.error('删除测试点失败:', error);
    }
  };

  const openEditTestPointDialog = (row) => {
    // 填充编辑表单
    editTestPoint.value = { tid: row.tid, t1: row.t1, answer1: row.answer1 };
    editTestPointDialogVisible.value = true; // 显示编辑对话框
  };

  const updateTestPointSubmit = async () => {
    try {
      const response = await updateTestPoint({
        tid: editTestPoint.value.tid,
        t1: editTestPoint.value.t1,
        answer1: editTestPoint.value.answer1
      });
      console.log('修改测试点:', response);
      if (response.data.code === 1) {
        // 重新获取测试点信息以更新列表
        await handleDetail(currentSid.value);
        editTestPointDialogVisible.value = false; // 关闭对话框
      } else {
        console.error('修改测试点失败:', response.msg);
      }
    } catch (error) {
      console.error('修改测试点失败:', error);
    }
  };

  const deleteCodeQuestionSubmit = async (sid) => {
    try {
      const response = await deleteCodeQuestion({ sid });
      console.log('删除编程题:', response);
      if (response.data.code === 1) {
        // 重新获取编程题以更新列表
        await fetchProgrammingQuestions();
      } else {
        console.error('删除编程题失败:', response.msg);
      }
    } catch (error) {
      console.error('删除编程题失败:', error);
    }
  };
  
  const openEditQuestionDialog = (row) => {
    // 填充编辑表单
    editQuestion.value = { sid: row.sid, scontent: row.scontent, difficulty: row.difficulty, timeout: row.timeout, memorylimit: row.memorylimit, cno: row.cno };
    editQuestionDialogVisible.value = true; // 显示编辑对话框
  };
  
  const updateCodeQuestionSubmit = async () => {
    try {
      const response = await updateCodeQuestion(editQuestion.value);
      console.log('修改编程题:', response);
      if (response.data.code === 1) {
        // 重新获取编程题以更新列表
        await fetchProgrammingQuestions();
        editQuestionDialogVisible.value = false; // 关闭对话框
      } else {
        console.error('修改编程题失败:', response.msg);
      }
    } catch (error) {
      console.error('修改编程题失败:', error);
    }
  };
  
  const openAddQuestionDialog = () => {
    addQuestionDialogVisible.value = true; // 显示添加题目对话框
  };
  
  const addQuestion = async () => {
    try {
      const response = await addCodeQuestion(newQuestion.value);
      console.log('添加编程题:', response);
      if (response.data.code === 1) {
        // 重新获取编程题以更新列表
        await fetchProgrammingQuestions();
        addQuestionDialogVisible.value = false; // 关闭对话框
        // 重置表单
        newQuestion.value = { scontent: '', difficulty: 1, timeout: 60, memorylimit: 256, cno: null };
      } else {
        console.error('添加编程题失败:', response.msg);
      }
    } catch (error) {
      console.error('添加编程题失败:', error);
    }
  };
  
  const openAddTestPointDialog = (sid) => {
    currentSid.value = sid; // 设置当前题目 ID
    addTestPointDialogVisible.value = true; // 显示添加测试点对话框
  };
  
  const addTestPointSubmit = async () => {
    try {
      console.log('currentSid:', currentSid.value, 'newTestPoint:', newTestPoint.value);
      const response = await addTestPoint({
        sid: currentSid.value,
        t1: newTestPoint.value.t1,
        answer1: newTestPoint.value.answer1
      });
      console.log('添加测试点:', response);
      if (response.data.code === 1) {
        // 重新获取测试点以更新列表
        await handleDetail(currentSid.value);
        addTestPointDialogVisible.value = false; // 关闭对话框
        // 重置表单
        newTestPoint.value = { t1: '', answer1: '' };
      } else {
        console.error('添加测试点失败:', response.msg);
      }
    } catch (error) {
      console.error('添加测试点失败:', error);
    }
  };
  
  // 组件挂载时获取编程题和课程列表
  onMounted(async () => {
    await fetchProgrammingQuestions();
    await fetchCourses();
  });
  </script>
  
  <style scoped>
  /* 可以在这里添加样式 */
  </style>