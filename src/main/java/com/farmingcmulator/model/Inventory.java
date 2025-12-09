package com.farmingcmulator.model;

public class Inventory {
    private Crop crop;
    private int quantity;
    
    public Inventory(Crop crop, int quantity) {
        this.crop = crop;
        this.quantity = quantity;
    }
    
    public Crop getCrop() { return crop; }
    public void setCrop(Crop crop) { this.crop = crop; }
    
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    
    @Override
    public String toString() {
        return crop.getName() + " (" + crop.getRarity() + ") x" + quantity;
    }
}
