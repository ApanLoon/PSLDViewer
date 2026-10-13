package psdlviewer.pkg;

import psdlviewer.Config;
import psdlviewer.CFloat;

import javax.vecmath.Color4f;

import java.io.InputStream;

public class Shader
    {
	String  textureName;
	Color4f ambient   = null;
	Color4f diffuse   = null;
	Color4f specular  = null;
	Color4f emissive  = null;
	float   shininess = 1f;

	public Shader (int type, InputStream in)
	throws Exception
	{
	    textureName = PKG.readString (in);

	    float r;
	    float g;
	    float b;
	    float a;
	    int rb;
	    int gb;
	    int bb;
	    int ab;

	    switch (type)
	    {
	    case 0:
		r = CFloat.parseCFloat (in);
		g = CFloat.parseCFloat (in);
		b = CFloat.parseCFloat (in);
		a = CFloat.parseCFloat (in);
		ambient = new Color4f (r, g, b, a);

		r = CFloat.parseCFloat (in);
		g = CFloat.parseCFloat (in);
		b = CFloat.parseCFloat (in);
		a = CFloat.parseCFloat (in);
		diffuse = new Color4f (r, g, b, a);

		r = CFloat.parseCFloat (in);
		g = CFloat.parseCFloat (in);
		b = CFloat.parseCFloat (in);
		a = CFloat.parseCFloat (in);
		specular = new Color4f (r, g, b, a);

		r = CFloat.parseCFloat (in);
		g = CFloat.parseCFloat (in);
		b = CFloat.parseCFloat (in);
		a = CFloat.parseCFloat (in);
		emissive = new Color4f (r, g, b, a);

		shininess = CFloat.parseCFloat (in);
		break;
	    case 1:
		rb = in.read();
		gb = in.read();
		bb = in.read();
		ab = in.read();

		rb = 255;
		gb = 255;
		bb = 255;
		ab = 255;
		ambient = new Color4f (rb / 255f, gb / 255f, bb / 255f, ab / 255f);
		rb = in.read();
		gb = in.read();
		bb = in.read();
		ab = in.read();
		diffuse = new Color4f (rb / 255f, gb / 255f, bb / 255f, ab / 255f);
		rb = in.read();
		gb = in.read();
		bb = in.read();
		ab = in.read();
		specular = new Color4f (rb / 255f, gb / 255f, bb / 255f, ab / 255f);
		rb = in.read();
		gb = in.read();
		bb = in.read();
		ab = 255;
		emissive = new Color4f (rb / 255f, gb / 255f, bb / 255f, ab / 255f);

		ab = in.read();
		shininess = ab / 255f;
		break;
	    }
	}

	public boolean isTextured()
	{
	    return textureName != null && textureName.length() != 0;
	}
    }
