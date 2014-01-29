package me.chriskramp.library;

import me.chriskramp.test_support.TestUnitOfWork;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

public class MemberDAOTest {

  private SessionFactory sessionFactory;

  @Before
  public void setup() {
    Configuration configuration = new Configuration();
    configuration.addAnnotatedClass(Member.class);
    configuration.configure();
    ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
    sessionFactory = configuration.buildSessionFactory(serviceRegistry);
  }

  @Test
  public void save_generatesNewUniqueMemberId() throws Exception {
    final Member memberHomer = new Member();
    memberHomer.setName("Homer");

    final Member memberMarge = new Member();
    memberMarge.setName("Marge");

    final Member[] savedHomer = new Member[1];
    final Member[] savedMarge = new Member[2];

    new TestUnitOfWork(sessionFactory) {
      @Override
      public void performTransaction(SessionFactory sessionFactory) {
        MemberDAO dao = new MemberDAO(sessionFactory);
        savedHomer[0] = dao.save(memberHomer);
        savedMarge[0] = dao.save(memberMarge);
      }
    }.execute();

    assertThat(savedHomer[0].getName()).isEqualTo(memberHomer.getName());
    assertThat(savedHomer[0].getId()).isNotZero();
    assertThat(savedHomer[0].getId()).isNotEqualTo(savedMarge[0].getId());
  }

  @Test
  public void findById_returnsSavedMember() throws Exception {
    new TestUnitOfWork(sessionFactory) {
      @Override
      protected void performTransaction(SessionFactory sessionFactory) {
        Member firstMember = new Member();
        firstMember.setName("Member1");
        MemberDAO memberDAO = new MemberDAO(sessionFactory);
        Long id = memberDAO.save(firstMember).getId();
        Member found = memberDAO.findById(id);
        assertThat(found.getId()).isEqualTo(id);
      }
    }.execute();
  }

  @Test
  public void getAllMembers_returnsAllSavedMembers() throws Exception {
    final List<Member> members = new ArrayList<Member>();
    Member member1 = new Member();
    member1.setName("Member 1");
    members.add(member1);

    Member member2 = new Member();
    member2.setName("Member 2");
    members.add(member2);

    Member member3 = new Member();
    member3.setName("Member 3");
    members.add(member3);

    final List<Member> found = new ArrayList<Member>();
    new TestUnitOfWork(sessionFactory) {
      @Override
      protected void performTransaction(SessionFactory sessionFactory) {
        MemberDAO dao = new MemberDAO(sessionFactory);
        dao.save(members.get(0));
        dao.save(members.get(1));
        dao.save(members.get(2));
      }
    }.execute();

    new TestUnitOfWork(sessionFactory) {
      @Override
      protected void performTransaction(SessionFactory sessionFactory) {
        MemberDAO dao = new MemberDAO(sessionFactory);
        found.addAll(dao.getAllMembers());
      }
    }.execute();

    assertThat(found).isEqualTo(members);
  }

  @Test
  public void deleteById_deletesTheMemberRecord() throws Exception {
    new TestUnitOfWork(sessionFactory) {
      @Override
      protected void performTransaction(SessionFactory sessionFactory) {
        MemberDAO dao = new MemberDAO(sessionFactory);
        Member member = new Member();
        member.setName("Member");
        dao.save(member);
      }
    }.execute();

    new TestUnitOfWork(sessionFactory) {
      @Override
      protected void performTransaction(SessionFactory sessionFactory) {
        MemberDAO dao = new MemberDAO(sessionFactory);
        List<Member> allMembers = dao.getAllMembers();
        assertThat(allMembers.size()).isEqualTo(1);

        Member member = allMembers.get(0);
        dao.delete(member.getId());

        List<Member> allMembers1 = dao.getAllMembers();
        assertThat(allMembers1.size()).isZero();
      }
    }.execute();
  }
}
