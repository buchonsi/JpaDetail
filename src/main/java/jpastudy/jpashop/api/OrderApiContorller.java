package jpastudy.jpashop.api;

import jpastudy.jpashop.domain.*;
import jpastudy.jpashop.repository.OrderRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

/* OneToMany 관계 성능 최적화
 * Order -> OrderItem -> Item
 */
@RestController
@RequiredArgsConstructor
public class OrderApiContorller {
    private final OrderRepository orderRepository;

    /**
     * V1: 엔티티를 API에 직접 노출하는 방식
     * N+1 문제 발생
     * Order 1건 : Member, Delivery N번 (Order row수 만큼)
     * OrderItem : N번 (Order row 수 만큼)
     * Item : N번 (Order row 수 만큼)
     * */
    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAll(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName(); //Lazy 강제 초기화
            order.getDelivery().getAddress(); //Lazy 강제 초기화
            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream().forEach(orderItem -> orderItem.getItem().getName()); //Lazy 강제초기화
        }
        return all;
    }

    /**
     * V2 : 엔티티를 DTO로 변환해서 노출하는 방식
     * N+1문제 발생
     * 엔티티를 직접 노출하지 않았다는 장점이 있음(v1과 비교했을때)
     */
    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2(){
        List<Order> orders = orderRepository.findAll(new OrderSearch());
        return orders.stream()                          //Stream<Order>
                .map(order -> new OrderDto(order))      //Stream<OrderDto>
                .collect(toList());                     //List<OrderDto>
    }

    /**
     * V3 : 엔티티를 DTO로 변환하여 노출, Fetch join을 사용하여 성능 최적화
     * 문제점 : xxxToMany 의존관계 객체들의 페이징 처리가 안된다.
     */
    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithItem();
        orders.forEach(System.out::println);
        //orders.forEach(order -> System.out.println(order);

        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(toList());
        return result;
    }

    /**
     * V3.1 : 엔티티를 DTO로 변환하여 노출, Fetch join을 사용하여 성능 최적화
     * ToMany 관계인 엔티티를 가져올때 페이징 처리되지 않는 문제를 해결하기 위해서
     * : 1) ToOne관계인 엔티티는 fetch join으로 가져오고
     * : 2) ToMany 관계는 hibernate.default_batch_fetch_size 설정하기
     */
    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_page(
                @RequestParam(value="offset", defaultValue = "0") int offset,
                @RequestParam(value="limit", defaultValue = "0") int limit){
        List<Order> orderList = orderRepository.findAllWithMemberDelivery(offset, limit);
        List<OrderDto> orderDtoList = orderList.stream()      //Stream<Order>
                .map(order -> new OrderDto(order))       //Stream<OrderDtO>
                .collect(toList());//List<OrderDtd>
        return orderDtoList;

    }

    //응답과 요청에 사용항 DTO InnerClass 선언

    @Data
    static class OrderItemDto {
        private String itemName; //상품 명
        private int orderPrice; //주문 가격
        private int count; //주문 수량
        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();   //Lazy Loding 초기화
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    } //static class OrderItemDto

    @Data
    static class OrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;
        public OrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();     //Lazy Loding 초기화
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();

            orderItems = order.getOrderItems()     //Lazy Loding 초기화
                    .stream()                                         //Stream<OrderItem>
                    .map(orderItem -> new OrderItemDto(orderItem))    //Stream<OrderItemDTO>
                    .collect(toList());                    //List<OrderItemDTO>
        }
    } //static class OrderDto
}