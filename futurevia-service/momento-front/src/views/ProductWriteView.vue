<template>
  <el-form ref="formRef" :model="form" :rules="rules" label-width="120px" label-position="top" class="product-form">
    <!-- 제목 -->
    <el-form-item label="상품 제목" prop="title">
      <el-input v-model="form.title" maxlength="30" show-word-limit placeholder="제목을 입력해주세요" />
    </el-form-item>

    <!-- 이미지 -->
    <el-form-item label="상품 이미지" required>
      <el-upload
        v-model:file-list="productImages"
        action=""
        list-type="picture-card"
        :auto-upload="false"
        :limit="5"
        accept="image/*"
      >
        <i class="el-icon-plus"></i>
      </el-upload>
      <div v-if="productImages.length < 2" class="checkRequired">사진을 2장 이상 등록해주세요</div>
    </el-form-item>

    <!-- 카테고리 -->
    <el-form-item label="카테고리" prop="category">
      <el-select v-model="form.category" placeholder="카테고리를 선택해주세요">
        <el-option v-for="item in categories" :key="item.id" :label="item.name" :value="item.id" />
      </el-select>
    </el-form-item>

    <!-- 구입시기 -->
    <el-form-item label="구입시기" prop="purchaseDate">
      <el-date-picker v-model="form.purchaseDate" type="date" placeholder="날짜 선택" style="width: 100%" />
    </el-form-item>

    <!-- 브랜드 -->
    <el-form-item label="브랜드/모델명" prop="brand">
      <el-input v-model="form.brand" placeholder="브랜드명을 입력해주세요" />
    </el-form-item>

    <!-- 가격 -->
    <el-form-item label="가격" prop="price">
      <el-input v-model="form.price" placeholder="숫자만 입력" @input="formatPrice" suffix-icon="el-icon-money" />
    </el-form-item>

    <!-- 배송비 포함 여부 -->
    <el-form-item label="배송비 포함" prop="isDeliveryIncluded">
      <el-radio-group v-model="form.isDeliveryIncluded">
        <el-radio :label="true">예</el-radio>
        <el-radio :label="false">아니오</el-radio>
      </el-radio-group>
    </el-form-item>

    <!-- 상태 -->
    <el-form-item label="상품 상태" prop="grade">
      <el-select v-model="form.grade" placeholder="상태 선택">
        <el-option v-for="g in grades" :key="g" :label="g" :value="g" />
      </el-select>
    </el-form-item>

    <!-- 결함 여부 -->
    <el-form-item label="결함 여부" prop="isDefect">
      <el-radio-group v-model="form.isDefect">
        <el-radio :label="true">예</el-radio>
        <el-radio :label="false">아니오</el-radio>
      </el-radio-group>
    </el-form-item>

    <!-- 결함 이미지 (조건부) -->
    <el-form-item v-if="form.isDefect" label="결함 이미지" required>
      <el-upload
        v-model:file-list="defectImages"
        action=""
        list-type="picture-card"
        :auto-upload="false"
        :limit="5"
        accept="image/*"
      >
        <i class="el-icon-plus"></i>
      </el-upload>
      <div v-if="defectImages.length < 2" class="checkRequired">결함 사진을 2장 이상 등록해주세요</div>
    </el-form-item>

    <!-- 판매 이유 -->
    <el-form-item label="판매 이유" prop="saleReason">
      <el-input
        type="textarea"
        v-model="form.saleReason"
        maxlength="50"
        show-word-limit
        placeholder="판매 이유를 입력해주세요"
      />
    </el-form-item>

    <!-- 상세 조건 -->
    <el-form-item label="상품 상세 조건" prop="description">
      <el-input
        type="textarea"
        v-model="form.description"
        maxlength="255"
        show-word-limit
        rows="4"
        placeholder="상세 설명 입력"
      />
    </el-form-item>

    <!-- 동의 체크 -->
    <el-form-item prop="agree">
      <el-checkbox v-model="form.agree"> 상품 상태의 허위기재로 인한 피해는 판매자가 부담합니다 (동의) </el-checkbox>
    </el-form-item>

    <!-- 제출 버튼 -->
    <el-form-item>
      <el-button type="primary" :loading="loading" @click="handleSubmit" :disabled="!form.agree">
        {{ isRegister ? '상품 등록' : '상품 수정' }}
      </el-button>
    </el-form-item>
  </el-form>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import type { UploadUserFile } from 'element-plus'

const isRegister = ref(true)
const loading = ref(false)

const formRef = ref()
const form = ref({
  title: '',
  category: '',
  purchaseDate: '',
  brand: '',
  price: '',
  isDeliveryIncluded: null,
  grade: '',
  isDefect: false,
  saleReason: '',
  description: '',
  agree: false,
})

const productImages = ref<UploadUserFile[]>([])
const defectImages = ref<UploadUserFile[]>([])

const categories = ref([
  { id: 1, name: '전자기기' },
  { id: 2, name: '의류' },
])
const grades = ['상', '중', '하']

const rules = {
  title: [{ required: true, message: '제목을 입력해주세요', trigger: 'blur' }],
  category: [{ required: true, message: '카테고리를 선택해주세요', trigger: 'change' }],
  purchaseDate: [{ required: true, message: '구입시기를 선택해주세요', trigger: 'change' }],
  brand: [{ required: true, message: '브랜드를 입력해주세요', trigger: 'blur' }],
  price: [{ required: true, message: '가격을 입력해주세요', trigger: 'blur' }],
  isDeliveryIncluded: [{ required: true, message: '선택해주세요', trigger: 'change' }],
  grade: [{ required: true, message: '상태를 선택해주세요', trigger: 'change' }],
  isDefect: [{ required: true, message: '결함 여부를 선택해주세요', trigger: 'change' }],
  saleReason: [{ required: true, message: '판매 이유를 입력해주세요', trigger: 'blur' }],
  description: [{ required: true, message: '상세조건을 입력해주세요', trigger: 'blur' }],
  agree: [{ required: true, message: '동의가 필요합니다', trigger: 'change' }],
}

const formatPrice = () => {
  const number = Number(form.value.price.replace(/,/g, ''))
  if (!isNaN(number)) {
    form.value.price = number.toLocaleString()
  }
}

const handleSubmit = () => {
  formRef.value?.validate((valid: boolean) => {
    if (valid) {
      loading.value = true
      // 여기에 등록 or 수정 API 호출
      setTimeout(() => {
        loading.value = false
        alert('업로드 완료!')
      }, 1000)
    }
  })
}
</script>

<style scoped>
.product-form {
  max-width: 800px;
  margin: 0 auto;
  padding: 40px 20px;
}

.checkRequired {
  color: #f56c6c;
  font-size: 13px;
  margin-top: 4px;
}
</style>
