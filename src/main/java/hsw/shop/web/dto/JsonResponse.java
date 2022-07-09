package hsw.shop.web.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * https://www.javacodegeeks.com/2012/02/spring-mvc-and-jquery-for-ajax-form.html
 * Spring bindingResult ajax를 통한 에러 바인딩 방법
 */
@Getter
@Setter
public class JsonResponse {
    private String status = null;
    private Object result = null;
}
