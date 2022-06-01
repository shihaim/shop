package hsw.shop.web.dto;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
public class MemberSignInDto {

    @NotEmpty
    private String id;

    @NotEmpty
    private String password;
}
