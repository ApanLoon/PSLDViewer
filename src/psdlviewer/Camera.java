package psdlviewer;
import psdlviewer.*;

import javax.media.j3d.TransformGroup;
import javax.media.j3d.Transform3D;
import javax.media.j3d.Canvas3D;

import javax.media.j3d.View;
import javax.media.j3d.ViewPlatform;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.DirectionalLight;

import com.sun.j3d.utils.universe.ViewingPlatform;
import com.sun.j3d.utils.universe.Viewer;
import com.sun.j3d.utils.universe.MultiTransformGroup;

import javax.vecmath.Vector3f;



public class Camera
extends BranchGroup
{
    Canvas3D canvas;

    float    zoom;
    Vector3f position = new Vector3f();
    Vector3f orientation = new Vector3f();

    ViewingPlatform viewingPlatform;
    Viewer viewer;

    TransformGroup t_rotX;
    TransformGroup t_rotY;
    TransformGroup t_rotZ;
    TransformGroup t_pos;

    public Camera (Canvas3D canvas_)
    {
	canvas = canvas_;

	viewingPlatform = new ViewingPlatform (4);
	MultiTransformGroup g = viewingPlatform.getMultiTransformGroup();

	t_pos       = g.getTransformGroup (0);
	t_rotZ      = g.getTransformGroup (1);
	t_rotY      = g.getTransformGroup (2);
	t_rotX      = g.getTransformGroup (3);

	viewer = new Viewer (canvas);
	viewer.setViewingPlatform (viewingPlatform);


	DirectionalLight headlight = new DirectionalLight();
//	headlight.setCapability (Light.ALLOW_STATE_WRITE);
//	headlight.setColor (White);
//	headlight.setInfluencingBounds (LIGHT_BOUNDS);
	t_rotZ.addChild (headlight);

/*
static
{
        Point3d origin = new Point3d(0, 0, 0);
        LIGHT_BOUNDS =
                new BoundingSphere(origin, BACK_CLIP_DISTANCE);
}
*/


	viewer.getView().setBackClipDistance (10000.0);

	zoom = (float)viewer.getView().getFieldOfView();

	rotate (0, 0, 0);
//	position.z = (float)(1.0 / (Math.tan (zoom / 2.0)));
	updatePosition();

	addChild (viewingPlatform);
    }




    public ViewingPlatform getViewingPlatform()
    {
	return viewingPlatform;
    }

    public Viewer getViewer()
    {
	return viewer;
    }





    /** Translates the camera in camera space.
     *
     */
    public void translate (float x, float y, float z)
    {
	Vector3f v = new Vector3f (x, y, z);

	Transform3D pos  = new Transform3D();
	Transform3D rotX = new Transform3D();
	Transform3D rotY = new Transform3D();
	Transform3D rotZ = new Transform3D();
	Transform3D rot  = new Transform3D();

	t_pos.getTransform  (pos);
	t_rotX.getTransform (rotX);
	t_rotY.getTransform (rotY);
	t_rotZ.getTransform (rotZ);

	rot.mul (rotY, rotX);
	rot.mul (rotZ);
	rot.mul (pos);
	rot.transform (v);

	position.x += v.x;
	position.y += v.y;
	position.z += v.z;

	updatePosition();
    }



    /** Rotates the camera in camera space.
     *
     */
    public void rotate (float x, float y, float z)
    {
	orientation.x += x;
	orientation.y += y;
	orientation.z += z;

	updateOrientation();
    }



    public void zoom (float z)
    {
	zoom += z;
	viewer.getView().setFieldOfView (zoom);
    }




    /** Set the camera position in world coordinates.
     *
     */
    public void setPosition (float x, float y, float z)
    {
	position.x = x;
	position.y = y;
	position.z = z;

	updatePosition();
    }

    public void setOrientation (float x, float y, float z)
    {
	orientation.x = x;
	orientation.y = y;
	orientation.z = z;

	updateOrientation();
    }









    protected void updatePosition()
    {
	Transform3D t = new Transform3D();
	t_pos.getTransform (t);
	t.set (position);
	t_pos.setTransform (t);
    }


    protected void updateOrientation()
    {
	Transform3D t = new Transform3D();

	t_rotX.getTransform (t);
	t.rotX (orientation.x);
	t_rotX.setTransform (t);

	t_rotY.getTransform (t);
	t.rotY (orientation.y);
	t_rotY.setTransform (t);

	t_rotZ.getTransform (t);
	t.rotZ (orientation.z);
	t_rotZ.setTransform (t);
    }



}
