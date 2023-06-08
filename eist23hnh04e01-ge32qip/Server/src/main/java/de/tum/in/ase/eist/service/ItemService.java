package de.tum.in.ase.eist.service;

import de.tum.in.ase.eist.model.Item;
import de.tum.in.ase.eist.util.ItemType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemService {
    // Do not change this
    private final List<Item> items;

    @Autowired
    public ItemService() {
        this.items = new ArrayList<>();
    }

    public Item createItem(Item newItem) {
        // TODO Part 1: Implement creation of an item.
        // find matches of item
        var optionalItem = items.stream().filter(x -> x.getName().equals(newItem.getName())).findFirst();
        //empty means its a new item
        if (optionalItem.isEmpty()) {
            items.add(newItem);
            return newItem;
        } else {
            var existingItem = optionalItem.get();
            existingItem.setName(newItem.getName());
            existingItem.setItemType(newItem.getItemType());
            return existingItem;
        }
    }

    public Item updateItem(Item updatedItem, String name) {
        // TODO Part 1: Implement update of an item.
        //check if name of old item is the same as updated
        if (items.stream().filter(item -> item.getName().equals(name)).toList().isEmpty()) {
            //the item does not exist
            return null;
        } else {
            deleteItemByName(name);
            items.add(updatedItem);
            return updatedItem;
        }
    }

    public void deleteItemByName(String name) {
        // TODO Part 1: Implement deletion of an item according to the name.
        items.removeIf(item -> item.getName().equals(name));
    }

    public void deleteItemsByPrice(double price) {
        // TODO Part 1: Implement deletion of an item according to the price.
        items.removeIf(item -> item.getPrice() == price);
    }

    public List<Item> getAllItems(ItemType itemType, double price) {
        // TODO Part 1: Implement retrieving of all items according to method parameters.
        // TODO Part 3: Add sorting here
        //return all items that match method parameters
        List<Item> tempList;
        tempList = items.stream().filter(x -> x.getItemType().equals(itemType)).filter(x -> x.getPrice() == price).collect(Collectors.toList());
        return tempList;
    }
}
