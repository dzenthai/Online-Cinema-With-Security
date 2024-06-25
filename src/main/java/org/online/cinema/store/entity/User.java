/**
 * CREATE TABLE online_cinema.users
 * (
 * id SERIAL PRIMARY KEY,
 * email VARCHAR(64),
 * password VARCHAR,
 * role VARCHAR(128)
 * );
 */

package org.online.cinema.store.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "online_cinema", name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //    @Max(value = 64, message = "Email must contain no more than 64 characters.")
//    @Min(value = 6, message = "An email must consist of at least 6 characters.")
//    @Pattern(regexp = "^(.+)@(\\S+)$", message = "Email should be in the format 'your_email@gmail.com'," +
//            " where 'something' can contain any characters and 'domain' should not contain spaces.")
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String role;
    @Transient
    private boolean enabled = false;
}
