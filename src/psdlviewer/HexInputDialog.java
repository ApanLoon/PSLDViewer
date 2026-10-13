package psdlviewer;
import psdlviewer.*;

/** Dialog box allowing input of one single hexadecimal integer.
 *
 */

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import java.awt.Component;

public class HexInputDialog
extends JDialog
implements PropertyChangeListener
{
    protected JOptionPane optionPane;
    protected HexField hexField = new HexField();




    /** Creates a new dialog.
     *
     */
    public HexInputDialog (Component parent,
			   String title,
			   String message)
    {
	super (JOptionPane.getFrameForComponent (parent), title, true);
	optionPane = new JOptionPane (message,
				      JOptionPane.PLAIN_MESSAGE,
				      JOptionPane.OK_CANCEL_OPTION);
	optionPane.add (hexField, 1);
	setContentPane (optionPane);
	optionPane.addPropertyChangeListener (this);
	pack();
    }




    /** @return true if the dialog was accepted, false if it was cancelled.
     *
     */
    public boolean accepted()
    {
	int value = ((Integer)optionPane.getValue()).intValue(); 
	return (value == JOptionPane.OK_OPTION);
    }




    /** @return the integer that was input in the dialog.
     *
     */
    public int getValue()
    {
	return hexField.getValue();
    }







    // PropertyChangeListener methods:
    //
    //
    public void propertyChange (PropertyChangeEvent e)
    {
	String prop = e.getPropertyName();
	if (   isVisible() 
	    && (e.getSource() == optionPane)
	    && (   prop.equals (JOptionPane.VALUE_PROPERTY)
		|| prop.equals (JOptionPane.INPUT_VALUE_PROPERTY)))
	{
	    setVisible (false);
	}
    }

}
