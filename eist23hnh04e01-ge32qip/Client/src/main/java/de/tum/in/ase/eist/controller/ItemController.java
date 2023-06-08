package de.tum.in.ase.eist.controller;

import de.tum.in.ase.eist.model.Item;
import de.tum.in.ase.eist.model.Person;
import de.tum.in.ase.eist.util.ItemType;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ItemController {

    private final WebClient webClient;
    private final List<Item> items;

    public ItemController() {
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8080/")
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.items = new ArrayList<>();
    }

    public void createItem(Item item, Consumer<List<Item>> itemConsumer) {
        // TODO Part 2: Make an http post request to the server
        webClient.post()
                .uri("items")
                .bodyValue(item)
                .retrieve()
                .bodyToMono(Item.class)
                .onErrorStop()
                .subscribe(newItem -> {
                    items.add(newItem);
                    itemConsumer.accept(items);
                });
    }

    public void updateItem(Item updatedItem, String name, Consumer<List<Item>> itemConsumer) {
        // TODO Part 2: Make an http post request to the server
        webClient.put()
                .uri("items/" + name)
                .bodyValue(updatedItem)
                .retrieve()
                .bodyToMono(Item.class)
                .onErrorStop()
                .subscribe(newItem -> {
                    // Update the item in the local list
                    int index = -1;
                    for (int i = 0; i < items.size(); i++) {
                        if (items.get(i).getName().equals(name)) {
                            index = i;
                            break;
                        }
                    }
                    if (index != -1) {
                        items.set(index, newItem);
                        itemConsumer.accept(items);
                    }
                });
        //adapted from ChatGPT-OpenAI
    }

    public void deleteItemByName(String name, Consumer<List<Item>> itemConsumer) {
        // TODO Part 2: Make an http post request to the server
        webClient.delete()
                .uri("items/name/" + name)
                .retrieve()
                .toBodilessEntity()
                .onErrorStop()
                .subscribe(v -> {
                    items.removeIf(item -> item.getName().equals(name));
                    itemConsumer.accept(items);
                });
    }

    public void deleteItemByPrice(String price, Consumer<List<Item>> itemConsumer) {
        // TODO Part 2: Make an http post request to the server
        double tempPrice = Double.parseDouble(price);
        webClient.delete()
                .uri("items/price/" + price)
                .retrieve()
                .toBodilessEntity()
                .onErrorStop()
                .subscribe(v -> {
                    items.removeIf(item -> item.getPrice() == tempPrice);
                    itemConsumer.accept(items);
                });
    }

    public void getAllItems(ItemType itemType, String price, Consumer<List<Item>> itemConsumer) {
        // TODO Part 2: Make an http post request to the server
        webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("items")
                        .queryParam("secret", "SecretKey")
                        .queryParam("itemType", itemType)
                        .queryParam("price", price)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Item>>() {
                })
                .onErrorStop()
                .subscribe(newItems -> {
                    items.clear();
                    items.addAll(newItems);
                    itemConsumer.accept(items);
                });
    }
}
