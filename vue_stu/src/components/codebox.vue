<!-- CodeMirrorEditor.vue -->
<template  >
    <div style="display: flex; flex-direction: column; align-items: flex-start;">
      <el-select
      v-model="selectedValue"
      clearable
      placeholder="Select"
      style="width: 240px"
      @change="selectChange"
      >
      <el-option
        v-for="item in options"
        :key="item.value"
        :label="item.label"
        :value="item.value"
      />
    </el-select>
    <Codemirror
      v-model:value="code"
      :options="cmOptions"
      border
      ref="cmRef"
      height="400"
      width="600"
      @change="onChange"
      @input="onInput"
      @ready="onReady"
    />
    </div>
  </template>
  
  <script setup>
  import { ref, defineProps, defineEmits } from 'vue';
  import Codemirror from 'codemirror-editor-vue3';
  import 'codemirror/mode/python/python.js';
  import 'codemirror/mode/javascript/javascript.js';
  
  // Define props
  const props = defineProps({
    modelValue: {
      type: String,
      default: 'python',
    },
    options: {
      type: Object,
      default: () => ({
        mode: 'javascript',
        theme: 'default',
        lineNumbers: true,
      }),
    },
    selectedValue: {
      type: String,
      default: 'javascript',
    }, 
    selectOption : {
      type: Array,
      default: () => ([
        {
          value: 'javascript',
          label: 'javascript',
        },
        {
          value: 'python',
          label: 'python',
        },
        {
          value: 'java',
          label: 'java',
        }
      ]),
    }
  });
  
  // Define emits
  const emit = defineEmits(['update:modelValue', 'update:selectedValue']);
  
  // Reactive code variable
  const code = ref(props.modelValue);
  const cmOptions = { ...props.options };
  
  const selectedValue = ref(props.selectedValue);
  const options = props.selectOption;
  
  // Event handlers
  const onChange = (newCode) => {
    emit('update:modelValue', newCode);
  };
  
  const onInput = (newCode) => {
  };
  
  const onReady = () => {
  };
  
  
  const selectChange = (value) => {
    emit('update:selectedValue', value);
  };
  
  </script>
  
  
  
  <style scoped>
  /* 你可以在这里添加样式 */
  </style>
  