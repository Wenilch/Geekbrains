package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class Tank {
    private Texture texture;
    private Texture textureWeapon;
    private float x;
    private float y;
    private float speed;
    private float angle;
    private float angleWeapon;
    private Projectile projectile;
    private float scale;
    private Target target;

    public Tank() {
        this.texture = new Texture("tank.png");
        this.textureWeapon = new Texture("weapon.png");
        this.projectile = new Projectile();
        this.x = 100.0f;
        this.y = 100.0f;
        this.speed = 240.0f;
        this.scale = 3.0f;
    }

    public void update(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            angle -= 90.0f * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            angle += 90.0f * dt;
        }
//        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
//            angle -= 90.0f;
//        }
//        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
//            angle += 90.0f;
//        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            moveByX(speed * MathUtils.cosDeg(angle) * dt);
            moveByY(speed * MathUtils.sinDeg(angle) * dt);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            moveByX(-(speed * MathUtils.cosDeg(angle) * dt * 0.2f));
            moveByY(-(speed * MathUtils.sinDeg(angle) * dt * 0.2f));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            angleWeapon -= 90.0f * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            angleWeapon += 90.0f * dt;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && !projectile.isActive()) {
            projectile.shoot(x + 16 * scale * MathUtils.cosDeg(angle), y + 16 * scale * MathUtils.sinDeg(angle), angle + angleWeapon);
        }
        if (projectile.isActive()) {
            projectile.update(dt);

            boolean isOnTargetByX = projectile.getX() >= target.getX() && projectile.getX() <= target.getX() + target.getSize();
            boolean isOnTargetByY = projectile.getY() >= target.getY() && projectile.getY() <= target.getY() + target.getSize();
            if (isOnTargetByX && isOnTargetByY) {
                projectile.deactivate();
                target.dispose();
            }
        }
    }

    private void moveByX(float moveX) {
        if (moveX > 0) {
            if (moveX + x <= 1280 - 40) {
                x += moveX;
            } else {
                x = 1280 - 40;
            }
        } else {
            if (moveX + x >= 40) {
                x += moveX;
            } else {
                x = 40;
            }
        }
    }

    private void moveByY(float moveY) {
        if (moveY > 0) {
            if (moveY + y <= 720 - 40) {
                y += moveY;
            } else {
                y = 720 - 40;
            }
        } else {
            if (moveY + y >= 40) {
                y += moveY;
            } else {
                y = 40;
            }
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, x - 20, y - 20, 20, 20, 40, 40, scale, scale, angle, 0, 0, 40, 40, false, false);
        batch.draw(textureWeapon, x - 20, y - 20, 20, 20, 40, 40, scale, scale, angle + angleWeapon, 0, 0, 40, 40, false, false);
        if (projectile.isActive()) {
            projectile.render(batch);
        }
    }

    public void dispose() {
        texture.dispose();
        projectile.dispose();
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }
}
