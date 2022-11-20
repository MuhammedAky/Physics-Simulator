package source.objects;

import org.joml.Vector3f;

public class Entity {
    private Model model;
    private Vector3f position;
    private Vector3f velocity;
    private Vector3f acceleration;
    private Vector3f rotation;
    private float scale;
    private float mass;
    private float e;
    private float staticFriction;
    private float kineticFriction;
    private Vector3f storedvelocity;

    public Entity(Model model, Vector3f position, Vector3f velocity, Vector3f acceleration,
                  Vector3f rotation, float scale, float mass, float e, float staticFriction, float kineticFriction) {


        this.model = model;

        // Position
        this.position = new Vector3f();
        this.position.x = position.x;
        this.position.y = position.y;
        this.position.z = position.z;

        // Velocity
        this.velocity = new Vector3f();
        this.velocity.x = velocity.x;
        this.velocity.y = velocity.y;
        this.velocity.z = velocity.z;

        // acceleration
        this.acceleration = new Vector3f();
        this.acceleration.x = acceleration.x;
        this.acceleration.y = acceleration.y;
        this.acceleration.z = acceleration.z;

        // rotation
        this.rotation = new Vector3f();
        this.rotation.x = rotation.x;
        this.rotation.y = rotation.y;
        this.rotation.z = rotation.z;

        // scale
        this.scale = scale;

        this.mass = mass;

        this.e = e;

        this.staticFriction = staticFriction;
        this.kineticFriction = kineticFriction;

        storedvelocity = new Vector3f(0, 0, 0);
    }

    public Entity(Model model, Vector3f position, float scale, Vector3f rotation) {
        this.model = model;

        this.position = new Vector3f();
        this.position.x = position.x;
        this.position.y = position.y;
        this.position.z = position.z;

        this.rotation = new Vector3f();
        this.rotation.x = rotation.x;
        this.rotation.y = rotation.y;
        this.rotation.z = rotation.z;

        this.scale = scale;

        storedvelocity = new Vector3f(0, 0, 0);
    }

    public void update(float dt) {
        velocity.x += acceleration.x * dt;
        velocity.y += acceleration.y * dt;
        velocity.z += acceleration.z * dt;

        position.x += velocity.x * dt;
        position.y += velocity.y * dt;
        position.z += velocity.z * dt;
    }

    public boolean intersects() {
        if (this instanceof Rectangle) {
            if (e instanceof Rectangle) {
                return (((Rectangle) this).getAabb().intersects(((Rectangle) e).getAabb()));
            }

            else if (e instanceof Circle) {
                return ((Rectangle) this).getAabb().intersects((Circle) e);
            }
        }

        else if (this instanceof Circle) {
            if (e instanceof Rectangle) {
                return ((Circle) this).intersects(((Rectangle) e).getAabb());
            }

            else if (e instanceof Circle) {
                return ((Circle) this).intersects((Circle) e);
            }
        }

        return false;
    }

    public boolean intersects(float x, float y) {
        if (this instanceof Rectangle)
            return ((Rectangle) this).getAabb().intersects(x, y);

        else if (this instanceof Circle)
            return ((Circle) this).intersects(x, y);

        return false;
    }

    public void increaseRotation(float dx, float dy, float dz) {
        this.rotation.x += dx;
        this.rotation.y += dy;
        this.rotation.z += dz;

        this.rotation.x %= 360;
        this.rotation.y %= 360;
        this.rotation.z %= 360;
    }

    public Model getModel() {
        return model;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position.x = position.x;
        this.position.y = position.y;
        this.position.z = position.z;
    }

    public Vector3f getVelocity() {
        return velocity;
    }

    public Vector3f getStoredVelocity() {
        return storedvelocity;
    }

    public void setStoredVelocityX(float x) {
		storedvelocity.x=x;
	}

	public void setStoredVelocityY(float y)	{
		storedvelocity.y=y;
	}

	public void setVelocity(Vector3f velocity) {
		this.velocity.x = velocity.x;
		this.velocity.y = velocity.y;
		this.velocity.z = velocity.z;
	}

	public Vector3f getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(Vector3f acceleration) {
		this.acceleration.x = acceleration.x;
		this.acceleration.y = acceleration.y;
		this.acceleration.z = acceleration.z;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation.x = rotation.x;
		this.rotation.y = rotation.y;
		this.rotation.z = rotation.z;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public float getMass() {
		return mass;
	}

	public void setMass(float mass) {
		this.mass = mass;
	}

	public float getCoefficientOfRestitution() {
		return e;
	}

	public void setCoefficientOfRestitution(float e) {
		this.e = e;
	}

	public float getStaticFriction() {
		return staticFriction;
	}

	public float getKineticFriction() {
		return kineticFriction;
	}

	public static float[] getVertices(float width, float height, float z) {

		float xMin = -width/2;
		float xMax = width/2;
		float yMin = -height/2;
		float yMax = height/2;

		float[] vertices = new float[] {

				xMin, yMax, z,
				xMin, yMin, z,
				xMax, yMin, z,
				xMax, yMax, z
		};

		return vertices;
	}

	public static float[] getTexCoords() {

		float[] texCoords = new float[] {

			0, 0,
			0, 1,
			1, 1,
			1, 0
		};
		return texCoords;
	}

	public static int[] getIndices() {
		int[] indices = new int[] {
			0,1,3,
			3,1,2
		};
		return indices;
	}
}