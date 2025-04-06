<script setup lang="ts">
import { defineComponent, ref, onMounted, onBeforeUnmount } from 'vue'

interface Banner {
  desktop: string
  mobile: string
}

const banners = ref<Banner[]>([
  {
    desktop: '/banner_desktop.png',
    mobile: '/banner_mobile.png',
  },
  {
    desktop: '/banner_desktop.png',
    mobile: '/banner_mobile.png',
  },
])

const currentIndex = ref(0)
let intervalId: number

const startSlide = () => {
  intervalId = window.setInterval(() => {
    currentIndex.value = (currentIndex.value + 1) % banners.value.length
  }, 5000)
}

const stopSlide = () => {
  clearInterval(intervalId)
}

const goTo = (index: number) => {
  currentIndex.value = index
}

onMounted(startSlide)
onBeforeUnmount(stopSlide)
</script>

<template>
  <div class="banner-slider">
    <div class="banner" v-for="(banner, index) in banners" :key="index" :class="{ active: index === currentIndex }">
      <!-- PC/태블릿용 이미지 -->
      <img class="desktop-img" :src="banner.desktop" alt="desktop banner" />
      <!-- 모바일용 이미지 -->
      <img class="mobile-img" :src="banner.mobile" alt="mobile banner" />
    </div>

    <div class="pagination">
      <span
        v-for="(_, index) in banners"
        :key="index"
        :class="{ active: index === currentIndex }"
        @click="goTo(index)"
      />
    </div>
  </div>
</template>

<style scoped lang="scss">
.banner-slider {
  position: relative;
  overflow: hidden;
  width: 100%;
}

.banner {
  display: none;
}

.banner.active {
  display: block;
}

.desktop-img,
.mobile-img {
  width: 100%;
  height: auto;
  display: none;
}

/* 기본은 데스크탑 이미지 보이기 */
.desktop-img {
  display: block;
}

.pagination {
  position: absolute;
  bottom: 10px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  gap: 8px;
}

.pagination span {
  width: 10px;
  height: 10px;
  background-color: #ccc;
  border-radius: 50%;
  cursor: pointer;
}

.pagination span.active {
  background-color: #333;
}

/* 모바일에서는 모바일용 이미지만 보이도록 */
@media (max-width: 768px) {
  .desktop-img {
    display: none;
  }

  .mobile-img {
    display: block;
  }
}
</style>
