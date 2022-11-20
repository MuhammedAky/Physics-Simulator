package source.objects;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Rectangle extends Entity {
    private float width;
    private float height;

    private AABB aabb;

    public Rectangle(Model model, Vector3f position, Vector3f velocity, Vector3f acceleration, Vector3f rotation,
			float scale, float mass, float e, float width, float height, float staticFriction, 
			float kineticFriction) {

		super(model, position, velocity, acceleration, rotation, scale, mass, e, 
				staticFriction, kineticFriction);

        this.width = width;
        this.height = height;

        float xMin = position.x - width / 2;
        float xMax = position.x + width / 2;
        float yMin = position.y - height / 2;
        float yMax = position.y + height / 2;

        this.aabb = new AABB(new Vector2f(xMin, xMax), new Vector2f(new Vector2f(xMax, yMax)));
    }

    public void update(float dt) {
        super.update(dt);

        updateAABB();
    }

    public void updateAABB() {
        this.aabb.setMin(new Vector2f(getPosition().x - width / 2, getPosition().y - height / 2));
        this.aabb.setMax(new Vector2f(getPosition().x + width / 2, getPosition().y + height / 2));
    }

    public AABB getAabb() {
        return aabb;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}