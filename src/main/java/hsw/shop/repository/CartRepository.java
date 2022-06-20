package hsw.shop.repository;

import hsw.shop.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("select c from Cart c" +
            " join fetch c.member m" +
            " join fetch c.product p" +
            " join fetch p.productImage pi" +
            " where m.memberId = :memberId")
    List<Cart> findAllByMemberId(@Param("memberId") Long memberId);
}
