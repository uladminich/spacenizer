package com.minich.project.training.spacenizer.core.server.formatter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minich.project.training.spacenizer.model.Board;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.IOException;

@Slf4j
public class BoardDecoder implements Decoder.Text<Board> {

    private ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public Board decode(String s) throws DecodeException {
        try {
            return MAPPER.readValue(s, Board.class);
        } catch (IOException e) {
            log.error("Errod during decode message from client.", e);
            return null;
        }
    }

    @Override
    public boolean willDecode(String s) {
        return StringUtils.isNotEmpty(s);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
        log.debug("BoardDecoder init method");
    }

    @Override
    public void destroy() {
        log.debug("BoardDecoder destroy method");
    }
}
