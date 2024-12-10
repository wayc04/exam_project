<!-- <template>
    <el-tree
    style="max-width: 600px"
    :data="data"
    :props="defaultProps"
    @node-click="handleNodeClick"
  />
</template>

<script setup>
import {defineProps , reactive} from 'vue';

let props = defineProps({
    rawData: {
        type: Array,
        required: true
    }
})

let rawData = props.rawData;

console.log(rawData)
// let rawData = [{
//             "sid": -1418944511,
//             "cno": 7,
//             "scontent": "软件工程1",
//             "sa": "1",
//             "sb": "2",
//             "sc": "3",
//             "sd": "4",
//             "skey": "2",
//             "type": "选择题",
//             "difficulty": 1
//         },
//         {
//             "sid": -118710271,
//             "cno": 7,
//             "scontent": "软件工程2？",
//             "sa": "正确",
//             "sb": "错误",
//             "sc": null,
//             "sd": null,
//             "skey": "正确",
//             "type": "判断题",
//             "difficulty": 1
//         },
//         {
//             "sid": -2048090111,
//             "cno": 7,
//             "scontent": "傻逼是___?",
//             "sa": null,
//             "sb": null,
//             "sc": null,
//             "sd": null,
//             "skey": "zhangwei",
//             "type": "简答题",
//             "difficulty": 1
//         },
//         {
//             "sid": 1579982850,
//             "cno": 7,
//             "scontent": "1234_56?",
//             "sa": null,
//             "sb": null,
//             "sc": null,
//             "sd": null,
//             "skey": "1",
//             "type": "简答题",
//             "difficulty": 1
//         },
//         {
//             "sid": 531406850,
//             "cno": 7,
//             "scontent": "????",
//             "sa": "正确",
//             "sb": "错误",
//             "sc": null,
//             "sd": null,
//             "skey": "正确",
//             "type": "判断题",
//             "difficulty": 2
//         }]

const typeOrder = {
    "选择题": 1,
    "判断题": 2,
    "简答题": 3
}
rawData.sort((a, b) => {
    return typeOrder[a.type] - typeOrder[b.type]
})

let data = [
    {
        label: '选择题',
        children:[

        ]
    },
    {
        label: '判断题',
        children:[

        ]
    },
    {
        label: '简答题',
        children:[

        ]
    }
]

console.log(data)

for (var index = 0; index < rawData.length; index ++ ) {
    if (rawData[index].type == '选择题') {
        data[0].children.push({
            label: index + 1
        })
    } else if (rawData[index].type == '判断题') {
        data[1].children.push({
            label: index + 1
        })
    } else if (rawData[index].type == '简答题') {
        data[2].children.push({
            label: index + 1
        })
    }
}

const defaultProps = {
    children: 'children',
    label: 'label'
}


const handleNodeClick = (data) => {
  console.log(data);
};

</script>

<style lang="less"s scoped>

</style> -->
<template>
    <el-tree
      style="max-width: 600px"
      :data="data"
      :props="defaultProps"
      @node-click="handleNodeClick"
    />
  </template>
  
  <script setup>
import { defineProps, reactive } from 'vue';
  
  const props = defineProps({
    rawData: {
      type: Array,
      required: true
    }
  });
  
  // 使用 reactive 来创建响应式数据
const rawData = reactive([...props.rawData]); // 克隆一份原始数据以保持响应性
  
  const typeOrder = {
    "选择题": 1,
    "判断题": 2,
    "简答题": 3,
    "编程题": 4
  };
  
  // 排序
  rawData.sort((a, b) => {
    return (typeOrder[a.type] || Infinity) - (typeOrder[b.type] || Infinity);
  });
  

  // 创建树形数据结构
  const data = [
    {
      label: '选择题',
      children: []
    },
    {
      label: '判断题',
      children: []
    },
    {
      label: '简答题',
      children: []
    },
    {
        label:'编程题',
        children:[]
    }
  ];

  // 填充树形结构
  for (let index = 0; index < rawData.length; index++) {
    const question = rawData[index];
    const childNode = {
      label: index + 1, // 使用题目内容作为标签
      sid: question.sid // 可以保留其他信息
    };
  
    if (question.type === '选择题') {
      data[0].children.push(childNode);
    } else if (question.type === '判断题') {
      data[1].children.push(childNode);
    } else if (question.type === '简答题') {
      data[2].children.push(childNode);
    } else {
        data[3].children.push(childNode);
    }
  }
  
  const defaultProps = {
    children: 'children',
    label: 'label'
  };
  
  const handleNodeClick = (data) => {
    console.log(data);
  };
</script>
  
<style lang="less" scoped>

</style>