<script setup lang="ts">
import { ref } from 'vue'

const product = {
  name: '무농약 레몬 3KG',
  price: '35,000',
  image: '//ecudemo276582.cafe24.com/web/product/big/202304/cced6f410eaecda95ddbeeb145e445bd.png',
  thumbnails: ['//ecudemo276582.cafe24.com/web/product/small/202304/fd300cca1828deb46c8f4b0d0f33da31.png'],
  deliveryMethod: '택배',
  shippingFee: '2,500원 (50,000원 이상 구매 시 무료)',
}

const tabs = ['상세정보', '상품후기', '상품문의']
const selectedTab = ref('상세정보')
</script>

<template>
  <div class="product-detail-container">
    <!-- 좌측 이미지 영역 -->
    <div class="product-images">
      <img :src="product.image" alt="상품 메인 이미지" class="main-image" />
      <div class="thumbnail-list">
        <img v-for="(thumb, index) in product.thumbnails" :key="index" :src="thumb" class="thumb-image" />
      </div>
    </div>

    <!-- 우측 정보 영역 -->
    <div class="product-info">
      <h1>{{ product.name }}</h1>
      <p class="price">{{ product.price }}원</p>
      <table class="info-table">
        <tr>
          <th>배송방법</th>
          <td>{{ product.deliveryMethod }}</td>
        </tr>
        <tr>
          <th>배송비</th>
          <td>{{ product.shippingFee }}</td>
        </tr>
      </table>
      <div class="actions">
        <button class="buy">BUY IT NOW</button>
        <button class="cart">CART</button>
        <button class="wish">WISH LIST</button>
      </div>
    </div>

    <!-- 하단 탭 영역 -->
    <div class="product-tabs">
      <div class="tabs">
        <button v-for="tab in tabs" :key="tab" :class="{ active: selectedTab === tab }" @click="selectedTab = tab">
          {{ tab }}
        </button>
      </div>
      <div class="tab-content">
        <div v-if="selectedTab === '상세정보'">상세정보 내용 표시</div>
        <div v-else-if="selectedTab === '상품후기'">상품후기 내용 표시</div>
        <div v-else-if="selectedTab === '상품문의'">상품문의 내용 표시</div>
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
.product-detail-container {
  display: flex;
  flex-wrap: wrap;
  gap: 40px;
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.product-images {
  flex: 1;
  min-width: 300px;
}

.main-image {
  width: 100%;
  max-width: 400px;
  border-radius: 8px;
}

.thumbnail-list {
  display: flex;
  gap: 10px;
  margin-top: 10px;
}

.thumb-image {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border: 1px solid #ddd;
}

.product-info {
  flex: 1;
  min-width: 300px;
}

.price {
  font-size: 20px;
  margin: 10px 0;
}

.info-table {
  width: 100%;
  margin-bottom: 20px;
  border-collapse: collapse;
}

.info-table th,
.info-table td {
  text-align: left;
  padding: 8px;
  border-bottom: 1px solid #eee;
}

.actions button {
  margin-right: 10px;
  padding: 10px 20px;
  border: none;
  background-color: #333;
  color: #fff;
  cursor: pointer;
  border-radius: 4px;
}

.product-tabs {
  width: 100%;
  margin-top: 40px;
  border-top: 1px solid #ddd;
  padding-top: 20px;
}

.tabs {
  display: flex;
  gap: 20px;
}

.tabs button {
  background: none;
  border: none;
  font-size: 16px;
  padding: 10px;
  cursor: pointer;
  border-bottom: 2px solid transparent;
}

.tabs button.active {
  font-weight: bold;
  border-bottom: 2px solid #333;
}

.tab-content {
  margin-top: 20px;
}

@media (max-width: 768px) {
  .product-detail-container {
    flex-direction: column;
    align-items: center;
  }
}
</style>
