package com.minich.project.training.spacenizer.core.server;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint(value = "/board/{id}")
public class ServerWebSocket {
    private Session session;
    private String roomId;
    private static CopyOnWriteArraySet<String> gameTokens = new CopyOnWriteArraySet<>(); // TODO move to separate component
    private static Map<String, Set<ServerWebSocket>> rooms = new ConcurrentHashMap<>(); // TODO ^

    @OnOpen
    public void onOpen(Session session) throws IOException {
        this.session = session;
        String path = session.getRequestURI().getPath();
        if (StringUtils.isNotEmpty(path)) {
            String[] pathSplitted = path.split("/");
            String id = pathSplitted[pathSplitted.length - 1];
            if (StringUtils.isNotEmpty(path)) {
                roomId = id;
                if (!gameTokens.contains(roomId)) {
                    // close connection if it's not present in room list
                    session.close();
                }
                Set<ServerWebSocket> listeners = rooms.get(roomId);
                if (Objects.isNull(listeners)) {
                    listeners = new CopyOnWriteArraySet<>();
                    listeners.add(this);
                    rooms.put(roomId, listeners);
                } else {
                    listeners.add(this);
                }
            }
        }
    }

    @OnMessage
    public void onMessage(String message) {
        broadcast(message);
    }

    @OnClose
    public void onClose(Session session) {
        rooms.get(roomId).remove(this);
    }

    private void broadcast(String message) {
        for (ServerWebSocket listener : rooms.get(roomId)) {
            listener.sendMessage(message);
        }
    }

    private void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public CopyOnWriteArraySet<String> getGameTokens() {
        return gameTokens;
    }

    public void setGameTokens(CopyOnWriteArraySet<String> gameTokens) {
        this.gameTokens = gameTokens;
    }
}