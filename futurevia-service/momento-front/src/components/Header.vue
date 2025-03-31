<template>
  <div class="header">
    <el-menu
      :default-active="activeIndex"
      class="el-menu-demo"
      mode="horizontal"
      :ellipsis="false"
      @select="handleSelect"
    >
      <el-menu-item index="0">
        <el-icon><IceCream /></el-icon>
        Momento
        <!--        <img style="width: 100%" src="/favicon.ico" alt="Element logo" />-->
      </el-menu-item>
      <!--      <el-menu-item index="1">Momento </el-menu-item>-->
      <el-menu-item index="2">Gelato </el-menu-item>
      <el-menu-item index="3">Dessert </el-menu-item>
      <!--      <el-menu-item class="custom-disabled" disabled>박진영님</el-menu-item>-->

      <el-sub-menu index="4">
        <template #title>-</template>
        <el-menu-item index="4-1">주문내역</el-menu-item>
        <el-menu-item index="4-2">내 정보</el-menu-item>
        <el-menu-item index="4-3">로그아웃</el-menu-item>
      </el-sub-menu>
      <!--      <el-menu-item index="5">로그인 </el-menu-item>-->
    </el-menu>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue'

const activeIndex = ref('1')
const activeIndex2 = ref('1')
const handleSelect = (key: string, keyPath: string[]) => {
  console.log(key, keyPath)
}
</script>

<style scoped lang="scss">
.el-menu--horizontal > .el-menu-item:nth-child(4) {
  margin-right: auto;
}

.custom-disabled {
  pointer-events: none; /* 클릭 방지 */
  color: #606266 !important; /* 기본 색상 유지 */
  opacity: 1 !important; /* 흐려지지 않도록 설정 */
}

.logo {
  width: 52px;
  object-fit: cover;
}

.title {
  font-size: 2rem;
  font-weight: 300;
  margin-left: 5px;
}
</style>
