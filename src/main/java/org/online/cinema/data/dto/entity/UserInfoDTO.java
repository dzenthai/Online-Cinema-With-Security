package org.online.cinema.data.dto.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDTO {
    private String first_name;
    private String last_name;
    private String gender;
    private boolean is_subscribed;
    private Date registration_date;
}
