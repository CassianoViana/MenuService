package com.allo.restaurant.menu.domain;

import java.math.BigDecimal;

public record MenuItem(
        String id,
        String name,
        String description,
        BigDecimal price
) {
}
