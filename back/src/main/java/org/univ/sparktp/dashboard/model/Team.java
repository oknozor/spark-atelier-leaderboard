package org.univ.sparktp.dashboard.model;

import java.io.Serializable;

// TODO : not used yet but we will need it for validation & persistence
public class Team implements Serializable {
  private Integer id;
  private String name;
  private Integer score;

  public Team(String name) {
    this.name = name;
    this.score = 0;
  }

  public Integer getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Integer getScore() {
    return score;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public void nextStep() {
    this.score += 50;
  }
}
