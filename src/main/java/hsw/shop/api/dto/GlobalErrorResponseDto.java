package hsw.shop.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class GlobalErrorResponseDto {
    private List<GlobalErrorDto> errors;

    public static GlobalErrorResponseDto of(BindingResult bindingResult) {
        return new GlobalErrorResponseDto(GlobalErrorDto.of(bindingResult));
    }

    @Getter
    @AllArgsConstructor
    static class GlobalErrorDto {

        private String code;
        private String reason;

        public static List<GlobalErrorDto> of(BindingResult bindingResult) {
            return bindingResult.getAllErrors().stream()
                    .map(error -> new GlobalErrorDto(
                            error.getCode(),
                            error.getDefaultMessage()))
                    .collect(Collectors.toList());
        }
    }
}
