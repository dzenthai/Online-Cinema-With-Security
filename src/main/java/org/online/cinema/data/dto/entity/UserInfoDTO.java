package org.online.cinema.data.dto.entity;

import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class UserInfoDTO {
    private String first_name;
    private String last_name;
    private String gender;
    private final Date registration_date;
}
