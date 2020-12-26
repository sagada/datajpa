package study.datajpa.dto;

import lombok.Data;
import study.datajpa.entity.Member;

@Data
public class MemberDto {
    private String name;
    private int age;

    public MemberDto(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public static MemberDto createMemberDto(Member member)
    {
        return new MemberDto(member.getUsername(), member.getAge());
    }
}
