package jpastudy.jpashop.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    //Member와 N:1관계
    //Lazy : 지연로딩 (M 쪽에 lazy로 설정) >> getMember시에만 member를 가져온다
    //eager로 하면 order 불러 올때 마다 member도 가져오기 떄문에 성능 하락
    //order를 주인으로 지정해라
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    //Delivery와 1대 1관계
    //cascade All : 객체의 상태가 어떻게 바뀌든 이 객체의 라이프 사이클과 함께 하겠다...
    //order만 저장하면 delivery도 저장됨
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    //OrderItem 과 관계 1:N 관계
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    //주문날짜
    private LocalDateTime orderDate;

    //주문상태
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    //연관관계 메서드
    //order와 member(N:1)
    public void setMember(Member member){
        this.member = member;
        member.getOrder().add(this);
    }

    //order와 delivery(1:1)
    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //order와 orderItem(1:N)
    public void addOrderItem(OrderItem orderItem){
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    //== 비즈니스 로직 : 주문생성 메서드==//
    public static Order createOrder (Member member, Delivery delivery,OrderItem... orderItems) {
        Order order = new Order();
        //order와 member 연결
        order.setMember(member);
        //order와 delivery 연결
        order.setDelivery(delivery);

        //order와 orderitem연결
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        //order 상태
        order.setStatus(OrderStatus.ORDER);
        //order 날짜
        order.setOrderDate(LocalDateTime.now());
        return order;
    }
    //==비즈니스 로직 : 주문 취소 ==//
    public void cancel() {
        //배송상태가 COMP면 주문 취소 불가능
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }
        //주문 상태를 취소로 변경
        this.setStatus(OrderStatus.CANCEL);
        //Order가 취소되면 orderItem 증가처리
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }
    //==비즈니스 로직 : 전체 주문 가격 조회 ==//
    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }


}
