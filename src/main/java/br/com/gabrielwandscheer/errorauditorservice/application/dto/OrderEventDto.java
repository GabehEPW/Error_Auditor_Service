package br.com.gabrielwandscheer.errorauditorservice.application.dto;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderEventDto {
    private String zipCode;
    private Long customerId;
    private List<OrderItemDto> orderItems;
    private String origin;
    private OffsetDateTime occurredAt;
}
