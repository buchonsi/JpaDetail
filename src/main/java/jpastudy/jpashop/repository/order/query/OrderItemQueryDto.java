package jpastudy.jpashop.repository.order.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

//조회한 결과를 담을 용도로 사용하는 Dto
@Data
public class OrderItemQueryDto {
    @JsonIgnore
    private Long orderId; //주문번호
    private String itemName;//상품명
    private int orderPrice; //주문가격
    private int count; //주문수량

    public OrderItemQueryDto(Long orderId, String itemName, int orderPrice, int count) {
        this.orderId = orderId;
        this.itemName = itemName;
        this.orderPrice = orderPrice;
        this.count = count;
    }
}