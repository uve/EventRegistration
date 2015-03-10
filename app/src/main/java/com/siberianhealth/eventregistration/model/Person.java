package com.siberianhealth.eventregistration.model;

import java.sql.Blob;

/**
 * Created by uve on 16/02/15.
 */
public class Person {


    public static final String PERSON_CONTRACT = "PERSON_CONTRACT";
    public static final String PERSON_ID = "PERSON_ID";

    private String name    = null;
    private int id;
    private String contract = null;
    private Blob photo = null;


    public Person() {
    }


    public void setName(String name){

        this.name = name;
    }


    public void setId(int id){

        this.id = id;
    }

    public void setPhoto(Blob photo){

        this.photo = photo;
    }


    public void setContract(String contract){

        this.contract = contract;
    }


    public String Contract(){
        return this.contract;
    }

    public String Name(){
        return this.name;
    }

    public int Id(){
        return this.id;
    }

}
