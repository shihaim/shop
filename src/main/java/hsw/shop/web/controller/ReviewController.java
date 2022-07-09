package hsw.shop.web.controller;

import hsw.shop.domain.Member;
import hsw.shop.repository.ProductRepository;
import hsw.shop.service.ProductReviewService;
import hsw.shop.web.argumentresolver.Login;
import hsw.shop.web.dto.JsonResponse;
import hsw.shop.web.dto.ProductIdsDto;
import hsw.shop.web.dto.PurchasedProductDto;
import hsw.shop.web.dto.ReviewSaveDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ReviewController {

    private final ProductRepository productRepository;
    private final ProductReviewService productReviewService;

    @GetMapping("/review")
    public String reviewPage(@Login Member loginMember, @ModelAttribute ProductIdsDto productIds, Model model) {
        List<PurchasedProductDto> purchasedProducts = productRepository.findImageByProductIds(productIds.getProductIds()).stream()
                .map(PurchasedProductDto::new).collect(Collectors.toList());
        purchasedProducts.stream().forEach(System.out::println);
        model.addAttribute("loginMember", loginMember);
        model.addAttribute("purchasedProducts", purchasedProducts);
        return "review/productReviewPage";
    }

    @GetMapping("/review/{productId}/save")
    public String reviewSavePage(@Login Member loginMember, @PathVariable("productId") Long productId, Model model) {
        log.info("productId={}", productId);
        model.addAttribute("productId", productId);
        model.addAttribute("loginMember", loginMember);
        return "review/reviewSave";
    }

    @PostMapping("/review/{productId}/save")
    @ResponseBody
    public JsonResponse reviewSave(@Valid @RequestBody ReviewSaveDto reviewSaveDto, BindingResult bindingResult) {
        log.info("ReviewSaveDto={}", reviewSaveDto.getMemberId());
        log.info("ReviewSaveDto={}", reviewSaveDto.getProductId());
        log.info("ReviewSaveDto={}", reviewSaveDto.getProductReview());

        JsonResponse res = new JsonResponse();
        if (bindingResult.hasErrors()) {
            res.setStatus("FAIL");
            res.setResult(bindingResult.getFieldErrors());
        } else {
            res.setStatus("SUCCESS");
            productReviewService.reviewSave(reviewSaveDto);
        }

        log.info("status={}", res.getStatus());
        log.info("result={}", res.getResult());

        return res;
    }
}
