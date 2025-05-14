import { inject, singleton } from 'tsyringe'
import HttpRepository from '@/repository/HttpRepository'
import type RequestProduct from '@/entity/order/RequestProduct'

@singleton()
export default class OrderRepository {
  constructor(@inject(HttpRepository) private readonly httpRepository: HttpRepository) {}

  public addCart(request: RequestProduct) {
    return this.httpRepository.post({
      path: '/api/cart',
      body: request,
    })
  }
}
