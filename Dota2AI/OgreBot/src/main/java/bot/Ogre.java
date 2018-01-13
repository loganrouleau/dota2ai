package bot;

import se.lu.lucs.dota2.framework.bot.BaseBot;
import se.lu.lucs.dota2.framework.bot.BotCommands.LevelUp;
import se.lu.lucs.dota2.framework.bot.BotCommands.Select;
import se.lu.lucs.dota2.framework.game.BaseEntity;
import se.lu.lucs.dota2.framework.game.ChatEvent;
import se.lu.lucs.dota2.framework.game.Hero;
import se.lu.lucs.dota2.framework.game.World;

public class Ogre extends BaseBot {
	private enum Mode {
		ENABLED, DISABLED
	}

	private static final String MY_HERO_NAME = "npc_dota_hero_ogre_magi";

	private static float distance(BaseEntity a, BaseEntity b) {
		final float[] posA = a.getOrigin();
		final float[] posB = b.getOrigin();
		return distance(posA, posB);
	}

	private static float distance(float[] posA, float[] posB) {
		return (float) Math.hypot(posB[0] - posA[0], posB[1] - posA[1]);
	}

	private Mode mode = Mode.DISABLED;
	private boolean shouldMoveToCenter = false;

	public Ogre() {
		System.out.println("Creating Ogre");
	}

	@Override
	public LevelUp levelUp() {
		LEVELUP.setAbilityIndex(-1);
		//System.out.println("LevelUp " + LEVELUP.getAbilityIndex());
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
	public void reset() {
		System.out.println("Resetting");
	}

	@Override
	public Select select() {
		SELECT.setHero(MY_HERO_NAME);
		return SELECT;
	}

	@Override
	public Command update(World world) {

		if (mode == Mode.DISABLED) {
			return NOOP;
		}

		final int myIndex = world.searchIndexByName(MY_HERO_NAME);
		if (myIndex < 0) {
			System.out.println("I'm dead?");
			return NOOP;
		}

		final Hero lina = (Hero) world.getEntities().get(myIndex);

		float[] location = lina.getOrigin();
		System.out.format("Current coordinate {%.0f, %.0f}%n", location[0], location[1]);

		if(shouldMoveToCenter){
		    return moveTo(-500, -500);
        }

        System.out.println("Reached end of update. Not doing anything");
        return NOOP;
	}

	private Command moveTo(float x, float y){
        MOVE.setX(x);
        MOVE.setY(y);
        MOVE.setZ(0);
        System.out.format("Moving to coordinate {%.0f, %.0f}%n", x, y);
        return MOVE;
    }

}
