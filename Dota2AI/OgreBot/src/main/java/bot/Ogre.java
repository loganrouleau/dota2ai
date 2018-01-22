package bot;

import qlearning.Action;
import qlearning.LookupTable;
import se.lu.lucs.dota2.framework.bot.BaseBot;
import se.lu.lucs.dota2.framework.bot.BotCommands.Reset;
import se.lu.lucs.dota2.framework.bot.BotCommands.LevelUp;
import se.lu.lucs.dota2.framework.bot.BotCommands.Select;
import se.lu.lucs.dota2.framework.game.ChatEvent;
import se.lu.lucs.dota2.framework.game.Hero;
import se.lu.lucs.dota2.framework.game.World;

public class Ogre extends BaseBot implements Action {

    private enum Mode {
		ENABLED, DISABLED
	}

	private static final String MY_HERO_NAME = "npc_dota_hero_ogre_magi";
	private Mode mode = Mode.ENABLED;
	private boolean shouldMoveToCenter = true;
	private boolean shouldReset = false;
	private int x;
	private int y;
	private int targetX;
	private int targetY;
	private int positionTolerance = 75;
	private LookupTable lut;

	public Ogre() {
	    System.out.println("Creating Ogre");
	    lut =  new LookupTable();
	}

	@Override
	public LevelUp levelUp() {
		LEVELUP.setAbilityIndex(-1);
		return LEVELUP;
	}

    @Override
    public void onChat(ChatEvent e) {
        switch (e.getText()) {
            case "go":
                mode = Mode.ENABLED;
                break;
            case "stop":
                mode = Mode.DISABLED;
                break;
            case "reset":
                mode = Mode.DISABLED;
                shouldReset = true;
                break;
            case "move center":
                if (mode == Mode.DISABLED) {
                    System.out.println("Mode must be enabled to issue a command");
                } else {
                    shouldMoveToCenter = true;
                }
                break;
        }
    }

    @Override
    public Reset reset() {
        mode = Mode.DISABLED;
        shouldReset = false;
        return RESET;
    }

	@Override
	public Select select() {
		SELECT.setHero(MY_HERO_NAME);
		return SELECT;
	}

	@Override
	public Command update(World world) {

	    if(shouldReset){
	        return reset();
        }

		if (mode == Mode.DISABLED) {
		    System.out.println("Disabled");
			return NOOP;
		}

		final int myIndex = world.searchIndexByName(MY_HERO_NAME);
		if (myIndex < 0) {
			System.out.println("Dead");
			return NOOP;
		}

		final Hero ogre = (Hero) world.getEntities().get(myIndex);

		int[] location = ogre.getOrigin();
		x = location[0];
		y = location[1];

        getState(x, y);

        System.out.println("Target x, y : " + targetX + ", " + targetY);

        if (moveInProgress()) {
            System.out.println("Move not complete");
            return moveTo(targetX, targetY);
        }

        if (shouldMoveToCenter) {
            System.out.println("Moving to center");
            shouldMoveToCenter = false;
            return moveTo(0, 0);
        }

        if (Math.random() > 0.5) {
            return moveLeft();
        } else {
            return moveRight();
        }
    }

    private void getState(int x, int y) {
        int nearestX = (int) Math.round((double) x / 500) * 500;
        int nearestY = (int) Math.round((double) y / 500) * 500;
        System.out.format("Current coordinate {%d, %d}%n", x, y);
        System.out.format("Closest coordinate {%d, %d}%n", nearestX, nearestY);
    }

	private Command moveTo(int x, int y){
	    targetX = x;
	    targetY = y;
        MOVE.setXY(targetX, targetY);
        System.out.format("Moving to coordinate {%d, %d}%n", targetX, targetY);
        return MOVE;
    }

    private boolean moveInProgress() {
        return Math.abs(targetX - x) > positionTolerance || Math.abs(targetY - y) > positionTolerance;
    }

    @Override
    public Command moveLeft() {
        System.out.println("Moving left");
        return moveTo(x - 500, y);
    }

    @Override
    public Command moveRight() {
        System.out.println("Moving right");
        return moveTo(x + 500, y);
    }

    @Override
    public Command moveForward() {
        System.out.println("Moving forward");
        return moveTo(x, y + 500);
    }

    @Override
    public Command moveBack() {
        System.out.println("Moving back");
        return moveTo(x, y - 500);
    }

}
