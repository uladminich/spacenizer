package com.minich.project.training.spacenizer.core.server.impl;

import com.minich.project.training.spacenizer.core.server.ConnectionManager;
import com.minich.project.training.spacenizer.core.server.ServerWebSocket;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@Component
public class ConnectionManagerImpl implements ConnectionManager {

    private final Map<String, Set<ServerWebSocket>> rooms = new ConcurrentHashMap<>();
    private Set<String> gameTokens = new CopyOnWriteArraySet<>();

    @Override
    public Set<String> getGameTokens() {
        return gameTokens;
    }

    @Override
    public void setGameTokens(Set<String> newIds) {
        this.gameTokens = newIds;
    }

    @Override
    public Map<String, Set<ServerWebSocket>> getRooms() {
        return rooms;
    }

    @Override
    public void addRoom(@NonNull String roomId, @NonNull Set<ServerWebSocket> listeners) {
        rooms.put(roomId, listeners);
    }

    @Override
    public Set<ServerWebSocket> getRoomById(@NonNull String roomId) {
        return rooms.get(roomId);
    }
}
