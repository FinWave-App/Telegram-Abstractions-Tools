package app.finwave.tat.handlers.scened;

import app.finwave.tat.BotCore;
import app.finwave.tat.handlers.command.CommandAbstractChatHandler;
import app.finwave.tat.scene.AbstractScene;

import java.util.HashMap;

public abstract class ScenedAbstractChatHandler extends CommandAbstractChatHandler {
    protected AbstractScene activeScene;
    protected HashMap<String, AbstractScene> scenes = new HashMap<>();

    public ScenedAbstractChatHandler(BotCore core, long chatId) {
        super(core, chatId);
    }

    public void registerScene(AbstractScene scene) {
        scenes.put(scene.name(), scene);
    }

    public boolean startScene(String name, Object arg) {
        if (!scenes.containsKey(name))
            return false;

        stopScene();

        AbstractScene newScene = scenes.get(name);

        activeScene = newScene;
        eventHandler.pushChild(newScene.getEventHandler());

        newScene.start(arg);

        return true;
    }

    public boolean startScene(String name) {
        return startScene(name, null);
    }

    public boolean stopScene() {
        if (activeScene == null)
            return false;

        activeScene.stop();
        eventHandler.popChild();

        activeScene = null;

        return true;
    }
}
