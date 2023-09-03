package live.omarmu.omcutils;


import com.velocitypowered.api.plugin.annotation.DataDirectory;
import org.tomlj.Toml;
import org.tomlj.TomlParseResult;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManager {
  public TomlParseResult loadConfig(Path dataDirectory) throws IOException {
    // Create data directory if it doesn't exist
    if (!dataDirectory.toFile().exists()) dataDirectory.toFile().mkdirs();

    Path configFilePath = dataDirectory.resolve("config.toml");
    File configFile = configFilePath.toFile();

    // If config file doesn't exist, copy one from resources folder
    if (!configFile.exists()) {
      InputStream input = getClass().getResourceAsStream("/" + configFile.getName());

      assert input != null;
      Files.copy(input, configFile.toPath());
    }

    return Toml.parse(configFilePath);
  }
}
