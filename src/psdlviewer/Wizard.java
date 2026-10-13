package psdlviewer;
import psdlviewer.*;
/** Base class for wizard style dialog interfaces.
 *
 */

import java.util.Vector;

import java.awt.Dimension;

import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JDialog;
import javax.swing.JButton;

public abstract class Wizard
extends JDialog
implements ActionListener
{
    protected int currentStep = 0;
    protected Vector steps = new Vector();

    protected Wizard (Frame parent, String title)
    {
	super (parent, title);
    }


    protected void addNavigation()
    {
	JButton prev;
	JButton close;
	JButton next;

	for (int i = 0; i < steps.size(); i++)
	{
	    WizardStep c = getStep (i);

	    if (i == 0)
	    {
		prev = null;

		close = new JButton ("CLOSE");
		close.setActionCommand ("close");
		close.addActionListener (this);

		next = new JButton (">>");
		next.setActionCommand ("next");
		next.addActionListener (this);

		c.setButtons (prev, close, next);
	    }
	    else if (i == steps.size() - 1)
	    {
		prev = new JButton ("<<");
		prev.setActionCommand ("prev");
		prev.addActionListener (this);

		close = new JButton ("CLOSE");
		close.setActionCommand ("close");
		close.addActionListener (this);

		next = new JButton ("Finnish");
		next.setActionCommand ("finnish");
		next.addActionListener (this);
	    }
	    else
	    {
		prev = new JButton ("<<");
		prev.setActionCommand ("prev");
		prev.addActionListener (this);

		close = new JButton ("CLOSE");
		close.setActionCommand ("close");
		close.addActionListener (this);

		next = new JButton (">>");
		next.setActionCommand ("next");
		next.addActionListener (this);
	    }
	    c.setButtons (prev, close, next);
	}
    }



    protected void setSize()
    {
	Dimension max = new Dimension (0, 0);
	for (int i = 0; i < steps.size(); i++)
	{
	    WizardStep c = getStep (i);
	    Dimension d = c.getPreferredSize();
	    if (d.width > max.width)
	    {
		max.width = d.width;
	    }

	    if (d.height > max.height)
	    {
		max.height = d.height;
	    }
	    System.err.println (d);
	}

	setSize (max);
    }




    protected int setCurrentStep (int i)
    {
	int prev = currentStep;
	currentStep = i;

	getContentPane().remove (getStep (prev));
	getContentPane().add (getStep (currentStep));

	validate();
	repaint();
	return prev;
    }






    protected void addStep (WizardStep s)
    {
	steps.add (s);
    }

    protected WizardStep getStep (int i)
    {
	return (WizardStep)steps.elementAt (i);
    }



    // ActionListener methods:
    //
    //
    public void actionPerformed (ActionEvent e)
    {
	String cmd = e.getActionCommand();
	if ("prev".equals (cmd))
	{
	    getStep (currentStep).prev();
	    setCurrentStep (currentStep - 1);
	    
	}
	else if ("next".equals (cmd))
	{
	    getStep (currentStep).next();
	    setCurrentStep (currentStep + 1);
	}
	else if ("finnish".equals (cmd))
	{
	    finnish();
	    setVisible (false);
	}
	else if ("close".equals (cmd))
	{
	    close();
	    setVisible (false);
	}

    }


    protected abstract void finnish();
    protected abstract void close();

}
