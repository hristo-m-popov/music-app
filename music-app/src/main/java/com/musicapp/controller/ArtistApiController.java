package com.musicapp.controller;

import com.musicapp.model.Artist;
import com.musicapp.service.ArtistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/artists")
@RequiredArgsConstructor
public class ArtistApiController {

    private final ArtistService artistService;

    @GetMapping
    public ResponseEntity<Page<Artist>> getAll(
            @RequestParam(defaultValue = "") String genre,
            @RequestParam(defaultValue = "") String country,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(artistService.searchArtists(genre, country, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Artist> getById(@PathVariable Long id) {
        return ResponseEntity.ok(artistService.getArtistById(id));
    }

    @PostMapping
    public ResponseEntity<Artist> create(@Valid @RequestBody Artist artist) {
        return ResponseEntity.ok(artistService.saveArtist(artist));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Artist> update(@PathVariable Long id,
                                         @Valid @RequestBody Artist artist) {
        artist.setId(id);
        return ResponseEntity.ok(artistService.saveArtist(artist));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        artistService.deleteArtist(id);
        return ResponseEntity.noContent().build();
    }
}