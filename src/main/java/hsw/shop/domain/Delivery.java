package hsw.shop.domain;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Delivery {

    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    private String zipcode;

    private String address1;

    private String address2;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    protected Delivery() {
    }

    @Builder
    public Delivery(String zipcode, String address1, String address2, DeliveryStatus deliveryStatus) {
        this.zipcode = zipcode;
        this.address1 = address1;
        this.address2 = address2;
        this.deliveryStatus = deliveryStatus;
    }

    //생성 메서드
    public static Delivery createDelivery(Member member) {
        return Delivery.builder()
                    .zipcode(member.getZipcode())
                    .address1(member.getAddress1())
                    .address2(member.getAddress2())
                    .deliveryStatus(DeliveryStatus.READY)
                    .build();
    }
}
