package jpastudy.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jpastudy.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "order_item")
@Getter @Setter
@NoArgsConstructor
public class OrderItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    //Order와의 관계 N:1
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    //Item과의 관계 N:1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    //주문가격
    private int orderPrice;

    //주문수량
    private int count;

    //==주문 상품 생성 메서드==//
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        //orderitem과 item 연결
        orderItem.setItem(item);
        //주문 상품의 가격
        orderItem.setOrderPrice(orderPrice);
        //주문 상품 수량
        orderItem.setCount(count);
        //주문 상품의 재고 수량 감소
        item.removeStock(count);
        return orderItem;
    }
    //==비즈니스 로직 : 주문 상품 취소 ==//
    public void cancel() {
        //주문이 취소되면 재고 수량 증가
        getItem().addStock(count);
    }
    //==비즈니스 로직 : 주문상품 전체 가격 조회 ==//
    public int getTotalPrice() {
        //주문 상품가격 * 수량
        return getOrderPrice() * getCount();
    }



}
