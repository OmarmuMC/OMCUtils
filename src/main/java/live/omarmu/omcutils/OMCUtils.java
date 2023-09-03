package live.omarmu.omcutils;

import com.google.inject.Inject;
import com.moandjiezana.toml.Toml;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import live.omarmu.omcutils.commands.GlobalBroadcast;
import org.slf4j.Logger;
import org.tomlj.TomlParseResult;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Plugin(
  id = "omcutils",
  name = "OMCUtils",
  version = BuildConstants.VERSION,
  description = "Utilities Plugin for OmarmuMC",
  url = "omarmu.live",
  authors = { "Xy <xy@omarmu.live>" }
)
public class OMCUtils {
  public static OMCUtils INSTANCE;

  @Inject
  public static Logger logger;
  @Inject
  public static ProxyServer proxy;
  @DataDirectory
  public static Path dataDirectory;

  public TomlParseResult config;

  public OMCUtils() {
    super();
    INSTANCE = this;
  }

  @Subscribe
  public void onProxyInitialization(ProxyInitializeEvent event) {
    // Load config
    try {
      config = new ConfigManager().loadConfig();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    // Register all commands
    CommandManager commandManager = proxy.getCommandManager();

    // TODO: Abstractize this utilizing OMCUtilsCommand
    CommandMeta commandMeta = commandManager.metaBuilder("globalbroadcast")
      .aliases("gcast")
      .plugin(this)
      .build();

    commandManager.register(commandMeta, new GlobalBroadcast());

    logger.info("OMCUtils has been loaded!");
  }
}
