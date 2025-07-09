package com.bookhub.bookhub_back.repository;

import com.bookhub.bookhub_back.entity.StockLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockLogRepository extends JpaRepository<StockLog, Long> {

}
