package ru.geekbrains.dungeon.units;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import ru.geekbrains.dungeon.GameController;

import java.util.Random;

public class Monster extends Unit {
    private float aiBrainsImplseTime;
    private Unit target;
    private float rageRadius = 5.0f;
    private Random random = new Random();

    public Monster(TextureAtlas atlas, GameController gc) {
        super(gc, 5, 2, 10);
        this.texture = atlas.findRegion("monster");
        this.textureHp = atlas.findRegion("hp");
        this.hp = -1;
    }

    public void activate(int cellX, int cellY) {
        this.cellX = cellX;
        this.cellY = cellY;
        this.targetX = cellX;
        this.targetY = cellY;
        this.hpMax = 10;
        this.hp = hpMax;
        this.target = gc.getUnitController().getHero();
    }

    public void update(float dt) {
        super.update(dt);
        if (canIMakeAction()) {
            if (isStayStill()) {
                aiBrainsImplseTime += dt;
            }
            if (aiBrainsImplseTime > 0.4f) {
                aiBrainsImplseTime = 0.0f;
                if (canIAttackThisTarget(target)) {
                    attack(target);
                } else {
                    tryToMove();
                }
            }
        }
    }

    public void tryToMove() {
        if (targetInRageRadius()) {
            goToTarget();
        } else {
            goToFreeRandomCell();
        }
    }

    private void goToTarget() {
        int bestX = -1, bestY = -1;
        float bestDst = 10000;

        for (int i = cellX - 1; i <= cellX + 1; i++) {
            for (int j = cellY - 1; j <= cellY + 1; j++) {
                if (Math.abs(cellX - i) + Math.abs(cellY - j) == 1 && gc.getGameMap().isCellPassable(i, j) && gc.getUnitController().isCellFree(i, j)) {
                    float dst = processDistanceToUnit(target);
                    if (dst < bestDst) {
                        bestDst = dst;
                        bestX = i;
                        bestY = j;
                    }
                }
            }
        }

        goTo(bestX, bestY);
    }

    private void goToFreeRandomCell() {
        int bestX = cellX + composeRandomStep();
        int bestY = cellY + composeRandomStep();
        while (!(gc.getGameMap().isCellPassable(bestX, bestY) && gc.getUnitController().isCellFree(bestX, bestY))) {
            bestX = cellX + composeRandomStep();
            bestY = cellY + composeRandomStep();
        }

        goTo(bestX, bestY);
    }

    private int composeRandomStep() {
        return random.nextInt(3) - 1;
    }

    private float processDistanceToUnit(Unit unit) {
        return (float) Math.sqrt((cellX - unit.getCellX()) * (cellX - unit.getCellX()) + (cellY - unit.getCellY()) * (cellY - unit.getCellY()));
    }

    private boolean targetInRageRadius() {
        return processDistanceToUnit(target) <= rageRadius;
    }
}
