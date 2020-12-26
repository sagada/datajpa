package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;
    @Test
    public void testMember()
    {
        Member member = new Member("MEMBER_A");
        Member savedMember = memberRepository.save(member);
        Member findMember = memberRepository.findById(savedMember.getId()).get();

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCRUD()
    {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        // 리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        // 카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        // 삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    public void pagingTest()
    {
        // given
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        Member member3 = new Member("member3");
        Member member4 = new Member("member4");
        Member member5 = new Member("member5");
        Member member6 = new Member("member6");

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        memberRepository.save(member5);
        memberRepository.save(member6);

        int page = 0;
        int size = 3;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "username"));

        // when
        Page<Member> memberPage = memberRepository.findAllMembers(pageRequest);

        assertThat(memberPage.getContent().size()).isEqualTo(size);
        assertThat(memberPage.getTotalPages()).isEqualTo(2);
        assertThat(memberPage.getTotalElements()).isEqualTo(6);
        assertThat(memberPage.isFirst()).isTrue();
        assertThat(memberPage.hasNext()).isTrue();
    }

    @Test
    public void pagingDtoTransferTest()
    {
        // given
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        Member member3 = new Member("member3");
        Member member4 = new Member("member4");
        Member member5 = new Member("member5");
        Member member6 = new Member("member6");

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        memberRepository.save(member5);
        memberRepository.save(member6);

        PageRequest pageRequest = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "username"));
        Page<Member> members = memberRepository.findAllMembers(pageRequest);
        Page<MemberDto> memberDtos = members.map(Member::createDto);

        assertThat(memberDtos.getContent().size()).isEqualTo(2);
        assertThat(memberDtos.getTotalPages()).isEqualTo(3);
        assertThat(memberDtos.getTotalElements()).isEqualTo(6);
        assertThat(memberDtos.isFirst()).isTrue();
        assertThat(memberDtos.hasNext()).isTrue();
    }

    @Test
    public void bulkUpdateTest()
    {
        Member member1 = new Member("member1",13);
        Member member2 = new Member("member2",13);
        Member member3 = new Member("member3",15);
        Member member4 = new Member("member4",16);
        Member member5 = new Member("member5",16);
        Member member6 = new Member("member6",21);

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        memberRepository.save(member5);
        memberRepository.save(member6);

        memberRepository.bulkAge(16);

        em.flush();
        em.clear();

        List<Member> members = memberRepository.findMemberByAgeGreaterThan(16);
        members.forEach(member -> System.out.println("member = " + member));

    }

    @Test
    public void entityGraphTest()
    {
        List<Member> members = memberRepository.findAll();
        members.forEach(System.out::println);
    }


    @Test
    public void queryHint()
    {
        // given
        Member newMember = new Member("member1", 10);
        memberRepository.save(newMember);
        em.flush();
        em.clear();

        // when
        Member m1 = memberRepository.findReadOnlyByUsername("member1");
        m1.setUsername("member2");

        em.flush();
    }

    @Test
    public void lock()
    {
        // given
        Member newMember = new Member("member1", 10);
        memberRepository.save(newMember);
        em.flush();
        em.clear();

        // when
        List<Member> ms = memberRepository.findLockByUsername("member1");
    }

    @Test
    public void customTest()
    {
        Member newMember = new Member("member1", 10);
        memberRepository.save(newMember);
        List<Member> members = memberRepository.findMemberCustom();
    }

    @Test
    public void JpaEventBaseEntity() throws Exception{
        // given
        Member member = new Member("member1");
        memberRepository.save(member); // @PrePersist

        Thread.sleep(100);
        member.setUsername("member2");

        em.flush(); // @PreUpdate
        em.clear();

        // when
        Member findMember = memberRepository.findById(member.getId()).get();

        // then
        System.out.println("findMember = " + findMember.getCreatedDate());
        System.out.println("findMember = " + findMember.getLastModifiedDate());

        System.out.println("regID = " + findMember.getCreatedBy());
        System.out.println("chgID = " + findMember.getUpdatedBy());
    }
}