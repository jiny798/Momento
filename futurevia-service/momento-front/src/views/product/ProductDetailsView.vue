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
            <img :src="imageUrl" class="thumb-image" alt="" />
          </li>
        </ul>
      </div>

      <div class="info-area">
        <div class="heading-area">
          <h1>{{ state.product.title }}</h1>
          <!--          <span class="delivery">(배송 가능상품)</span>-->
        </div>

        <table class="product-detail nomal-font">
          <tbody>
            <tr>
              <th>상품명</th>
              <td>{{ state.product.title }}</td>
            </tr>
            <tr>
              <th>판매가</th>
              <td>
                <strong>{{ state.product.price }}</strong>
              </td>
            </tr>
            <tr>
              <th>국내·해외배송</th>
              <td>국내배송</td>
            </tr>
            <tr>
              <th>배송비</th>
              <td><strong>2,500원</strong> (50,000원 이상 구매 시 무료)</td>
            </tr>
          </tbody>
        </table>

        <div class="order-section">
          <table class="order-list">
            <tbody>
              <!-- 맛선택 -->
              <tr>
                <td>맛 선택 1</td>
                <td>
                  <select v-model="selectedFlavor1">
                    <option disabled value="">맛을 선택하세요</option>
                    <option v-for="flavor in flavorOptions" :key="flavor" :value="flavor">
                      {{ flavor }}
                    </option>
                  </select>
                </td>
              </tr>
              <tr>
                <td>맛 선택 2</td>
                <td>
                  <select v-model="selectedFlavor2">
                    <option disabled value="">맛을 선택하세요</option>
                    <option v-for="flavor in flavorOptions" :key="flavor" :value="flavor">
                      {{ flavor }}
                    </option>
                  </select>
                </td>
              </tr>
              <tr>
                <td>맛 선택 3</td>
                <td>
                  <select v-model="selectedFlavor2">
                    <option disabled value="">맛을 선택하세요</option>
                    <option v-for="flavor in flavorOptions" :key="flavor" :value="flavor">
                      {{ flavor }}
                    </option>
                  </select>
                </td>
              </tr>
              <tr>
                <td>맛 선택 4</td>
                <td>
                  <select v-model="selectedFlavor2">
                    <option disabled value="">맛을 선택하세요</option>
                    <option v-for="flavor in flavorOptions" :key="flavor" :value="flavor">
                      {{ flavor }}
                    </option>
                  </select>
                </td>
              </tr>
              <tr>
                <td>맛 선택 5</td>
                <td>
                  <select v-model="selectedFlavor2">
                    <option disabled value="">맛을 선택하세요</option>
                    <option v-for="flavor in flavorOptions" :key="flavor" :value="flavor">
                      {{ flavor }}
                    </option>
                  </select>
                </td>
              </tr>
              <tr>
                <td>맛 선택 6</td>
                <td>
                  <select v-model="selectedFlavor2">
                    <option disabled value="">맛을 선택하세요</option>
                    <option v-for="flavor in flavorOptions" :key="flavor" :value="flavor">
                      {{ flavor }}
                    </option>
                  </select>
                </td>
              </tr>
            </tbody>
          </table>

          <!--          <div class="total-price">-->
          <!--            <strong>TOTAL:</strong>-->
          <!--            <span>{{ formatPrice(price * quantity) }}</span>-->
          <!--          </div>-->

          <div class="action-buttons">
            <button class="btn-buy">구매하기</button>
            <button class="btn-cart">장바구니 담기</button>
          </div>
        </div>
      </div>
    </div>
  </div>

  <div class="detail-tab-wrapper">
    <!--상세정보/후기/문의 탭-->
    <el-tabs v-model="activeTab" class="product-tabs" stretch>
      <el-tab-pane label="상세정보" name="detail">
        <!-- 상세정보 콘텐츠 -->
        <div class="tab-content">
          <div v-html="state.product.details"></div>
        </div>
      </el-tab-pane>

      <!--      <el-tab-pane :label="`상품후기 (${reviewCount})`" name="review">-->
      <!--        &lt;!&ndash; 상품후기 콘텐츠 &ndash;&gt;-->
      <!--        <div class="tab-content">-->
      <!--          <p>작성된 후기가 없습니다.</p>-->
      <!--        </div>-->
      <!--      </el-tab-pane>-->

      <el-tab-pane :label="`배송정보`" name="qna">
        <!-- 상품문의 콘텐츠 (${qnaCount}) -->
        <div class="tab-content">
          <p>등록된 문의가 없습니다.</p>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed, defineProps, reactive, onMounted } from 'vue'
import { container } from 'tsyringe'
import ProductRepository from '@/repository/ProductRepository'
import Product from '@/entity/product/Product'
import CategoryRepository from '@/repository/CategoryRepository'
import Category from '@/entity/product/Category'

const PRODUCT_REPOSITORY = container.resolve(ProductRepository)
const CATEGORY_REPOSITORY = container.resolve(CategoryRepository)

const props = defineProps<{
  productId: number
}>()

type StateType = {
  product: Product
  category: Category
}

const state = reactive<StateType>({
  product: new Product(),
  category: new Category(),
})

// 상품 조회
function get(productId) {
  PRODUCT_REPOSITORY.get(productId).then((product) => {
    state.product = product
    console.log('product : ' + JSON.stringify(state.product, null, 2))
  })
}

get(props.productId)

const price = 6000
const imageUrl = ref('/g1.JPG')
const activeTab = ref('detail')

// 맛선택
const flavorOptions = ['초콜릿', '바닐라', '딸기', '망고', '피스타치오', '레몬']
const selectedFlavor1 = ref('')
const selectedFlavor2 = ref('')

// 선택한 맛에 따라 quantity 자동 설정
const quantity = computed(() => {
  return selectedFlavor1.value && selectedFlavor2.value ? 2 : 1
})

function formatPrice(price: number): string {
  return price.toLocaleString('ko-KR') + '원'
}

onMounted(() => {
  CATEGORY_REPOSITORY.getAll().then((res) => {
    state.category = res
  })
})
</script>

<style scoped>
@import url('https://spoqa.github.io/spoqa-han-sans/css/SpoqaHanSansNeo.css');
@import url('https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;700&family=Noto+Sans+KR:wght@300;400;500;700&display=swap');

.nomal-font {
  font-weight: normal;
  text-align: left;
}

.important-font {
  font-weight: bold;
  text-align: left;
}
.order-section {
  border-top: 1px solid #ccc;
}
ul {
  padding-left: 0;
  margin: 0; /* 필요에 따라 */
  list-style: none;
}

* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

.detail-area-wrapper {
  display: flex;
  justify-content: center;
}

.detail-tab-wrapper {
  justify-items: center;
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

.product-tabs {
  padding: 2rem;
  max-width: 1200px;
  justify-content: center !important;
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
  padding: 0.6rem;
  font-size: 13px;
}

.order-list {
  width: 100%;
  border-collapse: collapse;
  margin-top: 1rem;
}

.order-list th,
.order-list td {
  padding: 0.75rem;
  font-size: 14px;
}

.total-price {
  margin-top: 1rem;
  font-size: 1.2rem;
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

select {
  border: 1px solid #ccc; /* 얇은 1px, 연한 회색 */
  border-radius: 4px; /* 모서리 둥글게 (선택사항) */
  padding: 0.1rem; /* 내부 여백 */
  outline: none; /* 포커스 시 기본 파란 테두리 제거 */
}

select:focus {
  border-color: #999; /* 선택했을 때 좀 더 진한 회색 */
}
</style>
