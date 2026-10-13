package psdlviewer;
import psdlviewer.*;

import javax.swing.JPanel;
import javax.swing.JComboBox;

import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.util.Vector;

import java.lang.Class;

public class ViewOptionTab
extends JPanel
implements ActionListener, OptionTab
{
    protected Vector viewOptions = new Vector();

    protected Component optionView;

    protected JComboBox viewList;

    public ViewOptionTab (Vector views)
    {
	if (views == null)
	    return;

	for (int i = 0; i < views.size(); i++)
	{
	    viewOptions.add (ViewOptionPanel.getInstance (views.elementAt (i)));
	}

	GridBagLayout gridBag = new GridBagLayout();
	GridBagConstraints c  = new GridBagConstraints();

	setLayout (gridBag);

	c.anchor = GridBagConstraints.NORTHWEST;


	c.weightx = 0f;
	c.fill = GridBagConstraints.NONE;
	c.gridwidth = GridBagConstraints.REMAINDER;
	viewList = new JComboBox (viewOptions);
	add (viewList, c);

	viewList.addActionListener (this);

	setOptionView();
    }








    protected void setOptionView()
    {
	if (optionView != null)
	{
	    remove (optionView);
	    optionView = null;
	}

	optionView = (Component)viewList.getSelectedItem();

	GridBagConstraints c  = new GridBagConstraints();
	c.weightx = 1f;
	c.weighty = 1f;
	c.fill = GridBagConstraints.BOTH;
	c.gridwidth = GridBagConstraints.REMAINDER;
	c.gridheight = GridBagConstraints.REMAINDER;
	add (optionView == null ? new JPanel()
		                : optionView, c);
//	revalidate();
    }






    // ActionListener method:
    //
    public void actionPerformed (ActionEvent e)
    {
//        JComboBox cb = (JComboBox)e.getSource();
	setOptionView();
    }




    public void apply()
    {
	for (int i = 0; i < viewOptions.size(); i++)
	{
	    ViewOptionPanel p = (ViewOptionPanel)viewOptions.elementAt (i);
	    p.apply();
	}
    }
}
