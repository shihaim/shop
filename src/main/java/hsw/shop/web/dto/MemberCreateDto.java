package hsw.shop.web.dto;

import hsw.shop.domain.Member;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * TODO
 * 로그인 검증
 * 1. id 중복 검증
 * 2. 패스워드 일치 검증
 */
@Getter
public class MemberCreateDto {

    private final String PHONE_REGEXP = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$";

    @NotEmpty(message = "아이디를 입력해주세요.")
    private String id;

    @NotEmpty(message = "비밀번호를 입력해주세요.")
    private String password;

    private String password_check;

    @NotEmpty(message = "이름을 입력해주세요.")
    private String name;

    @NotEmpty(message = "전화번호를 입력해주세요.")
    @Pattern(regexp = PHONE_REGEXP, message = "10 ~ 11 자리의 숫자만 입력 가능합니다.")
    private String phone;

    @Email
    private String email;

    @NotEmpty(message = "주소를 입력해주세요.")
    private String zipcode;

    private String address1;

    @NotEmpty(message = "주소를 입력해주세요.")
    private String address2;

    public MemberCreateDto() {
    }

    @Builder
    public MemberCreateDto(String id, String password, String name, String phone, String email, String zipcode, String address1, String address2) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.zipcode = zipcode;
        this.address1 = address1;
        this.address2 = address2;
    }

    public Member toEntity() {
        return Member.builder()
                .id(id)
                .password(password)
                .name(name)
                .phone(phone)
                .email(email)
                .zipcode(zipcode)
                .address1(address1)
                .address2(address2)
                .build();
    }
}
