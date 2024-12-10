<template>
    <el-menu
    :style="{width: !isCollapse ? '200px' :  '64px'}"
        active-text-color="#ffd04b"
        background-color="#545c64"
        class="aside-container"
        default-active="2"
        text-color="#fff"
        @open="handleOpen"
        @close="handleClose"
        :collapse="isCollapse"
      >
        <p class="logo-lg">{{isCollapse ? 'as' : 'aside bar'}}</p>


        <TreeMenu :menuData="menuData" :index="1"/>
    </el-menu>
</template>

<script setup>
import TreeMenu from './treeMenu.vue'
import {useRouter} from 'vue-router'
import {reactive, computed} from 'vue'
import {useStore} from 'vuex'

const router = useRouter()
const menuData = reactive(router.options.routes[0].children)

const handleOpen = () => {

}

const handleClose = () => {
}

const store = useStore()
const isCollapse = computed(() => store.state.menu.isCollapse)

</script>

<style lang="less" scoped>
.aside-container {
    transition: width 0.3s linear;
    height:100vh;
    .logo-lg {
        font-size: 20px;
        color: #fff;
        text-align: center;
        height:50 px;
        line-height: 50px;
    }
}

</style>