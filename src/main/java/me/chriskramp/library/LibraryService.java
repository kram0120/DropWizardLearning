package me.chriskramp.library;

import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.db.DatabaseConfiguration;
import com.yammer.dropwizard.hibernate.HibernateBundle;
import com.yammer.dropwizard.migrations.MigrationsBundle;
import org.hibernate.SessionFactory;

public class LibraryService extends Service<LibraryConfiguration> {

  private HibernateBundle<LibraryConfiguration> hibernateBundle = new HibernateBundle<LibraryConfiguration>(Member.class) {
    @Override
    public DatabaseConfiguration getDatabaseConfiguration(LibraryConfiguration configuration) {
      return configuration.getDatabaseConfiguration();
    }
  };

  private MigrationsBundle<LibraryConfiguration> migrationsBundle = new MigrationsBundle<LibraryConfiguration>() {
    @Override
    public DatabaseConfiguration getDatabaseConfiguration(LibraryConfiguration configuration) {
      return configuration.getDatabaseConfiguration();
    }
  };

  public static void main(String[] args) throws Exception {
    new LibraryService().run(args);
  }

  @Override
  public void initialize(Bootstrap<LibraryConfiguration> bootstrap) {
    bootstrap.addBundle(hibernateBundle);
    bootstrap.addBundle(migrationsBundle);
  }

  @Override
  public void run(LibraryConfiguration configuration, Environment environment) throws Exception {
    SessionFactory sessionFactory = hibernateBundle.getSessionFactory();
    MemberDAO memberDAO = new MemberDAO(sessionFactory);
    MemberResource memberResource = new MemberResource(memberDAO);
    environment.addResource(memberResource);
  }
}
