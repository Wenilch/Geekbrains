package ru.geekbrains.dungeon.game;

import com.badlogic.gdx.Gdx;
import ru.geekbrains.dungeon.helpers.Assets;
import ru.geekbrains.dungeon.game.GameController;

import java.util.Random;

public class Hero extends Unit {
    private String name;
    private Long money;
    private Random random;

    public Hero(GameController gc) {
        super(gc, 1, 1, 10);
        this.name = "Sir Mullih";
        this.hpMax = 100;
        this.hp = this.hpMax;
        this.texture = Assets.getInstance().getAtlas().findRegion("knight");
        this.textureHp = Assets.getInstance().getAtlas().findRegion("hp");
        this.money = 0L;
        this.textureHpAlpha = 0.2f;
        this.random = new Random();
    }

    public void update(float dt) {
        super.update(dt);
        if (Gdx.input.justTouched() && canIMakeAction()) {
            Monster m = gc.getUnitController().getMonsterController().getMonsterInCell(gc.getCursorX(), gc.getCursorY());
            if (m != null && canIAttackThisTarget(m)) {
                attack(m);
            } else {
                goTo(gc.getCursorX(), gc.getCursorY());
            }
        }
    }

    public String getName() {
        return name;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    @Override
    public boolean takeDamage(int amount) {
        if (amount > 0 && textureHpAlpha != 1.0f) {
            textureHpAlpha = 1.0f;
        }

        return super.takeDamage(amount);
    }

    @Override
    public void attack(Unit target) {
        super.attack(target);

        if(target.getHp() == 0 && this.hp > 0){
            money += random.nextInt(3) + 1;
        }
    }
}
