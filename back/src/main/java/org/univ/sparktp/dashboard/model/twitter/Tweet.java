package org.univ.sparktp.dashboard.model.twitter;

import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tweet {
  private Long id;
  private String text;
  private String lang;
  private User user;
  private LocalDateTime createdAt;
  private Long retweetCount;
  private Long favoriteCount;

  public static Tweet fromJson(JsonObject json) {

    JsonObject jsonUser = json.getJsonObject("user");
    Long createdAt = json.getLong("created_at");

    return new Tweet(json.getLong("id"),
                     json.getString("text"),
                     json.getString("lang"),
                     jsonUser == null ? null : User.fromJson(jsonUser),
                     createdAt == null ? null : Instant.ofEpochMilli(createdAt).atZone(ZoneId.systemDefault()).toLocalDateTime(),
                     json.getLong("retweet_count"),
                     json.getLong("favorite_count")
    );
  }
}
