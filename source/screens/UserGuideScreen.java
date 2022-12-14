package screens;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class UserGuideScreen {

	/**
	 * Shows the user guide pop-up.
	 */
	public static void showUserGuide() {

		String pt1 = "<html><body width='";
        String pt2 =
           "'><h1>User Guide</h1>" +
           "<p>Features " +
           "<pre>            1. The top bar : " +
           "<pre>               allows you to drag and drop the selected objects over to your simulation" +
           "<pre>            2. Once an object is in your simulation, you will be allowed to click on it " +
           "<pre>               to change the dimensions" +
           "<pre>            3. To change the position drag the object over to the desired position" +
           "<pre>            4. The next too bar allows you to : play the simulation" +
           "<pre>               or reset your simulation <br>" +
           "<pre>Game Mode " +
           "<pre>            Objective : using the objects try to get the ball to the target " +
           "<pre>Lesson Mode " +
           "<pre>            Click on the different lessons to learn about the basic laws of motion" +
           "<pre>Customized System Mode " +
           "<pre>            Create your own simulations and save it for later " ;


       //creates a JPanel
       JPanel p = new JPanel( new BorderLayout() );

       int width = 700;

       //adds all of the strings together
       String s = pt1 + width + pt2 ;

       //creates a JFrame
       JFrame frame = new JFrame("User Guide");
       
       frame.getContentPane().add(p);
       JOptionPane.showMessageDialog(null, s);

	}
}