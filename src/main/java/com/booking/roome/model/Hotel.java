package com.booking.roome.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data @Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "hotel")
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @NotEmpty(message = "Hotel name required")
    @NotNull(message = "Hotel name required")
    private String name;


    @NotEmpty(message = "Hotel description required")
    @NotNull(message = "Hotel description required")
    private String description;


    @NotEmpty(message = "Hotel location required")
    @NotNull(message = "Hotel location required")
    private String location;

    private Float rate;

    private Float price;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "admin_id")
    private User admin;

    @Column(name = "number_rooms")
    private int numberRooms;

    @ManyToMany
    @JoinTable(
            name = "hotel_facilities",
            joinColumns = @JoinColumn(name = "hotel_id"),
            inverseJoinColumns = @JoinColumn(name = "facility_id")
    )
    private List<Facility> facilities = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "hotel")
    private List<Reservation> reservations = new ArrayList<>();


    public void addFacility(Facility facility) {
        facilities.add(facility);
    }
}
