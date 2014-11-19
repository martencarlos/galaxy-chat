
package chatclient;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author carlos
 */
public class Common {

    public static void centerFrame(JFrame frame) {
        // Get the size of the screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        // Determine the new location of the window
        int w = frame.getSize().width;
        int h = frame.getSize().height;
        int x = (dim.width - w) / 2;
        int y = (dim.height - h) / 2;

        // Move the window
        frame.setLocation(x, y);
    }

    static void ErrorMessage(String strMsg, Exception ex) {
            System.out.println(strMsg);
            JOptionPane.showConfirmDialog((Component) null, strMsg, "Error", JOptionPane.DEFAULT_OPTION);
            java.util.logging.Logger.getLogger(ChatClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }

    static boolean ConfirmMessage(String strMsg) {
            if ((JOptionPane.showConfirmDialog((Component) null, strMsg, "Peticion", JOptionPane.OK_CANCEL_OPTION)) == JOptionPane.OK_OPTION)
                return true;
            else
                return false;
    }
}
