package com.example.titaneric.termproject;

/**
 * Created by TitanEric on 12/26/2016.
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.List;

/**
 *
 * @author TitanEric
 */
public class ListModel {
    String Text;
    int Image;

    public ListModel(){
        super();
    }
    public ListModel(String text, int image){
        Text = text;
        Image = image;
    }
    public String getText() {
        return Text;
    }

    public void setText(String shopName) {
        this.Text = shopName;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int Image) {
        this.Image = Image;
    }

}

