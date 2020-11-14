package ru.geekbrains.dungeon;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Projectile {
    private TextureRegion texture;
    private Vector2 position;
    private Vector2 velocity;
    private boolean active;

    public boolean isActive() {
        return active;
    }

    public Projectile(TextureRegion texture) {
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.texture = texture;
        this.active = false;
    }

    public void deactivate() {
        active = false;
    }

    public void activate(float x, float y, float vx, float vy) {
        active = true;
        position.set(x, y);
        velocity.set(vx, vy);
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        if (position.x < 0 || position.x > 1280 || position.y < 0 || position.y > 720) {
            deactivate();
        }
        // position = (100, 100)
        // velocity = (200, 40)
        // position.add(velocity) => (100 + 200, 100 + 40) !!! incorrect
        // position.mulAdd(velocity, dt) => (100 + 200 * dt, 100 + 40 * dt)
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 8, position.y - 8);
    }
}
