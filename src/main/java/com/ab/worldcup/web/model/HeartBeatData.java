package com.ab.worldcup.web.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class HeartBeatData {
    LocalDateTime dateTime;
    Boolean valid;
}
