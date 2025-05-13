<template>
  <div class="list-header">
    <p class="prd-count">총 <strong>x</strong>개의 상품이 있습니다.</p>
    <!--{{ products.length }} -->
  </div>

  <section>
    <ul class="product-grid">
      <li v-for="product in state.productPage.items" :key="product.id" class="product-item">
        <div class="thumbnail">
          <a @click="goProductDetail(product.id)" style="cursor: pointer">
            <img :src="product.images?.[0]" class="product-image" alt="" />
          </a>
          <div class="icon__box">
            <!--            <span class="wish"-->
            <!--              ><el-icon><ShoppingCart /></el-icon-->
            <!--            ></span>-->
          </div>
        </div>
        <div class="description">
          <div class="name">
            <a>{{ product.title }}</a>
          </div>
          <p class="price">{{ product.price }} 원</p>
        </div>
      </li>
    </ul>

    <div style="width: 100%; display: flex; justify-content: center">
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
import type Post from '@/entity/post/Post'
import { container } from 'tsyringe'
import ProductRepository from '@/repository/ProductRepository'
import type Product from '@/entity/product/Product'

const PRODUCT_REPOSITORY = container.resolve(ProductRepository)
const router = useRouter()

function goProductDetail(productId: number) {
  console.log('productId: ' + productId)
  router.push({ name: 'product', params: { productId } })
}

type StateType = {
  productPage: Paging<Product>
}

const selectedSort = ref('')
const state = reactive<StateType>({
  productPage: new Paging<Product>(),
})

function getList(page = 1) {
  PRODUCT_REPOSITORY.getList(page).then((responsePage) => {
    state.productPage = responsePage
    console.log('productList ' + JSON.stringify(state.productPage.items[0], null, 2))
  })
}

onMounted(() => {
  getList()
})
</script>

<style scoped>
ul {
  padding-left: 0;
  margin: 0; /* 필요에 따라 */
  list-style: none;
}

.product-grid {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;

  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(180px, 2fr));
  gap: 30px;
}

.list-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
  font-size: 14px;
  margin: 10px 20px 0 25px;
}

.product-item {
  max-width: 295px;
  border-radius: 6px;
  overflow: hidden;
  background: #fff;
}

.product-image {
  width: 100%;
  display: block;
  object-fit: cover;
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
  background: rgba(255, 255, 255, 0.7);
  border: 1px solid #ccc;
  font-size: 12px;
  padding: 2px 6px;
  cursor: pointer;
  border-radius: 3px;
}

.description {
  margin: 13px 0 0 0;
  padding: 0;
  line-height: 18px;
  text-align: left;
  color: #000000;
  font-family: 'Montserrat', 'Noto Sans KR', Arial, sans-serif !important;
}

.name {
  font-size: 15px;
}

.sort {
  align-content: center;
}

.price {
  margin: 10px 0 0 0;
  font-weight: bold;
  font-size: 14px;
  color: #000;
}

.pagination {
  display: flex;
  justify-content: center;
}
</style>
