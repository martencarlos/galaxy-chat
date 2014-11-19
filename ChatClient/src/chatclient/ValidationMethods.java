
package chatclient;

import java.util.regex.Pattern;

/**
 *
 * @author Carlos Marten
 */
public class ValidationMethods {

    public static boolean isUserValid(String username) {
        if (Pattern.compile("^[a-zA-Z][\\w_]*$").matcher(username).find()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isMailValid(String mail) {
        if (Pattern.compile("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$").matcher(mail).find()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isPasswordValid(String passwd) {
        if (passwd.length() >=6
                && Pattern.compile("^[\\w_]*$").matcher(passwd).find()
                && Pattern.compile("^[\\w_]*$").matcher(passwd).find()
                && Pattern.compile("^[\\w_]*$").matcher(passwd).find()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isPasswordsEqual(String passwd1, String passwd2) {
        if (passwd1.equalsIgnoreCase(passwd2)) {
            return true;
        } else {
            return false;
        }
    }
    
}
