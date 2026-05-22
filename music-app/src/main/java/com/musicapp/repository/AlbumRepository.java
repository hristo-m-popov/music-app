package com.musicapp.repository;

import com.musicapp.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

    Page<Album> findByArtist_Id(Long artistId, Pageable pageable);

    Page<Album> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    Page<Album> findByArtist_NameContainingIgnoreCase(String artistName, Pageable pageable);

    Page<Album> findByTitleContainingIgnoreCaseAndArtist_NameContainingIgnoreCase(
            String title, String recordLabel, Pageable pageable);

}