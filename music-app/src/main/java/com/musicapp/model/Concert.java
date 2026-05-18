package com.musicapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@Entity
@Table(name = "concerts")
public class Concert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "artist_id", nullable = false)
    @JsonIgnoreProperties({"albums", "concerts"})
    private Artist artist;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String city;

    @NotBlank
    @Size(max = 150)
    @Column(nullable = false, length = 150)
    private String venue;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime concertDateTime;

    @NotNull
    @Column(nullable = false)
    private Double ticketPrice;

    @NotNull
    @Min(1)
    @Column(nullable = false)
    private Integer capacity;

    @NotBlank
    @Size(max = 20)
    @Column(nullable = false, length = 20)
    private String status;
}