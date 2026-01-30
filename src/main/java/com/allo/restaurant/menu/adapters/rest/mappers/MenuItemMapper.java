package com.allo.restaurant.menu.adapters.rest.mappers;

import com.allo.restaurant.menu.adapters.rest.contracts.MenuItemRequest;
import com.allo.restaurant.menu.domain.MenuItem;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface MenuItemMapper {

    MenuItem mapToDomain(MenuItemRequest request);
}
