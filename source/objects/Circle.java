package source.objects;

import org.joml.Vector3f;

public class Circle extends Entity {
    private float radius;

    public Circle(Model model, Vector3f position, Vector3f velocity, Vector3f acceleration, Vector3f rotation,
                  float scale, float mass, float e, float radius, float staticFriction, float kineticFriction) {
                    super(model, position, velocity, acceleration, rotation, scale, mass, e, staticFriction, kineticFriction);
                    this.radius = radius;
    }

    public boolean intersects(Circle c) {
        float r = this.radius + c.getRadius();
        r *= r;

        return r > Math.pow(this.getPosition().x - c.getPosition().x, 2) + Math.pow(this.getPosition().y - c.getPosition().y, 2);
    }

    public boolean intersects(AABB a) {
        return a.intersects(this);
    }

    public boolean intersects(float x, float y) {
        return Math.abs(x - getPosition().x) < radius && Math.abs(y - getPosition().y) < radius;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
}