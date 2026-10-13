package psdlviewer;
import psdlviewer.*;

/** A class that encloses any Java object, and provides a dataflavor for it.
 *
 */


import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;


public class SimpleTransferable
implements Transferable
{
    /** Data flavor for this object.
     *
     */
    protected DataFlavor flavor;



    /** Object carried in this transferable.
     *
     */
    protected Object object;



    /** Creates a new SimpleTransferable.
     *
     */
    public SimpleTransferable (Object o)
    {
	object = o;
	flavor = new DataFlavor (o.getClass(),
				 o.getClass().getName());
    }





    public DataFlavor getDataFlavor()
    {
	return flavor;
    }



    // Transferable methods:
    //
    //
    public DataFlavor[] getTransferDataFlavors()
    {
	DataFlavor[] df = new DataFlavor[1]; 
	df[0] = flavor;
	return df;
    }

    public boolean isDataFlavorSupported (DataFlavor f)
    {
	return flavor.equals (f);
    }

    public Object getTransferData (DataFlavor f)
    throws UnsupportedFlavorException
    {
	// Ignores the data flavor.
	if (false)
	{
	    throw new UnsupportedFlavorException (f);
	}
	return object;
    }



}
