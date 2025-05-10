import AxiosHttpClient, { type HttpRequestConfig } from '@/http/AxiosHttpClient'
import { inject, singleton } from 'tsyringe'
import { plainToInstance } from 'class-transformer'
import Null from '@/entity/data/Null'
import Paging from '@/entity/data/Paging'

@singleton()
export default class HttpRepository {
  constructor(@inject(AxiosHttpClient) private readonly httpClient: AxiosHttpClient) {}

  public async get<T>(config: HttpRequestConfig, clazz: { new (...args: any[]): T }): Promise<T> {
    return this.httpClient.request({ ...config, method: 'GET' }).then((response) => plainToInstance(clazz, response))
  }

  public async getList<T>(config: HttpRequestConfig, clazz: { new (...args: any[]): T }): Promise<Paging<T>> {
    return this.httpClient.request({ ...config, method: 'GET' }).then((response) => {
      const paging = plainToInstance<Paging<T>, any>(Paging, response)
      // const items: T[] = plainToInstance<T, any>(clazz, response.items)
      const items: T[] = plainToInstance<T, any>(clazz, response.items ?? []) as T[]
      paging.setItems(items)
      return paging
    })
  }

  public async getAll<T>(config: HttpRequestConfig, clazz: { new (...args: any[]): T }): Promise<T[]> {
    return this.httpClient.request({ ...config, method: 'GET' }).then((response) => {
      return plainToInstance<T, any>(clazz, response ?? []) as T[]
    })
  }

  public async post<T>(config: HttpRequestConfig, clazz: { new (...args: any[]): T } | null = null): Promise<T> {
    return this.httpClient
      .request({ ...config, method: 'POST' })
      .then((response) => (clazz ? plainToInstance(clazz, response) : (response as T)))
    // .then((response) => plainToInstance(clazz !== null ? clazz : Null, response))
  }

  public async patch<T>(config: HttpRequestConfig, clazz: { new (...args: any[]): T } | null = null): Promise<T> {
    return this.httpClient
      .request({
        ...config,
        method: 'PATCH',
      })
      .then((response) => (clazz ? plainToInstance(clazz, response) : (response as T)))
    // .then((response) => plainToInstance(clazz !== null ? clazz : Null, response))
  }

  public async delete<T>(config: HttpRequestConfig, clazz: { new (...args: any[]): T } | null = null): Promise<T> {
    return this.httpClient
      .request({ ...config, method: 'DELETE' })
      .then((response) => (clazz ? plainToInstance(clazz, response) : (response as T)))
    // .then((response) => plainToInstance(clazz !== null ? clazz : Null, response))
  }
}
