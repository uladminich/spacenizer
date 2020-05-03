package com.minich.project.training.spacenizer.core.server;

import java.util.Map;
import java.util.Set;

public interface ConnectionManager {

    Set<String> getGameTokens();

    void setGameTokens(Set<String> newIds);

    Map<String, Set<ServerWebSocket>> getRooms();

    void addRoom(String roomId, Set<ServerWebSocket> listeners);

    Set<ServerWebSocket> getRoomById(String roomId);

    void removeRoomById(String id);
}
