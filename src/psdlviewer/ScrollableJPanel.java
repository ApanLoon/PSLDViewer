package psdlviewer;
import psdlviewer.*;

import javax.swing.JPanel;

import javax.swing.Scrollable;
import javax.swing.SwingConstants;

import java.awt.Dimension;
import java.awt.Rectangle;

public class ScrollableJPanel
extends JPanel
implements Scrollable
{
    private int maxUnitIncrement = 1;

    public ScrollableJPanel()
    {
        super();
    }

    public Dimension getPreferredScrollableViewportSize()
    {
        return getPreferredSize();
    }

    public int getScrollableUnitIncrement (Rectangle visibleRect,
					   int       orientation,
					   int       direction)
    {
        // Get the current position:
	//
        int currentPosition = 0;
        if (orientation == SwingConstants.HORIZONTAL)
            currentPosition = visibleRect.x;
        else
            currentPosition = visibleRect.y;

        // Return the number of pixels between currentPosition
        // and the nearest tick mark in the indicated direction:
	//
        if (direction < 0)
	{
            int newPosition = currentPosition
		              - (currentPosition / maxUnitIncrement)
		              * maxUnitIncrement;
            return (newPosition == 0) ? maxUnitIncrement : newPosition;
        }
	else 
	{
            return ((currentPosition / maxUnitIncrement) + 1)
		   * maxUnitIncrement - currentPosition;
        }
    }


    public int getScrollableBlockIncrement (Rectangle visibleRect,
                                            int       orientation,
                                            int       direction)
    {
        if (orientation == SwingConstants.HORIZONTAL)
            return visibleRect.width - maxUnitIncrement;
        else
            return visibleRect.height - maxUnitIncrement;
    }


    public boolean getScrollableTracksViewportWidth()
    {
        return false;
    }

    public boolean getScrollableTracksViewportHeight()
    {
        return false;
    }

    public void setMaxUnitIncrement(int pixels)
    {
        maxUnitIncrement = pixels;
    }
}
