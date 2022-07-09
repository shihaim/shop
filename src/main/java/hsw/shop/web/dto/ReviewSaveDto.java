package hsw.shop.web.dto;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
public class ReviewSaveDto {
    private Long memberId;
    private Long productId;

    @NotEmpty(message = "리뷰를 작성해주세요.")
    private String productReview;

    public ReviewSaveDto() {
    }
}
