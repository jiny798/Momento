<template>
  <section>
    <div class="category-title">
      {{ categoryTitle }}
    </div>

    <ul class="product-grid">
      <li v-for="product in state.productPage.items" :key="product.id" class="product-item">
        <div class="thumbnail">
          <a @click="goProductDetail(product.id)" style="cursor: pointer">
            <img :src="product.images?.[0]" class="product-image" alt="상품 이미지" />
          </a>
        </div>
        <div class="description">
          <div class="name">
            <a>{{ product.title }}</a>
          </div>
          <p class="price">{{ product.price.toLocaleString() }}원</p>
        </div>
      </li>
    </ul>

    <div class="pagination">
      <el-pagination
        :background="true"
        layout="prev, pager, next"
        v-model:current-page="state.productPage.page"
        :total="state.productPage.totalCount"
        :default-page-size="5"
        @current-change="(page: number) => getList(page)"
      />
    </div>
  </section>
</template>

<script lang="ts" setup>
import { onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import Paging from '@/entity/data/Paging'
import type Product from '@/entity/product/Product'
import ProductRepository from '@/repository/ProductRepository'
import { container } from 'tsyringe'

const PRODUCT_REPOSITORY = container.resolve(ProductRepository)
const router = useRouter()
const categoryTitle = ref('전체 상품') // 여기서 동적으로 변경 가능
function goProductDetail(productId: number) {
  router.push({ name: 'product', params: { productId } })
}

type StateType = {
  productPage: Paging<Product>
}

const state = reactive<StateType>({
  productPage: new Paging<Product>(),
})

function getList(page = 1) {
  PRODUCT_REPOSITORY.getList(page).then((responsePage) => {
    state.productPage = responsePage
  })
}

onMounted(() => {
  getList()
})
</script>

<style scoped>
.category-title {
  text-align: center;
  font-size: 21px;
  font-weight: 500;
  margin-top: 50px;
  margin-bottom: 15px;
  color: #222;
  font-family: 'Pretendard', 'Noto Sans KR', sans-serif;
}

ul {
  padding-left: 0;
  margin: 0;
  list-style: none;
}

.product-grid {
  max-width: 1080px;
  margin: 0 auto;
  padding: 48px 20px;
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap: 40px;
}

.product-item {
  border-radius: 12px;
  overflow: hidden;
  background: #ffffff;
  box-shadow: 0 0 0 transparent;
  transition: box-shadow 0.2s ease;
}

.product-item:hover {
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.06);
}

.product-image {
  width: 100%;
  height: 240px;
  object-fit: cover;
  border-bottom: 1px solid #eee;
}

.description {
  padding: 16px;
  font-family: 'Pretendard', 'Noto Sans KR', sans-serif;
  color: #111;
}

.name a {
  font-size: 16px;
  font-weight: 500;
  line-height: 1.4;
  color: #111;
  text-decoration: none;
}

.price {
  margin-top: 12px;
  font-weight: 700;
  font-size: 15px;
  color: #1e1e1e;
}

.pagination {
  display: flex;
  justify-content: center;
  padding: 40px 0;
}
</style>
