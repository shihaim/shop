package hsw.shop.web.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * TODO
 * 회원수정 검증
 * 1. null 값 검증
 */

@Getter
public class MemberUpdateDto {

    private String name;

    private String phone;

    private String email;

    private String zipcode;

    private String address1;

    private String address2;

    @Builder
    public MemberUpdateDto(String name, String phone, String email, String zipcode, String address1, String address2) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.zipcode = zipcode;
        this.address1 = address1;
        this.address2 = address2;
    }
}
