<template>
  <div class="mypage-container">
    <!-- 프로필 정보 -->
    <section class="profile-section">
      <div class="profile-left">
        <el-avatar :size="72" icon="UserFilled" class="avatar" />
        <div class="info-text">
          <h3>{{ state.user.nickname }}</h3>
          <p class="email">{{ state.user.email }}</p>
        </div>
      </div>
      <el-button class="edit-btn" size="small" @click="goToProfileEdit">프로필 수정</el-button>
    </section>

    <!-- 상단 요약 정보 -->
    <section class="summary-section">
      <div class="summary-item" v-for="(value, key) in summary" :key="key">
        <p class="label">{{ keyLabel(key) }}</p>
        <p class="value">{{ value }}</p>
      </div>
    </section>

    <!-- 탭 영역 -->
    <el-tabs v-model="state.activeTab" class="tabs" stretch>
      <el-tab-pane label="구매내역" name="orders">
        <!-- 날짜 필터 -->
        <div class="date-filter">
          <el-button v-for="(label, key) in dateRanges" :key="key" size="default" @click="filterByPresetRange(key)">
            {{ label }}
          </el-button>

          <el-date-picker
            v-model="selectedRange"
            type="daterange"
            unlink-panels
            range-separator="~"
            start-placeholder="시작일"
            end-placeholder="종료일"
            size="default"
          />
          <el-button type="primary" size="default" @click="onSearchByRange">조회</el-button>
        </div>

        <!-- 주문 목록 -->
        <div v-for="(order, index) in filteredProductList" :key="index" class="order-card">
          <div class="order-date">{{ order.getDisplayBuyDate() }}</div>

          <div class="order-products">
            <div v-for="(name, i) in order.productName" :key="i" class="product-item">
              <img :src="order.image[i]" alt="상품 이미지" class="product-img" />
              <div class="product-info">
                <div class="product-name">{{ name }}</div>
                <div class="product-count">수량: {{ order.count[i] }}개</div>
              </div>
            </div>
          </div>

          <div class="order-footer">
            <div>배송상태: {{ order.deliveryStatus }}</div>
            <div>총 금액: ₩{{ order.totalPrice.toLocaleString() }}</div>
            <el-button
              v-if="order.deliveryStatus === 'READY'"
              type="danger"
              size="small"
              @click="cancelOrder(order.productId)"
            >
              주문 취소
            </el-button>
          </div>
        </div>
      </el-tab-pane>

      <el-tab-pane label="문의내역" name="inquiries">
        <!-- 문의내역은 추후 구현 -->
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup lang="ts">
import { onBeforeMount, reactive, ref } from 'vue'
import { container } from 'tsyringe'
import OrderRepository from '@/repository/OrderRepository'
import UserProfile from '@/entity/user/UserProfile'
import ResponseOrderProduct from '@/entity/order/OrderProduct'
import ProfileRepository from '@/repository/ProfileRepository'

const ORDER_REPOSITORY = container.resolve(OrderRepository)
const PROFILE_REPOSITORY = container.resolve(ProfileRepository)

const today = new Date()
const oneWeekAgo = new Date()
oneWeekAgo.setDate(today.getDate() - 7)
const selectedRange = ref<[Date, Date] | null>([oneWeekAgo, today])
const filteredProductList = ref<ResponseOrderProduct[]>([])

const dateRanges = {
  week: '1주일',
  month1: '1개월',
  month3: '3개월',
  month6: '6개월',
}

const state = reactive({
  user: new UserProfile(),
  productList: [] as ResponseOrderProduct[],
  activeTab: 'orders',
})

function getOrderList(start: Date, end: Date) {
  ORDER_REPOSITORY.getOrders(start, end).then((orderList) => {
    state.productList = orderList.map((item) => Object.assign(new ResponseOrderProduct(), item))
    filteredProductList.value = [...state.productList]
  })
}

function cancelOrder(productId: number[]) {
  console.log('주문 취소 요청:', productId)
  alert('주문이 취소되었습니다.')
}

function filterByPresetRange(type: keyof typeof dateRanges) {
  const end = new Date()
  let start = new Date()

  switch (type) {
    case 'week':
      start.setDate(end.getDate() - 7)
      break
    case 'month1':
      start.setMonth(end.getMonth() - 1)
      break
    case 'month3':
      start.setMonth(end.getMonth() - 3)
      break
    case 'month6':
      start.setMonth(end.getMonth() - 6)
      break
  }

  selectedRange.value = [start, end]
  getOrderList(start, end)
}

function onSearchByRange() {
  if (selectedRange.value) {
    const [start, end] = selectedRange.value
    getOrderList(start, end)
  }
}

onBeforeMount(() => {
  state.user = PROFILE_REPOSITORY.getProfile() || new UserProfile()
  getOrderList(oneWeekAgo, today)
})

const summary = reactive({
  orders: 3,
  coupons: 2,
  points: '5,000P',
  reviews: 1,
})

function goToProfileEdit() {
  alert('프로필 수정 페이지 진입')
}

function keyLabel(key: string) {
  const map = { orders: '주문', coupons: '쿠폰', points: '포인트', reviews: '리뷰' }
  return map[key as keyof typeof map] || key
}
</script>

<style scoped>
.mypage-container {
  max-width: 720px;
  margin: 0 auto;
  padding: 32px 16px;
  font-family: 'Pretendard', sans-serif;
  color: #222;
}

.profile-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.profile-left {
  display: flex;
  align-items: center;
}

.avatar {
  background-color: #f5f5f5;
  margin-right: 16px;
}

.info-text h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
}

.email {
  margin-top: 4px;
  font-size: 14px;
  color: #666;
}

.edit-btn {
  font-weight: 500;
}

.summary-section {
  display: flex;
  justify-content: space-between;
  background: #fafafa;
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 32px;
  text-align: center;
}

.summary-item {
  flex: 1;
}

.label {
  font-size: 13px;
  color: #999;
  margin-bottom: 6px;
}

.value {
  font-size: 16px;
  font-weight: 600;
}

.tabs {
  background-color: #fff;
  border: none;
}

.date-filter {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 16px;
}

.order-card {
  border: 1px solid #eee;
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 20px;
  background-color: #fff;
}

.order-date {
  font-size: 13px;
  color: #999;
  margin-bottom: 12px;
}

.order-products {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.product-item {
  display: flex;
  align-items: center;
}

.product-img {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 8px;
  margin-right: 12px;
  border: 1px solid #eee;
}

.product-info {
  display: flex;
  flex-direction: column;
}

.product-name {
  font-size: 16px;
  font-weight: 500;
}

.product-count {
  font-size: 14px;
  color: #888;
}

.order-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 14px;
  margin-top: 16px;
  color: #555;
}

.more-toggle {
  font-size: 14px;
  color: #007bff;
  cursor: pointer;
  margin-top: 8px;
}
</style>
