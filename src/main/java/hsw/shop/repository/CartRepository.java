package hsw.shop.repository;

import hsw.shop.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("select c from Cart c" +
            " join c.member m" +
            " on m.memberId = :memberId")
    List<Cart> findAllByMemberId(@Param("memberId") Long memberId);
}
