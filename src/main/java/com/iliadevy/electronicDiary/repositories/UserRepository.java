package com.iliadevy.electronicDiary.repositories;

import com.iliadevy.electronicDiary.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
	Optional<User> findByUsername(String username);
	Optional<User> findByEmail(String email);

	boolean existsByEmail(String email);
	boolean existsByUsername(String username);
}
