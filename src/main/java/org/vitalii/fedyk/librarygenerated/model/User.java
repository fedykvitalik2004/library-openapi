package org.vitalii.fedyk.librarygenerated.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.vitalii.fedyk.librarygenerated.api.dto.FullName;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Accessors(chain = true)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private FullName fullName;
    private String email;
    private String password;
    private LocalDate birthday;
}