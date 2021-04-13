package mchorse.mclib.utils;

import net.minecraft.util.Rotation;
import net.minecraft.util.math.Rotations;
import net.minecraft.util.math.vector.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;

@OnlyIn(Dist.CLIENT)
public class MatrixUtils
{
    /**
     * Model view matrix buffer
     */
    public static final FloatBuffer buffer = BufferUtils.createFloatBuffer(16);

    /**
     * Float array for transferring data from FloatBuffer to the matrix
     */
    public static final float[] floats = new float[16];

    /**
     * Model view matrix captured here
     */
    public static Matrix4f matrix;

    /**
     * Read OpenGL's model view matrix
     */
    public static Matrix4f readModelView(Matrix4f matrix4f)
    {
        buffer.clear();
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX);
        buffer.get(floats);

        matrix4f.set(matrix4f);
        matrix4f.transpose();

        return matrix4f;
    }

    /**
     * Replace model view matrix with given matrix
     */
    public static void loadModelView(Matrix4f matrix4f)
    {
        matrixToFloat(floats, matrix4f);

        buffer.clear();
        buffer.put(floats);
        buffer.rewind();
        GL11.glLoadMatrixf(buffer);
    }

    /**
     * Private method to fill the float array with values from the matrix
     */
    public static void matrixToFloat(float[] floats, Matrix4f matrix4f)
    {
        floats[0] = matrix4f.m00;
        floats[1] = matrix4f.m01;
        floats[2] = matrix4f.m02;
        floats[3] = matrix4f.m03;
        floats[4] = matrix4f.m10;
        floats[5] = matrix4f.m11;
        floats[6] = matrix4f.m12;
        floats[7] = matrix4f.m13;
        floats[8] = matrix4f.m20;
        floats[9] = matrix4f.m21;
        floats[10] = matrix4f.m22;
        floats[11] = matrix4f.m23;
        floats[12] = matrix4f.m30;
        floats[13] = matrix4f.m31;
        floats[14] = matrix4f.m32;
        floats[15] = matrix4f.m33;
    }

    public static void setRow(Matrix4f matrix4f, int row, Vector4f input){
        switch(row) {
            case 0:
                matrix4f.m00 = input.getX();
                matrix4f.m01 = input.getY();
                matrix4f.m02 = input.getZ();
                matrix4f.m03 = input.getW();
                break;
            case 1:
                matrix4f.m10 = input.getX();
                matrix4f.m11 = input.getY();
                matrix4f.m12 = input.getZ();
                matrix4f.m13 = input.getW();
                break;
            case 2:
                matrix4f.m20 = input.getX();
                matrix4f.m21 = input.getY();
                matrix4f.m22 = input.getZ();
                matrix4f.m23 = input.getW();
                break;
            case 3:
                matrix4f.m30 = input.getX();
                matrix4f.m31 = input.getY();
                matrix4f.m32 = input.getZ();
                matrix4f.m33 = input.getW();
                break;
            }       
        }

        public static void setRow(Matrix3f matrix4f, int row, float x, float y, float z){
            switch(row){
                case 0:
                    matrix4f.m00 = x;
                    matrix4f.m01 = y;
                    matrix4f.m02 = z;
                    break;
                case 1:
                    matrix4f.m10 = x;
                    matrix4f.m11 = y;
                    matrix4f.m12 = z;
                    break;
                case 2:
                    matrix4f.m20 = x;
                    matrix4f.m21 = y;
                    matrix4f.m22 = z;
                    break; 
                }
            }

    public static boolean captureMatrix()
    {
        if (matrix == null)
        {
            matrix = MatrixUtils.readModelView(new Matrix4f());

            return true;
        }

        return false;
    }

    public static void releaseMatrix()
    {
        matrix = null;
    }

    public static Quaternion matrixToQuaternion(Matrix3f matrix)
    {
        double tr = matrix.m00 + matrix.m11 + matrix.m22;
        double qw = 0;
        double qx = 0;
        double qy = 0;
        double qz = 0;

        if (tr > 0)
        {
            double S = Math.sqrt(tr+1.0) * 2; // S=4*qw
            qw = 0.25 * S;
            qx = (matrix.m21 - matrix.m12) / S;
            qy = (matrix.m02 - matrix.m20) / S;
            qz = (matrix.m10 - matrix.m01) / S;
        }
        else if ((matrix.m00 > matrix.m11) & (matrix.m00 > matrix.m22))
        {
            double S = Math.sqrt(1.0 + matrix.m00 - matrix.m11 - matrix.m22) * 2; // S=4*qx
            qw = (matrix.m21 - matrix.m12) / S;
            qx = 0.25 * S;
            qy = (matrix.m01 + matrix.m10) / S;
            qz = (matrix.m02 + matrix.m20) / S;
        }
        else if (matrix.m11 > matrix.m22)
        {
            double S = Math.sqrt(1.0 + matrix.m11 - matrix.m00 - matrix.m22) * 2; // S=4*qy
            qw = (matrix.m02 - matrix.m20) / S;
            qx = (matrix.m01 + matrix.m10) / S;
            qy = 0.25 * S;
            qz = (matrix.m12 + matrix.m21) / S;
        }
        else
        {
            double S = Math.sqrt(1.0 + matrix.m22 - matrix.m00 - matrix.m11) * 2; // S=4*qz
            qw = (matrix.m10 - matrix.m01) / S;
            qx = (matrix.m02 + matrix.m20) / S;
            qy = (matrix.m12 + matrix.m21) / S;
            qz = 0.25 * S;
        }

        return new Quaternion((float) qw, (float) qx, (float) qy, (float) qz);
    }

    public static void sub(Matrix3f matrix3f, Matrix3f m1)
    {
        matrix3f.m00 -= m1.m00;
        matrix3f.m01 -= m1.m01;
        matrix3f.m02 -= m1.m02;

        matrix3f.m10 -= m1.m10;
        matrix3f.m11 -= m1.m11;
        matrix3f.m12 -= m1.m12;

        matrix3f.m20 -= m1.m20;
        matrix3f.m21 -= m1.m21;
        matrix3f.m22 -= m1.m22;
    }

    public static final void add(Matrix3f matrix3f, Matrix3f m1)
    {
        matrix3f.m00 += m1.m00;
        matrix3f.m01 += m1.m01;
        matrix3f.m02 += m1.m02;

        matrix3f.m10 += m1.m10;
        matrix3f.m11 += m1.m11;
        matrix3f.m12 += m1.m12;

        matrix3f.m20 += m1.m20;
        matrix3f.m21 += m1.m21;
        matrix3f.m22 += m1.m22;
    }


    /**
     * This method calculates the angular velocity around the arbitrary axis.
     * The arbitrary axis and the angle around it can be obtained from this result.
     * @param rotation
     * @return the angular velocity vector.
     */
    public static Vector3f getAngularVelocity(Matrix3f rotation)
    {
        Matrix3f step = new Matrix3f(rotation);
        Matrix3f angularVelocity = new Matrix3f();
        Matrix3f i = new Matrix3f();

        i.setIdentity();
        angularVelocity.setIdentity();
        angularVelocity.mul(2);

        add(step, i);
        step.invert();
        step.mul(4);

        sub(angularVelocity, step);

        Vector3f angularV = new Vector3f(angularVelocity.m21,
                                    -angularVelocity.m20,
                                     angularVelocity.m10);

        return angularV;
    }

    public static void rotX(Matrix3f matrix3f, float angle)
    {
        float	sinAngle, cosAngle;

        sinAngle = (float) Math.sin((double) angle);
        cosAngle = (float) Math.cos((double) angle);

        matrix3f.m00 = (float) 1.0;
        matrix3f.m01 = (float) 0.0;
        matrix3f.m02 = (float) 0.0;

        matrix3f.m10 = (float) 0.0;
        matrix3f.m11 = cosAngle;
        matrix3f.m12 = -sinAngle;

        matrix3f.m20 = (float) 0.0;
        matrix3f.m21 = sinAngle;
        matrix3f.m22 = cosAngle;
    }

    public static void rotY(Matrix3f matrix3f, float angle)
    {
        float	sinAngle, cosAngle;

        sinAngle = (float) Math.sin((double) angle);
        cosAngle = (float) Math.cos((double) angle);

        matrix3f.m00 = cosAngle;
        matrix3f.m01 = (float) 0.0;
        matrix3f.m02 = sinAngle;

        matrix3f.m10 = (float) 0.0;
        matrix3f.m11 = (float) 1.0;
        matrix3f.m12 = (float) 0.0;

        matrix3f.m20 = -sinAngle;
        matrix3f.m21 = (float) 0.0;
        matrix3f.m22 = cosAngle;
    }

    public static void rotZ(Matrix3f matrix3f, float angle)
    {
        float	sinAngle, cosAngle;

        sinAngle = (float) Math.sin((double) angle);
        cosAngle = (float) Math.cos((double) angle);

        matrix3f.m00 = cosAngle;
        matrix3f.m01 = -sinAngle;
        matrix3f.m02 = (float) 0.0;

        matrix3f.m10 = sinAngle;
        matrix3f.m11 = cosAngle;
        matrix3f.m12 = (float) 0.0;

        matrix3f.m20 = (float) 0.0;
        matrix3f.m21 = (float) 0.0;
        matrix3f.m22 = (float) 1.0;
    }

    public static Matrix3f getZYXrotationMatrix(float x, float y, float z) {
        Matrix3f rotation = new Matrix3f();
        Matrix3f rot = new Matrix3f();

        rotation.setIdentity();
        rotZ(rot, z);
        rotation.mul(rot);
        rotY(rot, y);
        rotation.mul(rot);
        rotX(rot, x);
        rotation.mul(rot);

        return rotation;

    }
        /**
         * This method extracts the rotation, translation and scale from the modelview matrix. It needs the view matrix to work properly
         * @author Christian F. (known as Chryfi)
         * @param cameraMatrix The cameraMatrix from rendering so you can extract modelView from OpenGL's matrix.
         * @param modelView The matrix containing the transformations that should be extracted.
         * @return Transformation contains translation, rotation and scale as 4x4 matrices. It also has getter methods for the 3x3 matrices.
         */
        public static Transformation extractTransformations (Matrix4f cameraMatrix, Matrix4f modelView)
        {
            Matrix4f parent = new Matrix4f(modelView);

            if (cameraMatrix != null) {
                parent.set(cameraMatrix);
                parent.invert();
                parent.mul(modelView);
            }

            Matrix4f translation = new Matrix4f();
            Matrix4f scale = new Matrix4f();
            Matrix4f rotation = new Matrix4f();

            translation.setIdentity();
            rotation.setIdentity();
            scale.setIdentity();

            translation.m03 = parent.m03;
            translation.m13 = parent.m13;
            translation.m23 = parent.m23;

            Vector4f ax = new Vector4f(parent.m00, parent.m01, parent.m02, 0);
            Vector4f ay = new Vector4f(parent.m10, parent.m11, parent.m12, 0);
            Vector4f az = new Vector4f(parent.m20, parent.m21, parent.m22, 0);

            ax.normalize();
            ay.normalize();
            az.normalize();
            setRow(rotation,0, ax);
            setRow(rotation,1, ay);
            setRow(rotation,2, az);

            scale.m00 = (float) Math.sqrt(parent.m00 * parent.m00 + parent.m01 * parent.m01 + parent.m02 * parent.m02);
            scale.m11 = (float) Math.sqrt(parent.m10 * parent.m10 + parent.m11 * parent.m11 + parent.m12 * parent.m12);
            scale.m22 = (float) Math.sqrt(parent.m20 * parent.m20 + parent.m21 * parent.m21 + parent.m22 * parent.m22);

            return new Transformation(translation, rotation, scale);
        }


    public static class Transformation
    {
        public Matrix4f translation = new Matrix4f();
        public Matrix4f rotation = new Matrix4f();
        public Matrix4f scale = new Matrix4f();

        public Transformation(Matrix4f translation, Matrix4f rotation, Matrix4f scale)
        {
            this.translation.set(translation);
            this.rotation.set(rotation);
            this.scale.set(scale);
        }

        public Transformation()
        {
            this.translation.setIdentity();
            this.rotation.setIdentity();
            this.scale.setIdentity();
        }

        public Matrix3f getScale3f()
        {
            Matrix3f scale3f = new Matrix3f();

            scale3f.setIdentity();

            scale3f.m00 = this.scale.m00;
            scale3f.m11 = this.scale.m11;
            scale3f.m22 = this.scale.m22;

            return scale3f;
        }

        public Vector3f getTranslation3f()
        {
            Vector3f translation3f = new Vector3f();

            translation3f.set(this.translation.m03,this.translation.m13, this.translation.m23);

            return translation3f;
        }

        public Matrix3f getRotation3f()
        {
            Matrix3f rotation3f = new Matrix3f();

            rotation3f.setIdentity();
            setRow(rotation3f, 0, this.rotation.m00, this.rotation.m01, this.rotation.m02);
            setRow(rotation3f, 1, this.rotation.m10, this.rotation.m11, this.rotation.m12);
            setRow(rotation3f, 2, this.rotation.m20, this.rotation.m21, this.rotation.m22);

            return rotation3f;
        }
    }
}