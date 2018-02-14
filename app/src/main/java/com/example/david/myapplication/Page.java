package com.example.david.myapplication;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by david on 14/02/2018.
 */

public class Page {
    private int[][] pageMatrix;
    private int[][] islandMap;
    private int width;
    private int height;
    private int islandCount;
    public HashSet<Integer> colors;

    public Page(int[][] page) {
        pageMatrix = page;
        width = getPageMatrix(0).length;
        height = getPageMatrix().length;

        createIslandMap();
    }

    public Page(Page p) {
        width = p.getWidth();
        height = p.getHeight();
        islandCount = p.getIslandCount();
        pageMatrix = new int[height][width];
        islandMap = new int[height][width];
        for (int r = 0; r < getHeight(); r++) {
            for (int c = 0; c < getWidth(); c++) {
                pageMatrix[r][c] = p.getPageMatrix(r, c);
                islandMap[r][c] = p.getIslandMap(r, c);
            }
        }
    }

    public static int[][] fileToArray(String fileName) throws IOException {
        ArrayList<ArrayList<Integer>> rows = new ArrayList<ArrayList<Integer>>();
        FileInputStream inFile = null;
        BufferedReader reader = null;
        try {
            inFile = new FileInputStream(fileName);
            reader = new BufferedReader(new InputStreamReader(inFile));

            String line;
            while ((line = reader.readLine()) != null) {
                ArrayList<Integer> row = new ArrayList<Integer>();
                for (int i = 0; i < line.length(); i++) {
                    row.add(Character.getNumericValue(line.charAt(i)));
                }
                rows.add(row);
            }
        } finally {
            if (inFile != null) {
                reader.close();
                inFile.close();
            }
        }
        int height = rows.size();
        int[][] m = new int[height][];
        for (int r = 0; r < height; r++) {
            int width = rows.get(r).size();
            m[r] = new int[width];
            for (int c = 0; c < width; c++) {
                m[r][c] = rows.get(r).get(c);
            }
        }
        return m;
    }

    public int[][] getPageMatrix() {
        return pageMatrix;
    }

    public int[] getPageMatrix(int r) {
        return pageMatrix[r];
    }

    public int getPageMatrix(int r, int c) {
        return pageMatrix[r][c];
    }

    public int[][] getIslandMap() {
        return islandMap;
    }

    public int[] getIslandMap(int r) {
        return islandMap[r];
    }

    public int getIslandMap(int r, int c) {
        return islandMap[r][c];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getIslandCount() {
        return islandCount;
    }

    public int getColorCount() {
        return colors.size();
    }

    private void createIslandMap() {
        initializeIslandMap();
        islandCount = 1;
        colors = new HashSet<Integer>();
        for (int r = 0; r < getHeight(); r++) {
            for (int c = 0; c < getWidth(); c++) {
                int currentIslandColor = getPageMatrix(r, c);
                if (checkAndMark(r, c, currentIslandColor)) {
                    islandCount++;
                    colors.add(currentIslandColor);
                }
            }
        }
        islandCount -= 1;
    }

    private boolean checkAndMark(int r, int c, int islandColor) {
        if (boundLimitExceeded(r, c)) {
            return false;
        }
        if (getIslandMap(r, c) != 0) {
            return false;
        } else {
            if (getPageMatrix(r, c) == islandColor) {
                markPoint(r, c, islandCount, islandColor);
            } else {
                return false;
            }
            checkAndMark(r, c + 1, islandColor);
            checkAndMark(r + 1, c, islandColor);
            checkAndMark(r, c - 1, islandColor);
            checkAndMark(r - 1, c, islandColor);
            return true;
        }
    }

    private boolean boundLimitExceeded(int r, int c) {
        return (c < 0 || r < 0 || c >= getWidth() || r >= getHeight());
    }

    private void markPoint(int r, int c, int islandID, int currentIslandColor) {
        islandMap[r][c] = islandID;
    }

    private void initializeIslandMap() {
        islandMap = new int[height][width];
        for (int r = 0; r < getHeight(); r++) {
            for (int c = 0; c < getWidth(); c++) {
                islandMap[r][c] = 0;
            }
        }
    }

    public void printMatrix(int[][] m) {
        System.out.println("=======");
        for (int r = 0; r < m.length; r++) {
            for (int c = 0; c < m[0].length; c++) {
                System.out.printf("%d ", m[r][c]);
            }
            System.out.println();
        }
        System.out.println("=======");
    }

    public void print() {
        printMatrix(getPageMatrix());
    }

    public Page flipIsland(int islandID, int islandColor) {
        Page p = new Page(this);
        for (int r = 0; r < getHeight(); r++) {
            for (int c = 0; c < getWidth(); c++) {
                if (getIslandMap(r, c) == islandID) {
                    p.flipPoint(r, c, islandColor);
                }
            }
        }
        p.createIslandMap();
        return p;
    }

    private void flipPoint(int r, int c, int islandColor) {
        pageMatrix[r][c] = islandColor;
    }

    public int getIslandColor(int islandID) {
        for (int r = 0; r < getHeight(); r++) {
            for (int c = 0; c < getWidth(); c++) {
                if (getIslandMap(r, c) == islandID) {
                    return getPageMatrix(r, c);
                }
            }
        }
        return 0;
    }
}
