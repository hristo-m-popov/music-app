package com.musicapp.service;

import com.musicapp.model.Album;
import com.musicapp.repository.AlbumRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.musicapp.model.Artist;
import com.musicapp.repository.ArtistRepository;

@Service
@RequiredArgsConstructor
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;

    public Page<Album> getAllAlbums(Pageable pageable) {
        return albumRepository.findAll(pageable);
    }

    public Page<Album> getAlbumsByArtist(Long artistId, Pageable pageable) {
        return albumRepository.findByArtist_Id(artistId, pageable);
    }

    public Page<Album> searchAlbums(String title, String recordLabel, Pageable pageable) {
        boolean hasTitle = title != null && !title.isBlank();
        boolean hasLabel = recordLabel != null && !recordLabel.isBlank();

        if (hasTitle && hasLabel) {
            return albumRepository.findByTitleContainingIgnoreCaseAndRecordLabelContainingIgnoreCase(
                    title, recordLabel, pageable);
        } else if (hasTitle) {
            return albumRepository.findByTitleContainingIgnoreCase(title, pageable);
        } else if (hasLabel) {
            return albumRepository.findByRecordLabelContainingIgnoreCase(recordLabel, pageable);
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