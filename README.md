# 🌾 Farming Simulator

<div align="center">

![Java](https://img.shields.io/badge/Java-17+-orange?style=for-the-badge&logo=openjdk&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-21-blue?style=for-the-badge&logo=java&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.8+-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)

**A cozy farming simulation game built with JavaFX**

*Plant seeds, water crops, harvest for profit, and become the ultimate farmer!*

[Features](#-features) • [Installation](#-installation) • [How to Play](#-how-to-play) • [Project Structure](#-project-structure)

</div>

---

## 📖 About

**Farming Simulator** is a casual farming simulation game where players manage a small farm over a 90-day season. Buy seed boxes from the store, plant crops in your fields, water them daily, and harvest when ready to earn coins. The game features a gacha-style seed box system with varying rarities, quality-based harvest bonuses, and a highscore system to track your farming achievements.

This project was developed as a JavaFX GUI application, demonstrating object-oriented programming principles, MVC architecture, and modern UI/UX design.

---

## ✨ Features

### Core Gameplay
- 🌱 **5 Farm Plots** - Manage multiple crops simultaneously
- 📦 **Gacha Seed Boxes** - Three tiers (Common, Rare, Epic) with different drop rates
- 💧 **Watering System** - Keep your crops hydrated for optimal growth
- 🎲 **Quality RNG** - Harvest quality affects final selling price (0-100%)
- 📅 **90-Day Seasons** - Strategic planning required to maximize profits
- ⚡ **Action Points** - Limited daily actions force meaningful decisions

### Crop System
- **20 Unique Crops** across 5 rarity tiers
- **Rarity Tiers**: Common → Uncommon → Rare → Epic → Legendary
- Each crop has unique growth time, water requirements, and base price
- Higher rarity = Higher profit potential

### Database & Progression
- 📊 **Crop Database** - Track all discovered crops (Pokédex-style)
- 🏆 **Highscore System** - Compete for the best seasonal earnings
- 💾 **Persistent Save** - Progress saved automatically to local files

---

## 🚀 Installation

### Prerequisites
- **Java JDK 17** or higher
- **Maven 3.8** or higher
- **JavaFX 21** (handled by Maven)

### Quick Start

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/farming-cmulator.git
   cd farming-cmulator
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the game**
   ```bash
   mvn javafx:run
   ```

---

## 🎯 How to Play

### Objective
Earn as many coins as possible within 90 days by growing and selling crops.

### Actions

| Action | Cost | Description |
|--------|------|-------------|
| **Buy Seed Box** | 1 action + coins | Purchase random seeds from store |
| **Plant** | 1 action | Plant a seed from inventory to empty plot |
| **Water** | 1 action | Water a growing crop (required daily) |
| **Harvest** | 1 action | Collect ready crops and earn coins |
| **Skip Day** | Free | End current day and start next |

### Seed Boxes

| Box Type | Price | Drop Rates |
|----------|-------|------------|
| **Common Box** | 50 coins | 60% Common, 25% Uncommon, 10% Rare, 4% Epic, 1% Legendary |
| **Rare Box** | 100 coins | 30% Common, 35% Uncommon, 25% Rare, 8% Epic, 2% Legendary |
| **Epic Box** | 200 coins | 10% Common, 20% Uncommon, 35% Rare, 25% Epic, 10% Legendary |

### Crop Rarities

| Rarity | Color | Base Price Range |
|--------|-------|------------------|
| Common | Gray | 30-50 coins |
| Uncommon | Green | 60-90 coins |
| Rare | Blue | 100-150 coins |
| Epic | Purple | 180-250 coins |
| Legendary | Gold | 300-500 coins |

### Tips & Strategy

1. **Early Game**: Buy Common Boxes to build seed inventory
2. **Balance Actions**: Don't spend all actions on buying; save some for planting/watering
3. **Water Management**: Crops die if not watered! Check water levels daily
4. **Quality Bonus**: Harvest quality (0-100%) directly multiplies your profit
5. **Risk vs Reward**: Epic Boxes cost more but have better legendary chances

---

## 📁 Project Structure

```
FarmingCmulator-JavaFX/
├── 📄 pom.xml                          
├── 📄 README.md                        
├── 📄 SETUP.md                         
│
└── 📂 src/main/
    ├── 📂 java/
    │   ├── 📄 module-info.java         
    │   │
    │   └── 📂 com/farmingcmulator/
    │       ├── 📄 MainApp.java         
    │       ├── 📄 GameState.java       
    │       ├── 📄 SceneManager.java    
    │       │
    │       ├── 📂 controller/          
    │       │   ├── 📄 MainMenuController.java
    │       │   ├── 📄 EnterNameController.java
    │       │   ├── 📄 DatabaseController.java
    │       │   ├── 📄 GameMenuController.java
    │       │   ├── 📄 FieldController.java
    │       │   └── 📄 StoreController.java
    │       │
    │       ├── 📂 model/               
    │       │   ├── 📄 Crop.java        
    │       │   ├── 📄 Plot.java        
    │       │   ├── 📄 Inventory.java   
    │       │   ├── 📄 Highscore.java   
    │       │   └── 📄 Rarity.java      
    │       │
    │       └── 📂 util/                
    │           ├── 📄 FileManager.java 
    │           ├── 📄 Randomizer.java  
    │           └── 📄 SoundManager.java
    │
    └── 📂 resources/
        ├── 📂 css/
        │   └── 📄 style.css            
        │
        ├── 📂 fxml/                    
        │   ├── 📄 MainMenu.fxml
        │   ├── 📄 EnterName.fxml
        │   ├── 📄 Database.fxml
        │   ├── 📄 GameMenu.fxml
        │   ├── 📄 Field.fxml
        │   └── 📄 Store.fxml
        │
        ├── 📂 images/                  
        └── 📂 audio/                   
```

---

## 🤝 Contributing

Contributions are welcome! Here are some ways you can help:

1. **Bug Reports**: Open an issue describing the bug
2. **Feature Requests**: Suggest new features or improvements
3. **Pull Requests**: Submit code changes
4. **Documentation**: Improve README or add comments

---

## 📜 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

```
MIT License

Copyright (c) 2024 SatrioAri

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.
```

<div align="center">

**Made with ❤️ and ☕**

</div>
