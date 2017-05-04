package objects;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Dialogs {

    public static void showInfoDialog(String title, String headerText, String contenText) {
	Alert alert = new Alert(AlertType.INFORMATION);
	alert.setTitle(title);
	alert.setHeaderText(headerText);
	alert.setContentText(contenText);

	alert.showAndWait();
    }

    public static void showErrorDialog(String title, String headerText, String contenText) {
	Alert alert = new Alert(AlertType.ERROR);
	alert.setTitle(title);
	alert.setHeaderText(headerText);
	alert.setContentText(contenText);

	alert.showAndWait();
    }

    public static void showWarnDialog(String title, String headerText, String contenText) {
	Alert alert = new Alert(AlertType.WARNING);
	alert.setTitle(title);
	alert.setHeaderText(headerText);
	alert.setContentText(contenText);

	alert.showAndWait();
    }
}
