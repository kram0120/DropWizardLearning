package me.chriskramp.library;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Member {
  private String name;
  private Long id;

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  @Id
  @GeneratedValue
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Member member = (Member) o;

    if (id != null ? !id.equals(member.id) : member.id != null) return false;
    //noinspection RedundantIfStatement
    if (name != null ? !name.equals(member.name) : member.name != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = name != null ? name.hashCode() : 0;
    result = 31 * result + (id != null ? id.hashCode() : 0);
    return result;
  }
}
