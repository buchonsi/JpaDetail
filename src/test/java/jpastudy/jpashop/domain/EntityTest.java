package jpastudy.jpashop.domain;

import jpastudy.jpashop.domain.item.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

@SpringBootTest
@Transactional
class EntityTest {

    @Autowired
    EntityManager em;

    //rollback하지마 >> value = false
    @Test
    @Rollback(value = false)
    public void entity() throws Exception{
        Member member = new Member();
        member.setName("user1");
        Address address = new Address("부천시","옥1","12345");
        member.setAddress(address);

        //영속성 컨텍스트에 저장
        em.persist(member);

        //order 생성
        Order order = new Order();
        //order와 member연결
        order.setMember(member);
        //delivery생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        delivery.setStatus(DeliveryStatus.READY);

        //delivery와 order연결
        order.setDelivery(delivery);

        //Item 생성(book)
        Book book = new Book();
        book.setName("책1");
        book.setPrice(100000);
        book.setStockQuantity(10);
        book.setAuthor("저자1");
        book.setIsbn("1234-ab");
        em.persist(book);

        //OrderItem 생성
        OrderItem orderItem = new OrderItem();
        orderItem.setCount(2);
        orderItem.setOrderPrice(200000);
        orderItem.setItem(book);
        
        //order와 orderItem연결
        order.addOrderItem(orderItem);

        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.ORDER);

        em.persist(order);
    }


}