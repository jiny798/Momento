import { createRouter, createWebHistory } from 'vue-router'

import LoginView from '@/views/user/LoginView.vue'
import HomeView from '@/views/HomeView.vue'
import ProductWriteView from '@/views/product/ProductWriteView.vue'
import ProductListView from '@/views/product/ProductListView.vue'
import ProductDetailsView from '@/views/product/ProductDetailsView.vue'
import ProfileView from '@/views/user/ProfileView.vue'
import FlavorWriteView from '@/views/product/FlavorWriteView.vue'
import CategoryWriteView from '@/views/product/CategoryWriteView.vue'
import CartView from '@/views/user/CartView.vue'

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
    {
      path: '/cart',
      name: 'cart list',
      component: CartView,
      props: true,
    },
    {
      path: '/flavor',
      name: 'product flavor',
      component: FlavorWriteView,
      props: true,
    },
    {
      path: '/category',
      name: 'product category',
      component: CategoryWriteView,
      props: true,
    },
  ],
})

export default router
