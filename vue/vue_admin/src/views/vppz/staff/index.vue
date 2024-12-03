<template>
  <el-button type="primary" @click="openAddDialog">新增题目</el-button>
  
  <h1>选择题</h1>
  <el-table :data="tableSelectData" style="width: 100%" highlight-current-row>
      <el-table-column fixed prop="scontent" label="题目" width="200" />
      <el-table-column prop="cname" label="所属课程" width="120" />
      <el-table-column prop="sa" label="选项A" width="120" />
      <el-table-column prop="sb" label="选项B" width="120" />
      <el-table-column prop="sc" label="选项C" width="120" />
      <el-table-column prop="sd" label="选项D" width="120" />
      <el-table-column prop="skey" label="答案" width="120"/>
      <el-table-column prop="difficulty" label="难度" width="120"/>
      <el-table-column fixed="right" label="Operations" min-width="120">
          <template #default="{ row }">
              <el-button link type="danger" size="small" @click="showDeleteDialog(row)">
                  删除
              </el-button>
              <el-button link type="primary" size="small" @click="Editxzt(row, 'choice')">修改</el-button>
          </template>
      </el-table-column>
  </el-table>

  <h1>判断题</h1>
  <el-table :data="tableJudgeData" style="width: 100%" highlight-current-row>
      <el-table-column fixed prop="scontent" label="题目" width="200" />
      <el-table-column prop="cname" label="所属课程" width="120" />
      <el-table-column prop="skey" label="答案" width="120"/>
      <el-table-column prop="difficulty" label="难度" width="120"/>
      <el-table-column fixed="right" label="Operations" min-width="120">
          <template #default="{ row }">
              <el-button link type="danger" size="small" @click="showDeleteDialog(row)">
                  删除
              </el-button>
              <el-button link type="primary" size="small" @click="Editxzt(row, 'judge')">修改</el-button>
          </template>
      </el-table-column>
  </el-table>

  <h1>填空题</h1>
  <el-table :data="tableShortAnswerData" style="width: 100%" highlight-current-row>
      <el-table-column fixed prop="scontent" label="题目" width="200" />
      <el-table-column prop="cname" label="所属课程" width="120" />
      <el-table-column prop="skey" label="参考答案" width="120"/>
      <el-table-column prop="difficulty" label="难度" width="120"/>
      <el-table-column fixed="right" label="Operations" min-width="120">
          <template #default="{ row }">
              <el-button link type="danger" size="small" @click="showDeleteDialog(row)">
                  删除
              </el-button>
              <el-button link type="primary" size="small" @click="Editxzt(row, 'shortAnswer')">修改</el-button>
          </template>
      </el-table-column>
  </el-table>

  <!-- 删除提示框 -->
  <el-dialog v-model="centerDeleteDialogVisible" title="警告" width="500" center>
      <span>此操作将永久删除该题目, 是否继续?</span>
      <template #footer>
          <div class="dialog-footer">
              <el-button @click="centerDeleteDialogVisible = false">取消</el-button>
              <el-button type="primary" @click="confirmDelete">确定</el-button>
          </div>
      </template>
  </el-dialog>

  <!-- 修改框 -->
  <el-dialog v-model="dialogFormVisible" title="修改题目信息" width="500">
      <el-form :model="xztform">
          <el-form-item label="题目" :label-width="formLabelWidth">
              <el-input v-model="xztform.scontent" autocomplete="off" />
          </el-form-item>

          <!-- 新增选择课程 -->
          <el-form-item label="所属课程" :label-width="formLabelWidth">
              <el-select v-model="xztform.cno" placeholder="选择课程">
                  <el-option
                      v-for="course in courses"
                      :key="course.cno"
                      :label="course.cname"
                      :value="course.cno"
                  />
              </el-select>
          </el-form-item>

          <template v-if="currentQuestionType === 'choice'">
              <el-form-item label="A选项" :label-width="formLabelWidth">
                  <el-input v-model="xztform.sa" autocomplete="off" />
              </el-form-item>
              <el-form-item label="B选项" :label-width="formLabelWidth">
                  <el-input v-model="xztform.sb" autocomplete="off" />
              </el-form-item>
              <el-form-item label="C选项" :label-width="formLabelWidth">
                  <el-input v-model="xztform.sc" autocomplete="off" />
              </el-form-item>
              <el-form-item label="D选项" :label-width="formLabelWidth">
                  <el-input v-model="xztform.sd" autocomplete="off" />
              </el-form-item>
          </template>

          <el-form-item label="正确答案" :label-width="formLabelWidth">
              <el-input v-model="xztform.skey" autocomplete="off" />
          </el-form-item>
          <el-form-item label="难度" :label-width="formLabelWidth">
              <el-input v-model="xztform.difficulty" autocomplete="off" />
          </el-form-item>
      </el-form>
      <template #footer>
          <div class="dialog-footer">
              <el-button @click="dialogFormVisible = false">取消</el-button>
              <el-button :plain="true" type="primary" @click="xztsubmit">
                  确定
              </el-button>
          </div>
      </template>
  </el-dialog>

  <!-- 新增题目对话框 -->
  <el-dialog v-model="addDialogVisible" title="新增题目" width="500">
      <el-form :model="newQuestionForm">
          <el-form-item label="课程" :label-width="formLabelWidth">
              <el-select v-model="newQuestionForm.cno" placeholder="选择课程">
                  <el-option
                      v-for="course in courses"
                      :key="course.cno"
                      :label="course.cname"
                      :value="course.cno"
                  />
              </el-select>
          </el-form-item>
          <el-form-item label="题目类型" :label-width="formLabelWidth">
              <el-select v-model="newQuestionForm.type" placeholder="选择题目类型" @change="handleQuestionTypeChange">
                  <el-option label="选择题" value="选择题" />
                  <el-option label="判断题" value="判断题" />
                  <el-option label="填空题" value="简答题" />
              </el-select>
          </el-form-item>

          <template v-if="newQuestionForm.type === '选择题'">
              <el-form-item label="题目" :label-width="formLabelWidth">
                  <el-input v-model="newQuestionForm.scontent" autocomplete="off" />
              </el-form-item>
              <el-form-item label="A选项" :label-width="formLabelWidth">
                  <el-input v-model="newQuestionForm.sa" autocomplete="off" />
              </el-form-item>
              <el-form-item label="B选项" :label-width="formLabelWidth">
                  <el-input v-model="newQuestionForm.sb" autocomplete="off" />
              </el-form-item>
              <el-form-item label="C选项" :label-width="formLabelWidth">
                  <el-input v-model="newQuestionForm.sc" autocomplete="off" />
              </el-form-item>
              <el-form-item label="D选项" :label-width="formLabelWidth">
                  <el-input v-model="newQuestionForm.sd" autocomplete="off" />
              </el-form-item>
              <el-form-item label="正确答案" :label-width="formLabelWidth">
                  <el-input v-model="newQuestionForm.skey" autocomplete="off" />
              </el-form-item>
          </template>

          <template v-if="newQuestionForm.type === '判断题'">
              <el-form-item label="题目" :label-width="formLabelWidth">
                  <el-input v-model="newQuestionForm.scontent" autocomplete="off" />
              </el-form-item>
              <el-form-item label="正确答案" :label-width="formLabelWidth">
                  <el-input v-model="newQuestionForm.skey" autocomplete="off" />
              </el-form-item>
          </template>

          <template v-if="newQuestionForm.type === '简答题'">
              <el-form-item label="题目" :label-width="formLabelWidth">
                  <el-input v-model="newQuestionForm.scontent" autocomplete="off" />
              </el-form-item>
              <el-form-item label="参考答案" :label-width="formLabelWidth">
                  <el-input v-model="newQuestionForm.skey" autocomplete="off" />
              </el-form-item>
          </template>

          <el-form-item label="难度" :label-width="formLabelWidth">
              <el-input v-model.number="newQuestionForm.difficulty" type="number" autocomplete="off" />
          </el-form-item>
      </el-form>
      <template #footer>
          <div class="dialog-footer">
              <el-button @click="addDialogVisible = false">取消</el-button>
              <el-button type="primary" @click="submitNewQuestion">确认</el-button>
          </div>
      </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import {
  getChoiceQuestion,
  getJudgeQuestion,
  getShortAnswerQuestion,
  updateQuestion,
  deleteQuestion,
  findCourseById,
  findAllCourse,
  addSubject,
} from '../../../api/subject';

const tableSelectData = ref([]);
const tableJudgeData = ref([]);
const tableShortAnswerData = ref([]);
const deleteSid = ref(null);
const centerDeleteDialogVisible = ref(false);
const dialogFormVisible = ref(false);
const formLabelWidth = '100px';
const currentQuestionType = ref('');
const addDialogVisible = ref(false); // 新增对话框的可见性
const courses = ref([]); // 存储课程列表

const xztform = reactive({
  scontent: '',
  sa: '',
  sb: '',
  sc: '',
  sd: '',
  skey: '',
  difficulty: '',
  sid: 0,
  cno: 0,
  cname: '',
});

const newQuestionForm = reactive({
    cno: null,
    type: '',
    scontent: '',
    sa: '',
    sb: '',
    sc: '',
    sd: '',
    skey: '',
    difficulty: null, // 难度可以为数字
});

// 获取所有课程
const fetchCourses = async () => {
    try {
        const response = await findAllCourse();
        courses.value = response.data.data; // 假设数据格式为 { data: { data: [...] } }
    } catch (error) {
        console.error('获取课程失败:', error);
    }
};

// 打开新增题目对话框
const openAddDialog = () => {
    addDialogVisible.value = true;
    // 重置表单
    Object.assign(newQuestionForm, {
        cno: null,
        type: '',
        scontent: '',
        sa: '',
        sb: '',
        sc: '',
        sd: '',
        skey: '',
        difficulty: null,
    });
};

// 处理题目类型变化
const handleQuestionTypeChange = (value) => {
    newQuestionForm.type = value;
};

// 提交新增题目
const submitNewQuestion = async () => {
    try {
        const questionData = {
            cno: newQuestionForm.cno,
            scontent: newQuestionForm.scontent,
            sa: newQuestionForm.sa,
            sb: newQuestionForm.sb,
            sc: newQuestionForm.sc,
            sd: newQuestionForm.sd,
            skey: newQuestionForm.skey,
            type: newQuestionForm.type,
            difficulty: newQuestionForm.difficulty,
        };
        await addSubject(questionData);
        console.log('新增题目成功:', questionData);
        
        await fetchQuestions(); // 刷新题目列表

        addDialogVisible.value = false; // 关闭对话框
    } catch (error) {
        console.error('新增题目失败:', error);
    }
};

// 初始获取所有课程和题目
onMounted(async () => {
    await fetchCourses();
    await fetchQuestions();
});

// Fetch questions and update data
const fetchQuestions = async () => {
  const choiceData = await getChoiceQuestion();
  const judgeData = await getJudgeQuestion();
  const shortAnswerData = await getShortAnswerQuestion();

  tableSelectData.value = choiceData.data.data;
  tableJudgeData.value = judgeData.data.data;
  tableShortAnswerData.value = shortAnswerData.data.data;

  updateCourseNames(tableSelectData.value);
  updateCourseNames(tableJudgeData.value);
  updateCourseNames(tableShortAnswerData.value);
};

// Update course names for the provided data
const updateCourseNames = async (dataArray) => {
  for (const item of dataArray) {
      item.cname = await cnoToCname(Number(item.cno));
  }
};

// Edit question
const Editxzt = (row, type) => {
  Object.assign(xztform, { ...row });
  currentQuestionType.value = type; // 设置当前题目类型
  dialogFormVisible.value = true;
};

// Update question
const xztsubmit = async () => {
  try {
      await updateQuestion(xztform);
      await fetchQuestions();
      dialogFormVisible.value = false;
  } catch (error) {
      console.error('更新失败:', error);
  }
};

// Delete question
const showDeleteDialog = (row) => {
  centerDeleteDialogVisible.value = true;
  deleteSid.value = row.sid;
};

// Confirm delete
const confirmDelete = async () => {
  try {
      await deleteQuestion({ sid: Number(deleteSid.value) });
      await fetchQuestions();
      centerDeleteDialogVisible.value = false;
  } catch (error) {
      console.error('删除失败:', error);
  }
};

// Convert course number to name
const cnoToCname = async (cno) => {
  const { data } = await findCourseById({cno: cno});
  return data.data.cname;
};

// Initial fetch
fetchQuestions();
</script>

<style lang="scss" scoped>

</style>