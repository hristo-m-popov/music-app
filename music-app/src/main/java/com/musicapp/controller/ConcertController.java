package com.musicapp.controller;

import com.musicapp.model.Artist;
import com.musicapp.model.Concert;
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
@RequestMapping("/concerts")
@RequiredArgsConstructor
public class ConcertController {

    private final RestTemplate restTemplate;
    private final String API_BASE = "http://localhost:8080/api/concerts";
    private final String ARTIST_API = "http://localhost:8080/api/artists";

    @GetMapping
    public String list(
            @RequestParam(defaultValue = "") String city,
            @RequestParam(defaultValue = "") String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "concertDateTime") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            Model model) {

        String url = API_BASE + "?city=" + city + "&status=" + status +
                "&page=" + page + "&size=" + size +
                "&sortBy=" + sortBy + "&direction=" + direction;

        ResponseEntity<RestPageImpl<Concert>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<RestPageImpl<Concert>>() {}
        );

        ResponseEntity<RestPageImpl<Artist>> artistResponse = restTemplate.exchange(
                ARTIST_API + "?page=0&size=100",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<RestPageImpl<Artist>>() {}
        );

        model.addAttribute("concerts", response.getBody());
        model.addAttribute("city", city);
        model.addAttribute("status", status);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);
        model.addAttribute("artists", artistResponse.getBody().getContent());
        return "concerts/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("concert", new Concert());
        ResponseEntity<RestPageImpl<Artist>> artistResponse = restTemplate.exchange(
                ARTIST_API + "?page=0&size=100",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<RestPageImpl<Artist>>() {}
        );
        model.addAttribute("artists", artistResponse.getBody().getContent());
        return "concerts/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Concert concert = restTemplate.getForObject(API_BASE + "/" + id, Concert.class);
        ResponseEntity<RestPageImpl<Artist>> artistResponse = restTemplate.exchange(
                ARTIST_API + "?page=0&size=100",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<RestPageImpl<Artist>>() {}
        );
        model.addAttribute("concert", concert);
        model.addAttribute("artists", artistResponse.getBody().getContent());
        return "concerts/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Concert concert,
                       BindingResult result, Model model) {
        if (result.hasErrors()) {
            ResponseEntity<RestPageImpl<Artist>> artistResponse = restTemplate.exchange(
                    ARTIST_API + "?page=0&size=100",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<RestPageImpl<Artist>>() {}
            );
            model.addAttribute("artists", artistResponse.getBody().getContent());
            return "concerts/form";
        }

        if (concert.getId() == null) {
            restTemplate.postForObject(API_BASE, concert, Concert.class);
        } else {
            restTemplate.exchange(
                    API_BASE + "/" + concert.getId(),
                    HttpMethod.PUT,
                    new HttpEntity<>(concert),
                    Concert.class
            );
        }

        return "redirect:/concerts";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        restTemplate.delete(API_BASE + "/" + id);
        return "redirect:/concerts";
    }
}