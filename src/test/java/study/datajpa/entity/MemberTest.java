package study.datajpa.entity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberTest {

    @PersistenceContext
    EntityManager em;

    @Test
    public void testEntity()
    {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 11, teamA);
        Member member2 = new Member("member1", 12, teamA);
        Member member3 = new Member("member2", 13, teamB);
        Member member4 = new Member("member2", 14, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        // 초기화
        em.flush(); // 강제 insert 쿼리 실행
        em.clear(); // 영속성 컨텍스트 캐시 날림

        // 확인
        List<Member> memberList =  em.createQuery("select m from Member m", Member.class).getResultList();
        memberList.forEach(member ->
                {
                    System.out.println("member : "+ member);
                    System.out.println("member Team : " + member.getTeam());
                    System.out.println();
                }
        );

    }


}