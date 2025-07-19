package org.aogitsune.Utils;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import org.aogitsune.Class.CrossplayDataClass;

public class PlayersDataUtil {
    public static CrossplayDataClass.Movement getPlayerData(Player player) {
        CrossplayDataClass.Movement movement = new CrossplayDataClass.Movement();

        movement.n = player.getUsername();
        movement.t = System.currentTimeMillis();

        Pos pos = player.getPosition();
        movement.x = pos.x();
        movement.y = pos.y();
        movement.z = pos.z();

        return movement;
    }
}
