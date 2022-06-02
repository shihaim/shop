package hsw.shop.repository;

import hsw.shop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public String findByName(Long id) {
        return em.createQuery("select m.name from Member m where m.memberId = :id", String.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    public Optional<Member> findByLoginId(String loginId) {
        return findAll().stream()
                .filter(m -> m.getId().equals(loginId))
                .findFirst();
    }

    private List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public void remove(Member member) {
        em.remove(member);
    }
}
