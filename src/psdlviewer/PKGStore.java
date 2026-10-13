package psdlviewer;

import psdlviewer.pkg.*;

import java.util.Hashtable;
import java.util.Vector;

import java.lang.Exception;

import javax.media.j3d.Node;
import javax.media.j3d.Link;
import javax.media.j3d.SharedGroup;

public class PKGStore
extends Hashtable
{
    public Node getPKGNode (String name,
			int lod,
			int paintJob,
			Vector gPath,
			TextureStore textureStore,
			Vector tPath)
    throws Exception
    {
	PKG pkg = getPKG (name, gPath);
	return new Link (pkg.render (lod, paintJob, textureStore, tPath));
    }




    public PKG getPKG (String name,
		       Vector gPath)
    throws Exception
    {
	PKG pkg = (PKG)get (name);
	if (pkg == null)
	{
	    pkg = new PKG (name, gPath);
	    put (name, pkg);
	}
	return pkg;
    }

}
