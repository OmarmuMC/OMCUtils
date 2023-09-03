package live.omarmu.omcutils.features.staffchat;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.Set;
import java.util.UUID;

import live.omarmu.omcutils.OMCUtilsCommand;
import live.omarmu.omcutils.utils.Permissions;

public class StaffChatCommand implements OMCUtilsCommand {
  // TODO: Migrate these to a locale file somewhere
  static Component onMessage = MiniMessage.miniMessage().deserialize("<#f9a8d4> ⛊ Staff chat toggled on");
  static Component offMessage = MiniMessage.miniMessage().deserialize("<#f9a8d4> ⛊ Staff chat toggled off");
  static Component mutedMessage = MiniMessage.miniMessage().deserialize("<#f9a8d4> ⛊ Staff chat is now muted");
  static Component unmutedMessage = MiniMessage.miniMessage().deserialize("<#f9a8d4> ⛊ Staff chat is no longer muted");

  public static BrigadierCommand createBrigadierCommand() {
    Set<UUID> chatToggled = StaffChatState.getInstance().ChatToggled;
    Set<UUID> chatMuted = StaffChatState.getInstance().ChatMuted;

    var staffChatNode = LiteralArgumentBuilder
      .<CommandSource>literal("staffchat")
      .requires(source -> source instanceof Player)
      .requires(source -> source.hasPermission(Permissions.STAFF_CHAT_SEE))
      .executes(context -> {
        // Toggle staff chat
        Player player = (Player) context.getSource();

        boolean isToggled = chatToggled.contains(player.getUniqueId());
        if (isToggled) {
          // Turn off
          chatToggled.remove(player.getUniqueId());
          player.sendMessage(offMessage);
        } else {
          // Turn on
          chatToggled.add(player.getUniqueId());
          player.sendMessage(onMessage);
        }

        return Command.SINGLE_SUCCESS;
      })
      .then(
        LiteralArgumentBuilder
          .<CommandSource>literal("on")
          .requires(source -> source instanceof Player)
          .requires(source -> source.hasPermission(Permissions.STAFF_CHAT_USE))
          .executes(context -> {
            Player player = (Player) context.getSource();
            chatToggled.add(player.getUniqueId());
            player.sendMessage(onMessage);

            return Command.SINGLE_SUCCESS;
          })
      )
      .then(
        LiteralArgumentBuilder
          .<CommandSource>literal("off")
          .requires(source -> source instanceof Player)
          .requires(source -> source.hasPermission(Permissions.STAFF_CHAT_USE))
          .executes(context -> {
            Player player = (Player) context.getSource();
            chatToggled.remove(player.getUniqueId());
            player.sendMessage(offMessage);

            return Command.SINGLE_SUCCESS;
          })
      )
      .then(
        LiteralArgumentBuilder
          .<CommandSource>literal("mute")
          .requires(source -> source instanceof Player)
          .requires(source -> source.hasPermission(Permissions.STAFF_CHAT_SEE))
          .executes(context -> {
            Player player = (Player) context.getSource();
            if (chatMuted.contains(player.getUniqueId())) {
              chatMuted.remove(player.getUniqueId());
              player.sendMessage(unmutedMessage);
            } else {
              chatMuted.add(player.getUniqueId());
              player.sendMessage(mutedMessage);
            }

            return Command.SINGLE_SUCCESS;
          })
      )
      .build();

    // BrigadierCommand implements Command
    return new BrigadierCommand(staffChatNode);
  }

  @Override
  public String getName() {
    return "staffchat";
  }

  @Override
  public String[] getAliases() {
    return new String[] { "sc" };
  }
}
