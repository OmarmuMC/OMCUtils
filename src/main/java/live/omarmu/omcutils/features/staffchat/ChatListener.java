package live.omarmu.omcutils.features.staffchat;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;

import java.util.Set;
import java.util.UUID;

import live.omarmu.omcutils.utils.Permissions;

public class ChatListener {
  private final Set<UUID> chatToggled = StaffChatState.getInstance().ChatToggled;
  private final StaffChatActions action = new StaffChatActions();

  @Subscribe
  void onPlayerChat(PlayerChatEvent e) {
    Player player = e.getPlayer();
    String message = e.getMessage();

    if (!player.hasPermission(Permissions.STAFF_CHAT_USE)) return; // Permission
    if (message.startsWith("!")) {
      e.setResult(PlayerChatEvent.ChatResult.message(message.substring(1)));
      return;
    }
    if (!chatToggled.contains(player.getUniqueId()) && !message.startsWith(".")) return;

    action.broadcastStaffMessage(player, message.startsWith(".") ? message.substring(1) : message);
    e.setResult(PlayerChatEvent.ChatResult.denied());
  }
}
