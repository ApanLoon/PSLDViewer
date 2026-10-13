package psdlviewer;
import psdlviewer.*;

/** Displays the data of a single block attribute.
 *
 */

import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.JCheckBox;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.border.Border;
import javax.swing.BorderFactory;
import java.awt.Component;
import java.awt.Color;

import java.awt.Dimension;
import java.awt.BorderLayout;

import java.util.Hashtable;

import java.lang.reflect.Constructor;
import java.lang.NoSuchMethodException;
import java.lang.reflect.InvocationTargetException;
import java.lang.IllegalAccessException;
import java.lang.InstantiationException;


public class BlockAttributeView
extends DefaultDataView
implements ActionListener
{
    protected static Hashtable attributeTypes = null;

    protected JPanel    panel;
    protected JCheckBox lastAttribute = new JCheckBox();

    protected BlockAttribute   attribute;
    protected UShortListView   view = null;

    protected GridBagConstraints leftC  = new GridBagConstraints();
    protected GridBagConstraints rightC = new GridBagConstraints();
    protected GridBagConstraints fillC  = new GridBagConstraints();





    public BlockAttributeView (BlockAttribute a)
    {
	initialize (a, true);
    }



    public BlockAttributeView (BlockAttribute a, boolean def)
    {
	initialize (a, def);
    }


    protected void initialize (BlockAttribute a, boolean def)
    {
	attribute = a;
	lastAttribute.setSelected (a.isLast());

	panel = new JPanel();
	component = new JScrollPane (panel);

	leftC.fill            = GridBagConstraints.NONE;
	leftC.gridwidth       = 1;
	leftC.anchor          = GridBagConstraints.NORTHEAST;

	rightC.fill           = GridBagConstraints.HORIZONTAL;
	rightC.gridwidth      = GridBagConstraints.REMAINDER;
	rightC.weightx        = 0.5;
	rightC.anchor         = GridBagConstraints.NORTHEAST;

	fillC.fill = GridBagConstraints.BOTH;
	fillC.gridwidth = GridBagConstraints.REMAINDER;
	fillC.gridheight = GridBagConstraints.REMAINDER;
	fillC.weightx = 0.0;
	fillC.weighty = 1.0;
	fillC.anchor = GridBagConstraints.NORTH;


	lastAttribute.addActionListener (this);

	GridBagLayout gbl = new GridBagLayout();
	panel.setLayout (gbl);
	GridBagConstraints c = new GridBagConstraints();

	JLabel label = new JLabel ("Value");
	c.fill = GridBagConstraints.NONE;
	c.anchor = GridBagConstraints.NORTHWEST;
	c.gridwidth = GridBagConstraints.REMAINDER;
	c.weightx = 0.0;
	c.weighty = 0.0;
	panel.add (label, c);

	panel.add (new JLabel ("Last attrib."), leftC);
	panel.add (lastAttribute,  rightC);

	if (def)
	{
	    view = new UShortListView ("Data");
	    view.setList (a.data);
	    panel.add (view, fillC);
	}

    }





    protected static void init()
    {
	attributeTypes = new Hashtable();
	attributeTypes.put (TextureAttribute.class,
			    TextureAttributeView.class);
	attributeTypes.put (Attribute0048.class,
			    Attribute0048View.class);
	attributeTypes.put (Attribute004b.class,
			    Attribute004bView.class);
	attributeTypes.put (Attribute003c.class,
			    Attribute003cView.class);
	attributeTypes.put (Attribute0051.class,
			    TextureAttributeView.class);
    }





    public void actionPerformed (ActionEvent e)
    {
	Object src = e.getSource();
	if (src == lastAttribute)
	{
	    attribute.setLast (lastAttribute.isSelected());
	}
    }






    public static BlockAttributeView getInstance (BlockAttribute a)
    {
	if (attributeTypes == null)
	{
	    init();
	}

	try
	{
	    Class c = (Class)attributeTypes.get (a.getClass());
	    if (c == null)
	    {

		if (Attribute0060.class.isInstance (a))
		{
		    c = Attribute0060View.class;
		}
		else if (Attribute0030.class.isInstance (a))
		{
		    c = Attribute0030View.class;
		}
		else if (Attribute0040.class.isInstance (a))
		{
		    c = Attribute0040View.class;
		}
		else if (Attribute001c.class.isInstance (a))
		{
		    c = Attribute001cView.class;
		}
		else if (Attribute005e.class.isInstance (a))
		{
		    c = Attribute005eView.class;
		}
		else if (VertexListAttribute.class.isInstance (a))
		{
		    c = VertexListAttributeView.class;
		}
		else
		{
		    c = BlockAttributeView.class;
		}
	    }

	    Class[] argTypes = {BlockAttribute.class};
	    Constructor constr = c.getConstructor (argTypes);

	    Object[] args = {a};
	    BlockAttributeView v = (BlockAttributeView)
		                         constr.newInstance (args);
	    return v;
	}
	catch (InvocationTargetException e)
	{
	    Throwable ex = e.getTargetException();
	    System.err.println ("BlockAttributeView.getInstance: " + ex);
	    ex.printStackTrace (System.err);
	}
	catch (NoSuchMethodException e)
	{
	    System.err.println ("BlockAttributeView.getInstance: " + e);
	    e.printStackTrace (System.err);
	}
	catch (IllegalAccessException e)
	{
	    System.err.println ("BlockAttributeView.getInstance: " + e);
	    e.printStackTrace (System.err);
	}
	catch (InstantiationException e)
	{
	    System.err.println ("BlockAttributeView.getInstance: " + e);
	    e.printStackTrace (System.err);
	}
	return null;
    }


}

