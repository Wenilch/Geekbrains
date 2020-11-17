package ru.geekbrains.dungeon.units;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import ru.geekbrains.dungeon.GameController;

import java.util.concurrent.ThreadLocalRandom;

public class Monster extends Unit {
    public boolean isActive() {
        return hp > 0;
    }

    public Monster(TextureAtlas atlas, GameController gc) {
        super(gc, 5, 2, 10);
        this.texture = atlas.findRegion("monster");
        this.textureHp = atlas.findRegion("hp");
        this.hp = -1;
        this.damage = 5;
    }

    public void activate(int cellX, int cellY) {
        this.cellX = cellX;
        this.cellY = cellY;
        this.hpMax = 10;
        this.hp = hpMax;
    }

    public void update(float dt) {
    }

    @Override
    public boolean dealDamage(Unit unit) {
        int randomInt = ThreadLocalRandom.current().nextInt(1, 5);
        if (randomInt == 1) {
            return unit.takeDamage(damage);
        }

        return false;
    }
}
