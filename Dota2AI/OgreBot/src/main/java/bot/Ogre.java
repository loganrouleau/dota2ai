package bot;

import qlearning.LookupTable;
import qlearning.State;
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
	private int x;
	private int y;
	private int positionTolerance = 75;
	private LookupTable lut;
	private State currentState;
	private State nextState = State.F;
	private boolean moveInProgress;

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

		if (moveInProgress){
		    return NOOP;
        }

		final Hero ogre = (Hero) world.getEntities().get(myIndex);

		float[] location = ogre.getOrigin();
		x = (int) location[0];
		y = (int) location[1];

        currentState = getState(x, y);
        System.out.println("Current state: " + currentState);

		if(shouldMoveToCenter){
		    if(Math.abs(-1000-x) > positionTolerance || Math.abs(-1000-y) > positionTolerance){
                return moveTo(-1000, -1000);
            } else{
		        shouldMoveToCenter = false;
		        System.out.println("Arrived at center");
		        return NOOP;
            }
        }

        if(arrivedAtGoalState()){
		    moveInProgress = false;
		    System.out.println("Arrive at state: " + nextState);
        } else{
            return NOOP;
        }

        if(Math.random() > 0.5){
            System.out.println("Moving left");
		    return moveLeft();
        } else{
            System.out.println("Moving right");
            return moveRight();
        }
	}

	private boolean arrivedAtGoalState(){
        if(Math.abs(nextState.getX()-x) > positionTolerance || Math.abs(nextState.getY()-y) > positionTolerance){
            return false;
        } else{
            return true;
        }
    }

	private Command moveTo(int x, int y){
        MOVE.setX((float) x);
        MOVE.setY((float) y);
        MOVE.setZ(0);
        System.out.format("Moving to coordinate {%d, %d}%n", x, y);
        return MOVE;
    }

    private Command moveLeft(){
	    nextState = getState(x - 500, y + 500);

	    if(nextState == null){
	        System.out.println("Invalid next state");
	        return NOOP;
        }

        moveInProgress = true;
	    MOVE.setX((float) x - 500);
	    MOVE.setY((float) y + 500);
	    MOVE.setZ(0);
	    System.out.format("Moving left");
	    return MOVE;
    }

    private Command moveRight(){
        nextState = getState(x + 500, y - 500);

        if(nextState == null){
            System.out.println("Invalid next state");
            return NOOP;
        }

        moveInProgress = true;
        MOVE.setX((float) x + 500);
        MOVE.setY((float) y - 500);
        MOVE.setZ(0);
        System.out.format("Moving right");
        return MOVE;
    }

    private State getState(int x, int y) {
        int nearestX = Math.round(x / 500) * 500;
        int nearestY = Math.round(y / 500) * 500;
        //System.out.format("Current coordinate {%d, %d}%n", x, y);
        //System.out.format("Closest coordinate {%d, %d}%n", nearestX, nearestY);
        for (State state : State.values()) {
            if (nearestX == state.getX() && nearestY == state.getY()) {
                return state;
            }
        }
        return null;
    }

}
