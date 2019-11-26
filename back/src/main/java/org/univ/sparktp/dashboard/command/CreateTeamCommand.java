package org.univ.sparktp.dashboard.command;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateTeamCommand {
  private String teamName;
}
