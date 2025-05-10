import { inject, singleton } from 'tsyringe'
import HttpRepository from '@/repository/HttpRepository'
import Flavor from '@/entity/product/Flavor'
import Category from '@/entity/product/Category'

@singleton()
export default class CategoryRepository {
  constructor(@inject(HttpRepository) private readonly httpRepository: HttpRepository) {}

  public write(request: Category) {
    return this.httpRepository.post({
      path: '/api/category',
      body: request,
    })
  }

  public getAll() {
    return this.httpRepository.getAll<Category>(
      {
        path: `/api/category`,
      },
      Category,
    )
  }

  public get(id: number) {
    return this.httpRepository.get<Category>(
      {
        path: `/api/category/{id}`,
      },
      Category,
    )
  }
}
