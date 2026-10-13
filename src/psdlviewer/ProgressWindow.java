package psdlviewer;
import psdlviewer.*;

/** Pop up window showing a progress bar.
 *
 */

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JPanel;
import javax.swing.JOptionPane;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Insets;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;



public class ProgressWindow
extends JDialog
{
    protected JLabel       message = new JLabel();
    protected JLabel       note    = new JLabel();
    protected JProgressBar bar     = new JProgressBar();



    /** Creates a new dialog.
     *
     */

    public ProgressWindow (Component parent, String message_)
    {
	super (JOptionPane.getFrameForComponent (parent), "Working", false);

	int width  = 300;
	int height = 110;
	Rectangle r = parent.getBounds();
	setBounds (r.x + (r.width  - width)  / 2,
		   r.y + (r.height - height) / 2,
		   width,
		   height);


	message.setText (message_);

	JPanel panel = new JPanel();

	GridBagLayout gridBag = new GridBagLayout();
	GridBagConstraints c = new GridBagConstraints();

	c.insets = new Insets (5, 5, 5, 5);

	panel.setLayout (gridBag);


	// Message:
	c.anchor = GridBagConstraints.NORTHWEST;
	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 1;
	c.weighty = 0;
	c.gridwidth = GridBagConstraints.REMAINDER;
	c.gridheight = 1;
	panel.add (message, c);

	// Note:
	c.anchor = GridBagConstraints.NORTHWEST;
	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 1;
	c.weighty = 0;
	c.gridwidth = GridBagConstraints.REMAINDER;
	c.gridheight = GridBagConstraints.RELATIVE;
	panel.add (note, c);

	// Bar:
	c.anchor = GridBagConstraints.NORTHWEST;
	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 1;
	c.weighty = 1;
	c.gridwidth = GridBagConstraints.REMAINDER;
	c.gridheight = GridBagConstraints.REMAINDER;
	panel.add (bar, c);

	setContentPane (panel);
    }


    public void setMinimum (int i)
    {
	bar.setMinimum (i);
    }

    public void setMaximum (int i)
    {
	bar.setMaximum (i);
    }

    public void setProgress (int i)
    {
	bar.setValue (i);
    }

    public void setNote (String s)
    {
	note.setText (s);
    }

    public void setMessage (String s)
    {
	message.setText (s);
    }

    public void open()
    {
	setVisible (true);
    }

    public void close()
    {
	setVisible (false);
    }
}
