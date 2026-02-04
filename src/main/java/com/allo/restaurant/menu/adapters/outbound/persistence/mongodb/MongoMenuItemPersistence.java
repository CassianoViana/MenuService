package com.allo.restaurant.menu.adapters.outbound.persistence.mongodb;

import com.allo.restaurant.menu.adapters.outbound.persistence.mappers.MenuItemModelMapper;
import com.allo.restaurant.menu.adapters.outbound.persistence.mongodb.model.MenuItemModel;
import com.allo.restaurant.menu.domain.MenuItem;
import com.allo.restaurant.menu.domain.PaginatedResult;
import com.allo.restaurant.menu.domain.PaginationRequest;
import com.allo.restaurant.menu.exceptions.NotFoundException;
import com.allo.restaurant.menu.ports.outbound.MenuItemPersistence;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

import static java.time.LocalDateTime.now;

@Repository
@RequiredArgsConstructor
public class MongoMenuItemPersistence implements MenuItemPersistence {

    private final MongoMenuItemRepository mongoMenuItemRepository;
    private final MongoTemplate mongoTemplate;
    private final MenuItemModelMapper mapper;

    @Override
    public MenuItem save(MenuItem menuItem) {
        var model = mapper.toModel(menuItem.withCreatedAt(now()));
        return mapper.fromModelToDomain(mongoTemplate.save(model));
    }

    @Override
    public MenuItem update(MenuItem menuItem) throws NotFoundException {
        var model = mongoTemplate.findById(menuItem.id(), MenuItemModel.class);
        if (model == null) {
            throw new NotFoundException("Menu item not found");
        }
        model = mapper.toModel(menuItem.withUpdatedAt(now())
                .withCreatedAt(model.createdAt())
                .withId(menuItem.id()));
        return mapper.fromModelToDomain(mongoTemplate.save(model));
    }

    @Override
    public void delete(String id) {
        mongoMenuItemRepository.deleteById(id);
    }

    @Override
    public MenuItem findById(String id) {
        return mongoMenuItemRepository.findById(id)
                .map(mapper::fromModelToDomain)
                .orElseThrow(() -> new NotFoundException("Menu item not found"));
    }

    @Override
    public PaginatedResult getMenuItems(PaginationRequest paginationRequest) {
        List<MenuItemModel> allItems = mongoMenuItemRepository.findAll();
        long totalRecords = allItems.size();

        List<MenuItem> paginatedItems = allItems.stream()
                .skip(paginationRequest.offset())
                .limit(paginationRequest.size())
                .map(mapper::fromModelToDomain)
                .toList();

        return new PaginatedResult(paginatedItems, totalRecords);
    }

}
