package com.app.api.gateway.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;

import java.util.Map;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class VersionControllerTest {

  private static BuildProperties buildProperties() {
    Properties props = new Properties();
    props.setProperty("version", "1.0.0-TEST");
    props.setProperty("time", "2024-01-01T00:00:00Z");
    return new BuildProperties(props);
  }

  @SuppressWarnings("unchecked")
  private static ObjectProvider<GitProperties> providerWith(GitProperties git) {
    ObjectProvider<GitProperties> provider = mock(ObjectProvider.class);
    when(provider.getIfAvailable()).thenReturn(git);
    return provider;
  }

  @Test
  void version_withGitProperties_returnsBuildAndCommitInfo() {
    Properties gitProps = new Properties();
    gitProps.setProperty("commit.id.abbrev", "abc1234");
    gitProps.setProperty("commit.id", "abc1234567890abcdef");
    gitProps.setProperty("commit.time", "2024-01-01T00:00:00Z");
    gitProps.setProperty("commit.message.short", "test commit");
    gitProps.setProperty("branch", "main");
    gitProps.setProperty("tags", "v1.0.0");
    GitProperties git = new GitProperties(gitProps);

    VersionController controller = new VersionController(buildProperties(), providerWith(git));

    @SuppressWarnings("unchecked")
    Map<String, Object> result = (Map<String, Object>) controller.version().block();

    assertThat(result).containsKey("build");
    assertThat(result).containsKey("commit");
    assertThat(result).containsKey("branch");
    assertThat(result).containsKey("startedAt");

    @SuppressWarnings("unchecked")
    Map<String, Object> commit = (Map<String, Object>) result.get("commit");
    assertThat(commit.get("id")).isEqualTo("abc1234");
    assertThat(commit.get("idFull")).isEqualTo("abc1234567890abcdef");
    assertThat(commit.get("message")).isEqualTo("test commit");
    assertThat(result.get("branch")).isEqualTo("main");
  }

  @Test
  void version_withoutGitProperties_returnsOnlyBuildAndStartedAt() {
    VersionController controller = new VersionController(buildProperties(), providerWith(null));

    @SuppressWarnings("unchecked")
    Map<String, Object> result = (Map<String, Object>) controller.version().block();

    assertThat(result).containsKey("build");
    assertThat(result).containsKey("startedAt");
    assertThat(result).doesNotContainKey("commit");
    assertThat(result).doesNotContainKey("branch");
  }
}
