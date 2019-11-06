package org.univ.sparktp.dashboard;

import org.univ.sparktp.dashboard.model.Team;


import java.util.ArrayList;
import java.util.List;

// TODO: replace this with persitence
class InMemoryTeams {
  private List<Team> teams;
  private int idSequence;

  InMemoryTeams() {
    this.idSequence = 0;
    this.teams = new ArrayList<>();
    this.addFakeTeams();
  }

  private void addFakeTeams() {
    this.add(new Team("les Michels"));
    this.add(new Team("les Patricks"));
    this.add(new Team("les Daniels"));
  }

  Team add(Team team) {
    team.setId(idSequence);
    this.teams.add(team);
    this.idSequence++;

    return team;
  }

  List<Team> getTeams() {
    return teams;
  }

  Team byId(Integer id) {
    return this.teams.get(id);
  }
}
