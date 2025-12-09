package com.farmingcmulator.model;

public class Crop {
    private int index;
    private String name;
    private String rarity;
    private String status;
    
    public Crop(int index, String name, String rarity, String status) {
        this.index = index;
        this.name = name;
        this.rarity = rarity;
        this.status = status;
    }
    
    public int getIndex() { return index; }
    public void setIndex(int index) { this.index = index; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getRarity() { return rarity; }
    public void setRarity(String rarity) { this.rarity = rarity; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getRarityColor() {
        return Rarity.getColor(rarity);
    }
}
