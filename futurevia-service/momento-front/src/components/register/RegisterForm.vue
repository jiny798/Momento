<template>
  <div class="register-form">
    <h2>{{ title }}</h2>
    <form @submit.prevent="onSubmit">
      <label :for="inputId">{{ label }}</label>
      <input :id="inputId" v-model="inputValue" type="text" :placeholder="placeholder" required />
      <button type="submit">등록하기</button>
    </form>
    <p v-if="successMessage" class="success">{{ successMessage }}</p>
    <hr />
    <div class="item-list">
      <h3>{{ title }} 리스트</h3>
      <ul>
        <li v-for="item in itemList" :key="item.id">
          <span>{{ item.name }}</span>
        </li>
      </ul>
      <p v-if="itemList.length === 0">아직 등록된 항목이 없습니다.</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import type HttpError from '@/http/HttpError'

const props = defineProps<{
  title: string
  label: string
  placeholder: string
  repository: any
}>()

const emit = defineEmits(['update'])

const inputValue = ref('')
const itemList = ref([])
const successMessage = ref('')
const inputId = `input-${Math.random().toString(36).substr(2, 9)}`

function onSubmit() {
  props.repository
    .write({ name: inputValue.value })
    .then(() => {
      fetchItems()
      successMessage.value = '등록이 완료되었습니다.'
      ElMessage({ type: 'success', message: '등록이 완료되었습니다.' })
      inputValue.value = ''
      emit('update')
    })
    .catch((e: HttpError) => {
      ElMessage({ type: 'error', message: e.getMessage() })
    })
}

function fetchItems() {
  props.repository.getAll().then((responseList: any[]) => {
    itemList.value = responseList
  })
}

onMounted(() => {
  fetchItems()
})
</script>

<style scoped>
.register-form {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 2rem;
  width: 100%;
}
form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  width: 100%;
  max-width: 400px;
}
input[type='text'] {
  padding: 0.75rem;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 1rem;
}
button {
  padding: 0.75rem;
  background-color: #111;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: 500;
  transition: background-color 0.3s;
}
button:hover {
  background-color: #444;
}
.success {
  margin-top: 1rem;
  color: green;
  text-align: center;
}
.item-list {
  margin-top: 2rem;
  width: 100%;
  max-width: 400px;
}
ul {
  list-style: none;
  padding: 0;
}
li {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.5rem;
  border-bottom: 1px solid #ddd;
}
</style>
