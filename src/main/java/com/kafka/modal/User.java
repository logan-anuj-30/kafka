package com.kafka.modal;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    private String id;
    private Long longitude;
    private Long latitude;
}
