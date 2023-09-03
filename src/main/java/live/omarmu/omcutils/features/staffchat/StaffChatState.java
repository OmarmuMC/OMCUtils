package live.omarmu.omcutils.features.staffchat;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class StaffChatState {
  private static StaffChatState INSTANCE;

  public Set<UUID> ChatToggled = new HashSet<>();
  public Set<UUID> ChatMuted = new HashSet<>();

  public static StaffChatState getInstance() {
    if (INSTANCE == null) INSTANCE = new StaffChatState();
    return INSTANCE;
  }
}
