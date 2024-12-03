<template>
    <!-- <div>
        试题管理
    </div> -->
    <h1>选择题</h1>
    <el-table :data="tableSelectData" 
    style="width: 100%" 
    highlight-current-row
    @current-change="handleCurrentChange">
    <el-table-column fixed prop="scontent" label="题目" width="150" />
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
        <el-button link type="primary" size="small" @click="Editxzt(row)">修改</el-button>
      </template>
    </el-table-column>
    </el-table>
    <el-dialog v-model="dialogFormVisible" title="修改题目信息" width="500">
    <el-form :model="xztform">
      <el-form-item label="题目" :label-width="formLabelWidth">
        <el-input v-model="xztform.scontent" autocomplete="off" />
      </el-form-item>
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
      <el-form-item label="正确答案" :label-width="formLabelWidth">
        <el-input v-model="xztform.skey" autocomplete="off" />
      </el-form-item>
      <el-form-item label="难度" :label-width="formLabelWidth">
        <el-input v-model="xztform.difficulty" autocomplete="off" />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible = false">Cancel</el-button>
        <el-button :plain="true" type="primary" @click="xztsubmit">
          Confirm
        </el-button>
      </div>
    </template>
  </el-dialog>

  




    <h1>判断题</h1>
    <el-table :data="tableJudgeData" 
    style="width: 100%"
    highlight-current-row
    @current-change="handleCurrentChange">
    <el-table-column fixed prop="scontent" label="题目" width="150" />
    <el-table-column prop="skey" label="答案" width="120"/>
    <el-table-column prop="difficulty" label="难度" width="120"/>
    <el-table-column fixed="right" label="Operations" min-width="120">
      <template #default>
        <el-button link type="primary" size="small" @click="handleClick">
          Detail
        </el-button>
        <el-button link type="primary" size="small">Edit</el-button>
      </template>
    </el-table-column>
    </el-table>

    




    <h1>简答题</h1>
    <el-table :data="tableShortAnswerData" style="width: 100%" height="250">
    <el-table-column fixed prop="scontent" label="题目" width="150" />
    <el-table-column prop="skey" label="参考答案" width="120"/>
    <el-table-column prop="difficulty" label="难度" width="120"/>
    <el-table-column fixed="right" label="Operations" min-width="120">
      <template #default>
        <el-button link type="primary" size="small" @click="handleClick">
          Detail
        </el-button>
        <el-button link type="primary" size="small">Edit</el-button>
      </template>
    </el-table-column>
    </el-table>

    <!-- 删除提示框 -->
    <el-dialog
      v-model="centerDeleteDialogVisible"
      title="警告"
      width="500"
      center
    >
      <span>此操作将永久删除该题目, 是否继续?</span>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="centerDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmDelete">
            确定
          </el-button>
        </div>
      </template>
    </el-dialog>
</template>

<script lang="ts" setup>
    import {Plus} from '@element-plus/icons-vue'
    import { ref, reactive } from 'vue'
    import { ElMessage, ElMessageBox, ElNotification} from 'element-plus'
    import {getChoiceQuestion, getJudgeQuestion, getShortAnswerQuestion, updateQuestion, deleteQuestion} from "../../../api/subject"
    import { sliderButtonEmits } from 'element-plus/es/components/slider/src/button.mjs';

    const tableSelectData = ref([])
    const tableJudgeData = ref([])
    const tableShortAnswerData = ref([])
    const deleteSid = ref(null) // 用于存储要删除的题目的 sid
    // 删除框
    const centerDeleteDialogVisible = ref(false)
    getChoiceQuestion().then(({data}) => {
        console.log(data.data)
        tableSelectData.value = data.data
    }) 
    getJudgeQuestion().then(({data}) => {
        console.log(data.data)
        tableJudgeData.value = data.data
    }) 
    getShortAnswerQuestion().then(({data}) => {
        console.log(data.data)
        tableShortAnswerData.value = data.data
    }) 

    // 修改框
    const dialogFormVisible = ref(false)
    const formLabelWidth = '100px'
    const xztform = reactive({
        scontent: '',
        sa: '',
        sb: '',
        sc: '',
        sd: '',
        skey: '',
        difficulty: '',
        sid: 0,
    })

    const Editxzt = (row) => {
            // 直接使用参数 row 获取当前行的信息
      console.log('当前行信息:', row)
      
      // 设置表单数据

      xztform.scontent = row.scontent
      xztform.sa = row.sa
      xztform.sb = row.sb
      xztform.sc = row.sc
      xztform.sd = row.sd
      xztform.skey = row.skey
      xztform.difficulty = row.difficulty 
      xztform.sid = row.sid

      // 打开对话框
      dialogFormVisible.value = true
    }
    // 更新数据
    const xztsubmit = () => {
        console.log(xztform);
        
        // 更新问题
        updateQuestion(xztform).then(({ data }) => {
            console.log(data);
          
            // 刷新列表
            getChoiceQuestion().then(({ data }) => {
                console.log(data.data);
                tableSelectData.value = data.data; // 更新数据
            });
            
            // 关闭对话框
            dialogFormVisible.value = false;
        }).catch(error => {
            console.error('更新失败:', error);
            // 可选择在这里显示错误消息
        });
    }
    // 删除题目
    const showDeleteDialog = (row) => {
        centerDeleteDialogVisible.value = true
        deleteSid.value = row.sid
    }

    // 确认删除
    const confirmDelete = () => {
        console.log('删除题目:', deleteSid.value)
        // 强制转化为数字
        deleteQuestion({
            sid: Number(deleteSid.value)
        }).then(({data}) => {
            console.log(data)
            getChoiceQuestion().then(({data}) => {
                console.log(data.data)
                tableSelectData.value = data.data
            }) 
            getJudgeQuestion().then(({data}) => {
                console.log(data.data)
                tableJudgeData.value = data.data
            }) 
            getShortAnswerQuestion().then(({data}) => {
                console.log(data.data)
                tableShortAnswerData.value = data.data
            }) 
            centerDeleteDialogVisible.value = false
        })
    }


    const handleClick = () => {
        ElMessage({
            message: 'Hello, Element Plus!',
            type: 'success'
        })
    }
    const handleCurrentChange = () => {
        console.log('aaa')
    }
</script>

<style lang="scss" scoped>

</style>