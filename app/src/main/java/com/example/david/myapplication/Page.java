package com.example.david.myapplication;

import java.util.HashSet;

/**
 * Created by david on 14/02/2018.
 */

public class Page {
    public int[][] pageMatrix;
    public int width;
    public int height;
    public int compterIsland;
    public HashSet<Integer> colors;
    private int[][] graphe;

    public Page(int[][] page) {
        pageMatrix = page;
        width = pageMatrix[0].length;
        height = pageMatrix.length;

        creategraphe();
    }

    public Page(Page p) {
        width = p.width;
        height = p.height;
        compterIsland = p.getcompterIsland();
        pageMatrix = new int[height][width];
        graphe = new int[height][width];
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                pageMatrix[r][c] = p.pageMatrix[r][c];
                graphe[r][c] = p.getgraphe(r, c);
            }
        }
    }


    public int getgraphe(int r, int c) {
        return graphe[r][c];
    }


    public int getcompterIsland() {
        return compterIsland;
    }

    public int getColorCount() {
        return colors.size();
    }

    private void creategraphe() {
        initialiserGraphe();
        compterIsland = 1;
        colors = new HashSet<Integer>();
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                int couleurIslandActuelle = pageMatrix[r][c];
                if (verifierEtCocher(r, c, couleurIslandActuelle)) {
                    compterIsland++;
                    colors.add(couleurIslandActuelle);
                }
            }
        }
        compterIsland -= 1;
    }

    private boolean verifierEtCocher(int r, int c, int islandColor) {
        if ((c < 0 || r < 0 || c >= width || r >= height)) {
            return false;
        }
        if (getgraphe(r, c) != 0) {
            return false;
        } else {
            if (pageMatrix[r][c] == islandColor) {
                graphe[r][c] = compterIsland;
            } else {
                return false;
            }
            verifierEtCocher(r, c + 1, islandColor);
            verifierEtCocher(r + 1, c, islandColor);
            verifierEtCocher(r, c - 1, islandColor);
            verifierEtCocher(r - 1, c, islandColor);
            return true;
        }
    }


    private void initialiserGraphe() {
        graphe = new int[height][width];
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                graphe[r][c] = 0;
            }
        }
    }


    public Page island(int islandID, int islandColor) {
        Page p = new Page(this);
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                if (getgraphe(r, c) == islandID) {
                    p.pageMatrix[r][c] = islandColor;
                }
            }
        }
        p.creategraphe();
        return p;
    }



    public int getIslandColor(int islandID) {
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                if (getgraphe(r, c) == islandID) {
                    return pageMatrix[r][c];
                }
            }
        }
        return 0;
    }
}
