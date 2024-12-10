<template>
  <template v-for="(item, index) in props.menuData">
    <el-menu-item 
    @click="handleClick(item, `${props.index}-${item.meta.id}`)"
    v-if="!item.children || item.children.length == 0" 
      :index ="`${props.index}-${item.meta.id}`"
      :key ="`${props.index}-${item.meta.id}`"
    >
      <el-icon size="20">
        <component :is="item.meta.icon" />
      </el-icon>
      <span>{{ item.meta.name }}</span>
    </el-menu-item>

    <el-sub-menu v-else
      :index ="`${props.index}-${item.meta.id}`"
    >
      <template #title>
        <el-icon size="20">
          <component :is="item.meta.icon" />
        </el-icon>
        <span>{{ item.meta.name }}</span>
      </template>
      <tree-menu :menuData="item.children" :index="`${props.index}-${item.meta.id}`"/>
    </el-sub-menu>
  </template>
</template>

<script setup>
import { useRouter } from 'vue-router'
import {useStore} from 'vuex'

const router = useRouter()
const props = defineProps(['menuData', 'index'])

const store = useStore()

// 点击菜单
const handleClick = (item, index) => {
  store.commit('addMenu', item.meta)
  router.push({ path: item.meta.path })
}
</script>

<style lang="less" scoped>
</style>