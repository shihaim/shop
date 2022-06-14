package hsw.shop.domain;

import hsw.shop.web.dto.MemberUpdateDto;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Member extends BaseTimeEntity{

    @Id
    @GeneratedValue
    private Long memberId;

    private String id;

    private String password;

    private String name;

    private String phone;

    private String email;

    private String zipcode;

    private String address1;

    private String address2;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    protected Member() {
    }

    @Builder
    public Member(String id, String password, String name, String phone, String email, String zipcode, String address1, String address2, MemberRole role) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.zipcode = zipcode;
        this.address1 = address1;
        this.address2 = address2;
        this.role = role;
    }

    //회원 수정
    public void update(MemberUpdateDto memberUpdateDto) {
        this.name = memberUpdateDto.getName();
        this.phone = memberUpdateDto.getPhone();
        this.email = memberUpdateDto.getEmail();
        this.zipcode = memberUpdateDto.getZipcode();
        this.address1 = memberUpdateDto.getAddress1();
        this.address2 = memberUpdateDto.getAddress2();
    }
}
