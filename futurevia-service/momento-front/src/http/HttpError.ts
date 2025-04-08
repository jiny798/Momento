import type { AxiosError } from 'axios'

export default class HttpError {
  private readonly code: string
  private readonly message: string

  constructor(e: AxiosError) {
    const errorData = e.response?.data as { code?: string; message?: string }

    this.code = errorData?.code ?? '500'
    this.message = errorData?.message ?? '네트워크 상태가 안좋아잉..'
  }

  public getMessage() {
    return this.message
  }
}
