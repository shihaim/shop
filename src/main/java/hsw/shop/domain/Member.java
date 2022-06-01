package hsw.shop.domain;

import hsw.shop.web.dto.MemberUpdateDto;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
public class Member {

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

    protected Member() {
    }

    @Builder
    public Member(String id, String password, String name, String phone, String email, String zipcode, String address1, String address2) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.zipcode = zipcode;
        this.address1 = address1;
        this.address2 = address2;
    }

    //로그인 로직
    public void signIn() {

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
