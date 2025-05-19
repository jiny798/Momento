<template>
  <div class="product-detail-wrapper">
    <div class="product-detail">
      <!-- 이미지 영역 -->
      <div class="image-area">
        <img class="main-image" :src="imageUrl" alt="상품 이미지" />
        <ul class="thumb-list">
          <li>
            <img :src="imageUrl" class="thumb-image" alt="썸네일 이미지" />
          </li>
        </ul>
      </div>

      <!-- 정보 영역 -->
      <div class="info-area">
        <h1 class="product-title">{{ state.product.title }}</h1>
        <p class="product-price">{{ state.product.price.toLocaleString() }}원</p>

        <dl class="product-meta">
          <div>
            <dt>배송</dt>
            <dd>국내배송 / 2,500원 (5만원 이상 무료)</dd>
          </div>
        </dl>

        <!-- 맛 선택 -->
        <div class="flavor-select" v-for="(f, index) in state.product.optionCount" :key="index">
          <label>맛 선택 {{ index + 1 }}</label>
          <select v-model="selectedFlavors[index]">
            <option disabled value="">맛을 선택하세요</option>
            <option v-for="flavor in state.flavorList" :key="flavor.name" :value="flavor">
              {{ flavor.name }}
            </option>
          </select>
        </div>

        <!-- 버튼 -->
        <div class="button-group">
          <button class="btn-primary">구매하기</button>
          <button class="btn-secondary" @click="addCart">장바구니</button>
        </div>
      </div>
    </div>

    <!-- 상세 정보 탭 -->
    <el-tabs v-model="activeTab" class="detail-tab">
      <el-tab-pane label="상세정보" name="detail">
        <div class="tab-content" v-html="state.product.details"></div>
      </el-tab-pane>
      <el-tab-pane label="배송정보" name="shipping">
        <div class="tab-content">
          <p>등록된 문의가 없습니다.</p>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, defineProps, onMounted } from 'vue'
import { container } from 'tsyringe'
import ProductRepository from '@/repository/ProductRepository'
import FlavorRepository from '@/repository/FlavorRepository'
import CategoryRepository from '@/repository/CategoryRepository'
import OrderRepository from '@/repository/OrderRepository'
import Product from '@/entity/product/Product'
import Flavor from '@/entity/product/Flavor'
import Category from '@/entity/product/Category'
import RequestProduct from '@/entity/order/RequestProduct'

const PRODUCT_REPOSITORY = container.resolve(ProductRepository)
const FLAVOR_REPOSITORY = container.resolve(FlavorRepository)
const CATEGORY_REPOSITORY = container.resolve(CategoryRepository)
const ORDER_REPOSITORY = container.resolve(OrderRepository)

const selectedFlavors = ref<string[]>([])
const activeTab = ref('detail')
const imageUrl = ref('/g1.JPG')

const props = defineProps<{ productId: number }>()

const state = reactive({
  product: new Product(),
  category: new Category(),
  flavorList: [] as Flavor[],
  requestProduct: new RequestProduct(),
})

PRODUCT_REPOSITORY.get(props.productId).then((product) => (state.product = product))
FLAVOR_REPOSITORY.getAll().then((flavors) => (state.flavorList = flavors))
CATEGORY_REPOSITORY.getAll().then((res) => (state.category = res))

function addCart() {
  state.requestProduct.productId = state.product.id
  state.requestProduct.count = 1
  state.requestProduct.option = ''
  selectedFlavors.value.forEach((flavor) => (state.requestProduct.option += flavor))
  ORDER_REPOSITORY.addCart(state.requestProduct)
}
</script>

<style scoped>
.product-detail-wrapper {
  max-width: 1080px;
  margin: 0 auto;
  padding: 40px 16px;
  font-family: 'Pretendard', sans-serif;
}

.product-detail {
  display: flex;
  flex-wrap: wrap;
  gap: 40px;
}

.image-area {
  flex: 1;
  min-width: 300px;
}

.main-image {
  width: 100%;
  border-radius: 8px;
  border: 1px solid #eee;
}

.thumb-list {
  display: flex;
  gap: 8px;
  margin-top: 12px;
}

.thumb-image {
  width: 64px;
  height: 64px;
  object-fit: cover;
  border-radius: 4px;
  border: 1px solid #ddd;
}

.info-area {
  flex: 1;
  min-width: 300px;
}

.product-title {
  font-size: 24px;
  font-weight: 700;
  margin-bottom: 12px;
}

.product-price {
  font-size: 18px;
  font-weight: 600;
  color: #111;
  margin-bottom: 16px;
}

.product-meta {
  font-size: 14px;
  color: #666;
  margin-bottom: 24px;
}

.product-meta dt {
  font-weight: 500;
  margin-right: 8px;
}

.product-meta div {
  display: flex;
  margin-bottom: 6px;
}

.flavor-select {
  margin-bottom: 12px;
}

.flavor-select label {
  display: block;
  font-size: 14px;
  font-weight: 500;
  margin-bottom: 4px;
}

select {
  width: 100%;
  padding: 8px;
  font-size: 14px;
  border: 1px solid #ddd;
  border-radius: 6px;
  outline: none;
}

.button-group {
  margin-top: 24px;
  display: flex;
  gap: 12px;
}

.btn-primary,
.btn-secondary {
  flex: 1;
  padding: 12px;
  font-size: 14px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: 0.3s;
}

.btn-primary {
  background-color: #111;
  color: #fff;
}

.btn-primary:hover {
  background-color: #444;
}

.btn-secondary {
  background-color: #f5f5f5;
  color: #333;
}

.btn-secondary:hover {
  background-color: #eaeaea;
}

.detail-tab {
  margin-top: 60px;
}

.tab-content {
  padding: 24px;
  font-size: 15px;
  color: #333;
  line-height: 1.6;
}
</style>
