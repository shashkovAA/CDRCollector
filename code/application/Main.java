package application;

//import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

import javax.imageio.ImageIO;

import controller.RootController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import objects.Paths;

public class Main extends Application {
    private Parent root;
    private FXMLLoader myLoader = new FXMLLoader();
    private RootController rootController;
    private Stage primaryStage;
    private TrayIcon trayIcon;

    public static Properties pbxProperties = new Properties();
    public static Properties loggingUserProperties = new Properties();
    public static Properties sqlUserProperties = new Properties();

    public static String colorAvalable = "-fx-background-color:#bfefc1";
    public static String colorNotAvalable = "-fx-background-color:#FFE4E0 ";

    @Override
    public void start(Stage primaryStage) {
	Debug.log.info("Start application.");
	/**
	 * ��������� ���� �����, ����� ���������� �� ����������� ��� ������� ��
	 * ������� ����.
	 */
	Platform.setImplicitExit(false);

	this.primaryStage = primaryStage;
	this.primaryStage.setTitle("W-Тарификатор");
	// this.primaryStage.setAlwaysOnTop(true);
	this.primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("../sources/iconMain32.png")));

	initRootLayout();
	addAppToTray();

	this.primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

	    public void handle(WindowEvent winEvent) {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Выход из программы W-Тарификатор");
		alert.setHeaderText("");
		alert.setContentText("Если вы хотите закрыть приложение, нажмите 'Выход'. \n"
			+ "Для работы приложения в фоновом режиме, нажмите 'Фоновый режим'");

		ButtonType buttonTypeOne = new ButtonType("Выход");
		ButtonType buttonTypeTwo = new ButtonType("Фоновый режим");

		alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonTypeOne) {
		    rootController.handleExit();
		} else if (result.get() == buttonTypeTwo) {
		    hideProgrammToTray(primaryStage);
		    Debug.log.debug(winEvent.getSource() + " " + winEvent.getEventType() + " " + winEvent.getTarget());
		}
	    }
	});

	this.primaryStage.setOnHidden(new EventHandler<WindowEvent>() {
	    public void handle(WindowEvent event) {
		Debug.log.debug(event.getSource() + " " + event.getEventType() + " " + event.getTarget());
	    }
	});

	this.primaryStage.setOnShown(new EventHandler<WindowEvent>() {
	    public void handle(WindowEvent we) {
		Debug.log.debug(we.getSource() + " " + we.getEventType() + " " + we.getTarget());
	    }
	});
    }

    public void hideProgrammToTray(Stage mStage) {
	mStage.hide();
	trayIcon.displayMessage("Я еще работаю!",
			"Если вы хотите закрыть приложение, воспользуйтель меню Приложение -> Выйти.",
		java.awt.TrayIcon.MessageType.INFO);
    }

    private void initRootLayout() {
	try {
	    myLoader.setLocation(getClass().getResource("../view/Root.fxml"));
	    root = myLoader.load();
	    rootController = myLoader.getController();
	    Scene scene = new Scene(root);
	    // scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
	    primaryStage.setScene(scene);
	    primaryStage.setMinWidth(800);
	    primaryStage.setMinHeight(600);
	    primaryStage.show();
	    rootController.init(this);
	} catch (Exception exceptEvent) {
	    Debug.log.error(exceptEvent.getMessage());
	}
    }

    @Override
    public void stop() 
    {

    }

  

    @Override
    public void init() {
	pbxProperties = UserProperties.getPropertiesFromXML(Paths.pbxUserSettingsXMLFilePathName);
	loggingUserProperties = UserProperties.getPropertiesFromXML(Paths.logUserSettingsXMLFilePathName);
	sqlUserProperties = UserProperties.getPropertiesFromXML(Paths.sqlUserSettingsXMLFilePathName);

	WorkDirsInits.checkExistDirectories();
    }

    private void restoreShowStageFromTray() {
	if (primaryStage != null) {
	    primaryStage.show();
	    primaryStage.toFront();
	}
    }

    private void addAppToTray() {
	try {
	    Toolkit.getDefaultToolkit();
	    if (!SystemTray.isSupported()) {
		Debug.log.error("No system tray support, application exiting.");
		Platform.exit();
	    }
	    SystemTray tray = SystemTray.getSystemTray();

	    java.awt.Image image = ImageIO.read(getClass().getResource("../sources/iconMain16.png"));
	    trayIcon = new TrayIcon(image);

	    // if the user double-clicks on the tray icon, show the main app
	    // stage.
	    trayIcon.addActionListener(event -> Platform.runLater(this::restoreShowStageFromTray));

	    // if the user selects the default menu item (which includes the app
	    // name),
	    // show the main app stage.
	    MenuItem openItem = new MenuItem("Открыть");
	    openItem.addActionListener(event -> Platform.runLater(this::restoreShowStageFromTray));

	    // the convention for tray icons seems to be to set the default icon
	    // for opening
	    // the application stage in a bold font.
	    java.awt.Font defaultFont = java.awt.Font.decode(null);
	    java.awt.Font boldFont = defaultFont.deriveFont(java.awt.Font.BOLD);
	    openItem.setFont(boldFont);

	    // to really exit the application, the user must go to the system
	    // tray icon
	    // and select the exit option, this will shutdown JavaFX and remove
	    // the
	    // tray icon (removing the tray icon will also shut down AWT).
	    MenuItem exitItem = new MenuItem("Exit");

	    exitItem.addActionListener(event -> {
		Platform.exit();
		tray.remove(trayIcon);
	    });

	    // setup the popup menu for the application.
	    PopupMenu popup = new PopupMenu();
	    popup.add(openItem);
	    // popup.addSeparator();
	    // popup.add(exitItem);
	    trayIcon.setPopupMenu(popup);
	    trayIcon.setToolTip("W-Тарификатор \r\n Обработано: ");
	    tray.add(trayIcon);
	} catch (java.awt.AWTException | IOException e) {
	    Debug.log.error(e.getMessage());
	}
    }

    public Stage getPrimaryStage() {
	return primaryStage;
    }

    public static void main(String[] args) {
	launch(args);
    }

}
