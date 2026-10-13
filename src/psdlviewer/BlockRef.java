package psdlviewer;
import psdlviewer.*;

/** Reference to a block.
 *
 */

import java.io.OutputStream;
import java.io.IOException;
import java.io.Serializable;

public class BlockRef
implements Serializable
{
    char  index;

    public BlockRef (char index_)
    {
	index = index_;
    }


    public BlockRef (UShort index_)
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
