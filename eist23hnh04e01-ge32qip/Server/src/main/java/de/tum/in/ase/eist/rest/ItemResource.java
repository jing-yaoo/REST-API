package de.tum.in.ase.eist.rest;

import de.tum.in.ase.eist.model.Item;
import de.tum.in.ase.eist.service.ItemService;
import de.tum.in.ase.eist.util.ItemType;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
public class ItemResource {
    private final ItemService itemService;

    public ItemResource(ItemService itemService) {
        this.itemService = itemService;
    }

    // TODO Part 1: Implement the specified endpoints here
    @PostMapping("items")
    public ResponseEntity<Item> createItem(@RequestBody Item item) {
        if (item.getName() == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(itemService.createItem(item));
        }
    }

    @PutMapping("items/{itemName}")
    public ResponseEntity<Item> updateItem(@RequestBody Item updatedItem,
                                           @PathVariable("itemName") String name) {
        // if the name of the updatedItem does not match the name of the item they want to replace it with
        if (!updatedItem.getName().equals(name)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(itemService.updateItem(updatedItem, name));
    }

    @DeleteMapping("items/name/{itemName}")
    public ResponseEntity<Void> deleteItemByName(@PathVariable("itemName") String itemName) {
        itemService.deleteItemByName(itemName);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("items/price/{itemPrice}")
    public ResponseEntity<Void> deleteItemByPrice(@PathVariable("itemPrice") double itemPrice) {
        itemService.deleteItemsByPrice(itemPrice);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("items")
    public ResponseEntity<List<Item>> getAllNotes(@RequestParam("secret") String secret,
                                                  @RequestParam("itemType") ItemType itemType,
                                                  @RequestParam("price") double price
    ) {
        if (!"SecretKey".equals(secret)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(itemService.getAllItems(itemType, price));
    }
}
