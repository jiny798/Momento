<script setup lang="ts"></script>

<template>
  <div id="banner">
    <div class="container"></div>
  </div>

  <div class="center-container" style="margin-top: 5rem">
    <div class="title_2">NEW! ARRIVALS</div>
  </div>
  <div class="center-container" style="margin-bottom: 2rem">
    <div class="title_2_txt" data-ez-role="subtitle">언제나 새로운 신상품</div>
  </div>
  <!-- https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png -->

  <div class="image-container">
    <div class="product-list">
      <el-card>
        <img
          src="https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png"
          style="width: 100%"
        />
      </el-card>
      <el-card>
        <img
          src="https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png"
          style="width: 100%; height: 100%"
        />
      </el-card>
      <el-card>
        <img
          src="https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png"
          style="width: 100%"
        />
      </el-card>
    </div>
  </div>

  <br />
  <br />
  <div></div>
</template>

<style scoped lang="scss">
#banner {
  position: relative;
  padding: 12em 0em;
  background: url(/banner.jpg) no-repeat center;
  background-size: cover;
  color: #fff;
}
.product-list {
  display: flex;
  justify-content: center;
  align-items: center;
}

.title_2 {
  font-size: 24px;
  font-weight: 700;
  line-height: 1;
  color: #3a3a3a;
}

.title_2_txt {
  font-weight: 400;
  line-height: 1;
  color: #9a9a9a;
  margin-top: 15px;
}

@media (max-width: 768px) {
  .product-list {
    flex-direction: column; /* 모바일에서 수직 정렬 */
    align-items: center; /* 중앙 정렬 */
  }

  .image {
    width: 100%; /* 이미지 크기 조정 */
    height: auto;
  }

  .el-card {
    margin-bottom: 20px; /* 카드 간 간격 */
  }
}

.product-list,
.center-container {
  display: flex;
  justify-content: center;
  gap: 30px; /* 이미지 간의 간격 */
}

.image {
  width: 200px; /* 이미지 크기 조정 */
  height: auto;
}
</style>
