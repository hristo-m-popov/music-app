package com.musicapp.controller;

import com.musicapp.model.Artist;
import com.musicapp.model.Concert;
import com.musicapp.service.ConcertService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/concerts")
@RequiredArgsConstructor
public class ConcertApiController {

    private final ConcertService concertService;

    @GetMapping
    public ResponseEntity<Page<Concert>> getAll(
            @RequestParam(defaultValue = "") String city,
            @RequestParam(defaultValue = "") String status,
            @RequestParam(defaultValue = "") String artistName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "concertDateTime") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(concertService.searchConcerts(city, status, artistName, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Concert> getById(@PathVariable Long id) {
        return ResponseEntity.ok(concertService.getConcertById(id));
    }

    @GetMapping("/artist/{artistId}")
    public ResponseEntity<Page<Concert>> getByArtist(
            @PathVariable Long artistId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(concertService.getConcertsByArtist(artistId, pageable));
    }

    @PostMapping
    public ResponseEntity<Concert> create(@Valid @RequestBody Concert concert) {
        return ResponseEntity.ok(concertService.saveConcert(concert));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Concert> update(@PathVariable Long id,
                                          @Valid @RequestBody Concert concert) {
        concert.setId(id);
        return ResponseEntity.ok(concertService.saveConcert(concert));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        concertService.deleteConcert(id);
        return ResponseEntity.noContent().build();
    }
}