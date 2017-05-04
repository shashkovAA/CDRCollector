package objects;

import application.Debug;

public class Logging {

    public static void debug(String message) {
	if (Debug.log != null)
	    Debug.log.debug(message);
	else
	    System.out.println(message);
    }

    public static void info(String message) {
	if (Debug.log != null)
	    Debug.log.info(message);
	else
	    System.out.println(message);
    }

    public static void warn(String message) {
	if (Debug.log != null)
	    Debug.log.warn(message);
	else
	    System.out.println(message);
    }

    public static void error(String message) {
	if (Debug.log != null)
	    Debug.log.error(message);
	else
	    System.out.println(message);
    }

    public static void fatal(String message) {
	if (Debug.log != null)
	    Debug.log.fatal(message);
	else
	    System.out.println(message);
    }

}
