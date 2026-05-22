package com.musicapp.service;

import com.musicapp.model.Album;
import com.musicapp.repository.AlbumRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlbumService {

    private final AlbumRepository albumRepository;

    public Page<Album> getAllAlbums(Pageable pageable) {
        return albumRepository.findAll(pageable);
    }

    public Page<Album> getAlbumsByArtist(Long artistId, Pageable pageable) {
        return albumRepository.findByArtist_Id(artistId, pageable);
    }

    public Page<Album> searchAlbums(String title, String artistName, Pageable pageable) {
        boolean hasTitle = title != null && !title.isBlank();
        boolean hasName = artistName != null && !artistName.isBlank();

        if (hasTitle && hasName) {
            return albumRepository.findByTitleContainingIgnoreCaseAndArtist_NameContainingIgnoreCase(
                    title, artistName, pageable);
        } else if (hasTitle) {
            return albumRepository.findByTitleContainingIgnoreCase(title, pageable);
        } else if (hasName) {
            return albumRepository.findByArtist_NameContainingIgnoreCase(artistName, pageable);
        } else {
            return albumRepository.findAll(pageable);
        }
    }

    public Album getAlbumById(Long id) {
        return albumRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Album not found with id: " + id));
    }

    public Album saveAlbum(Album album) {
        return albumRepository.save(album);
    }

    public void deleteAlbum(Long id) {
        if (!albumRepository.existsById(id)) {
            throw new EntityNotFoundException("Album not found with id: " + id);
        }
        albumRepository.deleteById(id);
    }
}