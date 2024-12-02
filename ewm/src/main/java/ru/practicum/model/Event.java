package ru.practicum.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "events", schema = "public")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    @NotNull
    @Column(name = "annotation")
    private String annotation;

    @NotNull
    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "title")
    private String title;

    @Column(name = "state")
    private String state;

    @NotNull
    @Column(name = "created_on")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    @NotNull
    @Column(name = "event_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @Column(name = "published_on")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;

    @NotNull
    @Column(name = "paid")
    private Boolean paid;

    @NotNull
    @Column(name = "request_moderation")
    private Boolean requestModeration;

    @NotNull
    @Column(name = "participant_limit")
    private Integer participantLimit;

    @Column(name = "views")
    private Long views;

    @Column(name = "confirmed_requests")
    private Integer confirmedRequests;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "location", nullable = false)
    private Location location;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator", nullable = false)
    private User initiator;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "category", nullable = false)
    private Category category;
}
