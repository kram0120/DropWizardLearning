package me.chriskramp.library;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class LibraryConfigurationTest {

  private LibraryConfiguration libraryConfiguration;

  @Before
  public void setUp() throws Exception {
    libraryConfiguration = new LibraryConfiguration();
  }

  @Test
  public void hasDatabaseConfiguration() throws Exception {
    assertThat(libraryConfiguration.getDatabaseConfiguration()).isNotNull();
  }
}
