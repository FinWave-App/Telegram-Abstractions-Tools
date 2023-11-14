package su.knst.tat.handlers.scened;

import su.knst.tat.BotCore;
import su.knst.tat.handlers.ChatHandler;
import su.knst.tat.handlers.command.CommandChatHandler;
import su.knst.tat.scene.BaseScene;

import java.util.HashMap;

public abstract class ScenedChatHandler extends CommandChatHandler {
    protected BaseScene activeScene;
    protected HashMap<String, BaseScene> scenes = new HashMap<>();

    public ScenedChatHandler(BotCore core, long chatId) {
        super(core, chatId);
    }

    public boolean switchScene(String name, Object arg) {
        if (!scenes.containsKey(name))
            return false;

        if (activeScene != null)
            activeScene.stop();

        activeScene = scenes.get(name);

        if (arg != null)
            activeScene.start(arg);
        else
            activeScene.start();

        return true;
    }

    public boolean switchScene(String name) {
        return switchScene(name, null);
    }
}
