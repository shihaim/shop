package hsw.shop.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JsonResultDto<T> {
    private String message;
    private T data;
}
