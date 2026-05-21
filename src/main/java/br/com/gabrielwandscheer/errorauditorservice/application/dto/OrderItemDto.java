package br.com.gabrielwandscheer.errorauditorservice.application.dto;

public class OrderItemDto {

    private Integer sku;
    private Integer amount;

    public Integer getSku() {
        return sku;
    }

    public void setSku(Integer sku) {
        this.sku = sku;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
