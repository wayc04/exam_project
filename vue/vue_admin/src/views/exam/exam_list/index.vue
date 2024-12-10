<template>
    <div>
        <h1>考试列表</h1>
        <el-button type="primary" @click="openAddExamDialog">添加考试</el-button>
        <el-table :data="formattedExamList" style="width: 100%" highlight-current-row>
            <el-table-column prop="pname" label="试卷名" width="200" />
            <el-table-column prop="courseName" label="所属课程" width="120" />
            <el-table-column prop="teacherName" label="创建者" width="120" />
            <el-table-column prop="examdate" label="考试时间" width="180" />
            <el-table-column prop="endtime" label="结束时间" width="180" />
            <el-table-column label="班级列表" width="200">
                <template #default="{ row }">
                    <el-select v-model="row.selectedClass" placeholder="选择班级" multiple disabled>
                        <el-option
                            v-for="cls in row.classOptions"
                            :key="cls.classid"
                            :label="cls.classname"
                            :value="cls.classid"
                        />
                    </el-select>
                </template>
            </el-table-column>
            <el-table-column fixed="right" label="操作" width="250">
                <template #default="{ row }">
                    <el-button type="primary" size="small" @click="() => editExam(row)">修改</el-button>
                    <el-button type="danger" size="small" @click="() => confirmDelete(row.eid)">删除</el-button>
                    <el-button type="primary" size="small" @click="showScore(row)">分数查询</el-button>
                </template>
            </el-table-column>
        </el-table>

        <!--分数查询-->
      <el-dialog v-model="showScoreDialogVisible" title="分数" width="800">
        <el-table :data="allScore">
          <el-table-column property="userid" label="用户id" width="150" />
          <el-table-column property="username" label="用户名" width="200" />
          <el-table-column property="grade" label="分数" width="100"/>
        </el-table>
      </el-dialog>

        <!-- 修改考试的对话框 -->
        <el-dialog v-model="dialogVisible" title="修改考试信息">
            <el-form :model="currentExam" ref="form">
                <el-form-item label="试卷名" :label-width="formLabelWidth">
                    <el-select v-model="currentExam.pid" placeholder="选择试卷">
                        <el-option
                            v-for="paper in allPapers"
                            :key="paper.pid"
                            :label="paper.paperName"
                            :value="paper.pid"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item label="班级 ID" :label-width="formLabelWidth">
                    <el-select v-model="currentExam.cids" placeholder="选择班级" multiple>
                        <el-option
                            v-for="cls in currentExam.classOptions"
                            :key="cls.classid"
                            :label="cls.classname"
                            :value="cls.classid"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item label="考试时间" :label-width="formLabelWidth">
                    <el-date-picker
                        v-model="currentExam.examdate"
                        type="datetime"
                        placeholder="选择考试时间"
                    />
                </el-form-item>
                <el-form-item label="结束时间" :label-width="formLabelWidth">
                    <el-date-picker
                        v-model="currentExam.endtime"
                        type="datetime"
                        placeholder="选择结束时间"
                    />
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="submitUpdate">确 定</el-button>
            </span>
        </el-dialog>

        <!-- 添加考试的对话框 -->
        <el-dialog v-model="addExamDialogVisible" title="添加考试信息">
            <el-form :model="newExam" ref="addForm">
                <el-form-item label="试卷名" :label-width="formLabelWidth">
                    <el-select v-model="newExam.pid" placeholder="选择试卷">
                        <el-option
                            v-for="paper in allPapers"
                            :key="paper.pid"
                            :label="paper.paperName"
                            :value="paper.pid"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item label="班级 ID" :label-width="formLabelWidth">
                    <el-select v-model="newExam.cids" placeholder="选择班级" multiple>
                        <el-option
                            v-for="cls in allClasses"
                            :key="cls.classid"
                            :label="cls.classname"
                            :value="cls.classid"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item label="考试时间" :label-width="formLabelWidth">
                    <el-date-picker
                        v-model="newExam.examdate"
                        type="datetime"
                        placeholder="选择考试时间"
                    />
                </el-form-item>
                <el-form-item label="结束时间" :label-width="formLabelWidth">
                    <el-date-picker
                        v-model="newExam.endtime"
                        type="datetime"
                        placeholder="选择结束时间"
                    />
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="addExamDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="submitAddExam">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script lang="ts" setup>
import {ref, onMounted, computed, reactive} from 'vue';
import { getExamList, 
    findCourseById, 
    findTeacherById, 
    deleteExam as apiDeleteExam, 
    getClassesByEid, 
    updateExam as apiUpdateExam,
    getAllClasses,
    getAllPaper,
    addExam as apiAddExam,
    getGradeByEidAndUserid,
    stubyid
} from '../../../api/exam';
import {all} from "axios"; // 根据你的实际路径调整

const examList = ref([]); // 考试列表数据
const courses = ref(new Map()); // 存储课程 ID 和名称的映射
const teachers = ref(new Map()); // 存储教师 ID 和姓名的映射
const allPapers = ref([]); // 存储所有试卷
const allClasses = ref([]); // 存储所有班级
const dialogVisible = ref(false); // 控制对话框显示
const addExamDialogVisible = ref(false); // 控制添加考试对话框显示
const currentExam = ref({}); // 当前修改的考试信息
const newExam = ref({}); // 新考试信息
const formLabelWidth = '100px'; // 表单标签宽度

const fetchExams = async () => {
    try {
        const response = await getExamList({});
        console.log('获取考试列表成功:', response.data.data);
        examList.value = response.data.data;

        // 获取课程和教师名称
        await fetchCoursesAndTeachers();

        // 获取班级列表
        await fetchClassesForExams();
    } catch (error) {
        console.error('获取考试列表失败:', error);
    }
};

const fetchCoursesAndTeachers = async () => {
    const coursePromises = [...new Set(examList.value.map(exam => exam.cno))].map(async (cno) => {
        const res = await findCourseById({ cno });
        console.log('获取课程信息:', res.data.data);
        courses.value.set(cno, res.data.data.cname);
    });

    const teacherPromises = [...new Set(examList.value.map(exam => exam.useid))].map(async (useid) => {
        const res = await findTeacherById({ teaId: useid });
        teachers.value.set(useid, res.data.data);
    });

    await Promise.all([...coursePromises, ...teacherPromises]);
};

// 获取班级列表
const fetchClassesForExams = async () => {
    try {
        const allClassesResponse = await getAllClasses();
        allClasses.value = allClassesResponse.data.data; // 保存所有班级

        for (const exam of examList.value) {
            try {
                const response = await getClassesByEid({ eid: exam.eid });
                console.log(`获取考试 ${exam.eid} 的班级列表:`, response.data.data);

                exam.selectedClass = response.data.data.map(cls => cls.classid);
                exam.classOptions = allClasses;
            } catch (error) {
                console.error(`获取考试 ${exam.eid} 的班级列表失败:`, error);
                exam.classOptions = [];
                exam.selectedClass = [];
            }
        }
    } catch (error) {
        console.error('获取所有班级列表失败:', error);
    }
};

// 获取所有试卷
const fetchAllPapers = async () => {
    try {
        const response = await getAllPaper();
        console.log('获取试卷列表成功:', response.data.data);
        allPapers.value = response.data.data; // 保存试卷列表
    } catch (error) {
        console.error('获取试卷列表失败:', error);
    }
};

// 格式化日期时间
const formatDateTime = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
        hour12: false
    }).replace(/\//g, '-');
};

// 计算属性，格式化考试列表
const formattedExamList = computed(() => {
    return examList.value.map(exam => ({
        ...exam,
        courseName: courses.value.get(exam.cno) || '未知课程',
        teacherName: teachers.value.get(exam.useid) || '未知教师',
        examdate: formatDateTime(exam.examdate),
        endtime: formatDateTime(exam.endtime),
        classList: exam.classList || '无班级'
    }));
});

// 组件挂载时获取考试列表和试卷列表
onMounted(async () => {
    await Promise.all([fetchExams(), fetchAllPapers()]);
});

// 打开添加考试对话框
const openAddExamDialog = () => {
    newExam.value = {
        pid: '',
        cids: [],
        examdate: '',
        endtime: '',
        userid: 1, // 假设当前用户 ID 为 1
    };
    addExamDialogVisible.value = true; // 显示添加考试对话框
};

// 修改考试的逻辑
const editExam = (row) => {
    console.log('编辑考试:', row);
    currentExam.value = {
        eid: row.eid,
        pid: row.pid,
        cids: row.selectedClass || [],
        examdate: row.examdate,
        endtime: row.endtime,
        classOptions: row.classOptions
    };
    dialogVisible.value = true; // 显示对话框
};

// 提交更新的逻辑
const submitUpdate = async () => {
    try {
        // await apiUpdateExam(currentExam.value);
        currentExam.value.examdate = formatDateTime(currentExam.value.examdate);
        currentExam.value.endtime = formatDateTime(currentExam.value.endtime);
        await apiUpdateExam(currentExam.value);
        console.log('修改考试成功:', currentExam.value);
        dialogVisible.value = false; // 关闭对话框
        await fetchExams(); // 重新获取考试列表
    } catch (error) {
        console.error('修改考试失败:', error);
    }
};

// 提交添加考试的逻辑
const submitAddExam = async () => {
    try {
        // await apiUpdateExam(newExam.value); // 假设使用相同的 API 更新添加
        newExam.value.examdate = formatDateTime(newExam.value.examdate);
        newExam.value.endtime = formatDateTime(newExam.value.endtime);
        await apiAddExam(newExam.value);
        console.log('添加考试成功:', newExam.value);
        addExamDialogVisible.value = false; // 关闭对话框
        await fetchExams(); // 重新获取考试列表
    } catch (error) {
        console.error('添加考试失败:', error);
    }
};

// 删除考试的逻辑
const confirmDelete = async (eid) => {
    try {
        await apiDeleteExam({ eid });
        console.log('删除考试成功:', eid);
        await fetchExams(); // 重新获取考试列表
    } catch (error) {
        console.error('删除考试失败:', error);
    }
};

let showScoreDialogVisible = ref(false);

let allScore = ref([])

/*const showScore = (row)=>{
  getGradeByEidAndUserid({eid:row.eid}).then(({data})=>{
    allScore.value = data.data;
    console.log(row.eid)
    console.log(data.data)
    console.log(allScore)
  })

  for (score in allScore.value){
    stubyid({userid:score.userid}).then(({data})=>{
      console.log('名称：')
      console.log(data.data.username)
      score.username = data.data.username
    })
  }

  showScoreDialogVisible.value = true;
}*/
const showScore = async (row) => {
  try {
    // 获取考试分数数据
    const { data } = await getGradeByEidAndUserid({ eid: row.eid });
    allScore.value = data.data || [];

    // 查询每个用户的用户名并更新分数表
    const usernamePromises = allScore.value.map(async (score) => {
      try {
        const userResponse = await stubyid({ userid: score.userid });
        return { ...score, username: userResponse.data.data.username || "未知用户" };
      } catch (error) {
        console.error(`获取用户 ${score.userid} 的用户名失败:`, error);
        return { ...score, username: "未知用户" };
      }
    });

    // 等待所有用户名查询完成
    allScore.value = await Promise.all(usernamePromises);
    console.log("更新后的分数数据:", allScore.value);

    // 显示分数对话框
    showScoreDialogVisible.value = true;
  } catch (error) {
    console.error("获取分数数据失败:", error);
  }
};

</script>

<style lang="scss" scoped>
</style>