package psdlviewer;
import psdlviewer.*;

/** Main class of the PSDL Viewer program.
 *
 *  The PSDL Viewer program displays terrain maps of the Microsoft game 
 *  Midtown Madness II.
 *
 */

import java.lang.NoClassDefFoundError;
import javax.swing.JOptionPane;

public class PSDLViewer
{
    public static void main (String argv[])
    {
	new Engine (argv);
    }
}

class Engine
{
    public Engine (String argv[])
    {
	try
	{
	    Config.mainWindow = new MainWindow();
	    Config.readOptions();

	    if (argv.length == 1)
	    {
		Config.mainWindow.openFile (argv[0]);
	    }
	    else
	    {
		Config.mainWindow.newPSDL();
	    }
	}
	catch (NoClassDefFoundError e)
	{
	    String s = e.getMessage();
	    if (s.startsWith ("javax/media/j3d"))
	    {
		JOptionPane.showMessageDialog (null,
					       "Can't find Java3D!",
					       "PSDL Viewer: Error",
					       JOptionPane.ERROR_MESSAGE); 
	    }
	    else
	    {
		JOptionPane.showMessageDialog (null,
					       e.toString(),
					       "PSDL Viewer: Error",
					       JOptionPane.ERROR_MESSAGE); 
	    }
	    System.exit (1);
	}
    }
}
