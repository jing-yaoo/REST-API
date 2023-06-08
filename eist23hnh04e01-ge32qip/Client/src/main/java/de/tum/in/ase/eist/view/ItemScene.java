package de.tum.in.ase.eist.view;

import de.tum.in.ase.eist.ClientApplication;
import de.tum.in.ase.eist.controller.ItemController;
import de.tum.in.ase.eist.model.Item;
import de.tum.in.ase.eist.util.ItemType;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Popup;

import java.util.List;

public class ItemScene extends Scene {
    private final ItemController itemController;
    private final ClientApplication clientApplication;
    private ItemType globalItemType;
    private String globalAvgPrice;
    private final ObservableList<Item> itemList;
    private final TableView<Item> table;


    public ItemScene(ItemController controller, ClientApplication application) {
        super(new VBox(), 500, 500);
        this.itemController = controller;
        this.clientApplication = application;
        this.itemList = FXCollections.observableArrayList();

        table = new TableView<>(itemList);
        table.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown()) {
                showPopup(table.getSelectionModel().getSelectedItem());
            }
        });

        var nameColumn = new TableColumn<Item, String>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setSortable(false);
        nameColumn.setPrefWidth(620 / 4D);
        var itemTypeColumn = new TableColumn<Item, String>("Item Type");
        itemTypeColumn.setCellValueFactory(new PropertyValueFactory<>("itemType"));
        itemTypeColumn.setSortable(false);
        itemTypeColumn.setPrefWidth(620 / 4D);
        var priceColumn = new TableColumn<Item, String>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceColumn.setSortable(false);
        priceColumn.setPrefWidth(620 / 4D);
        //noinspection unchecked
        table.getColumns().addAll(nameColumn, itemTypeColumn, priceColumn);

        var vBox = new VBox(10, createSortOptionBox(), table, createButtonBox());
        vBox.setAlignment(Pos.CENTER);
        setRoot(vBox);

        itemController.getAllItems(null, "", this::setItems);
    }

    private HBox createSortOptionBox() {
        var sortFieldChoiceBox = new ChoiceBox<ItemType>();
        sortFieldChoiceBox.getItems().addAll(ItemType.class.getEnumConstants());
        sortFieldChoiceBox.setValue(globalItemType);
        sortFieldChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            globalItemType = newValue;
            if (globalAvgPrice == null || globalAvgPrice.isEmpty()) {
                itemController.getAllItems(globalItemType, "", this::setItems);
            } else {
                itemController.getAllItems(globalItemType, globalAvgPrice, this::setItems);
            }
        });

        var avgPriceTextField = new TextField();
        avgPriceTextField.setPromptText("Sort By Price");
        avgPriceTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            globalAvgPrice = newValue;
            if (globalItemType == null) {
                itemController.getAllItems(null, globalAvgPrice, this::setItems);
            } else {
                itemController.getAllItems(globalItemType, globalAvgPrice, this::setItems);
            }
        });

        var hBox = new HBox(10, sortFieldChoiceBox, avgPriceTextField);
        hBox.setAlignment(Pos.CENTER);
        return hBox;
    }

    private HBox createButtonBox() {
        var backButton = new Button("Back");
        backButton.setOnAction(event -> clientApplication.showHomeScene());

        var addButton = new Button("Add Item");
        addButton.setOnAction(event -> showPopup(null));

        var priceTextField = new TextField();
        priceTextField.setPromptText("Avg Item Price");

        var deleteItemsByPrice = new Button("Delete By Price");
        deleteItemsByPrice.setOnAction(
                event -> itemController.deleteItemByPrice(priceTextField.getText(), this::setItems));

        var refreshButton = new Button("Refresh");
        refreshButton.setOnAction(event -> {
            if (globalItemType == null) {
                if (globalAvgPrice == null || globalAvgPrice.isEmpty()) {
                    itemController.getAllItems(null, "", this::setItems);
                } else {
                    itemController.getAllItems(null, globalAvgPrice, this::setItems);
                }
            } else {
                itemController.getAllItems(globalItemType, globalAvgPrice, this::setItems);
            }
        });

        var buttonBox = new HBox(10, backButton, addButton, priceTextField, deleteItemsByPrice, refreshButton);
        buttonBox.setAlignment(Pos.CENTER);
        return buttonBox;
    }

    private void showPopup(Item item) {
        var popup = new Popup();
        var nameTextField = new TextField();
        nameTextField.setPromptText("Item Name");
        nameTextField.setText(item == null ? "" : item.getName());

        var itemTypeTextField = new TextField();
        itemTypeTextField.setPromptText("Item Type");
        itemTypeTextField.setText(item == null ? "" : item.getItemType().name());

        var priceTextField = new TextField();
        priceTextField.setPromptText("Item Price");
        priceTextField.setText(item == null ? "" : String.valueOf(item.getPrice()));

        var addButton = new Button("Save");
        addButton.setOnAction(event -> {
            var newItem = item != null ? item : new Item(nameTextField.getText(),
                    ItemType.valueOf(itemTypeTextField.getText()),
                    Double.parseDouble(priceTextField.getText()));
            if (item == null) {
                itemController.createItem(newItem, this::setItems);
            } else {
                itemController.updateItem(newItem, item.getName(), this::setItems);
            }
            popup.hide();
        });

        var cancelButton = new Button("Cancel");
        cancelButton.setOnAction(event -> popup.hide());

        var deleteButton = new Button("Delete");
        deleteButton.setTextFill(Color.RED);
        deleteButton.setOnAction(event -> {
            itemController.deleteItemByName(item.getName(), this::setItems);
            popup.hide();
        });

        var hBox = new HBox(10, addButton, cancelButton);
        hBox.setAlignment(Pos.CENTER);
        if (item != null) {
            hBox.getChildren().add(deleteButton);
        }

        var vBox = new VBox(10, nameTextField, itemTypeTextField, priceTextField, hBox);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        vBox.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
        vBox.setPrefWidth(200);
        vBox.setPrefHeight(150);
        vBox.setPadding(new Insets(5));
        popup.getContent().add(vBox);
        popup.show(clientApplication.getStage());
        popup.centerOnScreen();
    }

    private void setItems(List<Item> items) {
        Platform.runLater(() -> itemList.setAll(items));
    }
}
