package me.chriskramp.library;

import com.yammer.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;

public class MemberDAO extends AbstractDAO<Member> {

  public MemberDAO(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  public List<Member> getAllMembers() {
    return list(currentSession().createCriteria(Member.class));
  }

  public Member save(Member member) {
    return persist(member);
  }

  public Member findById(Long id) {
    return get(id);
  }
}
