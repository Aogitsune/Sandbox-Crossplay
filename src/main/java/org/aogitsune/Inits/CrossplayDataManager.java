package org.aogitsune.Inits;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.instance.InstanceContainer;
import org.aogitsune.Class.CrossplayDataClass;

public class CrossplayDataManager {
    private final InstanceContainer instanceContainer;
    public CrossplayDataManager(InstanceContainer instanceContainer) {
        this.instanceContainer = instanceContainer;
    }

    public void executeData(CrossplayDataClass data) {
        for (CrossplayDataClass.Chat chat : data.Chats) {
            Component name = Component.text("[RB] ", NamedTextColor.RED)
                    .append(Component.text(chat.p + ": ", NamedTextColor.WHITE)
                    .hoverEvent(HoverEvent.showText(Component.text("User name: " + chat.n + "\n" +
                            "User Id: #" + chat.i
                    ))));

            instanceContainer.sendMessage(
                    name.append(Component.text(chat.m, NamedTextColor.WHITE))
            );
        }
    }
}
