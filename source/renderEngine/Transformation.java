package renderEngine;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transformation {
    public static Matrix4f getWorldMatrix(Vector3f offset, Vector3f rotation, float scale) {

		Matrix4f worldMatrix = new Matrix4f();

		worldMatrix.identity().translate(offset).
			rotateX((float) Math.toRadians(rotation.x)).
			rotateY((float) Math.toRadians(rotation.y)).
			rotateZ((float) Math.toRadians(rotation.z)).
			scale(scale);
		return worldMatrix;
	}
}
