package com.example.laptrinhmobile.toposmobile.Object;

import java.io.Serializable;

/**
 * Created by LapTrinhMobile on 10/15/2015.
 */
public class TypePayment implements Serializable{
    private String type ;
    private double amount;
    private int position;
    public TypePayment() {
        type = "Cash";
        amount = -1;
        position = -1;
    }

    public void setType(String in_Type) {
        this.type = in_Type;
    }
    public void setAmount(double in_amount) {
        this.amount = in_amount;
    }
    public void setPosition(int in_position) { this.position = in_position; }

    //get values
    public String getType() { return this.type; }
    public double getAmount() { return this.amount; }
    public int getPosition() { return this.position; }
}
