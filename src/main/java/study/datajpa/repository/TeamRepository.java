package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.entity.Team;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {

    @Query(name = "Team.namedQuery")
    Team namedQuery(@Param("name") String name);

    @Query("select t from Team t where t.name in :names order by t.name")
    List<Team> findTeamTestByNames(@Param("names") List<String> names);
}
