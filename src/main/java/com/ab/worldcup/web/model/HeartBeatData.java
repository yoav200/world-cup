package com.ab.worldcup.web.model;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HeartBeatData {
    LocalDateTime dateTime;
    Boolean valid;
}
