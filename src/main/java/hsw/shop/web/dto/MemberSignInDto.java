package hsw.shop.web.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class MemberSignInDto {

    @NotEmpty
    private String id;

    @NotEmpty
    private String password;
}
