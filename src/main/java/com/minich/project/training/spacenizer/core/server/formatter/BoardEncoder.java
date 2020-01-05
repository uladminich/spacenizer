package com.minich.project.training.spacenizer.core.server.formatter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minich.project.training.spacenizer.model.Board;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

@Slf4j
public class BoardEncoder implements Encoder.Text<Board> {

    public static final String EMPTY_JSON = "{}";
    private ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public String encode(Board board) throws EncodeException {
        try {
            return MAPPER.writeValueAsString(board);
        } catch (JsonProcessingException e) {
            log.error("Error during parse json", e);
            return EMPTY_JSON;
        }
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
        log.debug("BoardEncoder init method.");
    }

    @Override
    public void destroy() {
        log.debug("BoardEncoder destroy method.");
    }
}
