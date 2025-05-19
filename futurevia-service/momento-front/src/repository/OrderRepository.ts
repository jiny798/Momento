import { inject, singleton } from 'tsyringe'
import HttpRepository from '@/repository/HttpRepository'
import type RequestProduct from '@/entity/order/RequestProduct'
import ProductInCart from '@/entity/order/ProductInCart'
import type RequestOrder from '@/entity/order/RequestOrder'

@singleton()
export default class OrderRepository {
  constructor(@inject(HttpRepository) private readonly httpRepository: HttpRepository) {}

  // 장바구니 담기
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

  public order(request: RequestOrder) {
    return this.httpRepository.post({
      path: '/api/order',
      body: request,
    })
  }
}
