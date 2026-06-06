package com.musicapp.controller;

import com.musicapp.model.Album;
import com.musicapp.model.Artist;
import com.musicapp.model.RestPageImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/albums")
@RequiredArgsConstructor
public class AlbumController {

    private final RestTemplate restTemplate;
    private final String API_BASE = "http://localhost:8080/api/albums";
    private final String ARTIST_API = "http://localhost:8080/api/artists";

    @GetMapping
    public String list(
            @RequestParam(defaultValue = "") String title,
            @RequestParam(defaultValue = "") String recordLabel,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            Model model) {

        String url = API_BASE + "?title=" + title + "&recordLabel=" + recordLabel +
                "&page=" + page + "&size=" + size +
                "&sortBy=" + sortBy + "&direction=" + direction;

        ResponseEntity<RestPageImpl<Album>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<RestPageImpl<Album>>() {}
        );

        ResponseEntity<RestPageImpl<Artist>> artistResponse = restTemplate.exchange(
                ARTIST_API + "?page=0&size=100",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<RestPageImpl<Artist>>() {}
        );

        model.addAttribute("albums", response.getBody());
        model.addAttribute("title", title);
        model.addAttribute("recordLabel", recordLabel);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);
        model.addAttribute("artists", artistResponse.getBody().getContent());
        return "albums/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("album", new Album());
        ResponseEntity<RestPageImpl<Artist>> artistResponse = restTemplate.exchange(
                ARTIST_API + "?page=0&size=100",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<RestPageImpl<Artist>>() {}
        );
        model.addAttribute("artists", artistResponse.getBody().getContent());
        return "albums/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Album album = restTemplate.getForObject(API_BASE + "/" + id, Album.class);
        ResponseEntity<RestPageImpl<Artist>> artistResponse = restTemplate.exchange(
                ARTIST_API + "?page=0&size=100",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<RestPageImpl<Artist>>() {}
        );
        model.addAttribute("album", album);
        model.addAttribute("artists", artistResponse.getBody().getContent());
        return "albums/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Album album,
                       BindingResult result, Model model) {
        if (result.hasErrors()) {
            ResponseEntity<RestPageImpl<Artist>> artistResponse = restTemplate.exchange(
                    ARTIST_API + "?page=0&size=100",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<RestPageImpl<Artist>>() {}
            );
            model.addAttribute("artists", artistResponse.getBody().getContent());
            return "albums/form";
        }

        if (album.getId() == null) {
            restTemplate.postForObject(API_BASE, album, Album.class);
        } else {
            restTemplate.exchange(
                    API_BASE + "/" + album.getId(),
                    HttpMethod.PUT,
                    new HttpEntity<>(album),
                    Album.class
            );
        }

        return "redirect:/albums";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        restTemplate.delete(API_BASE + "/" + id);
        return "redirect:/albums";
    }
}