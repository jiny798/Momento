import { inject, singleton } from 'tsyringe'
import HttpRepository from '@/repository/HttpRepository'
import type RequestProduct from '@/entity/order/RequestProduct'
import ProductInCart from '@/entity/order/ProductInCart'
import type RequestOrder from '@/entity/order/RequestOrder'
import ResponseOrderProduct from '@/entity/order/OrderProduct'
import OrderProduct from '@/entity/order/OrderProduct'

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

  public getOrders(startDate: Date, endDate: Date) {
    const start = startDate.toISOString().slice(0, 10) // 'yyyy-MM-dd'
    const end = endDate.toISOString().slice(0, 10)

    return this.httpRepository.getAll<ResponseOrderProduct>(
      {
        path: `/api/order?startDate=${encodeURIComponent(start)}&endDate=${encodeURIComponent(end)}`,
      },
      ResponseOrderProduct,
    )
  }
}
