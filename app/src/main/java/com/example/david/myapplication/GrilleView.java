package com.example.david.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by david on 07/02/2018.
 */

public class GrilleView extends SurfaceView implements SurfaceHolder.Callback {
    private int nombreDeCoupJoue = 0;
    private List<int[][]>  coupPrecedent = new ArrayList<int[][]>();

    private List<int[][]>  coupSuivant = new ArrayList<int[][]>();
    Canvas canvas;
    private int height;
    private int width;
    private String colours;
    private String numColours;
    private String gold;
    private String silver;
    private String bronze;
    private SurfaceHolder sh;
    private Context context;
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintLine = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int tableau[][];
    private List<Integer> nombreDeCouleus = new ArrayList<Integer>();
    private int couleurSelectionne = 1;
    private int widthTailleEcran;
    private int heightTailleEcran;

    public GrilleView(Context context, String width, String height, String colours, String numColours, String gold, String silver, String bronze) {
        super(context);
        sh = getHolder();
        sh.addCallback(this);
        this.context = context;
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);
        paintLine.setColor(Color.BLACK);
        paintLine.setStyle(Paint.Style.FILL);
        paintLine.setStrokeWidth(1f);
        this.width = Integer.parseInt(width);
        this.height = Integer.parseInt(height);
        this.colours = colours;
        this.numColours = numColours;
        this.gold = gold;
        this.silver = silver;
        this.bronze = bronze;
        //récupère la grille qui est stocké dans la variable numColor et fait un tableau à 2 dimensions tableau
        remplirTableau();
        recupererNombreDeCouleurs();


    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {


        //dessine la grille
        dessinerGrille();

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int w, int h) {
        Context context = getContext();
        widthTailleEcran = w;
        heightTailleEcran = h;
        CharSequence text = "width : " + w + " Height : " + h;
        int duration = Toast.LENGTH_SHORT;
        surfaceCreated(this.getHolder());
        Toast toast = Toast.makeText(context, text, duration);
        //  toast.show();



    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        Context context = getContext();
        CharSequence text = "X : " + event.getX() + " Y : " + event.getY();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        // toast.show();

        press(event.getX(), event.getY());
        return super.onTouchEvent(event);

    }

    public void press(float x, float y) {


        if (estDanslaGrille(x, y)) {

            int ij[] = recupererPositionCouleur(x, y);
            modifierLaCouleurParLaCouleurSelectionnee(couleurSelectionne, ij[0], ij[1]);
            surfaceCreated(this.getHolder());

            if (leJeuEstTermine()) {
                CharSequence text = "Bravo, vous avez fini le niveau";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        } else  {
            ChoisirLeChangementDeCouleur(x,y);
        //    if(!coupPrecedent.isEmpty()) {
                //   choisirLeRetourEnArriere(x,y);
                if(!leJeuEstTermine()) {
                    solve();
                    choisirLeCoupSuivant(x, y);
                }
          //  }
        }

    }

    private void choisirLeRetourEnArriere(float x, float y) {
        int tailleCarree = (int) ((double) widthTailleEcran / 10.6666);
        int i = 0;
        if (x > widthTailleEcran - tailleCarree ) {
            CharSequence text = "Bravo, vous avez retournez en arriere";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            tableau =  coupPrecedent.get(coupPrecedent.size()-1);
            coupPrecedent.remove(coupPrecedent.size()-1);
            nombreDeCoupJoue--;
            surfaceCreated(this.getHolder());
        }

    }


    private void choisirLeCoupSuivant(float x, float y) {
        int tailleCarree = (int) ((double) widthTailleEcran / 10.6666);
        int i = 0;
        if (x > widthTailleEcran - tailleCarree ) {
            CharSequence text = "Bravo, vous avez joué le prochain coup";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            tableau =  coupSuivant.get(0);
            coupSuivant.remove(0);
            nombreDeCoupJoue++;
            surfaceCreated(this.getHolder());
        }

    }

    private void ChoisirLeChangementDeCouleur(float x, float y) {

        int tailleCarree = (int) ((double) widthTailleEcran / 10.6666);
        int i = 0;
        for (int couleur : nombreDeCouleus) {
            if (x > 5 + i && x <tailleCarree + i) {
                couleurSelectionne = couleur;

            }
            i += 200;
        }
    }



    private void modifierLaCouleurParLaCouleurSelectionnee(int couleurSelectionne, int posI, int posJ) {
        if (tableau[posI][posJ] != couleurSelectionne) {
            int[][] tabCopie= new int[height][width];
            for (int i=0;i< height;i++)
                for(int j=0; j< width; j++)
                    tabCopie[i][j] = tableau[i][j];
            nombreDeCoupJoue++;
            coupPrecedent.add(tabCopie);
/*
        if(posI+1 < height && tableau[posI+1][posJ] == couleurPosition){
            tableau[posI+1][posJ] = couleurSelectionne;
            modifierLaCouleurParLaCouleurSelectionnee(couleurSelectionne,couleurPosition,posI+1,posJ);
        }
        if(posI-1 > 0 &&tableau[posI-1][posJ] == couleurPosition){
            tableau[posI-1][posJ] = couleurSelectionne;
            modifierLaCouleurParLaCouleurSelectionnee(couleurSelectionne,couleurPosition,posI-1,posJ);
        }
        if(posJ+1 < width &&tableau[posI][posJ+1] == couleurPosition){
            tableau[posI][posJ+1] = couleurSelectionne;
            modifierLaCouleurParLaCouleurSelectionnee(couleurSelectionne,couleurPosition,posI,posJ+1);
        }
        if(posJ-1 > 0 && tableau[posI][posJ-1] == couleurPosition){
            tableau[posI][posJ-1] = couleurSelectionne;
            modifierLaCouleurParLaCouleurSelectionnee(couleurSelectionne,couleurPosition,posI,posJ-1);
        }
        */

            int line;
            int colonne;
            int lindex = -1;
            int couleurCible = tableau[posI][posJ];
            lindex++;
            int[] lline = new int[width * height];
            int[] lcol = new int[width * height];
            lline[lindex] = posI;
            lcol[lindex] = posJ;

            while (lindex >= 0) {
                line = lline[lindex];
                colonne = lcol[lindex];
                tableau[line][colonne] = couleurSelectionne;
                lindex--;

                // haut
                if (line > 0 && tableau[line - 1][colonne] == couleurCible) {
                    lindex++;
                    lline[lindex] = line - 1;
                    lcol[lindex] = colonne;
                }
                // bas
                if (line < (height - 1) && tableau[line + 1][colonne] == couleurCible) {
                    lindex++;
                    lline[lindex] = line + 1;
                    lcol[lindex] = colonne;
                }
                // est
                if (colonne < (width - 1) && tableau[line][colonne + 1] == couleurCible) {
                    lindex++;
                    lline[lindex] = line;
                    lcol[lindex] = colonne + 1;
                }
                // ouest
                if (colonne > 0 && tableau[line][colonne - 1] == couleurCible) {
                    lindex++;
                    lline[lindex] = line;
                    lcol[lindex] = colonne - 1;
                }
            }
        }
    }


    private int[] recupererPositionCouleur(float x, float y) {

        int tailleCarree = (int) ((double) widthTailleEcran / 10.6666);
        int cptx = (int) ((double) widthTailleEcran * 0.97222);
        int cpty;
        for (int i = 0; i < height; i++) {
            cpty = 0;
            for (int j = 0; j < width; j++) {
                if (x > (cptx - tailleCarree))
                    if (x < cptx)
                        if (y > cpty)
                            if (y < (tailleCarree + cpty)) {
                                return new int[]{i, j};
                            }
                cpty += tailleCarree;
            }
            cptx -= tailleCarree;
        }
        return new int[0];
    }

    private boolean estDanslaGrille(float x, float y) {
        int tailleCarree = (int) ((double) widthTailleEcran / 10.6666);
        int cptx = (int) ((double) widthTailleEcran * 0.97222);
        int cpty;
        if (x < cptx && x > cptx - (tailleCarree * height)) {
            if (y < width * tailleCarree) {
                return true;
            }
        }
        return false;

    }

    private boolean leJeuEstTermine() {
        for (int couleur : nombreDeCouleus) {
            if (verifMemeCouleur(tableau, couleur))
                return true;
        }
        return false;
    }

    private boolean verifMemeCouleur(int tableau[][], int couleur) {

        for (int i = 0; i < height; i++) {

            for (int j = 0; j < width; j++) {
                if (tableau[i][j] != couleur) {
                    return false;
                }
            }
        }
        return true;
    }

    private void recupererNombreDeCouleurs() {
        for (int i = 0; i < height; i++) {

            for (int j = 0; j < width; j++) {
                if (!nombreDeCouleus.contains(tableau[i][j]))
                    nombreDeCouleus.add(tableau[i][j]);
            }
        }
    }

    private void remplirTableau() {
        int debut = 0;
        int fin = 0;
        int[][] tab = new int[height][width];
        for (int i = 0; i < height; i++) {

            for (int j = 0; j < width; j++) {
                tab[i][j] = Integer.parseInt(colours.substring(debut, fin + 1));
                debut++;
                fin++;
            }
        }
        tableau = tab;
    }

    private void dessinerGrille() {
        if (widthTailleEcran != 0) {
            int tailleCarree = (int) ((double) widthTailleEcran / 10.6666);
            int cptx = (int) ((double) widthTailleEcran * 0.97222);
            int cpty;
            canvas = sh.lockCanvas();
            canvas.drawColor(Color.GRAY);
            for (int i = 0; i < height; i++) {
                cpty = 0;
                for (int j = 0; j < width; j++) {

                    if (tableau[i][j] == 1) {
                        paint.setColor(Color.BLUE);
                    }
                    if (tableau[i][j] == 2) {
                        paint.setColor(Color.RED);
                    }
                    if (tableau[i][j] == 3) {
                        paint.setColor(Color.YELLOW);
                    }
                    if (tableau[i][j] == 4) {
                        paint.setColor(Color.CYAN);
                    }
                    if (tableau[i][j] == 5) {
                        paint.setColor(Color.MAGENTA);
                    }
                    canvas.drawRect(cptx - tailleCarree, cpty, cptx, tailleCarree + cpty, paint);
                    cpty += tailleCarree;
                }

                cptx -= tailleCarree;
            }

            // dessiner boutons changements de couleurs
            int i = 0;
            for (int couleur : nombreDeCouleus) {
                if (couleur == 1) {
                    paint.setColor(Color.BLUE);
                }
                if (couleur == 2) {
                    paint.setColor(Color.RED);
                }
                if (couleur == 3) {
                    paint.setColor(Color.YELLOW);
                }
                if (couleur == 4) {
                    paint.setColor(Color.CYAN);
                }
                if (couleur== 5) {
                    paint.setColor(Color.MAGENTA);
                }
                canvas.drawRect(5 + i, tailleCarree * width + 30, tailleCarree + i, tailleCarree + tailleCarree * width + 30 + tailleCarree, paint);
                i += 200;
            }

            // dessiner boutons retour en arriere
            paint.setColor(Color.BLACK);
            canvas.drawRect(widthTailleEcran - tailleCarree, tailleCarree * width + 30, widthTailleEcran, tailleCarree + tailleCarree * width + 30 + tailleCarree, paint);
            if(nombreDeCoupJoue<=Integer.parseInt(gold)) {
                dessinerNombreDeCoups(Color.YELLOW,gold);
            }

            if(nombreDeCoupJoue > Integer.parseInt(gold) &&  nombreDeCoupJoue<=Integer.parseInt(silver)) {
                dessinerNombreDeCoups(Color.LTGRAY,silver);
            }

            if(nombreDeCoupJoue>= Integer.parseInt(bronze)) {
                dessinerNombreDeCoups(Color.MAGENTA,bronze);
            }


            sh.unlockCanvasAndPost(canvas);
        }
    }

    private void dessinerNombreDeCoups(int color1,String nombreDeCoupOptimal){
        int tailleCarree = (int) ((double) widthTailleEcran / 10.6666);


        //dessiner ligne meilleurs coups
        paint.setColor(color1);
        paint.setStrokeWidth(10.5f);
        canvas.drawLine(widthTailleEcran - 400, heightTailleEcran - 50, widthTailleEcran - 250, heightTailleEcran - 200, paint);

        //dessiner meilleur coup
        paint.setColor(color1);
        paint.setTextSize(100);
        canvas.drawText(nombreDeCoupOptimal, widthTailleEcran - 300, heightTailleEcran - 50, paint);

        //dessiner nombre de coup joué
        paint.setColor(Color.BLACK);
        paint.setTextSize(100);
        canvas.drawText(String.valueOf(nombreDeCoupJoue), widthTailleEcran - 400, heightTailleEcran - 150, paint);

    }

    private void solve(){
        Page p = new Page(tableau);
        Solver s = new Solver(p);
        s.solve();
        for (int i = 0; i <s.getStepsRequired(); i++)
        coupSuivant.add(s.getPageStep()[i].getPageMatrix()) ;
    }


}