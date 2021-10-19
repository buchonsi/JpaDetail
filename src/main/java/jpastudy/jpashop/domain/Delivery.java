package jpastudy.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Delivery {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="delivery_id")
    private Long id;

    @Embedded
    private Address address;

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    //EnumType.ORDINAL : enum 순서 값을 DB에 저장
    //EnumType.String : 문자열로 저장 (권장)
    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

}
