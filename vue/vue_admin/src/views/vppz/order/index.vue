<template>

  <div class="flex flex-wrap gap-4 items-center" style="margin-bottom: 20px">
    <el-select v-model="selectByCourse" placeholder="科目" size="large" style="width: 240px">
      <el-option :key="0" :label="all" :value="0"/>
      <el-option v-for="item in courseInfo" :key="item.cno" :label="item.cname" :value="item.cno"/>
    </el-select>
  </div>

  <div class="button" style="margin-bottom: 20px;" >
    <el-button type="primary" :icon="Plus" @click="searchPaper">搜索</el-button>
  </div>

  <el-table :data="exam_info" style="width: 100%">
    <el-table-column fixed prop="pid" label="试卷ID" width="130" />
    <el-table-column prop="paperName" label="试卷名称" width="300" />

    <el-table-column fixed="right" label="Operations" min-width="210">
      <template #default="{row}">
        <!-- 详情按钮 -->
        <el-button link type="primary" size="small" @click="dialogClick1(row)">题目详情</el-button>
        <!-- 编辑和删除按钮 -->
        <el-button link type="primary" size="small" @click="dialogAddSubject(row)">添加题目</el-button>

        <!--删除试卷-->
        <el-button link type="primary" size="small" @click="dialogDelete(row)">删除试卷</el-button>



      </template>
    </el-table-column>
  </el-table>

  <!--详情页-->
  <el-dialog v-model="dialogVisible" title="试卷题目" width="800">
    <el-table :data="subjectInPaper">
      <el-table-column prop="sid" label="题目ID" width="150" />
      <el-table-column prop="type" label="题目类型" width="100" />
      <el-table-column prop="difficulty" label="难度" width="70" />
      <el-table-column label="题目" width="300">
        <template #default="{ row }">
          <div v-html="row.scontent"></div>
        </template>
      </el-table-column>    </el-table>
  </el-dialog>


    <!-- 添加题目页面 -->
    <el-dialog v-model="dialogAddSubjectVisible" title="添加题目" width="800">
      <h1>选择题</h1>
      <el-table :data="ChoiceQuestion" @selection-change="handleSelectionChange" style="margin-bottom: 10px; margin-top: 10px">
        <!--el-table-column type="selection" width="50" /-->
        <el-table-column prop="sid" label="试题ID" width="150" />
        <el-table-column label="题目" width="250">
          <template #default="{ row }">
            <div v-html="row.scontent"></div>
          </template>
        </el-table-column>
        <el-table-column prop="difficulty" label="难度" width="70" />
        <el-table-column label="分数" width="180">
          <template #default="{ row }">
            <el-input v-model="questionScores[row.sid]" type="number" placeholder="输入分数以添加" style="width: 150px;"></el-input>
          </template>
        </el-table-column>
      </el-table>

      <h1>判断题</h1>
      <el-table :data="JudgeQuestion" @selection-change="handleSelectionChange" style="margin-bottom: 10px; margin-top: 10px">
        <!--el-table-column type="selection" width="50" /-->
        <el-table-column prop="sid" label="试题ID" width="150" />
        <el-table-column label="题目" width="250">
          <template #default="{ row }">
            <div v-html="row.scontent"></div>
          </template>
        </el-table-column>
        <el-table-column prop="difficulty" label="难度" width="70" />
        <el-table-column label="分数" width="180">
          <template #default="{ row }">
            <el-input v-model="questionScores[row.sid]" type="number" placeholder="输入分数以添加" style="width: 150px;"></el-input>
          </template>
        </el-table-column>
      </el-table>

      <h1>简答题</h1>
      <el-table :data="ShortAnswerQuestion" @selection-change="handleSelectionChange" style="margin-bottom: 10px; margin-top: 10px">
        <!--el-table-column type="selection" width="50" /-->
        <el-table-column prop="sid" label="试题ID" width="150" />
        <el-table-column label="题目" width="250">
          <template #default="{ row }">
            <div v-html="row.scontent"></div>
          </template>
        </el-table-column>
        <el-table-column prop="difficulty" label="难度" width="70" />
        <el-table-column label="分数" width="180">
          <template #default="{ row }">
            <el-input v-model="questionScores[row.sid]" type="number" placeholder="输入分数以添加" style="width: 150px;"></el-input>
          </template>
        </el-table-column>
      </el-table>

      <h1>编程题</h1>
      <el-table :data="ProgramQuestion" @selection-change="handleSelectionChange" style="margin-bottom: 10px; margin-top: 10px">
        <!--el-table-column type="selection" width="50" /-->
        <el-table-column prop="sid" label="试题ID" width="150" />
        <el-table-column label="题目" width="250">
          <template #default="{ row }">
            <div v-html="row.scontent"></div>
          </template>
        </el-table-column>
        <el-table-column prop="difficulty" label="难度" width="70" />
        <el-table-column label="分数" width="180">
          <template #default="{ row }">
            <el-input v-model="questionScores[row.sid]" type="number" placeholder="输入分数以添加" style="width: 150px;"></el-input>
          </template>
        </el-table-column>
      </el-table>

      <div class="dialog-footer">
        <el-button @click="dialogAddSubjectVisible = false">取消</el-button>
        <el-button type="primary" @click="addSubject">确认</el-button>
      </div>
    </el-dialog>

<!--删除页-->
  <el-dialog v-model="dialogDeleteVisible" title="确认删除？" width="500">
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogDeleteVisible = false">取消</el-button>
        <el-button type="primary" @click="deleteExam">确认</el-button>
      </div>
    </template>
  </el-dialog>

  <!--添加试卷详情页-->
  <el-dialog v-model="dialogAddExamVisible" title="添加试卷" width="500">
    <el-form :model="form">
      <el-form-item label="试卷名" :label-width="formLabelWidth">
        <el-input v-model="form.pname" autocomplete="off" />
      </el-form-item>
      <el-form-item label="科目" :label-width="formLabelWidth">
        <el-select v-model="form.cno" placeholder="选择科目" :data="courseInfo">
          <el-option
              v-for="(course,index) in courseInfo"
              :key="index"
              :label="course.cname"
              :value="course.cno"
          />
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogAddExamVisible = false">取消</el-button>
        <el-button type="primary" @click="addNewExam">确认</el-button>
      </div>
    </template>
  </el-dialog>
  <!--添加试卷按钮-->
  <div class="button" style="margin-top: 20px">
    <el-button type="primary" :icon="Plus" @click="addExam">新增试卷</el-button>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import {Plus} from '@element-plus/icons-vue'
// 获取教师ID
const userInfo = JSON.parse(localStorage.getItem('userInfo'));
const teaId = userInfo?.user?.userid;
if (teaId != null) {
  /*console.log("教师id：" + teaId);*/
}

// 获取试卷信息
import { findAllPaperInfo, findSubjectByPaperId, findAllCourse, addPaper, deletePaper, addSubjectToPaper} from "../../../api/subject";

let exam_info = ref([]); // 获取试卷信息
let total_exam_info = ref([]);
let pids = ref([]); //试卷的pid
findAllPaperInfo({}).then(({ data }) => {
  exam_info.value = data.data;
  total_exam_info.value = data.data;
  //console.log("试卷信息：")
  //console.log(data.data);
  //console.log(exam_info.value);
  pids = exam_info.value.map(exam_info => exam_info.pid);
  //console.log(pids);
});


let dialogVisible = ref(false); // 试卷详情的弹窗显示状态
let subjectInPaper = ref([]);
const dialogClick1 = (row)=>{
  //console.log("当前行信息：")
  //console.log(row)
  dialogVisible.value = true;

  findSubjectByPaperId({pid:row.pid}).then(({data})=>{
    /*console.log(data.data)//根据pid获取试卷中的题目*/
    subjectInPaper.value = data.data.map((item) => ({
      ...item,
      scontent: item.scontent.replace(/\n/g, '<br>'),
    }));
  });
}

import {reactive} from 'vue'
const formLabelWidth = '140px'
const form = reactive({
  pname: '',
  cno:''
})
let dialogAddExamVisible = ref(false);
let courseInfo = ref([]);
const addExam = () => {
  // 新增试卷dialog
  findAllCourse({}).then(({data})=>{
    //console.log("课程：")
    //console.log(data.data);
    courseInfo.value = data.data
  });

  dialogAddExamVisible.value = true;
};
const addNewExam = () => {
  //新增试卷
//console.log(form)
  addPaper({pname:form.pname, teaId:teaId, cno:form.cno}).then(({data})=>{
    //console.log(data.data)
  });
  dialogAddExamVisible.value = false;
  location.reload();
}

//删除页
let dialogDeleteVisible = ref(false)
let deleteExamInfo = ref([])
const dialogDelete=(row)=>{
  deleteExamInfo = row
  //console.log(deleteExamInfo)

  dialogDeleteVisible.value = true
}
const deleteExam=()=>{

  deletePaper({pid:deleteExamInfo.pid}).then(({data})=>{
    //console.log(data.data)
  });
  dialogDeleteVisible.value = false
  location.reload();
}


import {findSubjectByInfo} from "../../../api/subject";
let dialogAddSubjectVisible = ref(false)

let ChoiceQuestion = ref([])
let JudgeQuestion = ref([])
let ShortAnswerQuestion = ref([])
let ProgramQuestion = ref([])

let selectedSids = ref([]);//sid
let pidForAddSubject = ref([]);//pid
const dialogAddSubject=(row)=>{

  //console.log(row.pid)
  pidForAddSubject.value = row.pid;
  selectedSids.value = [];

  findSubjectByInfo({cno:row.cno,type:"选择题"}).then(({data})=>{
    ChoiceQuestion.value = data.data.map((item) => ({
      ...item,
      scontent: item.scontent.replace(/\n/g, '<br>'),
    }));
  });
  findSubjectByInfo({cno:row.cno,type:"判断题"}).then(({data})=>{
    JudgeQuestion.value = data.data.map((item) => ({
      ...item,
      scontent: item.scontent.replace(/\n/g, '<br>'),
    }));
  });
  findSubjectByInfo({cno:row.cno,type:"简答题"}).then(({data})=>{
    ShortAnswerQuestion.value = data.data.map((item) => ({
      ...item,
      scontent: item.scontent.replace(/\n/g, '<br>'),
    }));
  });
  findSubjectByInfo({cno:row.cno,type:"编程题"}).then(({data})=>{
    ProgramQuestion.value = data.data.map((item) => ({
      ...item,
      scontent: item.scontent.replace(/\n/g, '<br>'),
    }));
  });
  dialogAddSubjectVisible.value = true
};

// 更新选中题目

//更改的部分

// 用来存储每道题目的分数，结构是 { "sid": "分数" }
let questionScores = ref({});

/************************************************************
// 更新题目选择处理函数
 ************************************************************/

// 更新题目选择处理函数
const handleSelectionChange = (selection) => {
  // 创建一个新的 Set 来存储当前选中的题目 sid
  const selectedSids = new Set(selection.map((item) => item.sid));

  // 创建 questionScores 的副本
  const updatedScores = { ...questionScores.value };

  // 清除 questionScores 中未选中的题目的分数
  for (const sid in updatedScores) {
    if (!selectedSids.has(sid)) {
      delete updatedScores[sid];  // 移除未选中的题目
    }
  }

  // 将更新后的 questionScores 赋值回去
  questionScores.value = updatedScores;

  // 遍历选中的题目，如果没有分数，则初始化为空字符串
  selection.forEach((item) => {
    if (!(item.sid in updatedScores)) {
      updatedScores[item.sid] = "";  // 给未设置过分数的题目初始化为空字符串
    }
  });

  // 更新 questionScores 为新的分数对象
  questionScores.value = updatedScores;
};



let grade = ref({}); // 用来存储 { "sid": "分数" }
/*************************************************
// 添加题目并提交分数
const addSubject = () => {
  grade = ({});
  // 遍历所有选择的题目及其对应的分数
  for (const sid in questionScores.value) {
    const score = questionScores.value[sid];
    if (score) { // 只有输入了分数的题目才会被添加
      grade[String(sid)] = String(score);
    }
  }

  console.log(grade)
  // 调用 API 提交数据

  addSubjectToPaper({"pid": pidForAddSubject.value, "grade":grade }).then(({ data }) => {
    // 处理响应数据...
  });
  dialogAddSubjectVisible.value = false;
};
*******************************************************/


const addSubject = () => {
  // 用来存储题目的分数
  grade = {};

  // 只将那些有分数的题目添加到 grade 对象
  for (const sid in questionScores.value) {
    const score = questionScores.value[sid];
    if (score !== "") { // 只有输入了分数的题目才会被添加
      grade[sid] = score; // 添加题目的分数
    }
  }

  // 确保 grade 不为空
  if (Object.keys(grade).length > 0) {
    // 调用 API 提交选中的题目和分数
    addSubjectToPaper({
      pid: pidForAddSubject.value,
      grade: grade
    }).then(({ data }) => {
      // 处理 API 响应数据
    });
    dialogAddSubjectVisible.value = false;
  } else {
    // 如果没有选中题目或者没有输入分数，提示用户
    console.log("请勾选题目并输入分数后再提交。");
  }
};

let all = "全部科目"
let selectByCourse = ref([])
findAllCourse({}).then(({data})=>{
  //console.log("课程：")
  //console.log(data.data);
  courseInfo.value = data.data
});
const searchPaper=()=>{
  // 根据选择的科目过滤试卷数据
  if(selectByCourse.value===0){
    exam_info.value = total_exam_info.value;
  }
  else {
    exam_info.value = total_exam_info.value.filter(exam => exam.cno === selectByCourse.value);
  }
}


</script>

<style lang="scss" scoped>
</style>
