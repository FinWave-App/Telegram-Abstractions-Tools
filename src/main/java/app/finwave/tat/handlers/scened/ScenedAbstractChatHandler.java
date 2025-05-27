package app.finwave.tat.handlers.scened;

import app.finwave.tat.BotCore;
import app.finwave.tat.handlers.command.CommandAbstractChatHandler;
import app.finwave.tat.scene.BaseScene;
import app.finwave.tat.utils.Stack;

import java.util.HashMap;

public abstract class ScenedAbstractChatHandler extends CommandAbstractChatHandler {
    protected BaseScene activeScene;
    protected HashMap<String, BaseScene> scenes = new HashMap<>();

    public ScenedAbstractChatHandler(BotCore core, long chatId) {
        super(core, chatId);
    }

    public void registerScene(String name, BaseScene scene) {
        scenes.put(name, scene);
    }

    public boolean startScene(String name, Object arg) {
        if (!scenes.containsKey(name))
            return false;

        stopActiveScene();

        BaseScene newScene = scenes.get(name);

        if (arg != null)
            newScene.start(arg);
        else
            newScene.start();

        activeScene = newScene;

        return true;
    }

    public boolean startScene(String name) {
        return startScene(name, null);
    }

    public boolean stopActiveScene() {
        if (activeScene == null)
            return false;

        activeScene.stop();
        activeScene = null;

        return true;
    }
}
