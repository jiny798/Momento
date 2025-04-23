<template>
  <el-card class="mypage-container">
    <!-- 프로필 정보 영역 -->
    <div class="profile-info">
      <el-avatar :size="80" icon="UserFilled" class="avatar" />

      <div class="info-text">
        <h3>{{ user.nickname }}</h3>
        <p>{{ user.email }}</p>
        <el-tag type="success" size="small">{{ user.grade }}</el-tag>
      </div>

      <!-- 프로필 수정 버튼 -->
      <el-button class="edit-profile-btn" type="primary" size="small" @click="goToProfileEdit"> 프로필 수정 </el-button>
    </div>

    <!-- 요약 박스 -->
    <div class="summary-box">
      <el-card class="summary-item" shadow="hover">
        <div class="label">주문</div>
        <div class="value">{{ summary.orders }}</div>
      </el-card>
      <el-card class="summary-item" shadow="hover">
        <div class="label">쿠폰</div>
        <div class="value">{{ summary.coupons }}</div>
      </el-card>
      <el-card class="summary-item" shadow="hover">
        <div class="label">적립금</div>
        <div class="value">{{ summary.points }}</div>
      </el-card>
      <el-card class="summary-item" shadow="hover">
        <div class="label">리뷰</div>
        <div class="value">{{ summary.reviews }}</div>
      </el-card>
    </div>

    <!-- 탭 리스트 -->
    <el-tabs v-model="state.activeTab" type="border-card" class="tabs">
      <el-tab-pane label="구매내역" name="orders">
        <el-table :data="state.orderList" stripe style="width: 100%">
          <el-table-column prop="date" label="구매일자" width="180" />
          <el-table-column prop="product" label="상품명" />
          <el-table-column prop="price" label="금액" width="120" />
          <el-table-column prop="status" label="상태" width="100" />
        </el-table>
      </el-tab-pane>

      <el-tab-pane label="문의내역" name="inquiries">
        <el-table :data="state.inquiryList" stripe style="width: 100%">
          <el-table-column prop="date" label="문의일자" width="180" />
          <el-table-column prop="subject" label="제목" />
          <el-table-column prop="status" label="답변상태" width="100" />
        </el-table>
      </el-tab-pane>
    </el-tabs>
  </el-card>
</template>

<script setup lang="ts">
import { reactive } from 'vue'

// 사용자 프로필 정보
const user = reactive({
  email: 'user@example.com',
  nickname: '모멘토유저',
  grade: '일반회원',
})

// 상단 요약 정보
const summary = reactive({
  orders: 3,
  coupons: 2,
  points: '5,000P',
  reviews: 1,
})

// 탭 리스트 데이터
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

// 프로필 수정 버튼 클릭
const goToProfileEdit = () => {
  alert('프로필 수정 페이지 진입')
  // useRouter().push('/settings/profile')
}
</script>

<style scoped>
.mypage-container {
  max-width: 860px;
  margin: 40px auto;
  padding: 30px;
}

.profile-info {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 30px;
}

.avatar {
  margin-right: 20px;
  background-color: #f5f7fa;
}

.info-text {
  flex-grow: 1;
}

.info-text h3 {
  margin: 0;
  font-weight: bold;
}

.info-text p {
  margin: 4px 0 8px;
  color: #666;
}

.edit-profile-btn {
  margin-left: auto;
  align-self: flex-start;
  height: 32px;
}

.summary-box {
  display: flex;
  justify-content: space-between;
  gap: 10px;
  margin-bottom: 30px;
}

.summary-item {
  flex: 1;
  text-align: center;
  padding: 16px 0;
}

.summary-item .label {
  font-size: 14px;
  color: #888;
}

.summary-item .value {
  font-size: 20px;
  font-weight: bold;
  margin-top: 6px;
}

.tabs {
  margin-top: 20px;
}
</style>
