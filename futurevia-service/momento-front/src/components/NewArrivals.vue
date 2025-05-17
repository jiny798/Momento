<template>
  <main class="home-container">
    <!-- 인트로 배너 -->
    <section class="hero">
      <div class="hero-text">
        <h1>MOMENTO</h1>
        <p>자연을 담은 젤라또, 당신의 일상에 달콤한 쉼표를.</p>
      </div>
      <img :src="banners[0].desktop" alt="젤라또 배너" class="hero-image" />
    </section>

    <!-- 제품 리스트 -->
    <section class="product-list-section">
      <h2 class="section-title">Our Favorites</h2>
      <ul class="product-grid">
        <li v-for="product in products" :key="product.id" class="product-card">
          <div class="product-image-wrapper">
            <a :href="product.link">
              <img :src="product.image" :alt="product.name" class="product-image" />
              <div class="product-info-overlay">
                <p class="name">{{ product.name }}</p>
                <p class="price">{{ product.price.toLocaleString() }}원</p>
              </div>
              <span v-if="product.badge" :class="['badge', product.badge.toLowerCase()]">
                {{ product.badge }}
              </span>
            </a>
          </div>
        </li>
      </ul>
    </section>

    <!-- 인스타그램 섹션 -->
    <section class="instagram-section">
      <h2 class="section-title">Follow Us on Instagram</h2>
      <p class="insta-desc">우리의 젤라또가 궁금하다면 인스타에서 만나요!</p>
      <a href="https://instagram.com/gelateria.momento" target="_blank" class="insta-link"> @gelateria.momento </a>
      <div class="insta-gallery">
        <!--        <img v-for="n in 4" :key="n" :src="`/insta-${n}.jpg`" alt="인스타 미리보기" />-->
        <img
          v-for="n in 4"
          :key="n"
          src="data:image/jpeg;base64,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"
          alt="인스타 미리보기"
        />
      </div>
    </section>
  </main>
</template>

<script lang="ts" setup>
import { ref } from 'vue'

interface Product {
  id: number
  name: string
  image: string
  price: number
  link: string
  badge?: 'NEW' | 'BEST'
}
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

const products: Product[] = [
  {
    id: 1,
    name: '유기농 젤라또',
    price: 6000,
    image: '/g1.JPG',
    link: '/product/유기농-달걀/18/category/1/display/2/',
    badge: 'NEW',
  },
  {
    id: 2,
    name: '소르베',
    price: 12000,
    image: '/g2.JPG',
    link: '/product/유기농-아침-식빵/17/category/1/display/2/',
    badge: 'BEST',
  },
  {
    id: 3,
    name: '젤라또 포장',
    price: 30000,
    image: '/g3.JPG',
    link: '/product/딸기-무스-케이크/16/category/1/display/2/',
  },
]
</script>

<style scoped>
.home-container {
  font-family: 'Pretendard', sans-serif;
}

.hero {
  position: relative;
  text-align: center;
  padding: 60px 20px 40px;
  background: linear-gradient(to bottom, #fff, #f9f9f9);
}

.hero-text h1 {
  font-size: 36px;
  font-weight: 700;
  margin-bottom: 12px;
}

.hero-text p {
  font-size: 16px;
  color: #666;
}

.hero-image {
  width: 100%;
  max-height: 320px;
  object-fit: cover;
  margin-top: 20px;
  border-radius: 12px;
}

.section-title {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 20px;
  text-align: center;
}

.product-list-section {
  max-width: 1080px;
  margin: 0 auto;
  padding: 60px 20px;
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
  gap: 24px;
}

.product-card {
  border: 1px solid #eee;
  border-radius: 8px;
  overflow: hidden;
  transition: box-shadow 0.3s;
}

.product-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.06);
}

.product-image-wrapper {
  position: relative;
}

.product-image {
  width: 100%;
  display: block;
  object-fit: cover;
  transition: 0.3s ease;
}

.product-info-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  padding: 12px;
  background: rgba(255, 255, 255, 0.8);
  text-align: center;
  opacity: 0;
  transform: translateY(100%);
  transition: 0.3s;
  pointer-events: none;
}

.product-card:hover .product-info-overlay {
  opacity: 1;
  transform: translateY(0);
  pointer-events: auto;
}

.name {
  font-size: 14px;
  font-weight: 500;
  color: #333;
}

.price {
  font-size: 15px;
  font-weight: bold;
  color: #111;
  margin-top: 4px;
}

.badge {
  position: absolute;
  top: 10px;
  left: 10px;
  font-size: 12px;
  color: #fff;
  padding: 3px 8px;
  border-radius: 3px;
  font-weight: bold;
}

.badge.new {
  background-color: #26c6da;
}

.badge.best {
  background-color: #8bc34a;
}

.instagram-section {
  padding: 60px 20px;
  background: #f9f9f9;
  text-align: center;
}

.insta-desc {
  color: #666;
  margin-bottom: 10px;
}

.insta-link {
  display: inline-block;
  margin-bottom: 24px;
  font-weight: 500;
  color: #26c6da;
  text-decoration: none;
}

.insta-gallery {
  display: flex;
  justify-content: center;
  gap: 12px;
  flex-wrap: wrap;
}

.insta-gallery img {
  width: 120px;
  height: 120px;
  object-fit: cover;
  border-radius: 8px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
}
</style>
