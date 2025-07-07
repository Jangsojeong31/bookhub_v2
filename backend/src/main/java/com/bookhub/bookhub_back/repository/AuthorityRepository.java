package com.bookhub.bookhub_back.repository;

import com.bookhub.bookhub_back.entity.Authority;
import com.bookhub.bookhub_back.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    Optional<Authority> findByAuthorityName(String staff);
}
