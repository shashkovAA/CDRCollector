package objects;

import java.lang.reflect.Method;

import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.control.textfield.TextFields;

import javafx.beans.property.ObjectProperty;
import javafx.scene.control.TextField;

public class JavaFXControls {

    /** =Метод для поля ввода теста с возможностью быстрой очистки. */
    public static void setupClearButtonField(CustomTextField customTextField) {
	Method m;
	try {
	    m = TextFields.class.getDeclaredMethod("setupClearButtonField", TextField.class, ObjectProperty.class);
	    m.setAccessible(true);
	    m.invoke(null, customTextField, customTextField.rightProperty());
	} catch (Exception except) {

	    except.printStackTrace();
	}
    }
    // ************************************************************************************************************
    // ===

}
