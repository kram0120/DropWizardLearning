package me.chriskramp.library;

import com.yammer.dropwizard.hibernate.UnitOfWork;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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
  @UnitOfWork
  public List<Member> getAllMembers() {
    return memberDAO.getAllMembers();
  }

  @GET
  @UnitOfWork
  @Path("{id}")
  public Member getMember(@PathParam("id") Long id) {
    return memberDAO.findById(id);
  }

  @POST
  @UnitOfWork
  @Consumes(MediaType.APPLICATION_JSON)
  public Member create(@Valid Member member) {
    return memberDAO.save(member);
  }

  @DELETE
  @UnitOfWork
  @Path("{id}")
  public void destroy(@PathParam("id") Long id) {
    memberDAO.delete(id);
  }

  @PUT
  @UnitOfWork
  @Path("{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  public Member update(@Valid Member updatedMember) {
    return memberDAO.save(updatedMember);
  }
}
