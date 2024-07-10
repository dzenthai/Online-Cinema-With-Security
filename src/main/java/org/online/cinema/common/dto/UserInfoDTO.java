package org.online.cinema.common.dto;

import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class UserInfoDTO {
    private String username;
    private String gender;
    private final Date registration_date;
    private boolean is_subscribed;
}
