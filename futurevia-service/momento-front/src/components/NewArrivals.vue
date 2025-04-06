<template>
  <section class="product-list-section">
    <div class="section-header">
      <h2>Momento</h2>
      <p></p>
    </div>

    <ul class="product-grid">
      <li v-for="product in products" :key="product.id" class="product-card">
        <div class="product-image-wrapper">
          <a :href="product.link">
            <img :src="product.image" :alt="product.name" class="product-image" />
          </a>

          <!-- 여기에 겹쳐서 보이게! -->
          <div class="product-info-overlay">
            <p class="name">{{ product.name }}</p>
            <p class="price">{{ product.price.toLocaleString() }}원</p>
          </div>

          <span v-if="product.badge" :class="['badge', product.badge.toLowerCase()]">
            {{ product.badge }}
          </span>

          <div class="icon-buttons">
            <!--            <button class="cart-button">BUY</button>-->
          </div>
        </div>
      </li>
    </ul>
  </section>
</template>

<script lang="ts" setup>
interface Product {
  id: number
  name: string
  image: string
  price: number
  link: string
  badge?: 'NEW' | 'BEST'
}

const products: Product[] = [
  {
    id: 1,
    name: '유기농 달걀',
    price: 6000,
    image: '/g1.JPG',
    link: '/product/유기농-달걀/18/category/1/display/2/',
    badge: 'NEW',
  },
  {
    id: 2,
    name: '유기농 아침 식빵',
    price: 12000,
    image: '/g2.JPG',
    link: '/product/유기농-아침-식빵/17/category/1/display/2/',
    badge: 'BEST',
  },
  {
    id: 3,
    name: '딸기 무스 케이크',
    price: 30000,
    image: '/g3.JPG',
    link: '/product/딸기-무스-케이크/16/category/1/display/2/',
  },
]
</script>

<style scoped>
.product-image-wrapper {
  position: relative;
  overflow: hidden;
}

.product-info-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  padding: 12px;
  background: rgba(255, 255, 255, 0.8); /* 반투명 배경 */
  color: white;
  text-align: center;
  opacity: 0;
  transform: translateY(100%);
  transition:
    opacity 0.3s,
    transform 0.3s;
  pointer-events: none;
}

.product-card:hover .product-info-overlay {
  opacity: 1;
  transform: translateY(0);
  pointer-events: auto;
}

.product-list-section {
  max-width: 1200px;
  margin: 0 auto;
  padding: 60px 20px;
  text-align: center;
}

.section-header h2 {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 6px;
}

.section-header p {
  color: #666;
  font-size: 14px;
  margin-bottom: 32px;
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 30px;
}

.product-card {
  background: #fff;
  border: 1px solid #eee;
  border-radius: 6px;
  overflow: hidden;
  transition: box-shadow 0.3s;
}

.product-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.product-image-wrapper {
  position: relative;
}

.product-image {
  width: 100%;
  display: block;
  object-fit: cover;
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

.icon-buttons {
  position: absolute;
  bottom: 10px;
  right: 10px;
  display: flex;
  gap: 8px;
}

.icon-buttons button {
  background: #fff;
  border: 1px solid #ddd;
  font-size: 11px;
  padding: 4px 6px;
  cursor: pointer;
  border-radius: 4px;
  transition: 0.2s;
}

.icon-buttons button:hover {
  background-color: #f5f5f5;
}

.product-info {
  padding: 16px;
}

.name {
  font-size: 14px;
  color: #333;
}

.price {
  font-weight: bold;
  font-size: 16px;
  color: #000;
  margin-top: 4px;
}
</style>
