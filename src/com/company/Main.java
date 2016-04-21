package com.company;

import java.io.FileReader;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        GI graphSolution = new GI();

        //while(true) {
            Stack<Grid> fieldStack = new Stack();
            Grid field;
            Grid printGrid;
            Grid startOptionsField;
            String[][] testGrid;
            Stack<Grid> solutions = new Stack();


            // maakt het begin Grid (field) en de tegels (tile) van het probleem
            // adhv de waarde die in een txt.file staan (resources/problem"")
            try {
                Scanner sc = new Scanner(new FileReader("resources/problemA"));
                int breedte = sc.nextInt();
                int lengte = sc.nextInt();
                field = new Grid(breedte, lengte);
                //field.printVeld();
                System.out.println("");
                while (sc.hasNext()) {
                    int width = sc.nextInt();
                    int length = sc.nextInt();
                    String name = " " + sc.next() + " ";
                    Tile tile = new Tile(width, length, name);
                    field.collection.tiles.add(tile);
                }
            } catch (Exception e) {
                System.out.println("ERROR!");
                field = null;
            }
            if (field != null) {
                fieldStack.push(field);
            }

            //klok kijken: begint voor het maken van de combi's
            long combiTime = System.nanoTime();

            //maakt de combi opties en slaat deze op in een 2D Array <Opties<opties<combi van tiles>>>
            Tile biggestTile = new Tile(0,0, "x");
            for(Tile tile : field.collection.tiles){
            if(tile.width>biggestTile.width){
                biggestTile = tile;
            }
            }
            Options opties = new Options(field.breedte, field.collection, biggestTile);

            // telt het aantal combinaties
            int nCombinatie;
            nCombinatie = opties.options.size() ;
            System.out.print("Het aantal combinaties is: " + nCombinatie);
            System.out.println("");

            //Klok kijken: meet hoelang het maken van de combinaties duurt
            long endCombi = System.nanoTime();
            long combiRunTime = endCombi - combiTime;
            System.out.println("Maken van de tile combinaties - Runtime :" + combiRunTime + " nano seconden");


        // print de gemaakte combi-opties uit.
    /*        for (int i = 0; i < opties.options.size(); i++) {
                System.out.print("[");
                for (int j = 0; j < opties.options.get(i).size(); j++) {
                    System.out.print(opties.options.get(i).get(j).name + ", ");
                }
                System.out.print("], ");
            }
    */


            //klok kijken: begint voor je de combi's toevoegd aan de stack (voor de depth-first)
            long startTime = System.nanoTime();

            // combi-options toegevoegd aan de stack.
            for (int j = 0; j < opties.options.size(); j++) {
                Grid usefield = new Grid(field);
                for (Tile tile : opties.options.get(j)) {
                    usefield = usefield.addTile(tile);
                }
                if (usefield != null) {
                    fieldStack.push(usefield);
                }
            }


            // zoekt naar een oplossing mbv combi-opties depth-first search
            int space = field.breedte;
            while (!fieldStack.isEmpty()) {
                Grid currentfield = fieldStack.pop();
                if (currentfield != null) {
                    space = currentfield.emptyRowSize();
                    if (space == 0) {
                        fieldStack.push(currentfield);
                        break;
                    }
                    Options fillspace = new Options(space, currentfield.collection);
                    if (fillspace.options.isEmpty()) {
                        fieldStack.pop();
                    }
                    for (int j = 0; j < fillspace.options.size(); j++) {
                        Grid newField = new Grid(currentfield);
                        for (Tile tile : fillspace.options.get(j)) {
                           // System.out.println("aantal opties " + fillspace.options.size());
                            newField = newField.addTile(tile);
                        }
                        if (newField != null) {
                            fieldStack.push(newField);
                        }
                    }
                }
            }

            // klok kijken: geeft de nanoseconden die het zoeken naar de oplossing heeft geduurd.
            // print de Totale statistiek en de gevonden oplossing.

            if (fieldStack.isEmpty()){
                System.out.println("er is geen oplossing");

            }else{
                long endTime = System.nanoTime();
                long total = endTime - startTime;
                long solutionTime = total + combiRunTime;
                System.out.println("vullen van het veld met tiles - RunTime: " + total + " nano seconden");
                System.out.println("Oplossing gevonden in totale  - RunTime: " + solutionTime +" nano seconden");
                System.out.println("");
                System.out.println("de oplossing van het tegelzetten!");
                fieldStack.peek().printVeld();

                graphSolution.field = fieldStack.peek().field;
                graphSolution.go();

                solutions.push(fieldStack.pop());


            }




            //    int runTimeSec = (total)/(1*10^9);
            //    System.out.println(runTimeSec);




            //Zoekt naar oplossing mbv tegel voor tegel depth-first search
        /*    while (!fieldStack.isEmpty()) {
                Grid currentField = fieldStack.pop();
                for (int i = 0; i < currentField.collection.tiles.size(); i++) {
                    Tile tile = currentField.collection.giveTile(i);
                    Grid newField = currentField.addTile(tile);
                    if (newField != null) {
                        newField.collection.removeTile(tile);
                        fieldStack.push(newField);
                        if (fieldStack.peek().collection.tiles.isEmpty()) {
                            while (!fieldStack.isEmpty()) {
                                fieldStack.pop();
                            }
                        }
                    }

                }
            }

            if (!fieldStack.isEmpty()) {
                fieldStack.peek().printVeld();
            }


    */

        /*Waarom zijn bananen krom?
        Als ze recht zijn vallen ze om
        pom pom pom
        ze liggen krom van het lachen*/
       // }


    }
}

