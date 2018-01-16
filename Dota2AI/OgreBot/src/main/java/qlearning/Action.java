package qlearning;

public enum Action {
    LEFT(1),
    RIGHT(2),
    FORWARD(3),
    BACK(4);

    private int hashedAction;

    Action(int hashedAction) {
        this.hashedAction = hashedAction;
    }

    public int getHashedAction() {
        return hashedAction;
    }

}
