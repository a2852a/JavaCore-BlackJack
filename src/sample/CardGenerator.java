package sample;

import javafx.scene.image.Image;

import java.util.Random;

/** Provides card generator for card deck */
public class CardGenerator {

    private int cards[][]; //[0-pik 1-kier 2-karo 3-trefl] 13kart // 0-As ---- 9-10 10-J 11-Q 12-K [4][13]
    private int cardsLeft;
    private int deckCount;

    /** Generates deck multiplied by passed var */
    CardGenerator(int deckCount){
        this.deckCount = deckCount;
        cardsLeft = 0;
        cards = new int[4][13];
        for(int i = 0 ; i < 4 ; i ++)
            for(int j = 0 ; j < 13 ; j ++){
                cards[i][j] = deckCount;
                cardsLeft++;

            }
    }

    /** Re-generate the deck(s) */
    private void shuffle(){
        cardsLeft = 0;
        cards = new int[4][13];
        for(int i = 0 ; i < 4 ; i ++)
            for(int j = 0 ; j < 13 ; j ++){
                cards[i][j] = this.deckCount;
                cardsLeft++;

            }
    }

    /** Generate card and if card is available return it  */
    public Card drawCard(){
        Card karta = new Card();

        Random generator = new Random();

        while(true) {

           karta.setShape(generator.nextInt(13));
           karta.setColour(generator.nextInt(4));

           if(cards[karta.getColour()][karta.getShape()] != 0){
               cards[karta.getColour()][karta.getShape()]--; break;}
        }

        cardsLeft--;
        if(cardsLeft <= 3) this.shuffle();
        return karta;
    }

    /** "Convert" karta type to image */
    public Image karta2Image(Card karta){
        Image image = null;
        String path ;

        path = "res/cards_png/"+ karta.getShape() + "_" + karta.getColour() + ".png";
        image = new Image(path);

        return image;
    }




}
