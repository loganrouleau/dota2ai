package qlearning;

public enum State {
    A(10, -500, 500),
    B(20, 0, 0),
    C(30, 500, -500),
    D(40, -500, -500),
    E(50, -1500, -500),
    F(60, -1000, -1000),
    G(70, -500, -1500);

    private int hashedState;
    private int x;
    private int y;

    State(int hashedState, int x, int y) {
        this.hashedState = hashedState;
        this.x = x;
        this.y = y;
    }

    public int getHashedState() {
        return hashedState;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
