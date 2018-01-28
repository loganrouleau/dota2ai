package qlearning;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static qlearning.Action.*;

public class LookupTable {
    private Map<String, Double> lut = new TreeMap<>();
    private static final double EPSILON = 0.15;
    private static final double LEARNING_RATE = 0.05;
    private static final double DISCOUNT_FACTOR = 0.9;

    public LookupTable(){
        initialize();
        System.out.println("Lookup table initialized");
        printLut();
    }

    private void initialize() {
        for (int x = -1000; x <= 1000; x += 500) {
            for (int y = -1000; y <= 1000; y += 500) {
                for (Actions action : Actions.values()) {
                    lut.put(actionStateKey(new State(x, y), action), 0.01 * Math.random());
                }
            }
        }
    }

    public Actions findAction(State currentState){
        if (Math.random() < EPSILON){
            System.out.println("Performing random action");
            return Actions.randomAction();
        }

        double maxNextQValue = -Double.MAX_VALUE;
        double currentQValue;
        Actions bestAction = Actions.randomAction();
        for(Actions possibleAction : Actions.values()){
            currentQValue = lut.get(actionStateKey(currentState, possibleAction));
            if (currentQValue > maxNextQValue){
                maxNextQValue = currentQValue;
                bestAction = possibleAction;
            }
        }

        return bestAction;
    }

    public void update(State currentState, Actions currentAction, double reward){
        double currentQValue = lut.get(actionStateKey(currentState, currentAction));
        double maxNextQValue = -Double.MAX_VALUE;

        for(Actions possibleAction : Actions.values()){
            maxNextQValue = Math.max(maxNextQValue, lut.get(actionStateKey(currentState, possibleAction)));
        }

        lut.put(actionStateKey(currentState, currentAction),
                (1 - LEARNING_RATE) * currentQValue + LEARNING_RATE * (reward + DISCOUNT_FACTOR * maxNextQValue));

        printLut();
    }

    private String actionStateKey(State state, Actions action){
        return String.format("%5d %5d %-7s", state.getX(), state.getY(), action.name());
    }

    private void printLut() {
        System.out.println("--------------------------");
        for (Map.Entry<String, Double> entry : lut.entrySet()) {
            System.out.format("%s %.4f%n", entry.getKey(), entry.getValue());
        }
        System.out.println("--------------------------");
    }

    public void writeLut() {
        try {
            BufferedWriter outputStream = new BufferedWriter(new FileWriter("lut.txt"));

            for (Map.Entry<String, Double> entry : lut.entrySet()) {
                String line = String.format("%s %.4f%n", entry.getKey(), entry.getValue());
                outputStream.write(line);
            }

            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
