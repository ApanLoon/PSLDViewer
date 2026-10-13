package psdlviewer;
import psdlviewer.*;

import java.util.Vector;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;

public class TransferableVector
extends Vector
implements Transferable
{
    protected static DataFlavor flavor = new DataFlavor (
						   TransferableVector.class,
						  "TransferableVector");



    public static DataFlavor getDataFlavor()
    {
	return flavor;
    }


    public TransferableVector()
    {
	super();
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
	if (!isDataFlavorSupported (f))
	{
	    throw new UnsupportedFlavorException (f);
	}
	return this;
    }
}
