package psdlviewer;
import psdlviewer.*;

/** A height indicator.
 *
 */

import java.io.Serializable;

public class Height
implements Serializable
{
    protected float h;

    protected Height()
    {
    }

    public Height (float h_)
    {
	h = h_;
    }

    public void setHeight (float h_)
    {
	h = h_;
    }

    public float getHeight()
    {
	return h;
    }

    public String toString()
    {
	return "" + h + "m";
    }

}
