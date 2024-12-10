<template>
  <div>
    <el-button 
      type="primary" 
      :icon="Search" 
      @click="handleSearch"
    >
      查询
    </el-button>

    <!-- 表格展示数据 -->
    <el-table :data="test" style="width: 100%">
      <el-table-column label="考试id" prop="eid" width="180" />
      <el-table-column label="试卷id" prop="pid" width="180" />
      <el-table-column label="考试名称" prop="pname" width="180" />
      <el-table-column label="课程id" prop="cno" width="180" />
      <el-table-column label="学生id" prop="useid" width="180" />
      <el-table-column label="考试开始时间" width="200">
        <template #default="scope">
          {{ formatDateTime(scope.row.examdate) }}
        </template>
      </el-table-column>
      <el-table-column label="考试结束时间" width="200">
        <template #default="scope">
          {{ formatDateTime(scope.row.endtime) }}
        </template>
      </el-table-column>
      <el-table-column label="考试成绩" width="180">
        <template #default="scope">
          <span v-if="scope.row.grade !== undefined">{{ scope.row.grade }}</span>
          <span v-else>未评分</span>
        </template>
      </el-table-column>

      <!-- 操作列 -->
      <el-table-column label="操作" width="180">
        <template #default="scope">
          <el-button
            v-if="isExamNotStarted(scope.row.examdate)"
            type="warning"
            size="mini"
            style="min-width: 100px; text-align: center;"
            disabled
          >
            未开始
          </el-button>
          <el-button
            v-else-if="!isExamEnded(scope.row.endtime) && !scope.row.submitted"
            type="success"
            size="mini"
            style="min-width: 100px; text-align: center;"
            @click="joinExam(scope.row.pid, scope.row.eid)"
          >
            参加考试
          </el-button>
          <el-button
            v-else-if="scope.row.submitted || scope.row.grade !== undefined"
            type="success"
            size="mini"
            style="min-width: 100px; text-align: center;"
            disabled
          >
            已参加
          </el-button>
          <el-button
            v-else
            type="danger"
            size="mini"
            style="min-width: 100px; text-align: center;"
            disabled
          >
            已结束
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { getExamByStuId, getGradeByEidAndUserid } from '../../../api/exam';
import { ElMessage } from 'element-plus';

const router = useRouter();
const input = ref();
const test = ref([]);

// 获取用户信息
const userinfo = JSON.parse(localStorage.getItem('userInfo'));
input.value = Number(userinfo.user.userid);

// 查询按钮点击事件处理函数
const handleSearch = () => {
  if (!input.value) {
    console.log('请输入学生ID');
    return;
  }

  // 调用 API 获取数据
  getExamByStuId({ userid: input.value })
    .then((response) => {
      test.value = response.data.data; // 更新表格数据
      console.log(response.data.data); // 打印调试数据
      // 获取成绩数据
      console.log("userid是==============================================：",input.value)
      test.value.forEach(async (exam) => {
        await fetchExamGrade(exam.eid, input.value);
      });
    })
    .catch((err) => {
      console.error('查询失败', err);
    });
};

// 自动刷新数据
onMounted(() => {
  handleSearch(); // 自动调用查询方法
  ElMessage.success("页面已加载，自动查询数据");
});

// 跳转到考试页面
const joinExam = (pid, eid) => {
  console.log('跳转到考试页面，试卷ID:', pid);
  router.push({ path: '/dashboard', query: { eid } });
};

// 格式化日期时间
const formatDateTime = (dateTime) => {
  const date = new Date(dateTime);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  return `${year}-${month}-${day} ${hours}:${minutes}`;
};

// 判断考试是否已经结束
const isExamEnded = (endTime) => {
  const now = new Date();
  const examEndTime = new Date(endTime);
  return now > examEndTime;
};

// 判断考试是否未开始
const isExamNotStarted = (startTime) => {
  const now = new Date();
  const examStartTime = new Date(startTime);
  return now < examStartTime;
};

// 获取考试成绩
const fetchExamGrade = async (eid, userid) => {
  try {
    const response = await getGradeByEidAndUserid({ eid, userid });
    console.log("响应数据:", response);
    console.log("userid:", userid);

    if (response.data.code === 1) {
      const gradeData = response.data.data[0]?.grade; // 使用可选链安全访问

      if (gradeData !== undefined) {
        const examIndex = test.value.findIndex(exam => exam.eid === eid);

        if (examIndex !== -1) {
          test.value[examIndex] = {
            ...test.value[examIndex], // 保留原有字段
            submitted: true,         // 标记为已参加
            grade: gradeData || 0,   // 更新成绩，默认为 0
          };
          console.log("成绩数据:", gradeData);
        } else {
          console.warn(`未找到考试ID: ${eid}`);
        }
      }
    } else {
      console.error("获取成绩数据失败:", response.data.msg);
    }
  } catch (error) {
    console.error("fetchExamGrade error:", error);
  }
};

</script>