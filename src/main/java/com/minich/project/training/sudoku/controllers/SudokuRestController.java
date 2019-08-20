package com.minich.project.training.sudoku.controllers;

import com.minich.project.training.sudoku.model.SudokuTable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import java.util.Objects;

import static com.minich.project.training.sudoku.model.SudokuTable.SUDOKU_EMPTY_VALUE;
import static com.minich.project.training.sudoku.model.SudokuTable.SUDOKU_TABLE_SIZE;

@RestController
public class SudokuRestController {

    @RequestMapping("/api/sudoku/solution/get")
    public ResponseEntity<SudokuTable> getSudokuSolution(HttpServletRequest request) {
        SudokuTable table = new SudokuTable(request);
        int[][] map = table.getMap();
        System.out.println("Before solve:");
        for (int i = 0; i < SUDOKU_TABLE_SIZE; i++) {
            for (int j = 0; j < SUDOKU_TABLE_SIZE; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
        solve(table);
        System.out.println("After solve:");

        for (int i = 0; i < SUDOKU_TABLE_SIZE; i++) {
            for (int j = 0; j < SUDOKU_TABLE_SIZE; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
        return new ResponseEntity<SudokuTable>(table, HttpStatus.OK);
    }

    private void solve(SudokuTable table) {
        int[][] map = table.getMap();
        int possibleValue = 1;
        int counter = 0;
        for (int i = 0; i < SUDOKU_TABLE_SIZE; i++) {
            for (int j = 0; j < SUDOKU_TABLE_SIZE; j++) {
                counter++;
                if (map[i][j] != SUDOKU_EMPTY_VALUE) {
                    continue;
                }
                boolean back = false;
                for (/* init out of cycle definition to save prev value*/ ; possibleValue <= 9; possibleValue++) {
                    boolean validHorizontal = isValidHorizontal(map[i], possibleValue);
                    boolean validVertical = isValidVertical(map, possibleValue, j);
                    boolean validSmallSquare = isValidSmallSquare(map, j, i, possibleValue);

                    if (validHorizontal && validVertical && validSmallSquare) {
                        map[i][j] = possibleValue;
                        possibleValue = 1;
                        back = false;
                        break;
                    } else {
                        back = true;
                    }
                }
                if (back) {

                    String key;
                    boolean isNineValue;
                    do {
                        isNineValue = false;
                        if (j == 0 && i > 0) {
                            j = 8;
                            i--;
                        } else if (j > 0){
                            j--;
                        }
                        key = i + "_" + j;
                        if (map[i][j] == 9 && Objects.isNull(table.getInitialData().get(key))) {
                            map[i][j] = SUDOKU_EMPTY_VALUE;
                            isNineValue = true;
                        }

                    } while (Objects.nonNull(table.getInitialData().get(key)) || isNineValue);

                    if (map[i][j] <= 8) {
                        possibleValue = map[i][j] + 1;
                    } else {
                        possibleValue = 1;

                    }
                    map[i][j] = SUDOKU_EMPTY_VALUE;
                    if (j == 0 && i > 0) {
                        j = 8;
                        i--;
                    } else {
                        j--;
                    }
                }
            }
        }
        System.out.println("Total iteration amount: " + counter);
    }

    private boolean isValidHorizontal(int[] row, int value) {
        for (int i : row) {
            if (i == value) { // value is present in the row, check next;
                return false;
            }
        }
        return true;
    }

    private boolean isValidVertical(int[][] map, int value, int verticalIndex) {
        for (int i = 0; i < SUDOKU_TABLE_SIZE; i++) {
            if (map[i][verticalIndex] == value) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidSmallSquare(int[][] map, int verticalIndex, int horizontalIndex, int value) {
        // find small square vertical coordinate shift
        int verticalShift = 0;
        if (verticalIndex > 2 && verticalIndex < 6) {
            verticalShift = 3;
        } else if (verticalIndex >= 6) {
            verticalShift = 6;
        }

        int horizontalShift = 0;
        if (horizontalIndex > 2 && horizontalIndex < 6) {
            horizontalShift = 3;
        } else if (horizontalIndex >= 6) {
            horizontalShift = 6;
        }
        for (int i = horizontalShift; i < 3 + horizontalShift; i++) {
            for (int j = verticalShift; j < 3 + verticalShift; j++) {
                if (map[i][j] == value) {
                    return false;
                }
            }
        }
        return true;
    }
}
