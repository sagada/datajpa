package study.datajpa.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of ={"id", "name"})
@NamedQuery(
        name ="Team.namedQuery",
        query = "select t from Team t where t.name = :name"
)
public class Team extends JpaBaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "team_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();

    public Team(String name)
    {
        this.name = name;
    }
}
