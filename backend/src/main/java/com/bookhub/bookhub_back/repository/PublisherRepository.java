package com.bookhub.bookhub_back.repository;

import com.bookhub.bookhub_back.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {

    Publisher findByPublisherNameContaining(String keyword);

    Publisher getPublisherByPublisherId(Long publisherId);
}
