package live.omarmu.omcutils.commands;

import com.velocitypowered.api.command.RawCommand;
import com.velocitypowered.api.proxy.Player;
import live.omarmu.omcutils.OMCUtils;
import live.omarmu.omcutils.OMCUtilsCommand;
import live.omarmu.omcutils.utils.Permissions;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public final class GlobalBroadcast implements RawCommand, OMCUtilsCommand {

  @Override
  public void execute(final Invocation invocation) {
    String message = invocation.arguments();

    for (Player players : OMCUtils.proxy.getAllPlayers()) {
      players.sendMessage(
        Component
          .text()
          .appendNewline()
          .append(MiniMessage.miniMessage().deserialize("<bold><red>[GLOBAL BROADCAST]</red></bold> "))
          .append(MiniMessage.miniMessage().deserialize(message))
          .appendNewline()
      );
    }
  }

  @Override
  public boolean hasPermission(final Invocation invocation) {
    return invocation.source().hasPermission(Permissions.GLOBAL_BROADCAST);
  }

  @Override
  public String getName() {
    return "globalbroadcast";
  }

  @Override
  public String[] getAliases() {
    return new String[] { "gcast" };
  }
}
