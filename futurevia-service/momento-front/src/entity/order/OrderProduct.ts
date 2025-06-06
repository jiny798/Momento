import { DateTimeFormatter, LocalDateTime } from '@js-joda/core'

export default class ResponseOrderProduct {
  public image = []
  public count = []
  public productId = []
  public productName = []
  public purchaseDate = ''
  public status = ''
  public deliveryStatus = ''
  public totalPrice = 0

  public getDisplayBuyDate(): string {
    const date = new Date(this.purchaseDate)
    return `${date.getFullYear()}년 ${date.getMonth() + 1}월 ${date.getDate()}일 ${date.getHours()}시`
  }
}
