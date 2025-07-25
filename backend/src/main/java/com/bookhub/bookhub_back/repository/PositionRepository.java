package com.bookhub.bookhub_back.repository;

import com.bookhub.bookhub_back.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {

    Optional<Position> findByPositionName(String positionName);

    Position findByAuthority_AuthorityName(String authorityAuthorityName);
}
