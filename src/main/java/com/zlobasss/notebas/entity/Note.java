package com.zlobasss.notebas.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity(name = "note")
@Table(name = "note")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Data
@ToString
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String head;
    private String content;
    private Timestamp date;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
}
