package psdlviewer;
import psdlviewer.*;


import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;



public class BlockSearch
extends JDialog
implements ActionListener
{
    protected BlockListView parent;
    protected JTextField attributes = new JTextField();
    protected JTextField textures   = new JTextField();


    public BlockSearch (BlockListView parent_)
    {
	super (JOptionPane.getFrameForComponent (parent_), "Search for blocks", false);
	parent = parent_;

	JPanel pane = new JPanel();

	GridBagLayout gridBag = new GridBagLayout();
	pane.setLayout (gridBag);

	GridBagConstraints labelc  = new GridBagConstraints();
	GridBagConstraints valuec  = new GridBagConstraints();

	labelc.anchor = GridBagConstraints.NORTHWEST;
	labelc.gridwidth = GridBagConstraints.RELATIVE;
	labelc.weightx = 0;
	labelc.fill = GridBagConstraints.NONE;

	valuec.anchor = GridBagConstraints.NORTHWEST;
	valuec.gridwidth = GridBagConstraints.REMAINDER;
	valuec.weightx = 1;
	valuec.fill = GridBagConstraints.HORIZONTAL;

	pane.add (new JLabel ("Attribute"), labelc);
	pane.add (attributes, valuec);

	pane.add (new JLabel ("Texture"), labelc);
	pane.add (textures, valuec);




	JButton button;

	labelc.fill = GridBagConstraints.NONE;
	labelc.anchor = GridBagConstraints.SOUTHEAST;
	labelc.anchor = GridBagConstraints.SOUTHEAST;
	labelc.weightx = 0;
	labelc.weighty = 0;
	labelc.gridwidth = 1;
	labelc.gridheight = GridBagConstraints.REMAINDER;

	button = new JButton ("Find");
	button.addActionListener (this);
	pane.add (button, labelc);

	button = new JButton ("Find next");
	button.addActionListener (this);
	labelc.gridwidth = GridBagConstraints.RELATIVE;
	pane.add (button, labelc);

	button = new JButton ("Close");
	button.addActionListener (this);
	labelc.gridwidth = GridBagConstraints.REMAINDER;
	pane.add (button, labelc);

	setContentPane (pane);
	pack();
    }









    // ActionListener methods:
    //
    //
    public void actionPerformed (ActionEvent e)
    {
	String cmd = e.getActionCommand();

	if ("Find".equals (cmd))
	{
	    selectNext (true);
	}
	if ("Find next".equals (cmd))
	{
	    selectNext (false);
	}
	else if ("Close".equals (cmd))
	{
	    setVisible (false);
	}
    }





    protected void selectNext (boolean fromStart)
    {
	int i = 0;
	if (fromStart)
	{
	    i = 0;
	}
	else
	{
	    int[] sel = parent.getSelectedIndices();
	    i = sel[sel.length - 1] + 1;
	}

	BlockList bl = Config.getBlockList();
	Block block = null;
	boolean found = false;
	while (i < bl.size() && !found)
	{
	    block = bl.blockAt (i);
	    BlockAttributeList al = block.getAttributes();
	    int a = 0;
	    while (a < al.size() && !found)
	    {
		if (match (al.attributeAt (a)))
		{
		    found = true;
		}
		a++;
	    }
	    i++;
	}
	if (found)
	{
	    Object[] l = new Block[1];
	    l[0] = block;
	    parent.select (l);
	}
	else
	{
	    JOptionPane.showMessageDialog (this, "No match found.");
	}
    }




    protected boolean match (BlockAttribute a)
    {
	boolean match = false;

	// TODO: Innefficient to convert string to int here...

	int id = a.getID();

	String aExpr = attributes.getText().trim();
	if (aExpr.length() != 0)
	{
	    match = Integer.parseInt (aExpr, 16) == id;
	}


	String tExpr = textures.getText().trim();
	if (tExpr.length() != 0)
	{
	    if (   id == BlockAttribute.TEXTURE_ATTRIBUTE
		|| id == BlockAttribute.ATTRIBUTE_0051)
	    {
		TextureAttribute ta = (TextureAttribute)a;
		match |= Integer.parseInt (tExpr, 16) == ta.ref.getIndex();
	    }
	}

	return match;
    }


}
