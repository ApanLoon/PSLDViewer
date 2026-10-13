package psdlviewer;
import psdlviewer.*;

/** Reference to a height.
 *
 */

import java.io.OutputStream;
import java.io.IOException;
import java.io.Serializable;

public class HeightRef
implements Serializable
{
    char  index;

    public HeightRef (char index_)
    {
	index = index_;
    }


    public HeightRef (UShort index_)
    {
	index = index_.getCharValue();
    }


    public String toString()
    {
	return "" + Integer.toHexString (index);
    } 



    public char getIndex()
    {
	return index;
    }

    public void setIndex (char i)
    {
	index = i;
    }



    public void write (OutputStream out)
    throws IOException
    {
	UShort.write (index, out);
    }

}
