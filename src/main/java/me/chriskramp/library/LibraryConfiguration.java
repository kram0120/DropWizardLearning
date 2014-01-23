package me.chriskramp.library;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.db.DatabaseConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class LibraryConfiguration extends Configuration {

  @NotNull
  @Valid
  @JsonProperty
  private DatabaseConfiguration databaseConfiguration = new DatabaseConfiguration();

  public DatabaseConfiguration getDatabaseConfiguration() {
    return databaseConfiguration;
  }
}
