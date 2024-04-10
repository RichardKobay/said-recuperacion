package org.example;

public class Professor {
    private String name;
    private int ITIHours;
    private int IMHours;
    private int ITMHours;
    private int ISAHours;
    private int LAyGEHours;
    private int comercioHours;

    public Professor() {
        this.name = "";
        this.ITIHours = 0;
        this.IMHours = 0;
        this.ITMHours = 0;
        this.ISAHours = 0;
        this.LAyGEHours = 0;
    }

    public Professor(String name) {
        this.name = name;
        this.ITIHours = 0;
        this.IMHours = 0;
        this.ITMHours = 0;
        this.ISAHours = 0;
        this.LAyGEHours = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getITIHours() {
        return ITIHours;
    }

    public void setITIHours(int ITIHours) {
        this.ITIHours = ITIHours;
    }

    public int getIMHours() {
        return IMHours;
    }

    public void setIMHours(int IMHours) {
        this.IMHours = IMHours;
    }

    public int getITMHours() {
        return ITMHours;
    }

    public void setITMHours(int ITMHours) {
        this.ITMHours = ITMHours;
    }

    public int getISAHours() {
        return ISAHours;
    }

    public void setISAHours(int ISAHours) {
        this.ISAHours = ISAHours;
    }

    public int getLAyGEHours() {
        return LAyGEHours;
    }

    public void setLAyGEHours(int LAyGEHours) {
        this.LAyGEHours = LAyGEHours;
    }

    public int getComercioHours() {
        return comercioHours;
    }

    public void setComercioHours(int comercioHours) {
        this.comercioHours = comercioHours;
    }
}
