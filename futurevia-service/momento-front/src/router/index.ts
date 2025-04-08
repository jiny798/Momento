import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/views/HomeView.vue'
import WriteView from '@/views/WriteView.vue'
import ReadView from '@/views/ReadView.vue'
import LoginView from '@/views/LoginView.vue'
import MomentoHomeView from '@/views/MomentoHomeView.vue'
import ProductWriteView from '@/views/ProductWriteView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/write',
      name: 'write',
      component: WriteView,
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView,
    },
    {
      path: '/post/:postId',
      name: 'post',
      component: ReadView,
      props: true,
    },
    {
      path: '/momento',
      name: 'momento',
      component: MomentoHomeView,
    },
    {
      path: '/product/write',
      name: 'product write',
      component: ProductWriteView,
    },
  ],
})

export default router
