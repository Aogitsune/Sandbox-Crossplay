package org.aogitsune.Instances;

import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerChatEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.block.Block;
import org.aogitsune.Class.CrossplayDataClass;
import org.aogitsune.Inits.CrossplayDataManager;
import org.aogitsune.Inits.HttpInit;

import java.util.HashMap;

public class GameInstance {
    private final CrossplayDataClass crossplayDataClass = new CrossplayDataClass();

    public GameInstance(HashMap<String, String> argumentList,
                        InstanceContainer instanceContainer,
                        GlobalEventHandler globalEventHandler) {

        instanceContainer.setGenerator(unit -> unit.modifier().fillHeight(-1, 0, Block.GRASS_BLOCK));

        globalEventHandler.addListener(PlayerChatEvent.class, event -> {
            CrossplayDataClass.Chat chat = new CrossplayDataClass.Chat();
            chat.p = "[MC]";
            chat.n = event.getPlayer().getUsername();
            chat.m = event.getRawMessage();
            chat.t = System.currentTimeMillis();

            crossplayDataClass.Chats.add(chat);
        });

        CrossplayDataManager crossplayDataManager = new CrossplayDataManager(instanceContainer);

        try {
            new HttpInit(crossplayDataClass, crossplayDataManager, instanceContainer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
