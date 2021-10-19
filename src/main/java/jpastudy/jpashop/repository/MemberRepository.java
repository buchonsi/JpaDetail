package jpastudy.jpashop.repository;

import jpastudy.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    //@PersistenceContext
    //Autowired로 대체가능
    //@RequiredArgsConstructor 생성자 자동 생성 어노테이션으로 final을  변수를 생성자로 만들어준다
    private final EntityManager em;

    //등록
    public void save(Member member){
        em.persist(member);
    }

    //아이디로 member조회
    public Member findOne(Long id){
        return em.find(Member.class, id);
    }

    public List<Member> findAll(){
        //TypeQuery
        return em.createQuery( "select m from Member m", Member.class).getResultList();
    }

    //name으로 Member 하나 또는 여러개 조회
    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.name = :name",Member.class)
                .setParameter("name", name)     //TypedQuery
                .getResultList();
    }




}
