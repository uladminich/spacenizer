package com.minich.project.training.spacenizer.core.service.impl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minich.project.training.spacenizer.core.service.MockProvider;
import com.minich.project.training.spacenizer.model.Board;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

@Component
@Slf4j
public class MockProviderImpl implements MockProvider {

    private ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public boolean isMockId(String gameId) {
        return MOCK_GAME_IDS.contains(gameId);
    }

    @Override
    public Board getMockGame(String gameId) {
        String path = String.format(MOCK_BASE_PATH, gameId);
        try {
            File file = ResourceUtils.getFile(path);
            return MAPPER.readValue(file, Board.class);
        } catch (FileNotFoundException e) {
            log.error("Error during loading mock file.", e);
        } catch (JsonParseException | JsonMappingException e) {
            log.error("Error during parsing mock file.", e);
        } catch (IOException e) {
            log.error("Error during get mock file.", e);
        }
        return null;
    }
}
