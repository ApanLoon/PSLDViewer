package psdlviewer;
import psdlviewer.*;
/** Base class for wizard steps.
 *
 */
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.ImageIcon;

import javax.swing.Box;

import javax.swing.BoxLayout;

public abstract class WizardStep
extends JPanel
{
    protected JLabel iconLabel;
    protected JPanel title = new JPanel();
    protected JPanel content = new JPanel();
    protected JPanel buttons = new JPanel();

    protected WizardStep (String title_, ImageIcon icon)
    {
	setLayout (new BoxLayout (this, BoxLayout.Y_AXIS)); 

	title.setLayout (new BoxLayout (title, BoxLayout.X_AXIS));
	title.add (new JLabel (title_));
	title.add (Box.createHorizontalGlue());
	if (icon != null)
	{
	    iconLabel = new JLabel (icon);
	    title.add (iconLabel);
	}
	add (title);

	add (content);

	buttons.setLayout (new BoxLayout (buttons, BoxLayout.X_AXIS));
	add (buttons);
    }


    protected void setIcon (ImageIcon icon)
    {
	if (iconLabel != null)
	{
	    title.remove (iconLabel);
	}
	if (icon != null)
	{
	    iconLabel = new JLabel (icon);
	    title.add (iconLabel);
	}
	else
	{
	    iconLabel = null;
	}
    }


    public abstract void next();
    public abstract void prev();


    public void setButtons (JButton prev, JButton close, JButton next)
    {
	if (prev != null)
	{
	    buttons.add (prev);
	}
	buttons.add (Box.createHorizontalGlue());
	if (close != null)
	{
	    buttons.add (close);
	}
	buttons.add (Box.createHorizontalGlue());
	if (next != null)
	{
	    buttons.add (next);
	}
	validate();
    }
}

