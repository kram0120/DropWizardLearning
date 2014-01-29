package me.chriskramp.library;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.sun.jersey.api.client.GenericType;
import com.yammer.dropwizard.testing.ResourceTest;
import com.yammer.dropwizard.validation.InvalidEntityException;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Fail.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MemberResourceTest extends ResourceTest {

  private MemberDAO memberDAO;
  private List<Member> memberList;
  private Member firstMember;

  @Override
  protected void setUpResources() throws Exception {
    memberList = new ArrayList<Member>();

    firstMember = new Member();
    firstMember.setName("Member1");
    firstMember.setId(1L);
    memberList.add(firstMember);

    Member secondMember = new Member();
    secondMember.setName("Member2");
    secondMember.setId(2L);
    memberList.add(secondMember);

    memberDAO = mock(MemberDAO.class);
    when(memberDAO.getAllMembers()).thenReturn(memberList);

    MemberResource memberResource = new MemberResource(memberDAO);
    addResource(memberResource);
  }

  @Test
  public void getMembers_returnsAllMembers() throws Exception {
    List<Member> result = client().resource("/members").get(new GenericType<List<Member>>() {
    });
    assertThat(result).isEqualTo(memberList);
  }

  @Test
  public void getMemberById_returnsCorrectMember() throws Exception {
    when(memberDAO.findById(firstMember.getId())).thenReturn(firstMember);
    Member result = client().resource("/members/1").get(Member.class);
    assertThat(result).isEqualTo(firstMember);
  }

  @Test
  public void postMemberData_createsNewMember() throws Exception {
    Member newMember = new Member();
    newMember.setName("New Member");
    newMember.setId(4L);
    when(memberDAO.save(newMember)).thenReturn(newMember);

    ObjectMapper mapper = new ObjectMapper();
    String newMemberJson = mapper.writeValueAsString(newMember);
    client().resource("/members/create").type(MediaType.APPLICATION_JSON_TYPE).post(Member.class, newMemberJson);
    verify(memberDAO).save(newMember);

    when(memberDAO.findById(4L)).thenReturn(newMember);
    Member member = client().resource("/members/4").get(Member.class);
    assertThat(member).isEqualTo(newMember);
  }

  @Test
  public void postEmptyMemberName_raisesInvalidEntityException() throws Exception {
    Member invalidMember = new Member();

    ObjectMapper mapper = new ObjectMapper();
    String newMemberJson = mapper.writeValueAsString(invalidMember);

    try {
      client().resource("/members/create").type(MediaType.APPLICATION_JSON_TYPE).post(Member.class, newMemberJson);
      fail("Exception expected");
    } catch (Throwable t) {
      assertThat(t).isInstanceOf(InvalidEntityException.class);
      @SuppressWarnings("ConstantConditions") InvalidEntityException ex = (InvalidEntityException) t;
      ImmutableList<String> response = ex.getErrors();
      assertThat(response.get(0)).isEqualTo("name may not be empty (was null)");
    }
  }

  @Test
  public void deleteMemberById_deletesMember() throws Exception {
    client().resource("/members/1").delete();
    verify(memberDAO).delete(1L);
  }

  @Test
  public void putMember_updatesMember() throws Exception {
    firstMember.setName("MemberOne");
    when(memberDAO.save(firstMember)).thenReturn(firstMember);
    ObjectMapper mapper = new ObjectMapper();
    String updatedMemberJson = mapper.writeValueAsString(firstMember);

    Member updatedMember = client().resource("/members/1").type(MediaType.APPLICATION_JSON_TYPE).put(Member.class, updatedMemberJson);

    assertThat(updatedMember.getName()).isEqualTo("MemberOne");
    verify(memberDAO).save(firstMember);
  }

  @Test
  public void putMemberWithEmptyName_raisesInvalidEntityException() throws Exception {
    firstMember.setName(null);
    ObjectMapper mapper = new ObjectMapper();
    String updatedMemberJson = mapper.writeValueAsString(firstMember);

    try {
      client().resource("/members/1").type(MediaType.APPLICATION_JSON_TYPE).put(Member.class, updatedMemberJson);
      fail("Expected exception");
    } catch (Throwable t) {
      assertThat(t).isInstanceOf(InvalidEntityException.class);
      @SuppressWarnings("ConstantConditions") InvalidEntityException ex = (InvalidEntityException) t;
      assertThat(ex.getErrors().get(0)).isEqualTo("name may not be empty (was null)");
    }
  }
}
