package com.griep.postmortem.service.util;

import lombok.NoArgsConstructor;

import java.time.ZoneId;

import static java.time.ZoneId.of;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class Constants {
    public static final int P_TIMELINE = 20;
    public static final int P_IMPACT = 10;
    public static final int P_WHYS = 25;
    public static final int P_ROOT_FACTORS = 15;
    public static final int P_ACTIONS = 20;
    public static final int P_COMMUNICATION = 10;
    public static final ZoneId ZONE_ID = of("America/Sao_Paulo");
}
