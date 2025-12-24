# Farming Simulator

<div align="center">

![Java](https://img.shields.io/badge/Java-17+-orange?style=for-the-badge&logo=openjdk&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-21-blue?style=for-the-badge&logo=java&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.8+-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)

**A cozy farming simulation game built with JavaFX**

*Plant seeds, water crops, harvest for profit, and become the ultimate farmer!*

[Features](#features) • [How to Play](#how-to-play) • [Game Systems](#game-systems) • [Project Structure](#project-structure)

</div>

---

## About

**Farming Simulator** is a casual farming simulation game where players manage a farm over a 90-day season cycle. Buy seed boxes from the store, plant crops in your fields, water them daily, and harvest when ready to earn coins. The game features a gacha-style seed box system with varying rarities, a dynamic season system affecting crop growth, player leveling, upgrades, and a highscore leaderboard.

This project demonstrates object-oriented programming principles, MVC architecture, and JavaFX GUI development.

---

## Features

### Core Gameplay
- **10 Farm Plots** - Manage multiple crops simultaneously (unlock more as you level up)
- **Gacha Seed Boxes** - Three tiers (Common, Rare, Epic) with different drop rates
- **Watering System** - Keep your crops hydrated for optimal growth
- **Quality RNG** - Harvest quality affects final selling price (0-100%)
- **90-Day Season Cycle** - Four seasons with dynamic backgrounds and crop bonuses
- **Action Points** - 10 daily actions force meaningful strategic decisions

### Crop System
- **48 Unique Crops** across 5 rarity tiers
- **Rarity Tiers**: Common, Uncommon, Rare, Epic, Legendary
- Each crop has growth time, water requirements, base price, and preferred season
- Season-matching crops grow faster (+15% quality bonus)
- Off-season crops grow slower (-10% quality penalty)

### Progression System
- **Player Levels** - Earn EXP from harvests to level up
- **Plot Unlocks** - Unlock new plots at levels 5, 10, 15, 20, 25, 30, 35, 40, 45
- **Water Capacity Upgrades** - Increase how long crops stay watered (2 tiers)
- **Harvest Quality Upgrades** - Boost base harvest quality (5 tiers)
- **Fertilizer** - Consumable item for +10-25% quality and faster growth

### Database & Progression
- **Crop Database** - Track all discovered crops (collection-style)
- **Highscore System** - Compete for the best seasonal earnings
- **Persistent Save** - Progress saved automatically to local files
- **Sound Effects & Music** - Full audio experience with BGM and 10 SFX tracks

---

## How to Play

### Objective
Earn as many coins as possible within 90 days by growing and selling crops. Avoid going bankrupt (below -500 coins).

### Game Flow

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│   STORE     │────▶│   FIELD     │────▶│   WATER     │────▶│  HARVEST    │
│  Buy Seeds  │     │ Plant Crops │     │   Daily     │     │ Sell Crops  │
└─────────────┘     └─────────────┘     └─────────────┘     └─────────────┘
```

### Actions

| Action | Cost | Description |
|--------|------|-------------|
| **Buy Seed Box** | 1 action + coins | Purchase random seeds from store |
| **Plant** | 1 action | Plant a seed from inventory to empty plot |
| **Water** | 1 action | Water a growing crop (required daily) |
| **Harvest** | 1 action | Collect ready crops and earn coins |
| **Use Fertilizer** | 1 action | Apply fertilizer to boost crop quality |
| **Skip Day** | Free | End current day and start next |

### Seed Boxes

| Box Type | Price | Drop Rates |
|----------|-------|------------|
| **Common Box** | 50 coins | 60% Common, 25% Uncommon, 10% Rare, 4% Epic, 1% Legendary |
| **Rare Box** | 100 coins | 30% Common, 35% Uncommon, 25% Rare, 8% Epic, 2% Legendary |
| **Epic Box** | 200 coins | 10% Common, 20% Uncommon, 35% Rare, 25% Epic, 10% Legendary |

*Season biasing: 40% chance to re-roll for a season-matching crop*

### Crop Rarities

| Rarity | Color | Growth Time | Base Price |
|--------|-------|-------------|------------|
| Common | Gray | 3 days | 50 coins |
| Uncommon | Green | 4 days | 100 coins |
| Rare | Blue | 5 days | 150 coins |
| Epic | Purple | 7 days | 200 coins |
| Legendary | Gold | 9 days | 500 coins |

---

## Game Systems

### Season System

The 90-day game is divided into 4 seasons with dynamic visual themes:

| Season | Days | Effect |
|--------|------|--------|
| **Spring** | 90-68 | Spring crops: -1 day growth, +15% quality |
| **Summer** | 67-45 | Summer crops: -1 day growth, +15% quality |
| **Fall** | 44-23 | Fall crops: -1 day growth, +15% quality |
| **Winter** | 22-1 | Winter crops: -1 day growth, +15% quality |

*Off-season crops: +1 day growth, -10% quality penalty*

### Level & EXP System

- Earn EXP from harvests: `(Quality / 100) × Base Price`
- Exponential progression: Level 1→2 requires 100 EXP, increasing by 1.2x per level
- Higher levels unlock more farm plots and enable upgrades

### Upgrades

**Water Capacity** (how many days water lasts):
| Tier | Cost | Level Required | Capacity |
|------|------|----------------|----------|
| Base | - | - | 1 day |
| Tier 1 | 150 coins | Level 5 | 2 days |
| Tier 2 | 400 coins | Level 12 | 3 days |

**Harvest Quality** (bonus to base quality):
| Tier | Cost | Level Required | Bonus |
|------|------|----------------|-------|
| Tier 1 | 50 coins | Level 3 | +2% |
| Tier 2 | 100 coins | Level 6 | +2% |
| Tier 3 | 200 coins | Level 9 | +2% |
| Tier 4 | 350 coins | Level 12 | +2% |
| Tier 5 | 500 coins | Level 15 | +2% |

### Quality & Profit

- Harvest quality ranges from 0-100%
- Quality penalty: -10% per day crop goes unwatered
- Final sale price: `Base Price + (Base Price × Quality%)`
- Season bonus/penalty affects final quality

### Tips & Strategy

1. **Early Game**: Buy Common Boxes to build seed inventory
2. **Balance Actions**: Don't spend all actions on buying; save some for planting/watering
3. **Water Daily**: Crops lose quality (-10%) for each day unwatered
4. **Match Seasons**: Plant crops that match the current season for bonuses
5. **Level Up**: Focus on harvests to gain EXP and unlock more plots
6. **Upgrade Early**: Water capacity upgrades give more flexibility
7. **Risk vs Reward**: Epic Boxes cost more but have better legendary chances

---

## Project Structure

```
FarmingCmulator/
├── pom.xml                              # Maven configuration
├── README.md                            # Documentation
├── gamedata/
│   └── cropdata.txt                     # Crop database (48 crops)
│
└── src/main/
    ├── java/com/farmingcmulator/
    │   ├── MainApp.java                 # Application entry point
    │   ├── GameState.java               # Core game state & logic (Singleton)
    │   ├── SceneManager.java            # Scene navigation (Singleton)
    │   │
    │   ├── controller/                  # MVC Controllers
    │   │   ├── MainMenuController.java  # Main menu screen
    │   │   ├── EnterNameController.java # Player name input
    │   │   ├── DatabaseController.java  # Crop encyclopedia
    │   │   ├── GameMenuController.java  # Game hub & stats
    │   │   ├── FieldController.java     # Farm plot interactions
    │   │   └── StoreController.java     # Seed box & upgrade shop
    │   │
    │   ├── model/                       # Data Models
    │   │   ├── Crop.java                # Crop data (name, rarity, season)
    │   │   ├── Plot.java                # Farm plot state
    │   │   ├── Inventory.java           # Seed inventory wrapper
    │   │   ├── Highscore.java           # Score record
    │   │   └── Rarity.java              # Rarity tier definitions
    │   │
    │   └── util/                        # Utilities
    │       ├── FileManager.java         # Save/load persistence
    │       ├── Randomizer.java          # RNG for drops & quality
    │       └── SoundManager.java        # Audio management (Singleton)
    │
    └── resources/
        ├── css/style.css                # UI styling
        ├── fxml/                        # UI layouts (6 screens)
        ├── images/                      # Sprites & backgrounds
        └── audio/                       # BGM & sound effects
```

### Architecture

- **MVC Pattern**: Controllers handle FXML views, GameState manages model
- **Singleton Pattern**: GameState, SoundManager, SceneManager for global access
- **File-based Persistence**: Crop database and highscores saved to text files

---

## Requirements

- **Java**: 17 or higher
- **Maven**: 3.8 or higher
- **JavaFX**: 21.0.1 (managed by Maven)

## Building & Running

```bash
# Clone the repository
git clone https://github.com/your-username/FarmingCmulator.git

# Navigate to project directory
cd FarmingCmulator

# Build with Maven
mvn clean compile

# Run the application
mvn javafx:run
```

---

## Contributing

Contributions are welcome! Here are some ways you can help:

1. **Bug Reports**: Open an issue describing the bug
2. **Feature Requests**: Suggest new features or improvements
3. **Pull Requests**: Submit code changes
4. **Documentation**: Improve README or add comments

---

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

```
MIT License

Copyright (c) 2025 Satrio Ari

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.
```

---

<div align="center">

**Made with Java and JavaFX**

*Version 2.0*

</div>
