package hsw.shop.web.controller;

import hsw.shop.domain.Member;
import hsw.shop.domain.Product;
import hsw.shop.service.ProductService;
import hsw.shop.web.argumentresolver.Login;
import hsw.shop.web.SessionConst;
import hsw.shop.web.dto.ProductCreateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/product")
    public String productManage() {
        return "product/productManage";
    }

    //상품 저장
    @GetMapping("/product/save")
    public String productSavePage(Model model) {
        model.addAttribute("productSaveForm", new ProductCreateDto());
        return "product/productSave";
    }

    @PostMapping("/product/save")
    public String productSave(@Valid @ModelAttribute("productSaveForm") ProductCreateDto productCreateDto, BindingResult bindingResult) throws IOException {

        if (productCreateDto.getProductImage().isEmpty()) {
            bindingResult.reject("emptyProductImage" ,"상품 이미지를 넣어주세요.");
        }

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);

            return "product/productSave";
        }

        productService.saveProduct(productCreateDto);

        return "redirect:/product";
    }

    //상품 수정

    //상품 상세
    @GetMapping("/products/{productId}")
    public String productDetailPage(@Login Member loginMember,@PathVariable("productId") Long productId, Model model) {
        Product product = productService.findProduct(productId);
        model.addAttribute("product", product);
        model.addAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        return "product/productDetail";
    }
}
