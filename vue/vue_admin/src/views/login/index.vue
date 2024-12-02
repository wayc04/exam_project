<template>
    <el-row class="login-container" justify="center" align="middle">
        <el-card style="max-width: 480px;">
            <template #header>
                <div class="card-header">
                    <img :src="imgUrl" alt="">
                </div>
            </template>

            <div class="jump-link">
                <el-link type="primary" @click="handleChange">
                    {{ formType  ? '返回登录' : '注册账号' }}
                </el-link>
            </div>
            <el-form 
            ref = "loginFormRef"
            :model="loginForm" 
            style="max-width: 600px;"
            :rules="rules">
                <el-form-item  prop="username">
                    <el-input v-model="loginForm.username" placeholder="请输入用户名" :prefix-icon="UserFilled"></el-input>
                </el-form-item>
                <el-form-item  prop="userpwd">
                    <el-input v-model="loginForm.userpwd" placeholder="请输入密码" type="password" show-password :prefix-icon="UserFilled"></el-input>
                </el-form-item>
                <el-form-item v-if="formType"  prop="truename">
                    <el-input v-model="loginForm.truename"  placeholder="请输入真实姓名" :prefix-icon="UserFilled"></el-input>
                </el-form-item>
                <el-form-item v-if="formType"  prop="classid">
                    <el-input v-model="loginForm.classid" placeholder="请输入班级号" :prefix-icon="UserFilled"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" :style="{width:'100%'}" @click="submitForm(loginFormRef)" round>
                        {{ formType  ? '注册' : '登录' }}
                    </el-button>
                  
                </el-form-item>
            </el-form>

        </el-card>
    </el-row>

</template>




<script setup>
import {ref, reactive} from 'vue'
import {register, stulogin} from '../../api/stu'
import {UserFilled, } from  '@element-plus/icons-vue'
import {useRouter} from 'vue-router'
import { ElMessage } from 'element-plus';

const imgUrl = new URL('../../../public/images/login-head.png', import.meta.url).href

const formType = ref(0)

const handleChange = () => {
    formType.value = !formType.value
}

const router = useRouter()

const loginForm = reactive({
    username:'',
    userpwd:'',
    truename:'',
    classid: null
})



const validateUser = (rule, value, callback) => {
    if (value === '') {
        return callback(new Error('请输入用户名'))
    }else {
        callback()
    }

}

const validatePwd = (rule, value, callback) => {
    if (value === '') {
        return callback(new Error('请输入密码'))
    }else {
        callback()
    }
}

const validateName = (rule, value, callback) => {
    if (value === '' && formType.value) {
        return callback(new Error('请输入真实姓名'))
    }else {
        callback()
    }
}

const validateClass= (rule, value, callback) => {
    if (value === null && formType.value) {
        return callback(new Error('请输入班级'))
    }else {
        if(!Number.isInteger(Number(value))){
            return callback(new Error('请输入有效的班级号（整数）'));
        } else {
            callback()
        }
    }
}

const rules = reactive({
    username:[{validator: validateUser, trigger: 'blur'}],
    userpwd:[{validator: validatePwd, trigger: 'blur'}],
    truename:[{validator: validateName, trigger: 'blur'}],
    classid:[{validator: validateClass, trigger: 'blur'}]
})

const loginFormRef = ref(null)

const submitForm = async (FormEl) => {
    if(!FormEl) return;
    await FormEl.validate((valid, fields) => {
        if(valid) {
            // console.log(loginForm,'submit')
            // 注册
            if (formType.value) {
                register({
                    username: loginForm.username,
                    userpwd: loginForm.userpwd,
                    truename: loginForm.truename,
                    classid: Number(loginForm.classid)
                }).then(({data}) => {
                    if(data.code === 1) {
                        console.log('注册成功，请登录')
                        formType.value = 0
                    }
                }) 
            } else {
                // 登录
                stulogin({
                    username: loginForm.username,
                    userpwd: loginForm.userpwd
                }).then(({data}) => {
                    if(data.code === 1) {
                        ElMessage.success('登录成功')
                        console.log(data)
                        // localStorage.setItem('token', data.token)
                        localStorage.setItem('userInfo', JSON.stringify(data.data))
                        // 页面跳转
                        router.push('/')
                    }
                })
            }
        } else {
            console.log('error submit!!', fields)
        }

    })
}

</script>




<style lang="less" scoped>
:deep(.el-card__header) {
  padding: 0;
}

.login-container {
  height: 100%;

  .card-header {
    background-color: #899fe1;

    img {
      width: 430px;
    }
  }

  .jump-link {
    text-align: right;
    margin-bottom: 10px;
  }
}

</style>