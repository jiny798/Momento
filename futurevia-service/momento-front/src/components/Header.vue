<template>
  <header class="header-wrapper">
    <nav class="header-nav">
      <router-link to="/" class="logo">
        <el-icon><IceCream /></el-icon>
        <span class="brand">Momento</span>
      </router-link>

      <router-link to="/products" class="nav-link">Shop</router-link>

      <div class="nav-right">
        <template v-if="state.profile">
          <!--          <router-link to="/cart" class="nav-icon">-->
          <!--            <el-icon><ShoppingCart /></el-icon>-->
          <!--          </router-link>-->
          <el-dropdown>
            <span class="user-menu">{{ state.profile.nickname }}</span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="router.push('/cart')"> 장바구니 </el-dropdown-item>
                <el-dropdown-item @click="router.push('/profile')">마이페이지</el-dropdown-item>
                <el-dropdown-item divided @click="logout">로그아웃</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>
        <template v-else>
          <el-button size="small" @click="router.push('/login')">로그인</el-button>
        </template>
      </div>
    </nav>
  </header>
</template>

<script lang="ts" setup>
import { onBeforeMount, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { container } from 'tsyringe'
import type UserProfile from '@/entity/user/UserProfile'
import UserRepository from '@/repository/UserRepository'
import ProfileRepository from '@/repository/ProfileRepository'
import { IceCream, ShoppingCart } from '@element-plus/icons-vue'

const USER_REPOSITORY = container.resolve(UserRepository)
const PROFILE_REPOSITORY = container.resolve(ProfileRepository)
const router = useRouter()

const state = reactive<{ profile: UserProfile | null }>({ profile: null })

onBeforeMount(() => {
  USER_REPOSITORY.getProfile().then((profile) => {
    PROFILE_REPOSITORY.setProfile(profile)
    state.profile = profile
  })
})

function logout() {
  console.log('로그아웃 처리')
  state.profile = null
  router.push('/')
}
</script>

<style scoped>
.header-wrapper {
  background: #fff;
  border-bottom: 1px solid #eee;
  padding: 12px 16px;
  position: sticky;
  top: 0;
  z-index: 1000;
}

.header-nav {
  max-width: 1080px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-family: 'Pretendard', sans-serif;
}

.logo {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 18px;
  font-weight: 600;
  color: #333;
  text-decoration: none;
}

.nav-link {
  margin-left: 24px;
  font-size: 15px;
  color: #444;
  text-decoration: none;
}

.nav-link:hover {
  color: #222;
}

.nav-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.nav-icon {
  font-size: 18px;
  color: #333;
}

.user-menu {
  font-size: 14px;
  color: #333;
  cursor: pointer;
}
</style>
