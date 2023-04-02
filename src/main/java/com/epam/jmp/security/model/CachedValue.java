package com.epam.jmp.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CachedValue {

    private int attempts;

    private LocalDateTime blockedTimestamp;
}
