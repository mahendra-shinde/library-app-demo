package com.mahendra.library.dao;

import java.util.List;
import java.util.Optional;

import com.mahendra.library.models.Member;

import org.springframework.data.repository.CrudRepository;

public interface MemberDAO extends CrudRepository<Member, Integer> {

	Optional<Member> findById(Integer id);
	List<Member> findByFirstName(String firstName);
	List<Member> findByLastName(String lastName);
	List<Member> findByStatus(char status);
	
}
