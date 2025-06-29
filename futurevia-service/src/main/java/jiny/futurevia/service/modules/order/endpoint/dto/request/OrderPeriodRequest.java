package jiny.futurevia.service.modules.order.endpoint.dto.request;

import lombok.*;

import java.time.LocalDate;

@Data
@Setter
@AllArgsConstructor
public class OrderPeriodRequest {
    private LocalDate startDate;
    private LocalDate endDate;
}