package de.lifemytouch.ansi.staff;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class StaffChatService {

    private final Set<UUID> activeStaffChatUsers = ConcurrentHashMap.newKeySet();

    public boolean toggle(UUID playerId) {
        if(activeStaffChatUsers.remove(playerId)) {
            return false;
        }

        activeStaffChatUsers.add(playerId);
        return true;
    }

    public boolean isActive(UUID playerId) {
        return activeStaffChatUsers.contains(playerId);
    }

    public void disable(UUID playerId) {
        activeStaffChatUsers.remove(playerId);
    }

}
