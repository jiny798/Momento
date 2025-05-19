<script setup lang="ts">
import Header from '@/components/Header.vue'
import Menu from '@/components/Menu.vue'
import Content from '@/components/Content.vue'
</script>

<template>
  <el-row>
    <el-col :span="24" :offset="0">
      <Header />
    </el-col>
  </el-row>

  <el-row>
    <el-col
      :span="22"
      :offset="1"
      :sm="{ span: 24, offset: 0 }"
      :md="{ span: 24, offset: 0 }"
      :lg="{ span: 24, offset: 0 }"
    >
      <Content />
    </el-col>
  </el-row>
</template>

<style></style>

<style>
html {
  font-size: 16px;
}

a {
  text-decoration: none;
  color: inherit; /* 선택사항: 기본 색상도 유지 */
}

ul {
  list-style: none;
  padding: 0;
  margin: 0;
}
</style>
