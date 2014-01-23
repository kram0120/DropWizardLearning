package me.chriskramp.test_support;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.context.internal.ManagedSessionContext;

public abstract class TestUnitOfWork {
  private SessionFactory sessionFactory;

  public TestUnitOfWork(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  public void execute() {
    Session session = sessionFactory.openSession();
    ManagedSessionContext.bind(session);
    Transaction transaction = session.beginTransaction();

    performTransaction(sessionFactory);

    transaction.commit();
    session.close();
  }

  protected
  abstract void performTransaction(SessionFactory sessionFactory);
}
