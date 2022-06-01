package hsw.shop.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.stream.Collectors;

/**
 * https://velog.io/@qotndus43/valid 참고
 */

@Getter
@AllArgsConstructor
public class FieldErrorResponseDto {

    private List<FieldErrorDto> errors;

    public static FieldErrorResponseDto of(BindingResult bindingResult) {
        return new FieldErrorResponseDto(FieldErrorDto.of(bindingResult));
    }

    @Getter
    @AllArgsConstructor
    static class FieldErrorDto {

        private String field;
        private String value;
        private String reason;

        public static List<FieldErrorDto> of(BindingResult bindingResult) {

            return bindingResult.getAllErrors().stream()
                    .map(error -> new FieldErrorDto(
                            ((FieldError) error).getField(),
                            ((FieldError) error).getRejectedValue().toString(),
                            ((FieldError) error).getDefaultMessage()))
                    .collect(Collectors.toList());
        }
    }
}
