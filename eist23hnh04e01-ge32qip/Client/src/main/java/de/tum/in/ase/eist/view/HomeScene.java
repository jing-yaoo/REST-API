package de.tum.in.ase.eist.view;

import de.tum.in.ase.eist.ClientApplication;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class HomeScene extends Scene {
    public HomeScene(ClientApplication application) {
        super(new VBox(), 640, 500);

        var noteButton = new Button("Notes");
        noteButton.setOnAction(event -> application.showNoteScene());

        var personButton = new Button("Persons");
        personButton.setOnAction(event -> application.showPersonScene());

        var itemButton = new Button("Items");
        itemButton.setOnAction(event -> application.showItemScene());

        var vBox = new VBox(10, noteButton, personButton, itemButton);
        vBox.setAlignment(Pos.CENTER);
        setRoot(vBox);
    }
}
