<script lang="ts" setup>
import { onBeforeMount, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { container } from 'tsyringe'
import type UserProfile from '@/entity/user/UserProfile'
import UserRepository from '@/repository/UserRepository'
import ProfileRepository from '@/repository/ProfileRepository'

const USER_REPOSITORY = container.resolve(UserRepository)
const PROFILE_REPOSITORY = container.resolve(ProfileRepository)

const router = useRouter()
const activeIndex = ref('1')
const handleSelect = (key: string, keyPath: string[]) => {
  console.log(key, keyPath)
  if (key === '4-3') {
    router.push('/login')
  }
}

type StateType = {
  profile: UserProfile | null
}

const state = reactive<StateType>({
  profile: null,
})

onBeforeMount(() => {
  USER_REPOSITORY.getProfile().then((profile) => {
    PROFILE_REPOSITORY.setProfile(profile)
    state.profile = profile
  })
})
</script>

<template>
  <div class="header">
    <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect">
      <el-menu-item index="1">
        <el-icon><IceCream /></el-icon>
        Momento
      </el-menu-item>
      <el-menu-item index="2">
        <router-link to="/products">Gelato & Cookie</router-link>
      </el-menu-item>

      <el-sub-menu index="4">
        <template #title>내정보</template>
        <el-menu-item index="4-1">주문내역</el-menu-item>
        <el-menu-item index="4-2">프로필</el-menu-item>
        <el-menu-item v-if="state.profile === null" index="4-3">
          <router-link to="/login">로그인</router-link>
        </el-menu-item>
        <el-menu-item index="4-4" v-if="state.profile !== null">로그아웃</el-menu-item>
      </el-sub-menu>
    </el-menu>
  </div>
</template>

<style scoped lang="scss">
.el-menu--horizontal > .el-menu-item:nth-child(4) {
  margin-right: auto;
}

.custom-disabled {
  pointer-events: none; /* 클릭 방지 */
  color: #606266 !important; /* 기본 색상 유지 */
  opacity: 1 !important; /* 흐려지지 않도록 설정 */
}

.logo {
  width: 52px;
  object-fit: cover;
}

.title {
  font-size: 2rem;
  font-weight: 300;
  margin-left: 5px;
}
</style>
