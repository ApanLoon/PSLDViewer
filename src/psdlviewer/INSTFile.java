package psdlviewer;
import psdlviewer.*;

/** An INST file is the file containing the monument data.
 *
 */


import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;

import java.io.OutputStream;
import java.io.FileOutputStream;

import java.io.IOException;
import java.io.FileNotFoundException;

import java.lang.Exception;

import java.util.Vector;


public class INSTFile
{
    /** Vector holding all INSTComponents.
     *
     */
    protected INSTList components;



    protected ProgressWindow monitor;




    /** Creates an empty INSTFile object.
     *
     */
    public INSTFile()
    {
	components = new INSTList();
    }



    /** Opens and reads a named INST file.
     *
     */
    public INSTFile (String name,
		     ProgressWindow monitor_)
    throws Exception
    {
	components = new INSTList();
	monitor = monitor_;
	read (name, false);
    }







    /** Appends a named INST file to this one.
     *
     */
    public void append (String name)
    throws Exception
    {
	// TODO: Does this work? I think so...
	read (name, true);
    }



    /** Reads an INST file from disk into this object.
     *
     *  @param name path and name of file to read
     *  @param append if true, the INST file will be added to the current
     *                object.
     *
     */
    protected void read (String name,
			 boolean append)
    throws Exception
    {
	try
	{
	    debug ("Reading INST file...\n");
	    File f = new File (name);

	    FileInputStream in = new FileInputStream (f);

	    // Read components:
	    //
	    //
	    debug ("Reading components:\n");
	    readComponents (in);


	}
	catch (FileNotFoundException e)
	{
	    throw new Exception ("File not found. (" + name + ")");
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	    throw e;
	}
    }




    protected void readComponents (InputStream in)
    throws IOException
    {
	long bytesRead = 0;
	long len = in.available();
	if (monitor != null)
	{
	    monitor.setMinimum (0);
	    monitor.setMaximum ((int)len);
	    monitor.setProgress (0);
	    monitor.setNote ("Reading components");
	}
	while (in.available() > 0)
	{
	    INSTComponent c = INSTComponent.getInstance (in);
	    components.add (c);
	    debug (".");

	    if (monitor != null)
	    {
		bytesRead += len - in.available();
		monitor.setProgress ((int)bytesRead);
	    }
	}
	debug ("\n");
    }





    public void write (String fileName)
    throws IOException
    {
	write (new File (fileName));
    }

    public void write (File f)
    throws IOException
    {
	write (new FileOutputStream (f));
    }

    public void write (OutputStream out)
    throws IOException
    {
	writeComponents (out);
    }



    protected void writeComponents (OutputStream out)
    throws IOException
    {
	for (int i = 0; i < components.size(); i++)
	{
	    INSTComponent c = components.componentAt (i);
	    c.write (out);
	}

    }





    public void setComponents (INSTList l)
    {
	components = l;
    }

    public INSTList getComponents()
    {
	return components;
    }


    protected void debug (String s)
    {
	System.out.print (s);
    }


}



