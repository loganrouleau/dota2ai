package qlearning;

import se.lu.lucs.dota2.framework.bot.Bot.Command;

public interface Action {
    Command moveLeft();

    Command moveRight();

    Command moveForward();

    Command moveBack();
}
