package com.farmingcmulator.util;

import com.farmingcmulator.model.Crop;
import com.farmingcmulator.model.Highscore;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    
    private static final String DATA_DIR = "gamedata";
    private static final String CROPDATA_FILE = DATA_DIR + "/cropdata.txt";
    private static final String HIGHSCORE_FILE = DATA_DIR + "/highscore.txt";
    
    public FileManager() {
        createDataDirectory();
        initializeCropData();
    }
    
    private void createDataDirectory() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
    
    private void initializeCropData() {
        File file = new File(CROPDATA_FILE);
        if (!file.exists()) {
            List<Crop> defaultCrops = createDefaultCrops();
            writeCropData(defaultCrops);
        }
    }
    
    private List<Crop> createDefaultCrops() {
        List<Crop> crops = new ArrayList<>();
        String[] cropData = {
            "1,Wheat,Common,Not", "2,Corn,Common,Not", "3,Carrot,Common,Not",
            "4,Potato,Common,Not", "5,Tomato,Common,Not", "6,Lettuce,Common,Not",
            "7,Onion,Common,Not", "8,Cabbage,Common,Not", "9,Radish,Common,Not",
            "10,Spinach,Common,Not", "11,Cucumber,Common,Not", "12,Peas,Common,Not",
            "13,Beans,Common,Not", "14,Garlic,Common,Not", "15,Celery,Common,Not",
            "16,Turnip,Common,Not", "17,Beet,Common,Not",
            "18,Pumpkin,Uncommon,Not", "19,Melon,Uncommon,Not", "20,Strawberry,Uncommon,Not",
            "21,Blueberry,Uncommon,Not", "22,Grape,Uncommon,Not", "23,Apple,Uncommon,Not",
            "24,Orange,Uncommon,Not", "25,Peach,Uncommon,Not", "26,Cherry,Uncommon,Not",
            "27,Pear,Uncommon,Not", "28,Plum,Uncommon,Not", "29,Lemon,Uncommon,Not",
            "30,Eggplant,Rare,Not", "31,Pepper,Rare,Not", "32,Artichoke,Rare,Not",
            "33,Asparagus,Rare,Not", "34,Broccoli,Rare,Not", "35,Cauliflower,Rare,Not",
            "36,Zucchini,Rare,Not", "37,Avocado,Rare,Not", "38,Mango,Rare,Not",
            "39,Papaya,Rare,Not", "40,Pineapple,Rare,Not",
            "41,Dragon Fruit,Epic,Not", "42,Starfruit,Epic,Not", "43,Passion Fruit,Epic,Not",
            "44,Durian,Epic,Not", "45,Jackfruit,Epic,Not", "46,Rambutan,Epic,Not",
            "47,Golden Apple,Legendary,Not", "48,Crystal Melon,Legendary,Not"
        };
        
        for (String data : cropData) {
            String[] parts = data.split(",");
            crops.add(new Crop(Integer.parseInt(parts[0]), parts[1], parts[2], parts[3]));
        }
        return crops;
    }
    
    public List<Crop> readCropData() {
        List<Crop> crops = new ArrayList<>();
        File file = new File(CROPDATA_FILE);
        
        if (!file.exists()) {
            return createDefaultCrops();
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    crops.add(new Crop(
                        Integer.parseInt(parts[0].trim()),
                        parts[1].trim(),
                        parts[2].trim(),
                        parts[3].trim()
                    ));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return createDefaultCrops();
        }
        
        return crops;
    }
    
    public void writeCropData(List<Crop> crops) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CROPDATA_FILE))) {
            for (Crop crop : crops) {
                writer.println(crop.getIndex() + "," + crop.getName() + "," + 
                              crop.getRarity() + "," + crop.getStatus());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public List<Highscore> readHighscores() {
        List<Highscore> highscores = new ArrayList<>();
        File file = new File(HIGHSCORE_FILE);
        
        if (!file.exists()) {
            return highscores;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    highscores.add(new Highscore(
                        parts[0].trim(),
                        Integer.parseInt(parts[1].trim())
                    ));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return highscores;
    }
    
    public void writeHighscores(List<Highscore> highscores) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(HIGHSCORE_FILE))) {
            for (Highscore hs : highscores) {
                writer.println(hs.getPlayerName() + "," + hs.getScore());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
