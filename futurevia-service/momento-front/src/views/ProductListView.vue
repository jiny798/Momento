<template>
  <section class="product-grid">
    <div class="list-header">
      <p class="prd-count">
        총 <strong>{{ products.length }}</strong
        >개의 상품이 있습니다.
      </p>
      <div class="sort">
        <select v-model="selectedSort">
          <option value="">- 정렬방식 -</option>
          <option value="new">신상품</option>
          <option value="name">상품명</option>
          <option value="low">낮은가격</option>
          <option value="high">높은가격</option>
          <option value="brand">제조사</option>
          <option value="review">사용후기</option>
        </select>
      </div>
    </div>

    <ul class="grid">
      <li v-for="item in products" :key="item.id" class="product-item">
        <div class="thumbnail">
          <a :href="item.link">
            <img :src="item.image" :alt="item.name" />
          </a>
          <div class="icon__box">
            <span class="wish">WISH</span>
            <span class="cart">ADD</span>
          </div>
        </div>
        <div class="description">
          <div class="name">
            <a :href="item.link">{{ item.name }}</a>
          </div>
          <p class="price">{{ item.price.toLocaleString() }}원</p>
        </div>
      </li>
    </ul>
  </section>
</template>

<script lang="ts" setup>
import { ref } from 'vue'

interface Product {
  id: number
  name: string
  price: number
  image: string
  link: string
}

const selectedSort = ref('')

const products = ref<Product[]>([
  {
    id: 1,
    name: '산지직송 유기농 채소',
    price: 50000,
    image: 'https://ecudemo276582.cafe24.com/web/product/medium/202304/91773e23bdb5734e9a90c23d059a81ec.png',
    link: '/product/산지직송-유기농-채소/21/category/46/display/1/',
  },
  {
    id: 2,
    name: '무농약 레몬 3KG',
    price: 35000,
    image: 'https://ecudemo276582.cafe24.com/web/product/medium/202304/248f1ce8272e791ede18f27021bc4a41.png',
    link: '/product/무농약-레몬-3kg/15/category/46/display/1/',
  },
  {
    id: 3,
    name: '유기농 아보카도 3EA',
    price: 8500,
    image: 'https://ecudemo276582.cafe24.com/web/product/medium/202304/b615aea49d8b7ba4c927771616f67a7a.png',
    link: '/product/유기농-아보카도-3ea/11/category/46/display/1/',
  },
  {
    id: 4,
    name: '유기농 감자 5KG',
    price: 9000,
    image: 'https://ecudemo276582.cafe24.com/web/product/medium/202304/b424453f2a84d195e42cfd1dcd071e46.png',
    link: '/product/유기농-감자-5kg/12/category/46/display/1/',
  },
])
</script>

<style scoped>
.product-grid {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
}
.list-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
  font-size: 14px;
}
.grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 24px;
}
.product-item {
  border: 1px solid #eee;
  border-radius: 6px;
  overflow: hidden;
  background: #fff;
}
.thumbnail {
  position: relative;
}
.thumbnail img {
  width: 100%;
  display: block;
}
.icon__box {
  position: absolute;
  bottom: 10px;
  right: 10px;
  display: flex;
  gap: 8px;
}
.icon__box span {
  background: #fff;
  border: 1px solid #ccc;
  font-size: 12px;
  padding: 2px 6px;
  cursor: pointer;
  border-radius: 3px;
}
.description {
  padding: 10px;
  text-align: center;
}
.name {
  font-size: 14px;
  margin-bottom: 6px;
}
.price {
  font-weight: bold;
  font-size: 16px;
  color: #000;
}
</style>
