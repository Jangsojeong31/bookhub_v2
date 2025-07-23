package com.bookhub.bookhub_back.repository;

import com.bookhub.bookhub_back.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {

    List<Publisher> findByPublisherNameContaining(String keyword);

    boolean existsByPublisherName(String publisherName);

    boolean existsByPublisherNameAndPublisherIdNot(String publisherName, Long publisherId);
}
