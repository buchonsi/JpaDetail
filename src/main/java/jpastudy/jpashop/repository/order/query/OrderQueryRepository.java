package jpastudy.jpashop.repository.order.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * 1:N 관계(컬렉션)을 제외한
 * Order, Member, Delivery를 한번에 조회
 * : ToOne 관계인 Member, Delivery 만 조회함
 * : OrderItem은 조회하지 않음
 */
@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {
    private final EntityManager em;

    /**
     * 1:N 관계(컬렉션)를 제외한 Order, Member, Delivery를 한번에 조회
     */
    private List<OrderQueryDto> findOrders() {
        return em.createQuery(
                        "select new jpastudy.jpashop.repository.order.query.OrderQueryDto" +
                                "(o.id, m.name, o.orderDate, o.status, d.address)" +
                                " from Order o" +
                                " join o.member m" +
                                " join o.delivery d", OrderQueryDto.class)
                .getResultList();
    } //findOrders

    /**
     * 1:N 관계인 orderItems 조회
     * Order, OrderItem, Item을 조회
     * : ToMany관계인 OrderItem과 Item을 조회에서 Dto에 저장
     */
    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery(
                        "select new jpastudy.jpashop.repository.order.query.OrderItemQueryDto" +
                                "(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                                " from OrderItem oi" +
                                " join oi.item i" +
                                " where oi.order.id = : orderId", OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
    } //findOrderItems

    /**
     * OrderQueryDto가 참조하는 OrderItemQueryDto를 채워 넣기
     */
    public List<OrderQueryDto> findOrdersQueryDto(){
        //ToOne 관계인 객체를 한번에 조회
        List<OrderQueryDto> orderQueryDtos = findOrders();
        //루프를 돌면서 ToMany관계인 객체를 조회해서 저장하기
        orderQueryDtos.forEach(orderQD ->{
        List<OrderItemQueryDto> orderItemQueryDtos = findOrderItems(orderQD.getOrderId());
            orderQD.setOrderItems(orderItemQueryDtos);
        });

        return orderQueryDtos;
    }

}