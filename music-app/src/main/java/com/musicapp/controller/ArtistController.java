package com.musicapp.controller;

import com.musicapp.model.Artist;
import com.musicapp.service.ArtistService;
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
@RequestMapping("/artists")
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistService artistService;

    @GetMapping
    public String list(
            @RequestParam(defaultValue = "") String genre,
            @RequestParam(defaultValue = "") String country,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            Model model) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Artist> artists = artistService.searchArtists(genre, country, pageable);

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
        model.addAttribute("artist", artistService.getArtistById(id));
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
        artistService.saveArtist(artist);
        return "redirect:/artists";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        artistService.deleteArtist(id);
        return "redirect:/artists";
    }
}