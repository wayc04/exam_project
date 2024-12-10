<template>

  <el-container class="exam-container">
    <!-- 左侧：考试信息 -->
    <el-aside width="250px" class="exam-aside">
      <h3 class="exam-title">考试信息</h3>
      <ul class="exam-info">
        <li>考试名称: {{ examName }}</li>
        <li>考试时间: {{ examDuration }} 分钟</li>
        <li>剩余时间: <span class="countdown">{{ countdown }}</span></li>
      </ul>
      <h4 class="nav-title">题目导航</h4>
      <ul class="question-nav">
    <!-- 选择题导航 -->
    <li v-if="multipleChoiceQuestions.length">
      <h5>选择题</h5>
      <ul>
        <li
          v-for="(question, index) in multipleChoiceQuestions"
          :key="'choice-' + index"
          :class="{ answered: isAnswered(index, '选择题') }"
          @click="navigateToQuestion(index, '选择题')"
        >
          题目 {{ index + 1 }} - <span>{{ isAnswered(index, '选择题') ? "已答" : "未答" }}</span>
        </li>
      </ul>
    </li>

    <!-- 判断题导航 -->
    <li v-if="trueFalseQuestions.length">
      <h5>判断题</h5>
      <ul>
        <li
          v-for="(question, index) in trueFalseQuestions"
          :key="'truefalse-' + index"
          :class="{ answered: isAnswered(index, '判断题') }"
          @click="navigateToQuestion(index, '判断题')"
        >
          题目 {{ index + 1 }} - <span>{{ isAnswered(index, '判断题') ? "已答" : "未答" }}</span>
        </li>
      </ul>
    </li>

    <!-- 简答题导航 -->
    <li v-if="shortAnswerQuestions.length">
      <h5>简答题</h5>
      <ul>
        <li
          v-for="(question, index) in shortAnswerQuestions"
          :key="'shortanswer-' + index"
          :class="{ answered: isAnswered(index, '简答题') }"
          @click="navigateToQuestion(index, '简答题')"
        >
          题目 {{ index + 1 }} - <span>{{ isAnswered(index, '简答题') ? "已答" : "未答" }}</span>
        </li>
      </ul>
    </li>

    <!-- 编程题导航 -->
    <li v-if="codingQuestions.length">
      <h5>编程题</h5>
      <ul>
        <li
          v-for="(question, index) in codingQuestions"
          :key="'coding-' + index"
          :class="{ answered: isAnswered(index, '编程题') }"
          @click="navigateToQuestion(index, '编程题')"
        >
          题目 {{ index + 1 }} - <span>{{ isAnswered(index, '编程题') ? "已答" : "未答" }}</span>
        </li>
      </ul>
    </li>
  </ul>
    </el-aside>

    <!-- 右侧：答题区域 -->
    <el-container>
      <el-header class="exam-header fresh-theme">
  <div class="toolbar">
    <span>当前考生: {{ studentName }}</span>
    <div class="button-group">
      <el-button type="primary" @click="saveAnswers" class="save-button">
        保存答案
      </el-button>
      <el-button type="danger" @click="submitExam(false)" class="submit-button">
        提交试卷
      </el-button>
    </div>
  </div>
</el-header>

      <el-main>
        <el-scrollbar class="exam-main">
          <!-- 渲染选择题 -->
          <div class="question-category" v-if="multipleChoiceQuestions.length">
            <h3>选择题</h3>
            <div
              v-for="(question, index) in multipleChoiceQuestions"
              :key="'multiple-' + index"
              class="question-item"
            >
              <h4>题目 {{ index + 1 }}</h4>
              <p class="question-content">{{ question.content }}</p>
              <div
                class="choice-option"
                v-for="(option, i) in question.options"
                :key="i"
                :class="{selected: answers[question.id] === option }"
                @click="selectChoice(question.id, option)"
              >
                <div class="option-label">{{ String.fromCharCode(65 + i) }}</div>
                <div class="option-content">{{ option }}</div>
                <div v-if="answers[question.id] === option" class="tick-mark">✓</div>
              </div>
            </div>
          </div>

          <!-- 渲染判断题 -->
          <div class="question-category" v-if="trueFalseQuestions.length">
    <h3>判断题</h3>
    <div
      v-for="(question, index) in trueFalseQuestions"
      :key="'true-false-' + index"
      class="question-item"
    >
      <el-card class="question-card" :body-style="{ padding: '20px' }">
        <h4>题目 {{ index + 1 }}</h4>
        <p>{{ question.content }}</p>
        <el-radio-group v-model="answers[question.id]" size="medium">
          <el-radio :label="true">
            <span class="radio-label">
              <i class="el-icon-check"></i> 正确
            </span>
          </el-radio>
          <el-radio :label="false">
            <span class="radio-label">
              <i class="el-icon-close"></i> 错误
            </span>
          </el-radio>
        </el-radio-group>
      </el-card>
    </div>
  </div>

          <!-- 渲染简答题 -->
          <div class="question-category" v-if="shortAnswerQuestions.length">
            <h3>简答题</h3>
            <div
              v-for="(question, index) in shortAnswerQuestions"
              :key="'short-answer-' + index"
              class="question-item"
            >
              <h4>题目 {{ index + 1 }}</h4>
              <p>{{ question.content }}</p>
              <el-input
                type="textarea"
                v-model="answers[question.id]"
                placeholder="请输入答案"
                rows="4"
                
              ></el-input>
            </div>
          </div>

          <!-- 渲染编程题 -->
          <div class="question-category" v-if="codingQuestions.length">
    <h3>编程题</h3>
    <div
      v-for="(question, index) in codingQuestions"
      :key="'coding-' + index"
      class="question-item"
    >
      <h4>题目 {{ index + 1 }}</h4>
      <div class="question-content">{{ question.content }}</div>

      <!-- 选择语言的下拉框 -->
      <el-select
        v-model="selectedLanguages[question.id]"
        placeholder="选择编程语言"
        style="margin-bottom: 10px;"
      >
        <el-option label="JavaScript" value="javascript"></el-option>
        <el-option label="Python" value="python"></el-option>
        <el-option label="Java" value="java"></el-option>
        <el-option label="C++" value="cpp"></el-option>
        <el-option label="C#" value="csharp"></el-option>
        <el-option label="Ruby" value="ruby"></el-option>
        <!-- 可以根据需要添加更多语言 -->
      </el-select>

      <el-input
        type="textarea"
        v-model="answers[question.id]"
        placeholder="请输入代码"
        rows="10"
      ></el-input>
    </div>
  </div>
        </el-scrollbar>
      </el-main>
    </el-container>
  </el-container>




</template>


<script lang="ts" setup>
import { ref, reactive, onMounted, watch,computed } from "vue";
import { ElMessage } from "element-plus";
import { getExamByStuId, findSubjectByPaperId,judgePaperByList } from "../../api/exam";
import { isBoolean } from "element-plus/es/utils/types.mjs";
import { onUnmounted } from "vue";
import { useRouter, useRoute } from 'vue-router';




// 用户信息
const userinfo = JSON.parse(localStorage.getItem("userInfo") || "{}");
const userId = ref(userinfo.user.userid);
const selectedLanguages = ref({});
// 考试信息
const examName = ref("加载中...");
const examDuration = ref(0); // 考试时长（分钟）
const studentName = ref(userinfo.user.truename);
let Tpid = null;
let Teid = null;

// 倒计时
const countdown = ref("00:00");
let timer: number | null = null;
let answersWithIds = []; // 定义一个全局变量存储答案

// 题目数据
const questions = reactive<any[]>([]);
var answers = reactive<(string | boolean | string[])[]>([]);


const route = useRoute();
// 获取考试数据
const fetchExamData = async () => {
  try {
    const response = await getExamByStuId({ userid: userId.value });
    if (response.data.code === 1) {
      const exams = response.data.data;
      console.log("获取的考试数据:", exams);

      const eid = Number(route.query.eid); // 从路由获取 pid

      // 根据 eid 查找对应的考试数据
      const examData = exams.find(exam => exam.eid === eid);
      Teid = examData.eid;
      Tpid = examData.pid;
      console.log("保存的数据是：",examData);
      console.log("pid:",Tpid);
      
      if (examData) {
        examName.value = examData.pname;
        console.log("找到的考试数据:", examData);

        // 计算考试时长
        const startTime = new Date(examData.examdate).getTime();
        const endTime = new Date(examData.endtime).getTime();
        examDuration.value = Math.floor((endTime - startTime) / (1000 * 60));
        console.log("最终结束时间是：",endTime);

        // 开启倒计时
        startCountdown(endTime);

        // 加载题目数据
        await fetchQuestions(examData.pid);
        
        
      } else {
        ElMessage.warning("未找到对应的考试数据！");
      }
    } else {
      ElMessage.error("获取考试数据失败：" + response.data.msg);
    }
  } catch (error) {
    console.error("fetchExamData error:", error);
    ElMessage.error("无法加载考试数据！");
  }
};

console.log("pidsd23423a:",Tpid)
// 获取题目数据
const fetchQuestions = async (pid) => {
  try {
    const response = await findSubjectByPaperId({ pid });
    if (response.data.code === 1) {
      const rawQuestions = response.data.data;
      console.log("shuju:", rawQuestions);

      // 格式化题目数据
      const formattedQuestions = rawQuestions.map((q) => {
        const options =
          q.type === "选择题"
            ? [q.sa, q.sb, q.sc, q.sd].filter((opt) => opt !== null)
            : [];
        return {
          id: q.sid,
          type: q.type, // "选择题" / "判断题" / "简答题" / "编程题"
          content: q.scontent,
          options: options,
        };
      });

      // 更新题目和答案
      questions.splice(0, questions.length, ...formattedQuestions);
      answers.splice(0, answers.length, ...formattedQuestions.map(() => ""));

      // 更新全局变量
      answersWithIds = formattedQuestions.map(q => ({ sid: q.id, answer: "" }));
      console.log("answersWithIds:", answersWithIds);
      console.log("Type of answersWithIds:", Array.isArray(answersWithIds));

      // 存储题目到 localStorage
      localStorage.setItem("examQuestions", JSON.stringify(questions));
    } else {
      ElMessage.error("获取题目数据失败：" + response.data.msg);
    }
  } catch (error) {
    console.error("fetchQuestions error:", error);
    ElMessage.error("无法加载题目数据！");
  }
};


const multipleChoiceQuestions = computed(() =>
  questions.filter((q) => q.type === "选择题")
  
);
console.log("完整选择题信息:",multipleChoiceQuestions);


const trueFalseQuestions = computed(() =>
  questions.filter((q) => q.type === "判断题")
);

const shortAnswerQuestions = computed(() =>
  questions.filter((q) => q.type === "简答题")
);

const codingQuestions = computed(() =>
  questions.filter((q) => q.type === "编程题")
);


const selectChoice = (questionIndex: number, option: string) => {
answers[questionIndex] = option; // 将选中的选项保存到答案数组
console.log("selected", answers[questionIndex], questionIndex, option)
};


// 保存答案
const saveAnswers = () => {
  if (!Array.isArray(answersWithIds)) {
    console.error("传入的参数不是一个数组！", answersWithIds);
    return;
  }

  const answerObject = {};
  
  answersWithIds.forEach(({ sid, answer }) => {
    // 使用题目 ID 作为键
    answerObject[sid] = answer;
  });
   // 保存编程题选择的语言
  localStorage.setItem("selectedLanguages", JSON.stringify(selectedLanguages.value));
  
  localStorage.setItem("examAnswers", JSON.stringify(answerObject));
  console.log("我保存的答案是：", answerObject);
  console.log("huida数量", Object.keys(answerObject).length);
  ElMessageBox.alert("答案已保存");
};

// 提交试卷
// const submitExam = () => {
//   ElMessage.success("试卷提交成功！");
//   clearInterval(timer);
//   localStorage.removeItem("examAnswers");
//   localStorage.removeItem("examQuestions");
// };


// 倒计时逻辑
const startCountdown = (endTime: number) => {
  if (timer) clearInterval(timer);

  const updateCountdown = () => {
    const now = Date.now();
    const remainingTime = endTime - now;

    if (remainingTime <= 0) {
      countdown.value = "00:00";
      clearInterval(timer);
      submitExam(true); // 自动提交，不弹出确认框
      ElMessage.info("考试已结束！");
    } else {
      const minutes = Math.floor(remainingTime / (1000 * 60));
      const seconds = Math.floor((remainingTime % (1000 * 60)) / 1000);
      countdown.value = `${String(minutes).padStart(2, "0")}:${String(seconds).padStart(2, "0")}`;
    }
  };

  timer = setInterval(updateCountdown, 1000);
  updateCountdown(); // 立即更新一次
};

// 停止倒计时
const stopCountdown = () => {
  if (timer) {
    clearInterval(timer);
    timer = null;
  }
};

// 工具函数：格式化题目数据
const formatQuestions = (rawQuestions: any[]) => {
  return rawQuestions.map((q) => {
    const options = q.type === "选择题"
      ? [q.sa, q.sb, q.sc, q.sd].filter((opt) => opt !== null)
      : [];
    return {
      type: q.type,
      content: q.scontent,
      options: options,
    };
  });
};

// 检测题目是否已解答
const isAnswered = (index: number, type?: string) => {
      const category =
        type === "选择题"
          ? multipleChoiceQuestions.value
          : type === "判断题"
          ? trueFalseQuestions.value
          : type === "简答题"
          ? shortAnswerQuestions.value
          : codingQuestions.value;

      const question = category[index];
      if (!question) return false;

      const answer = answers[question.id];

      const answerIndex = answersWithIds.findIndex(q => q.sid === question.id);
        if (answerIndex !== -1) {
       answersWithIds[answerIndex].answer = answer; // 更新答案
        }

      
      // console.log("我选择的答案是1:",answer);
      if (typeof answer === "string") return answer.trim() !== "";
      if (typeof answer === "boolean") return answer !== null;
      if (Array.isArray(answer)) return answer.length > 0;

      return false;
    };

const orderedQuestions = computed(() => {
  const ordered = [
    ...multipleChoiceQuestions.value,
    ...trueFalseQuestions.value,
    ...shortAnswerQuestions.value,
    ...codingQuestions.value,
  ];
  return ordered;
});


const getQuestionIndex = (index: number, type: string) => {
  // 根据类型获取问题数组
  const allQuestions = {
    "选择题": multipleChoiceQuestions.value,
    "判断题": trueFalseQuestions.value,
    "简答题": shortAnswerQuestions.value,
    "编程题": codingQuestions.value,
  };

  const categoryQuestions = allQuestions[type] || [];
  const questionId = categoryQuestions[index]?.id; // 获取当前题目的 ID

  // 查找这个 ID 在 orderedQuestions 数组中的索引
  const globalIndex = orderedQuestions.value.findIndex((q) => q.id === questionId);

  console.log("类型:", type, "索引:", index, "全局索引:", globalIndex); // 调试信息
  return globalIndex;
};

// 导航到指定题目
const navigateToQuestion = (index: number, type?: string) => {
  const questionIndex = type ? getQuestionIndex(index, type) : index;

  console.log("questionIndex:", questionIndex); // 打印计算出的索引
  const allQuestions = document.querySelectorAll('.question-item');
  console.log("所有题目元素数量:", allQuestions.length);

  if (questionIndex >= 0 && questionIndex < allQuestions.length) {
    const target = allQuestions[questionIndex];
    console.log("target:", target);
    if (target) {
      target.scrollIntoView({ behavior: "smooth" });
    } else {
      console.error("未找到目标题目元素");
    }
  } else {
    console.error("索引超出范围:", questionIndex);
  }
};

// 从 localStorage 恢复数据
onMounted(async () => {
const savedQuestions = localStorage.getItem("examQuestions");
const savedAnswers = localStorage.getItem("examAnswers");
const savedExamData = localStorage.getItem("examData"); // 存储的考试数据

const savedLanguages = localStorage.getItem("selectedLanguages"); // 新增这一行
 stopwatch1();
 stopwatch2();
let endTime;
if (savedQuestions && savedExamData) {
  // 从 localStorage 恢复考试数据
  const parsedQuestions = JSON.parse(savedQuestions);
  const parsedExamData = JSON.parse(savedExamData);
  
  endTime = new Date(parsedExamData.endtime).getTime();

  // 更新题目和考试信息
  questions.splice(0, questions.length, ...parsedQuestions);
  examName.value = parsedExamData.pname;
  examDuration.value = Math.floor(
    (new Date(parsedExamData.endtime).getTime() - new Date(parsedExamData.examdate).getTime()) / (1000 * 60)
  );
} else {
  // 如果本地没有数据，从服务器加载
  await fetchExamData();
}

// 确保在恢复题目后初始化答案
if (savedAnswers) {
  const parsedAnswers = JSON.parse(savedAnswers);
  console.log("回复保存的答案是：", parsedAnswers);
  
  if (typeof parsedAnswers === "object" && parsedAnswers !== null) {
    console.log("当前题目数组是：", questions);
    
    // 用保存的答案更新 answers
    questions.forEach(q => {
      const answer = parsedAnswers[q.id] || ""; // 使用 q.id
      answers[q.id] = answer; // 使用题目ID作为键
      console.log(`题目ID: ${q.id}, 答案: ${answer}`);
    });

    console.log("我更新了");
    console.log("answer数据是：", answers);
  } else {
    // 如果解析后的答案不是有效对象，则初始化为空
    questions.forEach(q => {
      answers[q.id] = ""; // 初始化为空
    });
    console.log("我答应了吗");
  }
} else {
  // 如果没有保存的答案，初始化为空
  questions.forEach(q => {
    answers[q.id] = ""; // 初始化为空
  });
}

if (savedLanguages) {
    selectedLanguages.value = JSON.parse(savedLanguages) || {};
    console.log("恢复的选择语言是：", selectedLanguages.value);
  }

});


const router = useRouter();
const submitExam = async (isAutoSubmit = false) => {
  if (!isAutoSubmit) {
    // 弹出确认提示框
    const confirmation = await ElMessageBox.confirm("您确定要提交试卷吗？交卷后将无法再修改答案。", "确认交卷", {
      confirmButtonText: "确认",
      cancelButtonText: "取消",
      type: "warning"
    }).catch(() => {
      // 用户取消提交
      ElMessage.info("已取消提交试卷");
      return null;
    });

    // 如果用户选择取消，退出函数
    if (!confirmation) return;
  }

  console.log('submit answers:', answers);
  try {
    console.log('questions', questions);
    const payloads = {};

    for (let i = 0; i < questions.length; i++) {
      const questionId = questions[i].id.toString(); // 确保 ID 是字符串
      payloads[questionId] = {}; // 初始化每个题目的对象

      if (!isBoolean(answers[questionId])) {
        payloads[questionId].answer = answers[questionId]; // 存储答案
      } else {
        payloads[questionId].answer = answers[questionId] ? '正确' : '错误';
      }

      // 如果是编程题，添加所选语言
      if (questions[i].type === '编程题') { // 假设题目类型为 'coding'
        const language = selectedLanguages.value[questionId];
        if (language) {
          payloads[questionId].language = language; // 将语言添加到当前题目的对象中
        }
      }
    }

    console.log('payloads', payloads);
    console.log("提交的答案数据:", payloads);

    stopCountdown();

    console.log("ahsdjhashdouih,,,,", Tpid);

    // 调用判分接口
    const response = await judgePaperByList({
      exammap: payloads,
      userid: userId.value,
      pid: Tpid,
      eid: Teid
    });

    console.log("判分响应:", response);
    // 检查响应结果
    if (response.data.code === 1) {
      const resultData = response.data.data;
      console.log("判分结果:", resultData);

      // 更新题目状态
      questions.forEach((question) => {
        const sid = question.id.toString();
        if (resultData[sid]) {
          question.result = resultData[sid]; // 将“正确”或“错误”绑定到题目
        }
      });

      // ElMessage.success("判分成功！");
      
      // 提示用户已完成考试
      ElMessageBox.alert("你已完成考试，即将后返回考试信息界面。", "提交成功", {
  confirmButtonText: "确认",
  center: true, // 居中显示
}).finally(() => {
  // 无论用户是否点击确认，设置 5 秒后自动跳转
  setTimeout(() => {
    router.push('/class/group'); // 重定向到指定路径
  }, 500); // 5 秒后跳转
});

    } else {
      ElMessage.error("判分失败：" + response.msg);
    }
  } catch (error) {
    console.error("submitAnswers error:", error);
    ElMessage.error("无法提交答案！");
  }
};




onUnmounted(() => {
  if (timer) {
    clearInterval(timer);
  }
});


// 当 answers 改变时，自动存储到 localStorage
const stopwatch1= watch(
answers,
(newAnswers) => {
  localStorage.setItem("examAnswers", JSON.stringify(newAnswers));
},
{ deep: true }
);

// 当 questions 改变时，自动存储到 localStorage
const stopwatch2=watch(
questions,
(newQuestions) => {
  localStorage.setItem("examQuestions", JSON.stringify(newQuestions));
},
{ deep: true }
);
</script>


<style scoped>
.question-item {
margin-bottom: 20px;
}

.question-title {
font-size: 18px;
font-weight: bold;
margin-bottom: 10px;
}

.question-nav {
  list-style: none;
  padding: 0;
}
.question-nav h5 {
  margin: 10px 0 5px;
}
.question-nav li {
  cursor: pointer;
  margin: 5px 0;
  padding: 5px;
  border: 1px solid #ccc;
  border-radius: 5px;
}
.question-nav li.answered {
  background-color: #d3f261;
}
.question-nav li:not(.answered):hover {
  background-color: #f5f5f5;
}

.multiple-choice {
margin-bottom: 20px;
}

.choice-option {
display: flex;
align-items: center;
border: 1px solid #dcdcdc;
border-radius: 5px;
padding: 10px;
margin: 5px 0;
transition: all 0.3s ease;
cursor: pointer;
position: relative;
}

.choice-option:hover {
background-color: #f0f8ff;
}

.choice-option.selected {
border-color: #409eff;
background-color: #e6f7ff;
}

.option-label {
font-size: 16px;
font-weight: bold;
margin-right: 10px;
}

.option-content {
flex-grow: 1;
font-size: 14px;
}

.tick-mark {
position: absolute;
right: 10px;
font-size: 16px;
color: #409eff;
font-weight: bold;
}

.exam-container {
display: flex;
height: 100%;
}

.exam-aside {
  background-color: #f9fcff; /* 淡蓝白背景 */
  border-right: 2px solid #a2d9ff; /* 浅蓝色边框 */
  padding: 20px;
  box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1);
}

.exam-title {
  font-size: 1.5em;
  color: #4a90e2; /* 深蓝色字体 */
  margin-bottom: 20px;
  border-bottom: 2px solid #76c7f2;
  padding-bottom: 5px;
}

.exam-info {
  list-style-type: none;
  padding: 0;
  margin: 0 0 20px;
}

.exam-info li {
  font-size: 1em;
  color: #555;
  margin: 5px 0;
}
.countdown {
  font-weight: bold;
  color: #ff6666; /* 红色倒计时 */
}

.nav-title {
  font-size: 1.2em;
  color: #333;
  margin-top: 20px;
  margin-bottom: 10px;
  border-bottom: 1px solid #ddd;
  padding-bottom: 5px;
}

.question-nav ul {
  list-style-type: none;
  padding: 0;
  margin: 0;
}

.question-nav li {
  margin-bottom: 10px;
  padding: 10px;
  border-radius: 8px;
  transition: background-color 0.3s ease, color 0.3s ease;
  cursor: pointer;
}
.question-nav li.answered {
  background-color: #d3f9d8; /* 浅绿色背景代表已答 */
  color: #333;
}

.question-nav li:hover {
  background-color: var(--el-color-primary-light-7);
  border-color: var(--el-color-primary);
}

.exam-header {
background-color: var(--el-color-primary-light-7); /* 背景色 */
padding: 0 20px; /* 左右内边距 */
height: 60px; /* 固定高度，确保垂直居中 */
}

.toolbar {
display: flex; /* 启用 flex 布局 */
justify-content: space-between; /* 两端对齐 */
align-items: center; /* 确保子元素垂直居中 */
height: 100%; /* 占满父容器高度 */
  gap:0;
}

.save-button,
.submit-button {
margin: 0; /* 清除默认外边距 */
}

.toolbar .button-group {
display: flex;
align-items: center; /* 垂直居中 */
gap: 5px; /* 按钮间距 */
}

.submit-button {
margin-right: 10px; /* 按钮距离右侧边距 */
}

.exam-main {
  padding: 20px;
  background-color: var(--el-color-white);
}

.el-collapse-item {
  margin-bottom: 20px;
}
.choice-options li {
padding: 5px 10px;
border: 1px solid #ccc;
border-radius: 4px;
margin-bottom: 5px;
cursor: pointer;
transition: background-color 0.2s, color 0.2s;
}

.choice-options li:hover {
background-color: var(--el-color-primary-light-7);
color: var(--el-color-primary);
}

.choice-options li.selected {
background-color: var(--el-color-primary);
color: white;
font-weight: bold;
}

.fresh-theme {
  background-color: #e8f7fc;
  color: #333;
  border-bottom: 2px solid #a2d9ff;
  display: flex;
  justify-content: space-between; /* 水平方向两端对齐 */
  align-items: center; /* 垂直方向居中 */
  padding: 10px 20px;
}

.fresh-theme .toolbar {
  display: flex;
  width: 100%;
  justify-content: space-between; /* 左右分布 */
  align-items: center; /* 垂直居中 */
}

.fresh-theme .button-group {
  display: flex;
  gap: 10px; /* 按钮间距 */
}

.fresh-theme .button-group .el-button {
  border-radius: 20px;
}

.fresh-theme .button-group .el-button.primary {
  background-color: #76c7f2;
  border-color: #76c7f2;
  color: #fff;
}

.fresh-theme .button-group .el-button.primary:hover {
  background-color: #5bb8e6;
}

.fresh-theme .button-group .el-button.danger {
  background-color: #ff9999;
  border-color: #ff9999;
  color: #fff;
}

.fresh-theme .button-group .el-button.danger:hover {
  background-color: #ff6666;
}



.correct {
  color: green;
}

.incorrect {
  color: red;
}

.custom-message {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 9999; /* 确保在其他元素之上 */
}


.question-card {
  margin-bottom: 20px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.radio-label {
  display: flex;
  align-items: center;
}

.radio-label i {
  margin-right: 5px;
}

.question-content {
  white-space: pre-wrap; /* 保留文本中的换行符和空格 */
}

</style>
