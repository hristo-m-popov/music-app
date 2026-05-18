package com.musicapp.service;

import com.musicapp.model.Concert;
import com.musicapp.repository.ConcertRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.musicapp.model.Artist;
import com.musicapp.repository.ArtistRepository;

@Service
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertRepository concertRepository;
    private final ArtistRepository artistRepository;

    public Page<Concert> getAllConcerts(Pageable pageable) {
        return concertRepository.findAll(pageable);
    }

    public Page<Concert> getConcertsByArtist(Long artistId, Pageable pageable) {
        return concertRepository.findByArtist_Id(artistId, pageable);
    }

    public Page<Concert> searchConcerts(String city, String status, Pageable pageable) {
        boolean hasCity = city != null && !city.isBlank();
        boolean hasStatus = status != null && !status.isBlank();

        if (hasCity && hasStatus) {
            return concertRepository.findByCityContainingIgnoreCaseAndStatus(
                    city, status, pageable);
        } else if (hasCity) {
            return concertRepository.findByCityContainingIgnoreCase(city, pageable);
        } else if (hasStatus) {
            return concertRepository.findByStatus(status, pageable);
        } else {
            return concertRepository.findAll(pageable);
        }
    }

    public Concert getConcertById(Long id) {
        return concertRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Concert not found with id: " + id));
    }

    public Concert saveConcert(Concert concert) {
        if (concert.getArtist() != null && concert.getArtist().getId() != null) {
            Artist artist = artistRepository.findById(concert.getArtist().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Artist not found"));
            concert.setArtist(artist);
        }
        return concertRepository.save(concert);
    }

    public void deleteConcert(Long id) {
        if (!concertRepository.existsById(id)) {
            throw new EntityNotFoundException("Concert not found with id: " + id);
        }
        concertRepository.deleteById(id);
    }
}