package controller;

import java.util.Iterator;
import java.util.Properties;

import org.controlsfx.control.ToggleSwitch;

import application.Debug;
import application.Main;
import application.TextAreaAppender;
import application.UserProperties;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import objects.GlobalVars;
import objects.Paths;

public class ConsoleBarController {
	
	/*@FXML
	private AnchorPane cBar;*/
	@FXML
	private TextArea txtConsole;
	@FXML
	private ToggleSwitch tglOnOffLogging;
	@FXML
	private Button btnClearLogConsole;
	@FXML
	private ComboBox<String> cmbLogLevel;
	
	private Properties loggingUserProperties = new Properties(); 	
	
	private RootController rootController;
	
	
//************************************************************************************************************
//===	
	
	@FXML public void init(RootController rootController)
	{
		this.rootController = rootController;
		
	}
//************************************************************************************************************
//===
	@FXML
	public void initialize()
	{
	
		boolean existUserSettingsForAppConsoleLogLevelflag = false;
	
		TextAreaAppender.setTextArea(txtConsole);
		
		cmbLogLevel.getItems().addAll("Fatal", "Error", "Warning","Info","Debug");
		
		Iterator it = Main.loggingUserProperties.keySet().iterator();
		
		while (it.hasNext())
		{
			String key = it.next().toString();
			
			if (key.equals(GlobalVars.logLevelAppConsole))
			{
				System.setProperty("logLevelAppConsole", Main.loggingUserProperties.getProperty(key));
				
				existUserSettingsForAppConsoleLogLevelflag = true;
				
				switch (Main.loggingUserProperties.getProperty(key))
				{
				case "fatal"	: 
					cmbLogLevel.getSelectionModel().select(0);
					break;
					
				case "error"	: 
					cmbLogLevel.getSelectionModel().select(1);
					break;
					
				case "warn"	: 
					cmbLogLevel.getSelectionModel().select(2);
					break;
					
				case "info"	: 
					cmbLogLevel.getSelectionModel().select(3);
					break;
					
				case "debug"	: 
					cmbLogLevel.getSelectionModel().select(4);
					break;
 				
				}
							
			}
		}
			
		if (!existUserSettingsForAppConsoleLogLevelflag)  
			
			cmbLogLevel.getSelectionModel().select(0);
		
		checkComboBox();
		
				
	}
	
//************************************************************************************************************
//===

//************************************************************************************************************
//===
	@FXML
	void clearLogConsole(ActionEvent event)
	{
		txtConsole.setText("");
			
	}
//************************************************************************************************************
//=== Проверка и подсветка checkbox-а выбора протокола. Если значение в нем нет, то красный. Если есть - то зеленый.
		
	private void checkComboBox()
	{
		if (cmbLogLevel.getSelectionModel().getSelectedItem().isEmpty())
		{
			cmbLogLevel.setStyle(Main.colorNotAvalable);
		}

		else
		{
			cmbLogLevel.setStyle(Main.colorAvalable);
		}

	}
//************************************************************************************************************
//===
	@FXML
	private void setConsoleLogingLevel(ActionEvent event)
	{
		Debug.setLogLevelAppConsole(cmbLogLevel.getSelectionModel().getSelectedItem());
	}
	
//************************************************************************************************************
//===
	
	public void saveLoggingSettings()
	{
		loggingUserProperties.setProperty(GlobalVars.logFileLevel, Debug.getLogFileLevel());
		loggingUserProperties.setProperty(GlobalVars.logFileSize, Debug.getLogFileSize());
		loggingUserProperties.setProperty(GlobalVars.logFileCount, Debug.getLogFileCount());
		loggingUserProperties.setProperty(GlobalVars.logLevelConsole, Debug.getLogLevelConsole());
		loggingUserProperties.setProperty(GlobalVars.logLevelAppConsole, Debug.getLogLevelAppConsole());
		
		UserProperties.savePropertiesXML(Paths.logUserSettingsXMLFilePathName, loggingUserProperties, "Custom logging properties");
		Debug.log.info("Save logging settings to file  " + Paths.viewOnlyLogUserSettingsXMLFilePathName);
	}
}
