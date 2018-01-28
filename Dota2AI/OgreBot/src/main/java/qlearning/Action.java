package qlearning;

import se.lu.lucs.dota2.framework.bot.Bot.Command;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public interface Action {
    enum Actions {
        LEFT,
        RIGHT,
        FORWARD,
        BACK ;

        private static final List<Actions> VALUES =
                Collections.unmodifiableList(Arrays.asList(values()));
        private static final int SIZE = VALUES.size();
        private static final Random RANDOM = new Random();

        public static Actions randomAction()  {
            return VALUES.get(RANDOM.nextInt(SIZE));
        }
    }

    Command moveLeft();

    Command moveRight();

    Command moveForward();

    Command moveBack();

}
