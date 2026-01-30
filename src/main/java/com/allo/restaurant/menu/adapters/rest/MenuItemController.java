package com.allo.restaurant.menu.adapters.rest;

import com.allo.restaurant.menu.adapters.rest.mappers.MenuItemMapper;
import com.allo.restaurant.menu.ports.inbound.MenuItemUseCase;
import com.allo.restaurant.menu.adapters.rest.contracts.MenuItemRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MenuItemController {

    private final MenuItemUseCase menuItemUseCase;
    private final MenuItemMapper menuItemMapper;

    public MenuItemController(MenuItemUseCase menuItemUseCase, MenuItemMapper menuItemMapper) {
        this.menuItemUseCase = menuItemUseCase;
        this.menuItemMapper = menuItemMapper;
    }

    @PostMapping("/menu-items")
    public void createMenuItem(@RequestBody MenuItemRequest request) {
        var domain = menuItemMapper.mapToDomain(request);
        menuItemUseCase.createMenuItem(domain);
    }

}
