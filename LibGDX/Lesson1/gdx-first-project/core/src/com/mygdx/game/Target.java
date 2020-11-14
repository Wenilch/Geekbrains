package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Target {

    private float x;
    private float y;
    private float size;
    private Texture texture;

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getSize() {
        return size;
    }

    public Target() {
        x = 600.0f;
        y = 600.0f;
        size = 40.0f;
        texture = new Texture("target.png");
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, x - 20, y - 20, 20, 20,
                size, size, 2.0f, 2.0f,
                0, 0, 0, 1323, 1323, false, false);
    }

    public void dispose() {
        texture.dispose();
    }
}
