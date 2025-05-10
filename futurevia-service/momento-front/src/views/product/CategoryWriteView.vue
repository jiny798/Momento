<template>
  <div class="flavor-category-page">
    <RegisterForm
      title="카테고리 등록"
      label="카테고리 이름"
      placeholder="예: 디저트"
      :repository="categoryRepository"
      @update="onCategoryUpdate"
    />
  </div>
</template>

<script setup lang="ts">
import RegisterForm from '@/components/register/RegisterForm.vue'
import { container } from 'tsyringe'
import FlavorRepository from '@/repository/FlavorRepository'
import CategoryRepository from '@/repository/CategoryRepository'
const categoryRepository = container.resolve(CategoryRepository)

function onFlavorUpdate() {
  // Handle flavor update if needed
}

function onCategoryUpdate() {
  // Handle category update if needed
}
</script>

<style scoped>
.flavor-category-page {
  display: flex;
  justify-content: center;
  padding: 2rem;
}
</style>
