/**
 * CREATE TABLE online_cinema.users
 * (
 * id SERIAL PRIMARY KEY,
 * email VARCHAR(64),
 * password VARCHAR,
 * role VARCHAR(128)
 * );
 */

package org.online.cinema.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "online_cinema", name = "users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Pattern(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]" +
            "+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$",
            message = "Email should be in the format 'your_email@gmail.com'," +
            " where 'something' can contain any characters and 'domain' should not contain spaces.")
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String role;
    @Transient
    private boolean enabled = false;
}
