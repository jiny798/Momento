<template>
  <div class="flavor-register-wrapper">
    <div class="flavor-register-box">
      <h2>아이스크림 맛 등록</h2>

      <form @submit.prevent="handleSubmit">
        <label for="flavorInput">맛 이름</label>
        <input id="flavorInput" v-model="flavor" type="text" placeholder="예: 바닐라" required />
        <button type="submit">등록하기</button>
      </form>

      <p v-if="successMessage" class="success">{{ successMessage }}</p>

      <hr />

      <div class="flavor-list">
        <h3>등록된 맛 리스트</h3>
        <ul>
          <li v-for="item in flavorList" :key="item.id">
            <span>{{ item.name }}</span>
            <button class="btn-delete" @click="deleteFlavor(item.id)">삭제</button>
          </li>
        </ul>
        <p v-if="flavorList.length === 0">아직 등록된 맛이 없습니다.</p>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const flavor = ref('')
const successMessage = ref('')
const flavorList = ref<{ id: number; name: string }[]>([])

const fetchFlavorList = async () => {
  try {
    const res = await axios.get('/api/flavors')
    flavorList.value = res.data
  } catch (e) {
    console.error('맛 목록 조회 실패', e)
  }
}

const handleSubmit = async () => {
  try {
    await axios.post('/api/flavors', { name: flavor.value })
    successMessage.value = `'${flavor.value}' 맛이 등록되었습니다!`
    flavor.value = ''
    fetchFlavorList()
  } catch (e) {
    console.error('등록 실패', e)
    successMessage.value = '맛 등록에 실패했습니다.'
  }
}

const deleteFlavor = async (id: number) => {
  if (!confirm('정말 삭제하시겠습니까?')) return
  try {
    await axios.delete(`/api/flavors/${id}`)
    fetchFlavorList()
  } catch (e) {
    console.error('삭제 실패', e)
  }
}

onMounted(() => {
  fetchFlavorList()
})
</script>

<style scoped>
.flavor-register-wrapper {
  display: flex;
  justify-content: center;
  padding: 2rem;
}

.flavor-register-box {
  width: 100%;
  max-width: 500px;
  padding: 2rem;
  border: 1px solid #ccc;
  border-radius: 8px;
  background: #fafafa;
}

h2,
h3 {
  text-align: center;
}

form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
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

.flavor-list {
  margin-top: 2rem;
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

.btn-delete {
  background-color: #e74c3c;
  padding: 0.4rem 0.7rem;
  font-size: 0.9rem;
}

.btn-delete:hover {
  background-color: #c0392b;
}
</style>
