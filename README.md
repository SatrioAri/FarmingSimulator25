# 🌾 Farming C-Mulator

<div align="center">

![Java](https://img.shields.io/badge/Java-17+-orange?style=for-the-badge&logo=openjdk&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-21-blue?style=for-the-badge&logo=java&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.8+-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)

**A cozy farming simulation game built with JavaFX**

*Plant seeds, water crops, harvest for profit, and become the ultimate farmer!*

[Features](#-features) • [Screenshots](#-screenshots) • [Installation](#-installation) • [How to Play](#-how-to-play) • [Project Structure](#-project-structure)

</div>

---

## 📖 About

**Farming Simulator** is a casual farming simulation game where players manage a small farm over a 30-day season. Buy seed boxes from the store, plant crops in your fields, water them daily, and harvest when ready to earn coins. The game features a gacha-style seed box system with varying rarities, quality-based harvest bonuses, and a highscore system to track your farming achievements.

This project was developed as a JavaFX GUI application, demonstrating object-oriented programming principles, MVC architecture, and modern UI/UX design.

---

## ✨ Features

### Core Gameplay
- 🌱 **5 Farm Plots** - Manage multiple crops simultaneously
- 📦 **Gacha Seed Boxes** - Three tiers (Common, Rare, Epic) with different drop rates
- 💧 **Watering System** - Keep your crops hydrated for optimal growth
- 🎲 **Quality RNG** - Harvest quality affects final selling price (0-100%)
- 📅 **30-Day Seasons** - Strategic planning required to maximize profits
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

### Polish & UX
- 🎨 **Modern UI Design** - Glassmorphism effects, gradients, and smooth animations
- 🔊 **Sound Effects** - Immersive audio feedback for all actions
- 🎵 **Background Music** - Relaxing farm ambience
- 📱 **1920x1080 Resolution** - Optimized for modern displays

---

## 🎮 Screenshots

<div align="center">

| Main Menu | Game Menu |
|:---------:|:---------:|
| *Main menu with play, database, and exit options* | *Central hub showing stats and action buttons* |

| Field View | Store |
|:----------:|:-----:|
| *Manage your 5 farm plots* | *Purchase seed boxes with different rarities* |

| Harvest Result | Database |
|:--------------:|:--------:|
| *Quality-based earnings calculation* | *Track your crop collection progress* |

</div>

> 📸 *Add your own screenshots to the `screenshots/` folder*

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

### IDE Setup (IntelliJ IDEA)

1. **Import Project**: File → Open → Select `pom.xml`
2. **Mark Directories**:
   - Right-click `src/main/java` → Mark Directory as → Sources Root
   - Right-click `src/main/resources` → Mark Directory as → Resources Root
3. **Run**: Open `MainApp.java` → Right-click → Run

### Adding Media Assets (Optional)

The game runs without media files, but you can enhance the experience:

**Background Images** (`src/main/resources/images/`):
- `bg_mainmenu.jpg` - Main menu background
- `bg_gamemenu.jpg` - Game menu background
- `bg_field.jpg` - Field view background
- `bg_store.jpg` - Store background

**Audio Files** (`src/main/resources/audio/`):
- `bgm_farm.mp3` - Background music (looping)
- `sfx_click.wav` - Button click
- `sfx_popup.wav` - Popup notification
- `sfx_plant.wav` - Planting seed
- `sfx_water.wav` - Watering crop
- `sfx_harvest.wav` - Harvesting
- `sfx_purchase.wav` - Buying seed box
- `sfx_success.wav` - Success action
- `sfx_coins.wav` - Earning coins
- `sfx_error.wav` - Error/invalid action

> 💡 Free audio resources: [Freesound](https://freesound.org), [OpenGameArt](https://opengameart.org), [Pixabay](https://pixabay.com/sound-effects/)

---

## 🎯 How to Play

### Objective
Earn as many coins as possible within 30 days by growing and selling crops.

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
├── 📄 pom.xml                          # Maven configuration
├── 📄 README.md                        # This file
├── 📄 SETUP.md                         # Detailed setup guide
│
└── 📂 src/main/
    ├── 📂 java/
    │   ├── 📄 module-info.java         # Java module definition
    │   │
    │   └── 📂 com/farmingcmulator/
    │       ├── 📄 MainApp.java         # Application entry point
    │       ├── 📄 GameState.java       # Core game logic & state
    │       ├── 📄 SceneManager.java    # Scene switching handler
    │       │
    │       ├── 📂 controller/          # FXML Controllers (MVC)
    │       │   ├── 📄 MainMenuController.java
    │       │   ├── 📄 EnterNameController.java
    │       │   ├── 📄 DatabaseController.java
    │       │   ├── 📄 GameMenuController.java
    │       │   ├── 📄 FieldController.java
    │       │   └── 📄 StoreController.java
    │       │
    │       ├── 📂 model/               # Data models
    │       │   ├── 📄 Crop.java        # Crop entity
    │       │   ├── 📄 Plot.java        # Farm plot entity
    │       │   ├── 📄 Inventory.java   # Player inventory
    │       │   ├── 📄 Highscore.java   # Highscore entry
    │       │   └── 📄 Rarity.java      # Rarity enum & utilities
    │       │
    │       └── 📂 util/                # Utilities
    │           ├── 📄 FileManager.java # Save/load handling
    │           ├── 📄 Randomizer.java  # RNG for gacha & quality
    │           └── 📄 SoundManager.java# Audio management
    │
    └── 📂 resources/
        ├── 📂 css/
        │   └── 📄 style.css            # Complete UI styling (1100+ lines)
        │
        ├── 📂 fxml/                    # UI layouts
        │   ├── 📄 MainMenu.fxml
        │   ├── 📄 EnterName.fxml
        │   ├── 📄 Database.fxml
        │   ├── 📄 GameMenu.fxml
        │   ├── 📄 Field.fxml
        │   └── 📄 Store.fxml
        │
        ├── 📂 images/                  # Background images (user-provided)
        └── 📂 audio/                   # Sound files (user-provided)
```

---

## 🏗️ Architecture

### Design Patterns

| Pattern | Implementation |
|---------|----------------|
| **MVC** | Controllers handle UI logic, Models store data, FXML defines Views |
| **Singleton** | `GameState`, `SceneManager`, `SoundManager` - single instances |
| **Observer** | JavaFX properties for reactive UI updates |

### Key Classes

```
┌──────────────────┐
│     MainApp      │  Application entry point
└────────┬─────────┘
         │
         ▼
┌──────────────────┐     ┌──────────────────┐
│  SceneManager    │────▶│   Controllers    │  Handle user interactions
└────────┬─────────┘     └────────┬─────────┘
         │                        │
         ▼                        ▼
┌──────────────────┐     ┌──────────────────┐
│    GameState     │◀───▶│     Models       │  Crop, Plot, Inventory
└────────┬─────────┘     └──────────────────┘
         │
         ▼
┌──────────────────┐     ┌──────────────────┐
│   FileManager    │     │   SoundManager   │  Utilities
└──────────────────┘     └──────────────────┘
```

### Data Persistence

Game data is stored in plain text files in the application directory:

| File | Content |
|------|---------|
| `highscores.txt` | Player name and score, one per line |
| `obtained_crops.txt` | IDs of crops the player has discovered |

---

## 🎨 UI/UX Design

### Color Palette

| Color | Hex | Usage |
|-------|-----|-------|
| Fresh Green | `#7BC96F` | Primary buttons, success states |
| Morning Sky | `#A7C7E7` | Secondary elements, backgrounds |
| Sunflower Yellow | `#FFD966` | Accents, coins, active states |
| Earth Brown | `#8B7355` | Store theme, natural elements |
| Stone Gray | `#7A7A7A` | Borders, secondary text |
| Soft White | `#F5F7F4` | Content backgrounds |

### Typography

| Type | Font | Size | Usage |
|------|------|------|-------|
| Display | Georgia | 48-64px | Page titles |
| Headings | Georgia | 24-32px | Section titles |
| Body | Segoe UI | 14-18px | Content text |
| Buttons | Segoe UI Bold | 16-20px | Interactive elements |

### Visual Effects

- **Glassmorphism**: Semi-transparent backgrounds with blur
- **Gradients**: Smooth color transitions on buttons and boxes
- **Shadows**: Depth and elevation for UI elements
- **Hover States**: Scale and glow effects for feedback

---

## 🔧 Technical Details

### Dependencies

```xml
<dependencies>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-controls</artifactId>
        <version>21.0.1</version>
    </dependency>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-fxml</artifactId>
        <version>21.0.1</version>
    </dependency>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-media</artifactId>
        <version>21.0.1</version>
    </dependency>
</dependencies>
```

### System Requirements

| Requirement | Minimum |
|-------------|---------|
| Java | JDK 17+ |
| RAM | 512 MB |
| Display | 1920x1080 |
| OS | Windows / macOS / Linux |

---

## 📝 Development Notes

### Building from Source

```bash
# Clean build
mvn clean compile

# Run tests (if any)
mvn test

# Package as JAR
mvn package

# Run application
mvn javafx:run
```

### Creating Executable JAR

```bash
# Create fat JAR with dependencies
mvn clean package shade:shade
```

### Code Style

- Java 17 features (records, pattern matching where applicable)
- JavaFX best practices (FXML separation, property bindings)
- Consistent naming conventions (camelCase methods, PascalCase classes)

---

## 🤝 Contributing

Contributions are welcome! Here are some ways you can help:

1. **Bug Reports**: Open an issue describing the bug
2. **Feature Requests**: Suggest new features or improvements
3. **Pull Requests**: Submit code changes
4. **Documentation**: Improve README or add comments

### Development Setup

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/amazing-feature`
3. Commit changes: `git commit -m 'Add amazing feature'`
4. Push to branch: `git push origin feature/amazing-feature`
5. Open a Pull Request

---

## 📜 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

```
MIT License

Copyright (c) 2024 [Your Name]

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

## 🙏 Acknowledgments

- JavaFX community for excellent documentation
- [Freesound](https://freesound.org) for audio resources
- [Unsplash](https://unsplash.com) for background images
- All contributors and testers

---

<div align="center">

**Made with ❤️ and ☕**

⭐ Star this repo if you found it helpful!

</div>
