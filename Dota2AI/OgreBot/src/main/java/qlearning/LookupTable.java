package qlearning;

import java.util.*;

import static qlearning.Action.*;

public class LookupTable {
    private Map<Integer, Double> Lut = new TreeMap<>();
    private static final double LEARNING_RATE = 0.05;
    private static final double DISCOUNT_FACTOR = 0.9;

    public LookupTable(){
        initialize();
        System.out.println("Lookup table initialized");
        this.printLut();
    }

    private void initialize() {
        for (State state : State.values()) {
            for (Action action : getPossibleActions(state)) {
                Lut.put(getHashedActionState(state, action), 0.01 * Math.random());
            }
        }
    }

    private int getHashedActionState(State state, Action action){
        return state.getHashedState() + action.getHashedAction();
    }

    private void update(State currentState, Action currentAction, double reward){
        double currentQValue = lookup(currentState, currentAction);
        double maxNextQValue = -Double.MAX_VALUE;

        for(Action possibleAction : getPossibleActions(currentState)){
            maxNextQValue = Math.max(maxNextQValue, lookup(currentState, possibleAction));
        }

        Lut.put(getHashedActionState(currentState, currentAction),
                (1 - LEARNING_RATE) * currentQValue + LEARNING_RATE * (reward + DISCOUNT_FACTOR * maxNextQValue));
    }

    private double lookup(State state, Action action){
        return Lut.get(getHashedActionState(state, action));
    }

    private List<Action> getPossibleActions(State state){
        List<Action> possibleActions = new ArrayList<>();

        switch (state){
            case A:
                possibleActions.add(RIGHT);
                break;
            case B:
                possibleActions.add(LEFT);
                possibleActions.add(RIGHT);
                possibleActions.add(BACK);
                break;
            case C:
                possibleActions.add(LEFT);
                break;
            case D:
                possibleActions.add(FORWARD);
                possibleActions.add(BACK);
                break;
            case E:
                possibleActions.add(RIGHT);
                break;
            case F:
                possibleActions.add(LEFT);
                possibleActions.add(RIGHT);
                possibleActions.add(FORWARD);
                break;
            case G:
                possibleActions.add(LEFT);
                break;
        }

        return possibleActions;
    }

    public void printLut() {
        System.out.println("---------");
        for (Map.Entry<Integer, Double> entry : Lut.entrySet()) {
            System.out.format("%d %.4f%n", entry.getKey(), entry.getValue());
        }
        System.out.println("---------");
    }

}
