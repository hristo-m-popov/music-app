package com.musicapp.repository;

import com.musicapp.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {

    Page<Artist> findByGenreContainingIgnoreCase(String genre, Pageable pageable);

    Page<Artist> findByCountryContainingIgnoreCase(String country, Pageable pageable);

    Page<Artist> findByGenreContainingIgnoreCaseAndCountryContainingIgnoreCase(
            String genre, String country, Pageable pageable);


    Page<Artist> findAll(Pageable pageable);
}