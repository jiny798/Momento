import { createRouter, createWebHistory } from 'vue-router'
import ReadView from '@/views/test/ReadView.vue'
import LoginView from '@/views/user/LoginView.vue'
import HomeView from '@/views/HomeView.vue'
import ProductWriteView from '@/views/product/ProductWriteView.vue'

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
      component: ProductWriteView,
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView,
    },
    {
      path: '/product/:postId',
      name: 'product',
      component: ReadView,
      props: true,
    },
  ],
})

export default router
