package psdlviewer;
import psdlviewer.*;

/** Displays the data of a single pathset.
 *
 */

import javax.swing.JScrollPane;
import javax.swing.JPanel;
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

public class PathsetView
extends DefaultDataView
implements PathsetSelectionListener
{
    protected Pathset   pathset;

    protected PathListView pathListView = new PathListView ("Paths");

    protected JPanel panel = new JPanel();


    public PathsetView()
    {
	panel.add (pathListView);
	component = new JScrollPane (panel);
    }


    // PathsetSelectionListener methods:
    //
    //
    public void pathsetsSelected (Object[] pathsets)
    {
	if (pathsets.length >= 1)
	{
	    Pathset  p = (Pathset)pathsets[0];
	    pathListView.setList (p.getPaths());
	}
    }

}

