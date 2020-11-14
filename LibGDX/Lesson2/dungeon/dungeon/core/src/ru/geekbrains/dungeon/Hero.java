package ru.geekbrains.dungeon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Hero {
    private ProjectileController projectileController;
    private Vector2 position;
    private TextureRegion texture;
    private ShootingMode shootingMode = ShootingMode.Single;
    private float angle;
    private float speed;
    private float scale;

    public Hero(TextureAtlas atlas, ProjectileController projectileController) {
        this.position = new Vector2(100, 100);
        this.texture = atlas.findRegion("tank");
        this.projectileController = projectileController;
        this.speed = 240.0f;
        this.scale = 3.0f;
    }

    public void update(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if (shootingMode == ShootingMode.Double) {
                projectileController.activate(position.x, position.y, 200, -10);
                projectileController.activate(position.x, position.y, 200, 10);
            } else {
                projectileController.activate(position.x, position.y, 200, 0);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            if (shootingMode == ShootingMode.Single) {
                shootingMode = ShootingMode.Double;
            } else {
                shootingMode = ShootingMode.Single;
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            angle -= 90.0f * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            angle += 90.0f * dt;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            float x = position.x + speed * MathUtils.cosDeg(angle) * dt;
            if (x < 20.0f) {
                x = 20.0f;
            } else if (x > 1280.0f - 20.0f) {
                x = 1280.0f - 20.0f;
            }

            float y = position.y + speed * MathUtils.sinDeg(angle) * dt;
            if (y < 20.0f) {
                y = 20.0f;
            } else if (y > 720.0f - 20.0f) {
                y = 720.0f - 20.0f;
            }

            position.x = x;
            position.y = y;

        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            float x = position.x - speed * MathUtils.cosDeg(angle) * dt;
            if (x < 20.0f) {
                x = 20.0f;
            } else if (x > 1280.0f - 20.0f) {
                x = 1280.0f - 20.0f;
            }

            float y = position.y - speed * MathUtils.sinDeg(angle) * dt;
            if (y < 20.0f) {
                y = 20.0f;
            } else if (y > 720.0f - 20.0f) {
                y = 720.0f - 20.0f;
            }

            position.x = x;
            position.y = y;
        }

        System.out.println(position.x + ";" + position.y);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 10, position.y - 10, 20, 20, 20, 20, scale, scale, angle);
    }

    private enum ShootingMode {
        Single, Double
    }
}
