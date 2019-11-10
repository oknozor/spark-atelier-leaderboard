package org.univ.sparktp.dashboard.model;

import lombok.Data;


import java.io.Serializable;

@Data
public class Team implements Serializable {
  private Integer id;
  private String name;
  private Integer stepCount;

  public Team(String name) {
    this.name = name;
    this.stepCount = 0;
  }

  public void nextStep() {
    this.stepCount += 1;
  }
}
