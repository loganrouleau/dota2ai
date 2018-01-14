package se.lu.lucs.dota2.framework.bot;

import se.lu.lucs.dota2.framework.bot.BotCommands.LevelUp;
import se.lu.lucs.dota2.framework.bot.BotCommands.Select;
import se.lu.lucs.dota2.framework.bot.BotCommands.Reset;
import se.lu.lucs.dota2.framework.game.ChatEvent;
import se.lu.lucs.dota2.service.FrameListener;

public interface Bot extends FrameListener {
    interface Command {
        enum COMMAND_CODE {
            NOOP, MOVE, ATTACK, CAST, BUY, SELL, USE_ITEM, SELECT, RESET
        }

        COMMAND_CODE getCommand();
    }

    LevelUp levelUp();

    void onChat( ChatEvent e );

    Reset reset();

    Select select();

}
