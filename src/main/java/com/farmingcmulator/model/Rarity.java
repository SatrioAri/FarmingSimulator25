package com.farmingcmulator.model;

public class Rarity {
    private String name;
    private int time;
    private int water;
    private int sellValue;
    
    public Rarity(String name, int time, int water, int sellValue) {
        this.name = name;
        this.time = time;
        this.water = water;
        this.sellValue = sellValue;
    }
    
    public String getName() { return name; }
    public int getTime() { return time; }
    public int getWater() { return water; }
    public int getSellValue() { return sellValue; }
    
    public static Rarity getRarityByName(String name) {
        switch (name) {
            case "Common": return new Rarity("Common", 3, 3, 50);
            case "Uncommon": return new Rarity("Uncommon", 4, 4, 100);
            case "Rare": return new Rarity("Rare", 5, 5, 150);
            case "Epic": return new Rarity("Epic", 7, 7, 200);
            case "Legendary": return new Rarity("Legendary", 9, 1, 500);
            default: return new Rarity("Common", 3, 3, 50);
        }
    }
    
    public static String getColor(String rarityName) {
        switch (rarityName) {
            case "Common": return "#95a5a6";
            case "Uncommon": return "#27ae60";
            case "Rare": return "#3498db";
            case "Epic": return "#9b59b6";
            case "Legendary": return "#f39c12";
            default: return "#95a5a6";
        }
    }
    
    public static String getCssClass(String rarityName) {
        switch (rarityName) {
            case "Common": return "rarity-common";
            case "Uncommon": return "rarity-uncommon";
            case "Rare": return "rarity-rare";
            case "Epic": return "rarity-epic";
            case "Legendary": return "rarity-legendary";
            default: return "rarity-common";
        }
    }
}
