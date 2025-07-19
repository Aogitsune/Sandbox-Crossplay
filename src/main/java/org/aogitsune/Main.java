package org.aogitsune;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.LightingChunk;
import org.jetbrains.annotations.Nullable;
import org.aogitsune.Instances.GameInstance;
import org.aogitsune.Instances.LobbyInstance;

import java.util.HashMap;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        new Main().start(args);
    }

    public static int port;
    private GlobalEventHandler globalEventHandler;
    private CommandManager commandManager;
    private InstanceManager instanceManager;

    public GlobalEventHandler getGlobalEventHandler() {return globalEventHandler;}
    public CommandManager getCommandManager() {return commandManager;}
    public InstanceManager getInstanceManager() {return instanceManager;}

    private void start(@Nullable String[] args) {
        HashMap<String, String> argumentList = convertArgument(args);

        MinecraftServer minecraftServer = MinecraftServer.init();

        this.globalEventHandler = MinecraftServer.getGlobalEventHandler();
        this.commandManager = MinecraftServer.getCommandManager();
        this.instanceManager = MinecraftServer.getInstanceManager();

        InstanceContainer instanceContainer = instanceManager.createInstanceContainer();
        instanceContainer.setChunkSupplier(LightingChunk::new);

        globalEventHandler.addListener(AsyncPlayerConfigurationEvent.class, event -> {
            final Player player = event.getPlayer();
            event.setSpawningInstance(instanceContainer);
            player.setRespawnPoint(new Pos(0, 1, 0));
        });

        String serverName = argumentList.getOrDefault("server", "");
        if (serverName.startsWith("map") || serverName.isEmpty()) {
            new GameInstance(argumentList, instanceContainer, globalEventHandler);
        } else {
            switch (serverName) {
                case "game" -> new GameInstance(argumentList, instanceContainer, globalEventHandler);
                case "lobby" -> new LobbyInstance(argumentList, instanceContainer, globalEventHandler);
                default -> throw new RuntimeException("Unable to get server type. Did you misspell it?");
            }
        }

        port = Integer.parseInt(argumentList.getOrDefault("port", "25565"));

        MojangAuth.init();
        minecraftServer.start("0.0.0.0", port);
    }

    private HashMap<String, String> convertArgument(String[] args) {
        if (args == null) return null;
        HashMap<String, String> map = new HashMap<>();

        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].startsWith("--")) {
                String key = args[i].substring(2);
                String value = args[i + 1];
                if (!value.startsWith("--")) {
                    map.put(key, value);
                    i++;
                } else {
                    map.put(key, "");
                }
            }
        }

        if (args.length > 0 && args[args.length - 1].startsWith("--")) {
            String key = args[args.length - 1].substring(2);
            map.putIfAbsent(key, "");
        }
        return map;
    }
}