package org.aogitsune.Instances;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerChatEvent;
import net.minestom.server.event.player.PlayerUseItemOnBlockEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.block.Block;
import org.aogitsune.Class.CrossplayDataClass;
import org.aogitsune.Inits.CrossplayDataManager;
import org.aogitsune.Inits.HttpInit;
import org.aogitsune.NPCs.PlaybackPlayer;

import java.net.DatagramPacket;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

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
