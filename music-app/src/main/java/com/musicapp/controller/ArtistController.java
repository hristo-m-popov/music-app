package com.musicapp.controller;

import com.musicapp.model.Artist;
import com.musicapp.model.RestPageImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
@RequestMapping("/artists")
@RequiredArgsConstructor
public class ArtistController {

    private final RestTemplate restTemplate;
    private final String API_BASE = "http://localhost:8080/api/artists";

    @GetMapping
    public String list(
            @RequestParam(defaultValue = "") String genre,
            @RequestParam(defaultValue = "") String country,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            Model model) {

        String url = API_BASE + "?genre=" + genre + "&country=" + country +
                "&page=" + page + "&size=" + size +
                "&sortBy=" + sortBy + "&direction=" + direction;

        org.springframework.http.ResponseEntity<RestPageImpl<Artist>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<RestPageImpl<Artist>>() {}
        );
        Page<Artist> artists = response.getBody();

        model.addAttribute("artists", artists);
        model.addAttribute("genre", genre);
        model.addAttribute("country", country);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);
        return "artists/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("artist", new Artist());
        return "artists/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Artist artist = restTemplate.getForObject(API_BASE + "/" + id, Artist.class);
        model.addAttribute("artist", artist);
        return "artists/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Artist artist,
                       BindingResult result,
                       @RequestParam(value = "active", required = false) String activeParam,
                       Model model) {
        if (result.hasErrors()) {
            return "artists/form";
        }

        artist.setActive(activeParam != null && activeParam.equals("true"));

        if (artist.getId() == null) {
            restTemplate.postForObject(API_BASE, artist, Artist.class);
        } else {
            restTemplate.exchange(
                    API_BASE + "/" + artist.getId(),
                    HttpMethod.PUT,
                    new HttpEntity<>(artist),
                    Artist.class
            );
        }

        return "redirect:/artists";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        restTemplate.delete(API_BASE + "/" + id);
        return "redirect:/artists";
    }
}