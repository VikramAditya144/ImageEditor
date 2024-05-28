package application.imageeditor;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ExitDialog {

	static String action = "Yes";
	
	public String showDialog(String message) {
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Are you sure?");
		window.setMinWidth(250);
		
		Label label = new Label(message);
		Button yesButton = new Button("Yes");
		yesButton.setOnAction(e -> {action = "Yes"; window.close();});
		Button noButton = new Button("No");
		noButton.setOnAction(e -> {action = "No"; window.close();});
		Button cancelButton = new Button("Cancel");
		cancelButton.setOnAction(e -> {action = "Cancel"; window.close();});
		
		HBox hbox = new HBox(10);
		hbox.getChildren().addAll(yesButton, noButton, cancelButton);
		hbox.setAlignment(Pos.CENTER);
		VBox vbox = new VBox(10);
		vbox.getChildren().addAll(label, hbox);
		window.setScene(new Scene(vbox, 200, 100));
		window.showAndWait();
		
		return action;
	}
	
}
