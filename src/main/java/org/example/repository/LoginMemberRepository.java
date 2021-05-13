package org.example.repository;

import org.example.entity.LoginMember;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LoginMemberRepository extends JpaRepository<LoginMember, String> {

	// roleSet: left outer join
	@EntityGraph(attributePaths = { "roleSet" }, type = EntityGraph.EntityGraphType.LOAD)
	@Query("select m from LoginMember m where m.fromSocial = :social and m.username =:username")
	public LoginMember findByUsername(@Param("username") String username, @Param("social") boolean social);
}
