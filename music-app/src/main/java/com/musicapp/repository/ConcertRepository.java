package com.musicapp.repository;

import com.musicapp.model.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ConcertRepository extends JpaRepository<Concert, Long> {

    Page<Concert> findByArtist_Id(Long artistId, Pageable pageable);

    Page<Concert> findByCityContainingIgnoreCase(String city, Pageable pageable);

    Page<Concert> findByStatus(String status, Pageable pageable);

    Page<Concert> findByCityContainingIgnoreCaseAndStatus(
            String city, String status, Pageable pageable);
}