package com.allo.restaurant.menu.adapters.inbound.rest;

import com.allo.restaurant.menu.annotations.IntegrationTest;
import com.allo.restaurant.menu.helpers.MockHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class MenuItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MockHelper mockHelper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateMenuItemSuccessfully() throws Exception {

        String requestBody = mockHelper.readFile("menu-item-request.json");

        mockMvc.perform(post("/menu-items")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Margherita Pizza"))
                .andExpect(jsonPath("$.description").value("Classic pizza with tomato and mozzarella"))
                .andExpect(jsonPath("$.price").value(12.50))
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    void shouldGetMenuItemByIdSuccessfully() throws Exception {
        String requestBody = mockHelper.readFile("menu-item-request.json");

        var createResult = mockMvc.perform(post("/menu-items")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andReturn();

        String createdResponse = createResult.getResponse().getContentAsString();
        String id = objectMapper.readTree(createdResponse).get("id").asText();

        mockMvc.perform(get("/menu-items/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Margherita Pizza"))
                .andExpect(jsonPath("$.description").value("Classic pizza with tomato and mozzarella"))
                .andExpect(jsonPath("$.price").value(12.50));
    }

    @Test
    void shouldGetMenuItemsWithPaginationSuccessfully() throws Exception {
        for (int i = 0; i < 3; i++) {
            mockMvc.perform(post("/menu-items")
                            .contentType("application/json")
                            .content(mockHelper.readFile("menu-item-request.json")))
                    .andExpect(status().isCreated());
        }

        mockMvc.perform(get("/menu-items")
                        .param("offset", "0")
                        .param("limit", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items.length()").value(2))
                .andExpect(jsonPath("$.totalRecords").value(3));
    }

    @Test
    void shouldUpdateMenuItemSuccessfully() throws Exception {
        String requestBody = mockHelper.readFile("menu-item-request.json");

        var createResult = mockMvc.perform(post("/menu-items")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andReturn();

        String createdResponse = createResult.getResponse().getContentAsString();
        String id = objectMapper.readTree(createdResponse).get("id").asText();

        String updateRequest = """
                {
                    "name": "Updated Margherita Pizza",
                    "description": "Updated classic pizza with tomato and mozzarella",
                    "price": 15.00
                }
                """;

        mockMvc.perform(put("/menu-items/" + id)
                        .contentType("application/json")
                        .content(updateRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Updated Margherita Pizza"))
                .andExpect(jsonPath("$.description").value("Updated classic pizza with tomato and mozzarella"))
                .andExpect(jsonPath("$.price").value(15.00));
    }

    @Test
    void shouldDeleteMenuItemSuccessfully() throws Exception {
        String requestBody = mockHelper.readFile("menu-item-request.json");

        var createResult = mockMvc.perform(post("/menu-items")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andReturn();

        String createdResponse = createResult.getResponse().getContentAsString();
        String id = objectMapper.readTree(createdResponse).get("id").asText();

        mockMvc.perform(delete("/menu-items/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/menu-items/" + id))
                .andExpect(status().isNotFound());
    }

}