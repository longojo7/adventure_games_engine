# Adventure Game Engine

A Java adventure game engine that loads game worlds from JSON and supports three ways to play: a Swing graphical interface, an interactive terminal, and batch command files.

Players explore connected rooms, collect and use items, solve puzzles, encounter monsters, and save or restore their progress. Game data defines the world independently of the engine, allowing different adventures to run through the same application.

## Features

- Load different adventure worlds from JSON.
- Play through a Swing GUI, terminal, or batch command file.
- Explore rooms, manage inventory, and resolve puzzles and monsters.
- Track player health, score, and rank.
- Save and restore game progress.

## Technology

Java, Swing, JSON.simple 1.1.1, and JUnit Jupiter. The engine follows a Model–View–Controller architecture.

## Getting Started

### Requirements

- JDK 24 to build the project
- Java 24 or newer to run the executable JAR
- A graphical desktop environment for graphics mode

### Build the Executable JAR

In IntelliJ IDEA:

1. Open **File → Project Structure → Artifacts**.
2. Select **+ → JAR → From modules with dependencies**.
3. Select `enginedriver.GameEngineApp` as the main class.
4. Include the JSON.simple dependency in the JAR.
5. Apply the configuration.
6. Select **Build → Build Artifacts**, then build the JAR artifact.

IntelliJ places the generated JAR under `out/artifacts/`.

### Prepare the Game Files

Place the executable JAR alongside the `data/` directory:

```text
adventure-game/
├── game_engine.jar
└── data/
    ├── museum.json
    ├── simple_hallway.json
    ├── alignquest.json
    └── images/
        └── ...
```

The examples below use `game_engine.jar`. Substitute your generated JAR's filename if it differs.

Open a terminal in this directory. Keep images under `data/images/`, where the engine expects to find them.

### Graphics Mode

Launch the Swing interface:

```bash
java -jar game_engine.jar data/museum.json -graphics
```

### Text Mode

Play interactively in the terminal:

```bash
java -jar game_engine.jar data/museum.json -text
```

Enter your player name when prompted. Enter `H` during gameplay to display the available commands.

### Batch Mode

Read the player name and game commands from a text file:

```bash
java -jar game_engine.jar data/museum.json -batch commands.txt
```

To write the game output to a file instead of the terminal:

```bash
java -jar game_engine.jar data/museum.json -batch commands.txt output.txt
```

A batch input file follows the same input sequence as text mode. For example:

```text
Explorer
L
I
Q
```

The first line supplies the player name. Each subsequent line supplies a command.

### Choosing a Game

Replace `data/museum.json` with the path to another game file. Quote paths containing spaces:

```bash
java -jar game_engine.jar "my games/adventure.json" -text
```

Image resources must still be available under `data/images/` relative to the directory where you launch the application.

Launch the JAR from a terminal rather than double-clicking it: the application requires command-line arguments.

## Terminal Commands

Commands are case-insensitive.

| Command | Action |
| --- | --- |
| `N`, `S`, `E`, `W` | Move north, south, east, or west |
| `L` or `LOOK` | Describe the current room |
| `I` or `INVENTORY` | Show carried items and player status |
| `T <item>` | Take an item |
| `D <item>` | Drop an item |
| `X <name>` | Examine an item or fixture |
| `U <item>` | Use an item |
| `A <answer>` | Submit a text solution |
| `SAVE` | Save the current game |
| `RESTORE` | Restore the associated save |
| `H` or `HELP` | Show available commands |
| `Q` or `QUIT` | End the game |

Item names and answers can contain spaces.

## Included Worlds

The `data/` directory contains several game definitions:

| File | Purpose |
| --- | --- |
| `simple_hallway.json` | A compact adventure demonstrating navigation, items, and obstacles |
| `museum.json` | A museum adventure with item-based and password puzzles |
| `alignquest.json` | A larger adventure with accompanying image assets |
| `empty_rooms.json` | A minimal world focused on room navigation |
| `empty_rooms_missing_data.json` | A loading fixture with omitted optional content |

## Defining a Game

A game file contains a top-level JSON object with a `rooms` array. Other sections define the items, fixtures, puzzles, and monsters referenced by rooms.

A minimal game looks like this:

```json
{
  "name": "A Small Adventure",
  "version": "1.0.0",
  "rooms": [
    {
      "room_number": "1",
      "room_name": "Entrance",
      "description": "A quiet room at the beginning of your adventure.",
      "N": "0",
      "S": "0",
      "E": "0",
      "W": "0"
    }
  ]
}
```

New games begin in room `"1"`.

### Room Connections

The `N`, `S`, `E`, and `W` properties describe directional connections:

- `"0"` means there is no exit.
- `"2"` means an open exit leads to room `"2"`.
- `"-2"` means an exit leads to room `"2"` but starts locked.

Locked exits should have an associated room obstacle that unlocks them when solved.

### Optional Content

A world does not need to define every element type. Rooms can omit optional items, fixtures, puzzles, monsters, and pictures.

When adding content, define it in its corresponding top-level section and reference it by name from the room. See the included game files for complete examples.

Puzzle solutions use an item name or a text answer enclosed in single quotes:

```json
"solution": "Ticket"
```

```json
"solution": "'Align'"
```

Image assets are stored under `data/images/`.

## Save and Restore

Saves contain the game definition together with player data such as health, inventory, current room, and solved obstacles.

The save path is derived from the loaded game filename. For example:

```text
Game: data/museum.json
Save: data/museum_save.json
```

`RESTORE` loads that associated save. It does not accept a player name or arbitrary file path.

Saving again overwrites the same save file. Players using the same game file therefore share its save location.

## Architecture

```text
src/
├── enginedriver/
│   └── GameEngineApp.java    Application entry point and mode selection
├── engine/
│   ├── controller/          Text/graphics controllers and save handling
│   ├── model/               Game state, JSON loading, and domain objects
│   │   ├── events/          Events and event handlers
│   │   ├── items/           Items, item factory, and inventories
│   │   ├── player/          Player and health behavior
│   │   └── rooms/           Rooms, connections, fixtures, and obstacles
│   └── view/                Swing view and view interface
└── json-simple-1.1.1.jar
```

The model manages game state and rules. Controllers translate player input into model operations. The Swing view displays state and forwards user interactions to the graphics controller.

`JsonBuilder` constructs a model from game data, while `ItemFactory` provides shared item lookup. Events connect obstacle completion and health changes to other game behavior.

Additional directories:

- `test/`: JUnit tests and a standalone application demonstration
- `data/`: Game definitions and image assets
- `diagrams/`: Design and interaction diagrams

## Testing

Open the project in IntelliJ IDEA using JDK 24. Ensure `src/` and `test/` are marked as source and test roots, and resolve the project's JSON.simple and JUnit dependencies.

Run the JUnit tests under `test/`. `SimpleGameEngineAppTest` is a separate manual demonstration.

## Project Status

This project began as a collaborative course project and is being refined for public release.

JSON validation is being improved incrementally. The loader reports several structural errors explicitly, but complete validation of every definition and cross-reference is still in progress.
