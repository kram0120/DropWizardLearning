package me.chriskramp.library;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/members")
@Produces(MediaType.APPLICATION_JSON)
public class MemberResource {

  private MemberDAO memberDAO;

  public MemberResource(MemberDAO memberDAO) {
    this.memberDAO = memberDAO;
  }

  @GET
  public List<Member> getAllMembers() {
    return memberDAO.getAllMembers();
  }

  @GET
  @Path("{id}")
  public Member getMember(@PathParam("id") Long id) {
    return memberDAO.findById(id);
  }

  @POST
  @Path("/create")
  @Consumes(MediaType.APPLICATION_JSON)
  public Member createMember(Member member) {
    return memberDAO.save(member);
  }
}
