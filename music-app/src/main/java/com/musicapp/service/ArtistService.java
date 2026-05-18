package com.musicapp.service;

import com.musicapp.model.Artist;
import com.musicapp.repository.ArtistRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArtistService {

    private final ArtistRepository artistRepository;

    public Page<Artist> getAllArtists(Pageable pageable) {
        return artistRepository.findAll(pageable);
    }

    public Page<Artist> searchArtists(String genre, String country, Pageable pageable) {
        boolean hasGenre = genre != null && !genre.isBlank();
        boolean hasCountry = country != null && !country.isBlank();

        if (hasGenre && hasCountry) {
            return artistRepository.findByGenreContainingIgnoreCaseAndCountryContainingIgnoreCase(
                    genre, country, pageable);
        } else if (hasGenre) {
            return artistRepository.findByGenreContainingIgnoreCase(genre, pageable);
        } else if (hasCountry) {
            return artistRepository.findByCountryContainingIgnoreCase(country, pageable);
        } else {
            return artistRepository.findAll(pageable);
        }
    }

    public Artist getArtistById(Long id) {
        return artistRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Artist not found with id: " + id));
    }

    public Artist saveArtist(Artist artist) {
        return artistRepository.save(artist);
    }

    public void deleteArtist(Long id) {
        if (!artistRepository.existsById(id)) {
            throw new EntityNotFoundException("Artist not found with id: " + id);
        }
        artistRepository.deleteById(id);
    }
}