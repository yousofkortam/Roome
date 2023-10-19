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
@NoArgsConstructor @AllArgsConstructor
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "first_name")
    @NotEmpty(message = "First name required")
    @NotNull(message = "First name required")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    @NotEmpty(message = "Last name required")
    @NotNull(message = "Last name required")
    private String lastName;

    @NotEmpty(message = "Username required")
    @NotNull(message = "Username required")
    @Column(unique = true)
    private String username;

    @NotEmpty(message = "Email required")
    @NotNull(message = "Email required")
    @Column(unique = true)
    private String email;

    @NotEmpty(message = "Password required")
    @NotNull(message = "Password required")
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "occupation")
    private String occupation;

    @Column(name = "active")
    private boolean active;

    @JsonIgnore
    @OneToMany(mappedBy = "admin")
    private List<Hotel> ManagedHotels;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Reservation> reservations;

    @ManyToMany
    @JoinTable(
            name = "favorites",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "hotel_id")
    )
    private List<Hotel> favorites;

    public void addFavorite(Hotel hotel) {
        if (favorites == null) {
            favorites = new ArrayList<>();
        }
        favorites.add(hotel);
    }

    public void removeFavorite(Hotel hotel) {
        favorites.remove(hotel);
    }

    public void addReservation(Reservation reservation) {
        if (reservations.isEmpty()) {
            reservations = new ArrayList<>();
        }
        reservations.add(reservation);
    }

    public boolean isInFavorite(Hotel hotel) {
        return favorites.contains(hotel);
    }

    public void removeReservation(Reservation reservation) {
        if (reservations.isEmpty()) {
            reservations = new ArrayList<>();
        }
        reservations.remove(reservation);
    }
}
