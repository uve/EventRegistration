package com.siberianhealth.eventregistration.tickets;


/**
 * Created by uve on 16/02/15.
 */
public class Ticket {

    private String name;
    private String sector;

    private String id;
    private int row = 0;
    private int col = 0;


    private boolean selected = false;

    public Ticket() {
    }

    public Ticket(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }


    public void setName(String name){

        this.name = name;
    }


    public void setId(String id){

        this.id = id;
    }

    public void setPlace(String sector, int row, int col){

        this.sector = sector;
        this.row = row;
        this.col = col;
    }


    public String Place(){

        String result;

        if (isPlaced()){
            result = "Сектор: " + this.sector + " Ряд: " + this.row + " Место: " + this.col;
        }
        else{
            result = "Место не выбрано";
        }

        return result;
    }

    public String Name(){
        return this.name;
    }

    public String Id(){
        return this.id;
    }


    public boolean isPlaced(){

        if ((row == 0) && (col == 0)){
            return false;
        }

        return true;
    }

    public boolean isSelected() {
        return selected;
    }

    public void Toggle() {
        selected = !selected;
    }


    public void setSelected(boolean selected) {
        this.selected = selected;
    }





}
