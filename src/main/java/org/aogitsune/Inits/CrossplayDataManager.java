package org.aogitsune.Inits;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.instance.InstanceContainer;
import org.aogitsune.Class.CrossplayDataClass;
import org.aogitsune.NPCs.PlaybackPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CrossplayDataManager {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private static class NPCDetails {
        private PlaybackPlayer entity;
        private long update;
    }

    private final InstanceContainer instanceContainer;
    private Map<String, NPCDetails> robloxPlayers = new HashMap<>();
    public CrossplayDataManager(InstanceContainer instanceContainer) {
        this.instanceContainer = instanceContainer;
    }

    public void executeData(CrossplayDataClass data) {
        for (CrossplayDataClass.Chat chat : data.Chats) {
            long delay = ((System.currentTimeMillis() - chat.t)  /1000);
            scheduler.schedule(() -> {
                Component name = Component.text("[RB] ", NamedTextColor.RED)
                        .append(Component.text(chat.p + ": ", NamedTextColor.WHITE)
                                .hoverEvent(HoverEvent.showText(Component.text("User name: " + chat.n + "\n" +
                                        "User Id: #" + chat.i
                                ))));

                instanceContainer.sendMessage(
                        name.append(Component.text(chat.m, NamedTextColor.WHITE))
                );
            }, delay, TimeUnit.MILLISECONDS);

        }

        long baseTime = data.Movements.getFirst().t;

        for (CrossplayDataClass.Movement movement : data.Movements) {
            long delay = movement.t - baseTime;

            scheduler.schedule(() -> {
                if (!robloxPlayers.containsKey(movement.n)) createNPC(movement.n);

                Entity player = robloxPlayers.get(movement.n).entity;
                player.teleport(new Pos(movement.x, movement.y, movement.z));
                player.setView(movement.ya, movement.pi);
            }, delay, TimeUnit.MILLISECONDS);
        }
    }

    private void createNPC(String name) {
        PlaybackPlayer player = new PlaybackPlayer(name,
                "ewogICJ0aW1lc3RhbXAiIDogMTc0NTE3OTQzMTEyMSwKICAicHJvZmlsZUlkIiA6ICJhYzI2NjllOWRlNzg0MDVjOTYzZGU0YzM0ZGYwZGU1MyIsCiAgInByb2ZpbGVOYW1lIiA6ICJJbnRlcmdhbGF0dGljbyIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS80MTNhMjQ5YTYxOTQ1YzhmNDdkYzE5NDdhNWI1N2Y1YzRmYzU3ODVjNzBjYzM5MjM4ZTlmYWMwOTdkMjY4OTkwIgogICAgfQogIH0KfQ==",
                "CSEs07ZsT1aimm4Vgzn/klD6/a94T/zp/dGIUcSq3+lzsJ/kjR7biGrfAEAqkSSH+to1P6Zvh2DgFGNCj5ainKr82Nf5lLqUpftSDA9xQHjL85+Ph0S6KZCi273C7wZgeeZaT24COZwlXnobytZt8+1/xPi9T2zNI+hv8YGp39LgzgGOhOnlQVAHBTm6eGaHbbtDh+k17mAQ2aiQaGXf0viB0phGsJew3MWaHma008k1XSJmkbT3riW1ZNGbWH0oLwJOf57sMsmCpe0oYuGlB8ZFx00OzR37etHYvibrztYuuNfgQhnwFRiDYyTHbTKy/gfdKmBzmhB4nMuJppYt1sL+7Vm9XGGhUPhheu3vst223Dgz2rYktAy10J6z0MIf1+KU1ofjpOYx+GYlrbjsPeducty6XeVnZ7i6z77eubRD3day/qMr0Wmj1OFg5nB9Pp13RxvqhCvO4PeKSueJDo2ari/eq/45zoy5J3yLEQUqd+SS5CglLeLlHv7xDuMawE6W3aNEKshBg6zni2xV/K2/KWKWcFGby+gkgX76ddA1N6e4mzWHVvqxU1vpbu/+1NCkiPRjYcClmJnBxpvyfbor2wxtlVtCgt4wj7YdW0pQ/XYsr0uT9FMlWBUO/PKbS6mJkAicBToJnj6n10+mSBTYRThhfRbRBCFQr0F5NKU="
        );

        player.setInstance(instanceContainer, new Pos(0.5,0,0.5));

        NPCDetails npcDetails = new NPCDetails();
        npcDetails.update = System.currentTimeMillis();
        npcDetails.entity = player;

        robloxPlayers.put(name, npcDetails);
    };
}
