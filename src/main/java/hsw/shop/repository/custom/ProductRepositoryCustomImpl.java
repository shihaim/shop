package hsw.shop.repository.custom;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hsw.shop.web.dto.ProductListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.List;

import static hsw.shop.domain.QProduct.*;
import static hsw.shop.domain.QProductImage.*;

@RequiredArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ProductListDto> searchProducts(ProductSearchCondition condition) {

        List<ProductListDto> content = queryFactory
                .select(Projections.constructor(ProductListDto.class,
                        product)
                )
                .from(product)
                .join(product.productImage, productImage)
                .where(nameContain(condition.getProductName()))
                .fetch();

        return content;
    }

    private BooleanExpression nameContain(String productName) {
        return StringUtils.hasText(productName) ? product.name.contains(productName) : null;
    }
}
