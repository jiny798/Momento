import HttpRepository from '@/repository/HttpRepository'
import type Login from '@/entity/user/Login'
import { inject, singleton } from 'tsyringe'
import type ProductWrite from '@/entity/product/ProductWrite'
import Product from '@/entity/product/Product'
import Paging from '@/entity/data/Paging'

@singleton()
export default class ProductRepository {
  constructor(@inject(HttpRepository) private readonly httpRepository: HttpRepository) {}

  public write(request: ProductWrite) {
    return this.httpRepository.post({
      path: '/api/products',
      body: request,
    })
  }

  public get(productId: number) {
    return this.httpRepository.get<Product>({ path: `/api/products/${productId}` }, Product)
  }

  public getList(page: number) {
    return this.httpRepository.getList<Product>(
      {
        path: `/api/products?page=${page}&size=10`,
      },
      Product,
    )
  }

  public delete(productId: number) {
    return this.httpRepository.delete({
      path: `/api/products/${productId}`,
    })
  }
}
