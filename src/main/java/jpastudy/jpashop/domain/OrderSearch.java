package jpastudy.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderSearch {
    //회원의 이름으로 검색
    private String name;

    //주문 상태(ORDER,CANCEL)
    private OrderStatus orderStatus;

}
