package psdlviewer;
import psdlviewer.*;

/** Displays the data of a single block.
 *
 */

import javax.swing.JSplitPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextArea;

import javax.swing.BoxLayout;


import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import java.awt.Component;
import java.awt.Color;

import java.awt.Dimension;

import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

public class BlockView
extends DefaultDataView
implements BlockSelectionListener
{
    protected Block   block;

    protected JSplitPane splitPane = new JSplitPane (JSplitPane.VERTICAL_SPLIT);

    protected PerimeterListView      perimeter = new PerimeterListView ("Perimeter");
    protected BlockAttributeListView attribs   = new BlockAttributeListView ("Attributes");

    public BlockView()
    {
	component = splitPane;
	splitPane.setTopComponent (perimeter);
	splitPane.setBottomComponent (attribs);
	splitPane.setDividerSize (2);
    }


    // BlockSelectionListener methods:
    //
    //
    public void blocksSelected (Object[] blocks)
    {
	if (blocks.length >= 1)
	{
	    Block  p = (Block)blocks[0];
	    perimeter.setList (p.getPerimeter());
	    attribs.setList (p.getAttributes());
	    splitPane.setDividerLocation (0.5);
	}
    }

}

