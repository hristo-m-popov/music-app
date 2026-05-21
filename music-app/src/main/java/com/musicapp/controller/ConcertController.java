package com.musicapp.controller;

import com.musicapp.model.Concert;
import com.musicapp.service.ArtistService;
import com.musicapp.service.ConcertService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/concerts")
@RequiredArgsConstructor
public class ConcertController {

    private final ConcertService concertService;
    private final ArtistService artistService;

    @GetMapping
    public String list(
            @RequestParam(defaultValue = "") String city,
            @RequestParam(defaultValue = "") String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "concertDateTime") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            Model model) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Concert> concerts = concertService.searchConcerts(city, status, pageable);

        model.addAttribute("concerts", concerts);
        model.addAttribute("city", city);
        model.addAttribute("status", status);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);
        model.addAttribute("artists", artistService.getAllArtists(
                PageRequest.of(0, 100)).getContent());
        return "concerts/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("concert", new Concert());
        model.addAttribute("artists", artistService.getAllArtists(
                PageRequest.of(0, 100)).getContent());
        return "concerts/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("concert", concertService.getConcertById(id));
        model.addAttribute("artists", artistService.getAllArtists(
                PageRequest.of(0, 100)).getContent());
        return "concerts/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Concert concert,
                       BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("artists", artistService.getAllArtists(
                    PageRequest.of(0, 100)).getContent());
            return "concerts/form";
        }
        concertService.saveConcert(concert);
        return "redirect:/concerts";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        concertService.deleteConcert(id);
        return "redirect:/concerts";
    }
}