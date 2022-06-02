package hsw.shop.web.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
public class MemberUpdateDto {

    private final String PHONE_REGEXP = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$";

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

    /**
     * Cannot construct instance of 'MemberUpdateDto' ~~ 에러가 발생한다.
     * 기본 생성자를 찾을 수 없어서 생기는 에러이다.
     * https://eeyatho.tistory.com/5를 통해 해결
     */
    public MemberUpdateDto() {
    }

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
