<template>
  <el-form ref="formRef" label-width="120px" label-position="top" class="product-form">
    <!-- 제목 -->
    <el-form-item label="상품 제목" prop="title">
      <el-input v-model="state.productWrite.title" maxlength="30" show-word-limit placeholder="제목을 입력해주세요" />
    </el-form-item>

    <!-- 이미지 -->
    <el-form-item label="상품 이미지" required>
      <el-upload
        :show-file-list="true"
        list-type="picture-card"
        :file-list="fileList"
        :http-request="handleUpload"
        :on-remove="handleRemove"
      >
        <el-icon><Plus /></el-icon>
      </el-upload>
      <div v-if="fileList.length < 2" class="checkRequired">사진을 2장 이상 등록해주세요</div>
    </el-form-item>

    <!-- 카테고리 -->
    <el-form-item label="카테고리" prop="category">
      <el-select v-model="form.category" value-key="id" placeholder="카테고리를 선택해주세요">
        <el-option v-for="item in state.categories" :key="item.id" :label="item.name" :value="item.name" />
      </el-select>
    </el-form-item>

    <!-- 가격 -->
    <el-form-item label="가격" prop="price">
      <el-input
        v-model="state.productWrite.price"
        placeholder="숫자만 입력"
        @input="formatPrice"
        run
        suffix-icon="el-icon-money"
      />
    </el-form-item>

    <!--    <el-form-item label="카테고리" prop="price">-->
    <!--      <el-select v-model="select" placeholder="Select" style="width: 100%">-->
    <!--        <el-option label="Restaurant" value="1" />-->
    <!--        <el-option label="Order No." value="2" />-->
    <!--        <el-option label="Tel" value="3" />-->
    <!--      </el-select>-->
    <!--    </el-form-item>-->

    <!-- 상세 조건 -->
    <div ref="editorRoot" />

    <div v-if="showPopup" class="image-popup" :style="{ top: popupY + 'px', left: popupX + 'px' }">
      <label>Width: <input v-model="inputWidth" /></label>
      <label>Height: <input v-model="inputHeight" /></label>
      <button @click="applyResize">적용</button>
    </div>

    <!-- 동의 체크 -->
    <el-form-item prop="agree">
      <el-checkbox v-model="form.agree"> 상품 가격 및 정보를 확인하였습니다</el-checkbox>
    </el-form-item>

    <!-- 제출 버튼 -->
    <el-form-item>
      <el-button type="primary" :loading="loading" @click="write()" :disabled="!form.agree">
        {{ isRegister ? '상품 등록' : '상품 수정' }}
      </el-button>
    </el-form-item>
  </el-form>
</template>

<script setup lang="ts">
import { ElMessage, type UploadRawFile, type UploadUserFile } from 'element-plus'
import { onMounted, onBeforeUnmount, ref, reactive, watch } from 'vue'
import Editor from '@toast-ui/editor'
import '@toast-ui/editor/dist/toastui-editor.css'
import ProductWrite from '@/entity/product/ProductWrite'
import { container } from 'tsyringe'
import { useRouter } from 'vue-router'
import type HttpError from '@/http/HttpError'
import ProductRepository from '@/repository/ProductRepository'
import CategoryRepository from '@/repository/CategoryRepository'
import Category from '@/entity/product/Category'
import Paging from '@/entity/data/Paging'
import type Product from '@/entity/product/Product'

const select = ref('')
const editorRoot = ref<HTMLDivElement | null>(null)
let editorInstance: Editor | null = null
const showPopup = ref(false)
const popupX = ref(0)
const popupY = ref(0)
const selectedImage = ref<HTMLImageElement | null>(null)
const inputWidth = ref('')
const inputHeight = ref('')
const isRegister = ref(true)
const loading = ref(false)
const formRef = ref()
const form = ref({
  title: '',
  category: '',
  price: '',
  description: '',
  agree: false,
})

type StateType = {
  productWrite: ProductWrite
  categories: Category[]
}

const state = reactive<StateType>({
  productWrite: new ProductWrite(),
  categories: [],
})

const PRODUCT_REPOSITORY = container.resolve(ProductRepository)
const CATEGORY_REPOSITORY = container.resolve(CategoryRepository)

const router = useRouter()

function write() {
  console.log(getUploadedImageUrls())

  // 대표 이미지
  state.productWrite.imageUrls = getUploadedImageUrls()
  state.productWrite.details = editorInstance?.getHTML()
  state.productWrite.category = form.value.category

  console.log(state.productWrite)
  PRODUCT_REPOSITORY.write(state.productWrite)
    .then(() => {
      ElMessage({ type: 'success', message: '글 등록이 완료되었습니다.' })
      router.replace('/')
    })
    .catch((e: HttpError) => {
      ElMessage({ type: 'error', message: e.getMessage() })
    })
}

// 에디터
onMounted(() => {
  CATEGORY_REPOSITORY.getAll().then((responseList) => {
    state.categories = responseList
  })

  if (editorRoot.value) {
    editorInstance = new Editor({
      el: editorRoot.value,
      height: '500px',
      initialEditType: 'wysiwyg',
      previewStyle: 'vertical',
      initialValue: 'Hello Toast UI ✨',
      hideModeSwitch: true,
      hooks: {
        addImageBlobHook: async (blob, callback) => {
          const formData = new FormData()
          formData.append('file', blob)

          try {
            const response = await fetch('http://localhost:8080/api/images', {
              method: 'POST',
              body: formData,
              credentials: 'include',
            })
            const imageUrl = await response.text()
            callback(imageUrl, blob.name)
          } catch (err) {
            console.error('업로드 실패', err)
          }
        },
      },
    })

    // 에디터 내부 콘텐츠 영역에 직접 접근
    const contentEl = editorRoot.value.querySelector('.toastui-editor-main')

    if (contentEl) {
      contentEl.addEventListener('click', (event: MouseEvent) => {
        const target = event.target as HTMLElement
        console.log('dd')
        console.log('d : ' + target.tagName)
        if (target.tagName === 'IMG') {
          const img = target as HTMLImageElement
          const rect = img.getBoundingClientRect()

          selectedImage.value = img
          inputWidth.value = img.getAttribute('width') || ''
          inputHeight.value = img.getAttribute('height') || ''

          popupX.value = rect.left + window.scrollX
          popupY.value = rect.top + window.scrollY + rect.height + 8 // 이미지 아래에 위치

          showPopup.value = true
        } else {
          showPopup.value = false
        }
      })
    }
  } // 에디터 끝
})

// 이미지 라시이즈
const applyResize = () => {
  if (!selectedImage.value) return

  const width = inputWidth.value.trim()
  const height = inputHeight.value.trim()

  if (width) {
    selectedImage.value.setAttribute('width', width)
  } else {
    selectedImage.value.removeAttribute('width')
  }

  if (height) {
    selectedImage.value.setAttribute('height', height)
  } else {
    selectedImage.value.removeAttribute('height')
  }

  showPopup.value = false
}

onBeforeUnmount(() => {
  editorInstance?.destroy()
})

const formatPrice = () => {
  const number = Number(form.value.price.replace(/,/g, ''))
  if (!isNaN(number)) {
    form.value.price = number.toLocaleString()
  }
}

const fileList = ref<any[]>([])
// blob -> 실제 업로드 URL 매핑용 Map
const imageMap = new Map<string, string>()

// 업로드 처리
const handleUpload = async ({ file, onSuccess, onError }: any) => {
  try {
    const formData = new FormData()
    console.log(file)
    formData.append('file', file)

    const res = await fetch('http://localhost:8080/api/images', {
      method: 'POST',
      body: formData,
      credentials: 'include',
    })
    const uploadedUrl = await res.text()

    // 실제 업로드된 URL 매핑 저장
    imageMap.set(file.uid, uploadedUrl)

    onSuccess()
  } catch (err) {
    ElMessage.error('업로드 실패')
    onError(err)
  }
}

// 삭제 이벤트 핸들링
const handleRemove = (file: any) => {
  // blob → 서버 URL 제거
  console.log(file.uid + ' 삭제삭제 ' + file.url)
  console.log(imageMap)
  imageMap.delete(file.uid)
}

// 글 등록 시 이미지 주소 가져오기
const getUploadedImageUrls = () => {
  return Array.from(imageMap.values())
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

.editor-container {
  max-width: 800px;
  margin: 40px auto;
  padding: 20px;
  background: #fdfdfd;
  border: 1px solid #ddd;
  border-radius: 8px;
}

.flavor-bar {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 16px;
}

.flavor-bar button {
  padding: 6px 12px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 4px;
  background: #f4f4f4;
  cursor: pointer;
  transition: all 0.2s ease;
}

.flavor-bar button:hover {
  background-color: #eee;
}

.flavor-bar button.active {
  background-color: #007aff;
  color: #fff;
  border-color: #007aff;
  font-weight: bold;
}

.editor-body {
  min-height: 300px;
  padding: 16px;
  border: 1px solid #e0e0e0;
  background: #fff;
  line-height: 1.8;
  border-radius: 6px;
  font-size: 16px;
  color: #333;
}

.image-popup {
  position: absolute;
  background: white;
  border: 1px solid #ddd;
  padding: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  border-radius: 6px;
  z-index: 9999;
}

.image-popup label {
  display: block;
  font-size: 13px;
  margin-bottom: 6px;
}

.image-popup input {
  width: 80px;
  padding: 4px;
  margin-left: 8px;
}

.image-popup button {
  margin-top: 8px;
  padding: 6px 12px;
  background: #007aff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
</style>
