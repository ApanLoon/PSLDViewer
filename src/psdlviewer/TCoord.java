package psdlviewer;
import psdlviewer.*;

/** Texture coordinate for primary and secondary texture.
 *
 */

public class TCoord
{
    protected int u0;
    protected int v0;
    protected int u1;
    protected int v1;

    public TCoord (int u0_,
		   int v0_,
		   int u1_,
		   int v1_)
    {
	u0 = u0_;
	v0 = v0_;
	u1 = u1_;
	v1 = v1_;
    }



    public String toString()
    {
	return "(" + u0 + ", " + v0 + "; " + u1 + ", " + v1 + ")";
    }


}
