package psdlviewer;
import psdlviewer.*;

import com.sun.j3d.utils.universe.SimpleUniverse;

import javax.media.j3d.Canvas3D;
import javax.media.j3d.Locale;
import javax.media.j3d.Node;


import javax.media.j3d.BranchGroup;
import javax.media.j3d.AmbientLight;




public class Universe
extends SimpleUniverse
{
    protected Camera camera;

    public Universe (Camera c)
    {
	super (c.getViewingPlatform(), c.getViewer());
	camera = c;
    }

    public Camera getCamera()
    {
	return camera;
    }


    public void addBranchGraph (BranchGroup g)
    {
	g.setCapability (BranchGroup.ALLOW_DETACH); 
	g.compile();
	super.addBranchGraph (g);
    }


    public void removeBranchGraph (BranchGroup g)
    {
	getLocale().removeBranchGraph (g);
    }




}
