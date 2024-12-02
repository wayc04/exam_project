<template>
    <div class="header-containerr">
        <div class="header-left flex-box">
            <el-icon class="icon" size="20" @click="store.commit('collapseMenu')"><Fold></Fold></el-icon>
            <ul class="flex-box">
                <li v-for="(item, index) in selectMenu"
                :key="item.path"
                :class = "{selected: item.path === route.path}"
                class="tab flex-box"
                >
                    <el-icon size="12" ><component :is="item.icon" /></el-icon>
                    <router-link class="text flex-box" :to="{path: item.path }">
                        {{item.name}}
                    </router-link>
                    <el-icon  size="12" class="close" @click="closeTab(item, index)"><Close /></el-icon>
                </li>

            </ul>
        
        </div>
        <div class="header-right">
            <el-dropdown>
                <div  class="el-dropdown-link flex-box">
                    <el-avatar src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png">    
                    </el-avatar>
                    <p class="user-name">管理员</p>
                </div>
                <template #dropdown>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
                        
                    </el-dropdown-menu>
                </template>
            </el-dropdown>
        </div>
    </div>
</template>

<script setup>
    import {useStore} from 'vuex';
    import {computed} from 'vue';
    import {useRoute, useRouter} from 'vue-router'

    const store = useStore();

    const route = useRoute();
    const router = useRouter();
    const selectMenu = computed(() => store.state.menu.selectMenu);

    // 退出登录
    const logout = (item, index) => {
        // store.commit('clearMenu');
        localStorage.removeItem('userInfo');
        router.push('/login');
        // router.push('/login');
        // window.history.replaceState(null, "", 'user.html');
        window.location.reload();
    }


    // 点击关闭tag
    const closeTab = (item,index)=>{
        store.commit('closeMenu', item);
        if(route.path !== item.path) return;

        const selectMenuData = selectMenu.value;

        if(index === selectMenuData.length) {
            if(!selectMenuData.length) {
                router.push('/');
            } else {
                router.push({
                    path:selectMenuData[index - 1].path
                });
            }
        } else {
            router.push({
                path:selectMenuData[index].path
            })
        }
    }
</script>

<style lang="less" scoped>
.flex-box {
    height: 100%;
    display: flex;
    align-items: center;
}
.header-containerr {
    display: flex;
    justify-content: space-between;
    align-items: center;
    height: 100%;
    background-color:#fff;
    padding-right:25 px;

    .header-left {
        height: 100%;
        .icon {
            width: 45px;
            height: 100%;
        }
        .icon :hover{
            background-color: #f5f5f5;
            cursor: pointer;
        }
        .tab {
            padding: 0 10px;
            height:100%;
            .text{
                margin: 0 5px;

            }
            .close {
                visibility: hidden;
            }
            &.selected{
                a{
                    color:#409eff;
                }
                i{
                    color:#409eff;
                }
                background-color: #f5f5f5;
            }
        }

        .tab:hover {
            background-color: #f5f5f5;
            .close {
                visibility: inherit;
                cursor:pointer;
                color:black;
            }
        }
    }

    .header-right {
        margin-right: 20px;
        .user-name {
            margin-left: 10px;
        }
    }

    a {
        height: 100%;
        color: #333;
        font-size: 15px;
    }
}
</style>