package org.univ.sparktp.dashboard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Team implements Serializable {
  private Integer id;
  private String name;
  private Integer currentStepId;

  public void nextStep() {
    this.currentStepId += 1;
  }
}
