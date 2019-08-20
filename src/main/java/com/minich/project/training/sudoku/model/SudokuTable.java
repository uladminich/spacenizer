package com.minich.project.training.sudoku.model;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class SudokuTable {
    public static final int SUDOKU_TABLE_SIZE = 9;
    public static final int SUDOKU_EMPTY_VALUE = 0;

    private final int[][] map = new int[SUDOKU_TABLE_SIZE][SUDOKU_TABLE_SIZE];
    private Map<String, Boolean> initialData = new HashMap<>();

    public SudokuTable() {
    }

    public SudokuTable(final HttpServletRequest request) {
        for (int i = 0; i < SUDOKU_TABLE_SIZE; i++) {
            for (int j = 0; j < SUDOKU_TABLE_SIZE; j++) {
                String cellValue = request.getParameter(i + "_" + j);
                int value = SUDOKU_EMPTY_VALUE;
                if (StringUtils.isNotEmpty(cellValue)) {
                    value = Integer.parseInt(cellValue);
                    initialData.put(i + "_" + j, true);
                }
                map[i][j] = value;
            }
        }

    }

    public int[][] getMap() {
        return map;
    }

    public Map<String, Boolean> getInitialData() {
        return initialData;
    }
}
