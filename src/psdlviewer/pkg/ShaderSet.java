package psdlviewer.pkg;

import psdlviewer.Config;
import psdlviewer.TextureStore;
import psdlviewer.ULong;

import javax.media.j3d.Appearance;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.TransparencyAttributes;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Material;

import java.io.InputStream;
import java.util.Vector;

public class ShaderSet
    {
	int          shaderType;
	int          paintJobs;
	int          shadersPerPaintJob;
	Shader[]  shader;
	Appearance[] appearance;

	public ShaderSet (InputStream in,
			     long len)
        throws Exception
	{
	    shaderType         = (int)ULong.parseULong (in);
	    shadersPerPaintJob = (int)ULong.parseULong (in);

	    paintJobs = shaderType & 0x7f;
	    shaderType >>= 7;
 
	    int nShaders = paintJobs * shadersPerPaintJob;

//	    Config.debug ("Reading " + nShaders + " shaders\n");

	    shader     = new Shader[nShaders];
	    appearance = new Appearance[nShaders];

	    for (int i = 0; i < nShaders; i++)
	    {
		shader[i] = new Shader (shaderType, in);
	    }
	}



	public int nPaintJobs()
	{
	    return paintJobs;
	}


	Shader getShader (int shaderOffset,
			  int paintJob)
	{
	    return shader[paintJob * shadersPerPaintJob + shaderOffset];
	}



	/** Get a shader appearance object from the this shader set.
	 *
	 *  If a shader with the given index does not exist, new Appearance
	 *  objects will be created up to the given index and the last Appearance
	 *  object will be returned.
	 *
	 */
	public Appearance getAppearance (int          shaderOffset,
					 int          paintJob,
					 TextureStore tStore,
					 Vector       tPath)
	{
	    int index = paintJob * shadersPerPaintJob + shaderOffset;
	    Appearance a = appearance[index];
	    if (a == null)
	    {
		a = new Appearance();
		appearance[index] = a;


		Shader s = shader[index];

		// Create polygon attributes:
		//
		PolygonAttributes pAttr = new PolygonAttributes();
		pAttr.setPolygonMode (PolygonAttributes.POLYGON_FILL);
		pAttr.setCullFace    (PolygonAttributes.CULL_BACK);
		a.setPolygonAttributes (pAttr);

		/* TODO: I have no idea about how to set up transparency correctly
		// Create transparency attributes:
                TransparencyAttributes tAttr = new TransparencyAttributes (
		   TransparencyAttributes.BLENDED,
		   1.0f,
		   TransparencyAttributes.BLEND_ONE,
		   TransparencyAttributes.BLEND_ONE_MINUS_SRC_ALPHA);
	        a.setTransparencyAttributes (tAttr);
	        */

//		Config.debug ("Ambient:  " + s.ambient + "\n");
//		Config.debug ("Diffuse:  " + s.diffuse + "\n");
//		Config.debug ("Specular: " + s.specular + "\n");
//		Config.debug ("Emissive: " + s.emissive + "\n");

		Material m = new Material();

		if (s.ambient != null)
		{
		    m.setAmbientColor (s.ambient.x,
				       s.ambient.y,
				       s.ambient.z);
		}
		if (s.diffuse != null)
		{
		    m.setDiffuseColor (s.diffuse.x,
				       s.diffuse.y,
				       s.diffuse.z,
				       s.diffuse.w);
		}
		if (s.specular != null)
		{
		    m.setSpecularColor (s.specular.x,
					s.specular.y,
					s.specular.z);
		}
		if (s.emissive != null)
		{
		    m.setEmissiveColor (s.emissive.x,
					s.emissive.y,
					s.emissive.z);
		}
		m.setShininess (s.shininess);

		a.setMaterial (m);

		ColoringAttributes cAttr = new ColoringAttributes();
		cAttr.setShadeModel (ColoringAttributes.NICEST);
		cAttr.setColor (s.ambient.x, s.ambient.y, s.ambient.z);
		a.setColoringAttributes (cAttr);
		if (s.isTextured())
		{
		    a.setTexture (tStore.getTexture (s.textureName,
						     tPath));
		}

	    }
	    return a;
	}

    }
