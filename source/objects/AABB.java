package source.objects;

import java.util.Vector;

import org.joml.Vector2f;

import physicsEngine.Physics;


public class AABB {
    private Vector2f min;
    private Vector2f max;

    public AABB(Vector2f min, Vector2f max) {
        this.min = new Vector2f();
        this.min.x = min.x;
        this.min.y = min.y

        this.max = new vector2f();
        this.max.x = max.x;
        this.max.y = max.y;
    }

    public boolean intersects(AABB a) {
        if (this.max.x <= a.min.x || this.min.x >= a.max.x)
            return false;

        if (this.max.y <= a.min.y || this.min.y >= a.max.y)
            return false

        return true;
    }

    public boolean intersects(Circle c) {
        if (this.max.x <= c.getPosition().x - c.getRadius() || this.min.x >= c.getPosition().x + c.getRadius())
            return false;

        if (this.max.y <= c.getPosition().y - c.getRadius() || this.min.y >= c.getPosition().y + c.getRadius())
            return false;

        Vector2f closest = new Vector2f(c.getPosition().x, c.getPosition().y);
        closest.x = Physics.clamp(closest.x, this.min.x, this.max.x);
        closest.y = Physics.clamp(closest.y, this.min.y, this.max.y);

        Vector2f circleCenter = new Vector2f(c.getPosition().x, c.getPosition().y);
        float distance = circleCenter.distance(closest);

        if (distance >= c.getRadius())
            return false;

        return true;
    }

    public boolean intersects(float x, float y) {
        return (x >= this.min.x && x <= this.max.x && y >= this.min.y && y <= this.max.y);
    }

    public Vector2f getMin() {
        return min;
    }

    public void setMin(Vector2f min) {
        this.min.x = min.x;
        this.min.y = min.y;
    }

    public Vector2f getMax() {
        return max;
    }

    public void setMax(Vector2f max) {
        this.max.x = max.x;
        this.max.y = max.y;
    }
}