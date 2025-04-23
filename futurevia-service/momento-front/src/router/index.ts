import { createRouter, createWebHistory } from 'vue-router'
import ReadView from '@/views/test/ReadView.vue'
import LoginView from '@/views/user/LoginView.vue'
import HomeView from '@/views/HomeView.vue'
import ProductWriteView from '@/views/product/ProductWriteView.vue'
import ProductListView from '@/views/product/ProductListView.vue'
import ProductDetailsView from '@/views/product/ProductDetailsView.vue'
import ProfileView from '@/views/user/ProfileView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView,
    },
    {
      path: '/profile',
      name: 'my page',
      component: ProfileView,
    },
    {
      path: '/write',
      name: 'write',
      component: ProductWriteView,
    },
    {
      path: '/products/:productId',
      name: 'product',
      component: ProductDetailsView,
      props: true,
    },
    {
      path: '/products',
      name: 'product list',
      component: ProductListView,
      props: true,
    },
  ],
})

export default router
