package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Team;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class TeamRepositoryTest {

    @Autowired
    TeamRepository teamRepository;

    @Test
    public void namedQueryTest()
    {
        Team teamA = new Team("teamA");
        teamRepository.save(teamA);
        Team team = teamRepository.namedQuery("teamA");

        assertThat(teamA).isEqualTo(team);
        assertThat(teamA.getName()).isEqualTo(team.getName());
    }

    @Test
    public void findTeamsByNamesTest()
    {
        teamRepository.save(new Team("A"));
        teamRepository.save(new Team("B"));
        teamRepository.save(new Team("C"));
        teamRepository.save(new Team("D"));
        teamRepository.save(new Team("DE"));
        teamRepository.save(new Team("DG"));

        List<String> teamNames = Arrays.asList("A","B","C","D");

        List<Team> findTeams = teamRepository.findTeamTestByNames(teamNames);

        assertThat(findTeams.size()).isEqualTo(4);
        assertThat(findTeams.get(0).getName()).isEqualTo("A");
    }
}
