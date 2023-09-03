package live.omarmu.omcutils.features.staffchat;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.Set;
import java.util.UUID;

import live.omarmu.omcutils.OMCUtilsCommand;
import live.omarmu.omcutils.utils.Permissions;

public class StaffChatCommand implements OMCUtilsCommand {
  public static BrigadierCommand createBrigadierCommand(final ProxyServer proxy) {
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
          player.sendMessage(Component.text("Staff chat toggled off"));
        } else {
          // Turn on
          chatToggled.add(player.getUniqueId());
          player.sendMessage(Component.text("Staff chat toggled on"));
        }

        return Command.SINGLE_SUCCESS;
      })
      .then(
        RequiredArgumentBuilder
          .<CommandSource, String>argument("on", StringArgumentType.string())
          .requires(source -> source instanceof Player)
          .requires(source -> source.hasPermission(Permissions.STAFF_CHAT_USE))
          .executes(context -> {
            Player player = (Player) context.getSource();
            chatToggled.add(player.getUniqueId());
            player.sendMessage(
              MiniMessage
                .miniMessage()
                .deserialize("Staff chat toggled on")
            );

            return Command.SINGLE_SUCCESS;
          })
      )
      .then(
        RequiredArgumentBuilder
          .<CommandSource, String>argument("off", StringArgumentType.string())
          .requires(source -> source instanceof Player)
          .requires(source -> source.hasPermission(Permissions.STAFF_CHAT_USE))
          .executes(context -> {
            Player player = (Player) context.getSource();
            chatToggled.remove(player.getUniqueId());
            player.sendMessage(
              MiniMessage
                .miniMessage()
                .deserialize("Staff chat toggled off")
            );

            return Command.SINGLE_SUCCESS;
          })
      )
      .then(
        RequiredArgumentBuilder
          .<CommandSource, String>argument("mute", StringArgumentType.string())
          .requires(source -> source instanceof Player)
          .requires(source -> source.hasPermission(Permissions.STAFF_CHAT_SEE))
          .executes(context -> {
            Player player = (Player) context.getSource();
            if (chatMuted.contains(player.getUniqueId())) {
              chatMuted.remove(player.getUniqueId());
              player.sendMessage(
                MiniMessage.miniMessage().deserialize("Staff chat is now no longer muted")
              );
            } else {
              chatMuted.add(player.getUniqueId());
              player.sendMessage(
                MiniMessage.miniMessage().deserialize("Staff chat is now muted")
              );
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
