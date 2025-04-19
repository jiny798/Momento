<template>
  <div class="detail-area-wrapper">
    <div class="detail-area">
      <div class="img-area">
        <div class="prd-img">
          <img class="big-image" :src="imageUrl" alt="유기농 달걀" loading="lazy" />
          <span id="zoomMouseGuide"> </span>

          <span class="wish">
            <!-- 관심 상품 표시  -->
          </span>
        </div>
        <ul class="thumb-list">
          <li>
            <img :src="imageUrl" class="thumb-image" alt="썸네일" />
          </li>
        </ul>
      </div>

      <div class="info-area">
        <div class="heading-area">
          <h1>젤라또 포장 - 컵</h1>
          <!--          <span class="delivery">(배송 가능상품)</span>-->
        </div>

        <table class="product-detail">
          <tbody>
            <tr>
              <th>상품명</th>
              <td>젤라또 컵 6EA</td>
            </tr>
            <tr>
              <th>판매가</th>
              <td><strong>28,000원</strong></td>
            </tr>
            <tr>
              <th>국내·해외배송</th>
              <td>국내배송</td>
            </tr>
            <tr>
              <th>배송방법</th>
              <td>택배</td>
            </tr>
            <tr>
              <th>배송비</th>
              <td><strong>2,500원</strong> (50,000원 이상 구매 시 무료)</td>
            </tr>
          </tbody>
        </table>

        <div class="order-section">
          <table class="order-list">
            <thead>
              <tr>
                <th>상품명</th>
                <th>수량</th>
                <th>가격</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>유기농 달걀</td>
                <td>
                  <input type="number" v-model.number="quantity" min="1" />
                </td>
                <td>{{ formatPrice(price * quantity) }}</td>
              </tr>
            </tbody>
          </table>

          <div class="total-price">
            <strong>TOTAL:</strong>
            <span>{{ formatPrice(price * quantity) }}</span>
          </div>

          <div class="action-buttons">
            <button class="btn-buy">BUY IT NOW</button>
            <button class="btn-cart">CART</button>
            <button class="btn-wish">WISH LIST</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue'

const quantity = ref(1)
const price = 6000
const imageUrl = ref('/g1.JPG')

function formatPrice(price: number): string {
  return price.toLocaleString('ko-KR') + '원'
}
</script>

<style scoped>
@import url('https://spoqa.github.io/spoqa-han-sans/css/SpoqaHanSansNeo.css');
@import url('https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;700&family=Noto+Sans+KR:wght@300;400;500;700&display=swap');
ul {
  padding-left: 0;
  margin: 0; /* 필요에 따라 */
  list-style: none;
}
* {
  font-family: 'Spoqa Han Sans Neo', 'Noto Sans KR', 'Montserrat', sans-serif;
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

.detail-area-wrapper {
  display: flex;
  justify-content: center;
}

.detail-area {
  display: flex;
  flex-wrap: wrap;
  gap: 2rem;
  padding: 2rem;
  color: #333;
  line-height: 1.6;
  max-width: 1200px;
  width: 100%;
}

.img-area {
  flex: 1;
  min-width: 300px;
}

.info-area {
  flex: 1;
  min-width: 300px;
}

.big-image {
  width: 100%;
  max-width: 550px;
  border: 1px solid #ddd;
}

#zoomMouseGuide img {
  width: 170px;
  position: absolute;
  top: -27px;
  right: 0;
}

.thumb-list {
  margin-top: 1rem;
}

.thumb-list img {
  width: 80px;
  border: 1px solid #eee;
}

.heading-area h1 {
  font-size: 24px;
  font-weight: 700;
}

.delivery {
  color: #777;
  font-size: 14px;
}

.product-detail {
  width: 100%;
  margin-top: 1rem;
  border-collapse: collapse;
}

.product-detail th,
.product-detail td {
  border: 1px solid #eaeaea;
  padding: 0.75rem;
  font-size: 14px;
}

.order-list {
  margin-top: 2rem;
  width: 100%;
  border-collapse: collapse;
}

.order-list th,
.order-list td {
  border: 1px solid #ccc;
  padding: 0.75rem;
}

.total-price {
  margin-top: 1rem;
  font-size: 1.2rem;
  font-weight: bold;
}

.action-buttons {
  margin-top: 1.5rem;
}

.action-buttons button {
  background: #111;
  color: #fff;
  border: none;
  padding: 0.75rem 1.5rem;
  margin-right: 1rem;
  cursor: pointer;
  font-weight: 500;
  transition: background 0.3s;
}

.action-buttons button:hover {
  background: #444;
}
</style>
