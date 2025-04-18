<script setup lang="ts">
interface Product {
  name: string
  image: string
}

const products: Product[] = [
  { name: '젤라또 1', image: '/' },
  { name: '젤라또 2', image: 'https://via.placeholder.com/150' },
  { name: '젤라또 3', image: 'https://via.placeholder.com/150' },
  { name: '젤라또 4', image: 'https://via.placeholder.com/150' },
  { name: '젤라또 5', image: 'https://via.placeholder.com/150' },
  { name: '젤라또 6', image: 'https://via.placeholder.com/150' },
  { name: '젤라또 7', image: 'https://via.placeholder.com/150' },
  { name: '젤라또 8', image: 'https://via.placeholder.com/150' },
]
</script>

<template>
  <div class="product-grid">
    <div v-for="(product, index) in products" :key="index" class="product-card">
      <img :src="product.image" :alt="product.name" />
      <p class="name">{{ product.name }}</p>
    </div>
  </div>
</template>

<style scoped lang="scss"></style>
