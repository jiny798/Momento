import { DateTimeFormatter, LocalDateTime } from '@js-joda/core'
import { Transform } from 'class-transformer'

export default class Product {
  public id = 0
  public title = ''
  public price = 0
  public stockQuantity = 0
  public optionCount = 0
  public details = ''
  public imageUrls = []

  @Transform(({ value }) => LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME))
  public regDate = LocalDateTime.now()

  public getDisplayRegDate() {
    return this.regDate.format(DateTimeFormatter.ofPattern('yyyy년 MM월 dd일 HH시'))
  }

  public getDisplaySimpleRegDate() {
    return this.regDate.format(DateTimeFormatter.ofPattern('yyyy-MM-dd'))
  }
}
