package org.univ.sparktp.dashboard.model.twitter;

import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
class User {
  private Long id;
  private String name;
  private String location;
  private String screenNAme;
  private Long followerCount;

  static User fromJson(JsonObject json) {
    return new User(
      json.getLong("id"),
      json.getString("name"),
      json.getString("location"),
      json.getString("screen_name"),
      json.getLong("follower_count")
    );
  }
}
