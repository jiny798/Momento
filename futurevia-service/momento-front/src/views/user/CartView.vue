<template>
  <div class="cart-page">
    <!--    <h2 class="page-title">장바구니</h2>-->

    <!-- 구매자 정보 -->
    <section class="buyer-info">
      <h3 class="section-title">구매자 정보</h3>
      <div class="info-row">
        <span>이름</span><strong>{{ buyer.name }}</strong>
      </div>
      <div class="info-row">
        <span>배송지</span><strong>{{ buyer.address }}</strong>
      </div>
      <div class="info-row">
        <span>전화번호</span><strong>{{ buyer.phone }}</strong>
      </div>
      <div class="textarea-box">
        <el-input
          v-model="buyer.request"
          type="textarea"
          placeholder="배송 시 요청사항이 있다면 입력해주세요."
          :rows="3"
        />
      </div>
    </section>

    <br />

    <!-- 상품 리스트 -->
    <section class="product-list">
      <div v-for="item in cartItems" :key="item.id" class="product-item">
        <img :src="item.image" alt="product" class="thumb" />
        <div class="details">
          <p class="product-name">{{ item.productName }}</p>
          <p class="product-option">{{ item.option }}</p>
          <div class="qty-price">
            <el-input-number v-model="item.count" :min="1" size="small" @change="updateQuantity(item)" />
            <p class="price">{{ formatPrice(item.price * item.count) }}원</p>
          </div>
        </div>
        <el-icon @click="removeItem(item.id)" class="remove-btn"><delete /></el-icon>
      </div>
    </section>

    <!-- 쿠폰 선택 -->
    <section class="coupon-select">
      <h3 class="section-title">쿠폰 선택</h3>
      <el-select v-model="selectedCoupon" placeholder="쿠폰을 선택하세요" @change="applyCoupon">
        <el-option
          v-for="coupon in couponList"
          :key="coupon.code"
          :label="coupon.name + ' (-' + formatPrice(coupon.amount) + '원)'"
          :value="coupon.code"
        />
      </el-select>
    </section>

    <!-- 요약 -->
    <section class="summary">
      <div class="summary-row">
        <span>상품 금액</span><strong>{{ formatPrice(totalAmount) }}원</strong>
      </div>
      <div class="summary-row">
        <span>할인 금액</span><strong class="discount">-{{ formatPrice(discountAmount) }}원</strong>
      </div>
      <div class="summary-row total">
        <span>최종 결제 금액</span><strong>{{ formatPrice(finalAmount) }}원</strong>
      </div>
    </section>

    <!-- 하단 고정 결제 -->
    <div class="checkout-bar">
      <el-button type="primary" size="large" class="checkout-btn" @click="goToPayment">
        {{ formatPrice(finalAmount) }}원 결제하기
      </el-button>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { reactive, ref, computed } from 'vue'
import { container } from 'tsyringe'
import OrderRepository from '@/repository/OrderRepository'
import type ProductInCart from '@/entity/product/ProductInCart'
import { Delete } from '@element-plus/icons-vue'

const ORDER_REPOSITORY = container.resolve(OrderRepository)

const cartItems = reactive<ProductInCart[]>([])
function getList() {
  ORDER_REPOSITORY.getCart().then((responseArr) => {
    cartItems.splice(0, cartItems.length, ...responseArr)
  })
}
getList()

const buyer = reactive({
  name: '홍길동',
  address: '서울특별시 강남구 테헤란로 123',
  phone: '010-1234-5678',
  request: '',
})

const formatPrice = (amount: number) => amount.toLocaleString()
const totalAmount = computed(() => cartItems.reduce((sum, item) => sum + item.price * item.count, 0))

function updateQuantity(item: ProductInCart) {}
function removeItem(id: number) {
  const index = cartItems.findIndex((item) => item.id === id)
  if (index !== -1) cartItems.splice(index, 1)
}
function goToPayment() {
  console.log('결제 요청')
}

const couponList = ref([
  { code: 'SAVE10', name: '10% 할인쿠폰', amount: 3000 },
  { code: 'WELCOME', name: '신규가입쿠폰', amount: 5000 },
])
const selectedCoupon = ref<string | null>(null)
const discountAmount = ref(0)

function applyCoupon(code: string) {
  const selected = couponList.value.find((c) => c.code === code)
  discountAmount.value = selected?.amount ?? 0
}
const finalAmount = computed(() => totalAmount.value - discountAmount.value)
</script>

<style scoped>
.cart-page {
  max-width: 720px;
  margin: 0 auto;
  padding: 24px 16px 120px;
  font-family: 'Pretendard', sans-serif;
}

.page-title {
  font-size: 24px;
  font-weight: 700;
  margin-bottom: 32px;
}

.section-title {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 12px;
}

.info-row {
  display: flex;
  justify-content: space-between;
  padding: 8px 0;
  border-bottom: 1px solid #eee;
  font-size: 15px;
  color: #333;
}

.textarea-box {
  margin-top: 12px;
}

.product-item {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 24px;
}

.thumb {
  width: 80px;
  height: 80px;
  border-radius: 6px;
  object-fit: cover;
}

.details {
  flex: 1;
}

.product-name {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 4px;
}

.product-option {
  font-size: 14px;
  color: #999;
  margin-bottom: 8px;
}

.qty-price {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.price {
  font-size: 15px;
  font-weight: 500;
  color: #333;
}

.remove-btn {
  font-size: 20px;
  color: #bbb;
  cursor: pointer;
  transition: color 0.2s;
}
.remove-btn:hover {
  color: #f56c6c;
}

.coupon-select {
  margin: 32px 0;
}

.summary {
  border-top: 1px solid #eee;
  padding-top: 20px;
  font-size: 15px;
}

.summary-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 12px;
}
.summary-row.total {
  font-size: 18px;
  font-weight: 700;
}
.discount {
  color: #f56c6c;
}

.checkout-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: #fff;
  padding: 16px;
  box-shadow: 0 -1px 10px rgba(0, 0, 0, 0.05);
  display: flex;
  justify-content: center;
  z-index: 100;
}

.checkout-btn {
  width: 100%;
  max-width: 680px;
  font-size: 16px;
  font-weight: 600;
}
</style>
