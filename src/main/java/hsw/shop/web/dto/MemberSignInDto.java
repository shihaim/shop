package hsw.shop.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class MemberSignInDto {

    @NotEmpty(message = "아이디를 입력해주세요.")
    private String id;

    @NotEmpty(message = "비밀번호를 입력해주세요.")
    private String password;

    public MemberSignInDto() {
    }

    @Builder
    public MemberSignInDto(String id, String password) {
        this.id = id;
        this.password = password;
    }
}
