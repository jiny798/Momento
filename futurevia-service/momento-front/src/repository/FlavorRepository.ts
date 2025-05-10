import { inject, singleton } from 'tsyringe'
import HttpRepository from '@/repository/HttpRepository'
import Flavor from '@/entity/product/Flavor'

@singleton()
export default class FlavorRepository {
  constructor(@inject(HttpRepository) private readonly httpRepository: HttpRepository) {}

  public write(request: Flavor) {
    return this.httpRepository.post({
      path: '/api/flavors',
      body: request,
    })
  }

  public getAll() {
    return this.httpRepository.getAll<Flavor>(
      {
        path: `/api/flavors`,
      },
      Flavor,
    )
  }
}
