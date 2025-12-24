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
        // Format: index,name,rarity,status,season
        String[] cropData = {
            "1,Wheat,Common,Not,Fall", "2,Corn,Common,Not,Summer", "3,Carrot,Common,Not,Fall",
            "4,Potato,Common,Not,Fall", "5,Tomato,Common,Not,Summer", "6,Lettuce,Common,Not,Spring",
            "7,Onion,Common,Not,Fall", "8,Cabbage,Common,Not,Winter", "9,Radish,Common,Not,Spring",
            "10,Spinach,Common,Not,Spring", "11,Cucumber,Common,Not,Summer", "12,Peas,Common,Not,Spring",
            "13,Beans,Common,Not,Summer", "14,Garlic,Common,Not,Fall", "15,Celery,Common,Not,Spring",
            "16,Turnip,Common,Not,Winter", "17,Beet,Common,Not,Fall",
            "18,Pumpkin,Uncommon,Not,Fall", "19,Melon,Uncommon,Not,Summer", "20,Strawberry,Uncommon,Not,Spring",
            "21,Blueberry,Uncommon,Not,Summer", "22,Grape,Uncommon,Not,Fall", "23,Apple,Uncommon,Not,Fall",
            "24,Orange,Uncommon,Not,Winter", "25,Peach,Uncommon,Not,Summer", "26,Cherry,Uncommon,Not,Spring",
            "27,Pear,Uncommon,Not,Fall", "28,Plum,Uncommon,Not,Summer", "29,Lemon,Uncommon,Not,Winter",
            "30,Eggplant,Rare,Not,Summer", "31,Pepper,Rare,Not,Summer", "32,Artichoke,Rare,Not,Spring",
            "33,Asparagus,Rare,Not,Spring", "34,Broccoli,Rare,Not,Winter", "35,Cauliflower,Rare,Not,Winter",
            "36,Zucchini,Rare,Not,Summer", "37,Avocado,Rare,Not,Summer", "38,Mango,Rare,Not,Summer",
            "39,Papaya,Rare,Not,Summer", "40,Pineapple,Rare,Not,Summer",
            "41,Dragon Fruit,Epic,Not,Summer", "42,Starfruit,Epic,Not,Summer", "43,Passion Fruit,Epic,Not,Spring",
            "44,Durian,Epic,Not,Summer", "45,Jackfruit,Epic,Not,Summer", "46,Rambutan,Epic,Not,Summer",
            "47,Golden Apple,Legendary,Not,Fall", "48,Crystal Melon,Legendary,Not,Winter"
        };

        for (String data : cropData) {
            String[] parts = data.split(",");
            crops.add(new Crop(Integer.parseInt(parts[0]), parts[1], parts[2], parts[3], parts[4]));
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
                if (parts.length >= 5) {
                    crops.add(new Crop(
                        Integer.parseInt(parts[0].trim()),
                        parts[1].trim(),
                        parts[2].trim(),
                        parts[3].trim(),
                        parts[4].trim()
                    ));
                } else if (parts.length >= 4) {
                    // Backward compatibility for old data without season
                    crops.add(new Crop(
                        Integer.parseInt(parts[0].trim()),
                        parts[1].trim(),
                        parts[2].trim(),
                        parts[3].trim(),
                        "Spring"
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
                              crop.getRarity() + "," + crop.getStatus() + "," + crop.getSeason());
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
