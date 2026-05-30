package com.musicapp.controller;

import com.musicapp.model.Album;
import com.musicapp.service.AlbumService;
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
@RequestMapping("/albums")
@RequiredArgsConstructor
public class AlbumController {

    private final AlbumService albumService;
    private final ArtistService artistService;

    @GetMapping
    public String list(
            @RequestParam(defaultValue = "") String title,
            @RequestParam(defaultValue = "") String artistName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            Model model) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Album> albums = albumService.searchAlbums(title, artistName, pageable);

        model.addAttribute("albums", albums);
        model.addAttribute("title", title);
        model.addAttribute("artistName", artistName);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);
        model.addAttribute("artists", artistService.getAllArtists(
                PageRequest.of(0, 100)).getContent());
        return "albums/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("album", new Album());
        model.addAttribute("artists", artistService.getAllArtists(
                PageRequest.of(0, 100)).getContent());
        return "albums/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("album", albumService.getAlbumById(id));
        model.addAttribute("artists", artistService.getAllArtists(
                PageRequest.of(0, 100)).getContent());
        return "albums/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Album album,
                       BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("artists", artistService.getAllArtists(
                    PageRequest.of(0, 100)).getContent());
            return "albums/form";
        }
        albumService.saveAlbum(album);
        return "redirect:/albums";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        albumService.deleteAlbum(id);
        return "redirect:/albums";
    }
}