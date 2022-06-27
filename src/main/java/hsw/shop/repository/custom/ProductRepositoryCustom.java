package hsw.shop.repository.custom;

import hsw.shop.web.dto.ProductListDto;

import java.util.List;

public interface ProductRepositoryCustom {

    public List<ProductListDto> searchProducts(ProductSearchCondition condition);
}
