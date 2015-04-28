package com.siberianhealth.eventregistration.tickets;

import android.content.ClipData;

import com.siberianhealth.eventregistration.Model;
import com.siberianhealth.eventregistration.model.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class ListTickets {

    private static Model model = Model.getInstance();
    /**
     * An array of sample (dummy) items.
     */
    public static List<Ticket> ITEMS = new ArrayList<Ticket>();

    public static List<Person> PERSONS = new ArrayList<Person>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, Ticket> ITEM_MAP = new HashMap<String, Ticket>();

    static {
        // Add 3 sample items.
        /*
        addItem(new Ticket("1", "Item 1"));
        addItem(new Ticket("2", "Item 2"));
        addItem(new Ticket("3", "Item 3"));*/
    }


    public static void addPerson(Person new_person){


        for( Person person : PERSONS) {

            if (person.Id() == new_person.Id()){
                return;
            }
        }


        PERSONS.add(new_person);


    }

    public static void reloadTickets(){

        ITEMS.clear();
        ITEM_MAP.clear();

        for( Person person : PERSONS) {

            ArrayList<Ticket> new_tickets = model.GetTickets(person.Id());


            for( Ticket ticket : new_tickets) {

                if (!contains(ticket)){

                    ticket.setSelected( !ticket.isPlaced() );

                    addItem( ticket );
                }
            }
        }

    }


    public static int size(){
        return ITEMS.size();
    }

    public static boolean isEmpty(){
        return (ITEMS.size() <= 0);
    }

    public static void addItem(Ticket item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.Id(), item);
    }


    public static boolean contains(Ticket Item){

        for (Ticket ticket : ITEMS){
            if (Item.Id().equals(ticket.Id())){
                return true;
            }
        }

        return false;

    }

    public void clear(){

        ITEMS.clear();
        ITEM_MAP.clear();

        PERSONS.clear();
    }

    public static String getSelected(){

        StringBuilder sbStr = new StringBuilder();

        for (Ticket ticket : ITEMS){
            if (!ticket.isPlaced() && ticket.isSelected()){

                if (sbStr.length() > 0){
                    sbStr.append(",");
                }
                sbStr.append(ticket.Id());

            }
        }

        return sbStr.toString();
    }


    public static boolean isNotPlaced(){

        for (Ticket ticket : ITEMS){
            if (!ticket.isPlaced()){
                return true;
            }
        }

        return false;
    }

    /**
     * A dummy item representing a piece of content.
     */
    /*
    public static class Ticket {
        public String id;
        public String content;

        public Ticket(String id, String content) {
            this.id = id;
            this.content = content;
        }

        @Override
        public String toString() {
            return content;
        }
    }
    */
}
