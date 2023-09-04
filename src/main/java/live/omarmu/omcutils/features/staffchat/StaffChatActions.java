package live.omarmu.omcutils.features.staffchat;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import live.omarmu.omcutils.OMCUtils;
import live.omarmu.omcutils.utils.Permissions;

public class StaffChatActions {
  private final ProxyServer proxy = OMCUtils.proxy;
  private final Set<UUID> chatMuted = StaffChatState.getInstance().ChatMuted;

  public void broadcastStaffMessage(Player author, String message) {
    Optional<ServerConnection> currentServer = author.getCurrentServer();
    if (currentServer.isEmpty()) throw new Error("Unable to broadcast staff message - player not connected to server!");
    String serverName = currentServer.get().getServerInfo().getName();

    TextComponent chatMessage = formStaffChatMessage(serverName, author.getUsername(), message);

    OMCUtils.logger.info(chatMessage.toString());
    for (Player player : proxy.getAllPlayers()) {
      if (!player.hasPermission(Permissions.STAFF_CHAT_SEE)) continue;
      if (chatMuted.contains(player.getUniqueId())) continue;

      player.sendMessage(chatMessage);
    }
  }

  private TextComponent formStaffChatMessage(String serverName, String username, String content) {
    String s = convertServerName(serverName);

    return Component
      .empty()
      .append(MiniMessage.miniMessage().deserialize("<bold><#831843>【<#f9a8d4>⛊ " + s + "<#831843>】</bold>"))
      .append(MiniMessage.miniMessage().deserialize("" + username))
      .append(MiniMessage.miniMessage().deserialize(" ⏩ <#f9a8d4>" + content));
  }

  private String convertServerName(String name) {
    // TODO: Consider hashmapping later maybe
    switch (name.toUpperCase()) {
      case "LOBBY":
        return "LOBBY";
      case "SURVIVAL":
        return "SURV";
      case "MINIGAME":
        return "M.GAME";
      default:
        return name;
    }
  }
}
