package com.example.david.myapplication;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Created by david on 14/02/2018.
 */

public class Solver {
    private Page page;
    private int stepsRequired;
    private Page[] pageStep;
    public boolean solved;
    public long combinationsTested;
    private long timeElapsed;

    public Solver(Page p) {
        page = p;
        stepsRequired = 0;
        pageStep = null;
        solved = false;
        combinationsTested = 0;
        timeElapsed = 0;
    }

    public Page getPage() {
        return page;
    }

    public int getStepsRequired() {
        return stepsRequired;
    }

    public Page[] getPageStep() {
        return pageStep;
    }

    public void solve() {
        long startTime = System.currentTimeMillis();
        combinationsTested = 0;
        int height = page.getHeight();
        int width = page.getWidth();
        int islandCount = page.getIslandCount();
        int colorCount = page.getColorCount();
        System.out.printf("Solving %d by %d matrix with %d islands and %d colors.\n", height, width, islandCount,
                colorCount);
        while (!solved) {
            stepsRequired++;
            System.out.printf("Working on %d step solutions...", stepsRequired);
            pageStep = new Page[stepsRequired];
            solved = solve(page, 0);
            if (solved) {
                System.out.print("Done! ");
            } else {
                System.out.print("No solutions found. ");
            }
            timeElapsed = System.currentTimeMillis() - startTime;
            System.out.println();
            System.out.printf("Time Elapsed: %dms ", timeElapsed);
            System.out.printf("Combinations Tested: %d\n", combinationsTested);
        }
    }

    public void print() {
        if (solved) {
            getPage().print();
            for (int i = 0; i < stepsRequired; i++) {
                System.out.printf(" ||\n");
                System.out.printf(" ||\n");
                System.out.printf(" || Step %d\n", i + 1);
                System.out.printf("\\||/\n");
                System.out.printf(" \\/\n");
                if (pageStep[i] != null) {
                    pageStep[i].print();
                }
            }
            System.out.printf("Time Elapsed: %dms\n", timeElapsed);
        } else {
            System.out.println("Not solved.");
        }
    }

    public void printToFile(String fileName) throws IOException {
        FileOutputStream outFile = null;
        PrintStream outStream = null;

        outFile = new FileOutputStream(fileName);
        outStream = new PrintStream(outFile);
        PrintStream stdout = System.out;
        System.setOut(outStream);
        print();
        System.setOut(stdout);
        outStream.close();
        outFile.close();

    }

    public boolean solve(Page p, int stepNumber) {
        combinationsTested++;
        int islandCount = p.getIslandCount();
        if (islandCount == 1) {
            return true;
        } else if (p.getColorCount() - 1 > stepsRequired - stepNumber) {
            return false;
        }
        for (int islandID = 1; islandID <= p.getIslandCount(); islandID++) {
            for (int islandColor : p.colors) {
                if (islandColor != p.getIslandColor(islandID)) {
                    Page flippedPage = p.flipIsland(islandID, islandColor);
                    if (flippedPage.getIslandCount() < islandCount) {
                        if (solve(flippedPage, stepNumber + 1)) {
                            pageStep[stepNumber] = flippedPage;
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
