<template>
  <div class="mypage-container">
    <!-- 프로필 정보 -->
    <section class="profile-section">
      <div class="profile-left">
        <el-avatar :size="72" icon="UserFilled" class="avatar" />
        <div class="info-text">
          <h3>{{ user.nickname }}</h3>
          <p class="email">{{ user.email }}</p>
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
        <el-table :data="state.orderList" stripe style="width: 100%">
          <el-table-column prop="date" label="구매일자" width="160" />
          <el-table-column prop="product" label="상품명" />
          <el-table-column prop="price" label="금액" width="100" />
          <el-table-column prop="status" label="상태" width="100" />
        </el-table>
      </el-tab-pane>

      <el-tab-pane label="문의내역" name="inquiries">
        <el-table :data="state.inquiryList" stripe style="width: 100%">
          <el-table-column prop="date" label="문의일자" width="160" />
          <el-table-column prop="subject" label="제목" />
          <el-table-column prop="status" label="상태" width="100" />
        </el-table>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup lang="ts">
import { reactive } from 'vue'

const user = reactive({
  email: 'user@example.com',
  nickname: '모멘토유저',
  grade: '일반회원',
})

const summary = reactive({
  orders: 3,
  coupons: 2,
  points: '5,000P',
  reviews: 1,
})

const state = reactive({
  activeTab: 'orders',
  orderList: [
    { date: '2024-04-01', product: '유기농 젤라또 3종', price: '12,000원', status: '배송완료' },
    { date: '2024-04-15', product: '레몬 젤라또', price: '8,000원', status: '배송중' },
  ],
  inquiryList: [
    { date: '2024-04-05', subject: '배송 언제 오나요?', status: '답변완료' },
    { date: '2024-04-17', subject: '상품 교환 가능할까요?', status: '처리중' },
  ],
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
</style>
