package com.bookhub.bookhub_back.repository;

import com.bookhub.bookhub_back.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {

}
