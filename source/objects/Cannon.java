package source.objects;

import org.joml.Vector3f;

public class Cannon extends Rectangle {
    public Cannon(Model model, Vector3f position, Vector3f velocity, Vector3f acceleration, Vector3f rotation,
                  float scale, float mass, float e, float width, float height, float staticFriction, float kineticFriction) {
                    super(model, position, velocity, acceleration, rotation, scale, mass, e, width, height, staticFriction, kineticFriction);
    }
}