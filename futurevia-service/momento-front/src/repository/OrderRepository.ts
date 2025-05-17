import { inject, singleton } from 'tsyringe'
import HttpRepository from '@/repository/HttpRepository'
import type RequestProduct from '@/entity/order/RequestProduct'
import ProductInCart from '@/entity/product/ProductInCart'

@singleton()
export default class OrderRepository {
  constructor(@inject(HttpRepository) private readonly httpRepository: HttpRepository) {}

  public addCart(request: RequestProduct) {
    return this.httpRepository.post({
      path: '/api/cart',
      body: request,
    })
  }

  public getCart() {
    return this.httpRepository.getAll<ProductInCart>(
      {
        path: '/api/cart',
      },
      ProductInCart,
    )
  }
}
