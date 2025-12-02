package com.swafy.common.util;

import java.time.Instant;

public final class DateTimeUtils {
    private DateTimeUtils() {}

    public static Instant now() { return Instant.now(); }
}
