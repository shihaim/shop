package hsw.shop.api.controller;

import hsw.shop.api.dto.JsonResultDto;
import hsw.shop.domain.Product;
import hsw.shop.service.ProductService;
import hsw.shop.web.dto.ProductCreateDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductApiController {

    private final ProductService productService;

    /**
     * @RequestBody ProductCreateDto productCreateDto -> Unsupported Media Type 오류가 생긴다.
     * https://thesse.tistory.com/141 해당 주소를 통해 해결함
     */
    @PostMapping("/save")
    public ResponseEntity save(ProductCreateDto productCreateDto) throws IOException {

        log.info("productCreateDto={}", productCreateDto);

        Long saveProduct = productService.saveProduct(productCreateDto);
        Product findProduct = productService.findProduct(saveProduct);

        ResponseDto responseDto = new ResponseDto(findProduct);

        return new ResponseEntity<JsonResultDto>(new JsonResultDto("상품 저장 성공!", responseDto), HttpStatus.OK);
    }

    @Data
    static class ResponseDto {
        private String storeFileName;
        private String productName;
        private int productPrice;
        private int stockQuantity;
        private String description;

        public ResponseDto(Product product) {
            this.storeFileName = product.getProductImage().getStoreImageName();
            this.productName = product.getName();
            this.productPrice = product.getPrice();
            this.stockQuantity = product.getStockQuantity();
            this.description = product.getDescription();
        }
    }
}
