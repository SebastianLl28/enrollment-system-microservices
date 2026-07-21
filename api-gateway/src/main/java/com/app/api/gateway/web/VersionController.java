package com.app.api.gateway.web;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class VersionController {

  private final BuildProperties buildProperties;
  private final Instant startedAt = Instant.now();

  @Autowired(required = false)
  private GitProperties gitProperties;

  public VersionController(BuildProperties buildProperties) {
    this.buildProperties = buildProperties;
  }

  @GetMapping("/version")
  public Mono<Map<String, Object>> version() {
    Map<String, Object> info = new HashMap<>();

    if (gitProperties != null) {
      Map<String, Object> commit = new HashMap<>();
      commit.put("id", gitProperties.getShortCommitId());
      commit.put("idFull", gitProperties.getCommitId());
      commit.put("time", gitProperties.getCommitTime());
      commit.put("message", gitProperties.get("commit.message.short"));
      info.put("commit", commit);
      info.put("branch", gitProperties.getBranch());
      info.put("tags", gitProperties.get("tags"));
    }

    Map<String, Object> build = new HashMap<>();
    build.put("time", buildProperties.getTime());
    build.put("version", buildProperties.getVersion());
    info.put("build", build);

    info.put("startedAt", startedAt);

    return Mono.just(info);
  }
}
