package com.example.david.myapplication;

/**
 * Created by david on 14/02/2018.
 */

public class SolverThread {
    public Page page;
    public int etapeRequises;
    public Page[] EtapeDeLaPage;
    public boolean resolu;
    public long combinaisonsTester;
    private GrilleView grilleView;


    public SolverThread(GrilleView grilleView) {
        this.page = new Page(grilleView.getTableau());
        etapeRequises = 0;
        EtapeDeLaPage = null;
        resolu = false;
        combinaisonsTester = 0;
        this.grilleView = grilleView;

    }


    public SolverThread(Page p) {
        this.page = p;
        etapeRequises = 0;
        EtapeDeLaPage = null;
        resolu = false;
        combinaisonsTester = 0;

    }

    public void solve() {
        combinaisonsTester = 0;
        while (!resolu) {
            etapeRequises++;
            EtapeDeLaPage = new Page[etapeRequises];
            resolu = solve(page, 0);
            if (resolu) {
            } else {
            }
        }
    }

    public boolean solve(Page p, int stepNumber) {
        combinaisonsTester++;
        int islandCount = p.compterIsland;
        if (islandCount == 1) {
            return true;
        } else if (p.getColorCount() - 1 > etapeRequises - stepNumber) {
            return false;
        }
        for (int islandID = 1; islandID <= p.compterIsland; islandID++) {
            for (int islandColor : p.colors) {
                if (islandColor != p.getIslandColor(islandID)) {
                    Page page2 = p.island(islandID, islandColor);
                    if (page2.compterIsland < islandCount) {
                        if (solve(page2, stepNumber + 1)) {
                            EtapeDeLaPage[stepNumber] = page2;
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

}
