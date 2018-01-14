package bot;

import se.lu.lucs.dota2.framework.bot.BaseBot;
import se.lu.lucs.dota2.framework.bot.BotCommands.Reset;
import se.lu.lucs.dota2.framework.bot.BotCommands.LevelUp;
import se.lu.lucs.dota2.framework.bot.BotCommands.Select;
import se.lu.lucs.dota2.framework.game.ChatEvent;
import se.lu.lucs.dota2.framework.game.Hero;
import se.lu.lucs.dota2.framework.game.World;

public class Ogre extends BaseBot {
	private enum Mode {
		ENABLED, DISABLED
	}

	private static final String MY_HERO_NAME = "npc_dota_hero_ogre_magi";
	private Mode mode = Mode.DISABLED;
	private boolean shouldMoveToCenter = false;
	private boolean shouldReset = false;
	private int positionTolerance = 25;

	public Ogre() {
		System.out.println("Creating Ogre");
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
		    if(mode == Mode.DISABLED){
		        System.out.println("Mode must be enabled to issue a command");
            } else{
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

		final Hero lina = (Hero) world.getEntities().get(myIndex);

		float[] location = lina.getOrigin();
		int x = (int) location[0];
		int y = (int) location[1];
        int nearestX = Math.round(location[0]/500)*500;
        int nearestY = Math.round(location[1]/500)*500;
		System.out.format("Current coordinate {%d, %d}%n", x, y);
        System.out.format("Closest coordinate {%d, %d}%n", nearestX, nearestY);

		if(shouldMoveToCenter){
		    if(Math.abs(-500-x) > positionTolerance || Math.abs(-500-y) > positionTolerance){
                return moveTo(-500, -500);
            } else{
		        shouldMoveToCenter = false;
		        System.out.println("Arrived at center");
		        return NOOP;
            }
        }

        System.out.println("Reached end of update. Not doing anything");
        return NOOP;
	}

	private Command moveTo(int x, int y){
        MOVE.setX((float) x);
        MOVE.setY((float) y);
        MOVE.setZ(0);
        System.out.format("Moving to coordinate {%d, %d}%n", x, y);
        return MOVE;
    }

}
