/**
 * CREATE TABLE online.cinema.users_info
 * (
 * id SERIAL PRIMARY KEY,
 * user_id BIGINT NOT NULL,
 * first_name VARCHAR(128),
 * last_name VARCHAR(128),
 * gender VARCHAR(16),
 * is_subscribed BOOLEAN,
 * registration_date DATE,
 * FOREIGN KEY (user_id) REFERENCES online.cinema.users(id)
 * );
 */

package org.online.cinema.store.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(schema = "online_cinema", name = "users_info")
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "gender")
    private String gender;
    @Column(name = "is_subscribed")
    private boolean isSubscribed;
    @Column(name = "registration_date")
    private Date registrationDate;
}
