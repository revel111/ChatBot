package com.chatbot.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class Chat {

    @Id
    @UuidGenerator
    @Column(nullable = false)
    @EqualsAndHashCode.Include
    @ToString.Include
    private UUID id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "chat", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Message> messages = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    private UserProfile userProfile;

    @CreationTimestamp
    @Column(columnDefinition = "TIMESTAMP", nullable = false, updatable = false)
    private Instant createdAt;

}
