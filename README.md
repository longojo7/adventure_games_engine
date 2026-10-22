# Adventure Game Engine

A Java adventure game engine that loads game worlds from JSON and supports three ways to play: a Swing graphical interface, an interactive terminal, and batch command files.

Players explore connected rooms, collect and use items, solve puzzles, encounter monsters, and save or restore their progress. Game data defines the world independently of the engine, allowing different adventures to run through the same application.

## Features

- **JSON-defined worlds:** Configure rooms, connections, items, fixtures, puzzles, and monsters.
- **Multiple interfaces:** Play through a Swing GUI, terminal commands, or scripted batch input.
- **Exploration:** Navigate rooms and discover their descriptions, available exits, and contents.
- **Inventory management:** Take, drop, inspect, and use items within a carrying-weight limit.
- **Puzzles and monsters:** Resolve obstacles using items or text answers.
- **Player state:** Track health, inventory, score, and rank.
- **Event-driven interactions:** Solving obstacles can unlock exits and update player scores.
- **Save and restore:** Serialize game data and player progress to JSON.
- **Automated tests:** JUnit tests cover model components, JSON loading, and application startup.

## Technology

- Java
- Swing
- JSON.simple 1.1.1
- JUnit Jupiter
- Git
- IntelliJ IDEA project configuration

The project uses a Model–View–Controller architecture and does not currently include a Maven or Gradle build.

## Getting Started

### Requirements

- JDK 24, matching the current IntelliJ project configuration
- A graphical desktop session for Swing mode
- JUnit dependencies when running automated tests

JSON.simple is included at `src/json-simple-1.1.1.jar`.

Run the application from the repository root so relative paths to game data and images resolve correctly.

### Compile

The following commands use Bash on Linux or macOS:

```bash
mkdir -p out/classes

find src -name '*.java' > out/sources.list

javac \
  -cp src/json-simple-1.1.1.jar \
  -d out/classes \
  @out/sources.list
```

On Windows, use `;` instead of `:` to separate classpath entries in the commands below.

### Play in the Terminal

```bash
java -cp out/classes:src/json-simple-1.1.1.jar \
  enginedriver.GameEngineApp data/simple_hallway.json -text
```

Enter a player name when prompted, then use the available commands to explore.

### Play with the Graphical Interface

```bash
java -cp out/classes:src/json-simple-1.1.1.jar \
  enginedriver.GameEngineApp data/alignquest.json -graphics
```

Graphics mode requires a display and cannot run in a headless terminal alone.

### Run a Batch Game

Batch mode reads the player name and subsequent commands from a text file.

For example, create `out/commands.txt` containing:

```text
Explorer
L
T Notebook
I
N
Q
```

Run it against the hallway game:

```bash
java -cp out/classes:src/json-simple-1.1.1.jar \
  enginedriver.GameEngineApp \
  data/simple_hallway.json -batch out/commands.txt
```

To write game output to a file, add an output path:

```bash
java -cp out/classes:src/json-simple-1.1.1.jar \
  enginedriver.GameEngineApp \
  data/simple_hallway.json -batch out/commands.txt out/game-output.txt
```

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

### IntelliJ IDEA

1. Open the repository as a Java project.
2. Select JDK 24.
3. Confirm `src/` is marked as Sources Root and `test/` as Test Sources Root.
4. Ensure JSON.simple and the JUnit dependencies referenced by the project are available.
5. Run the JUnit test classes under `test/`.

`SimpleGameEngineAppTest` is a standalone demonstration with a `main()` method, separate from the automated JUnit tests.

### Command Line

With the JUnit Platform Console Standalone 6.0.0 JAR available at `lib/junit-platform-console-standalone-6.0.0.jar`, compile and run the tests:

```bash
mkdir -p out/test-classes

find test -name '*.java' > out/tests.list

javac \
  -cp out/classes:src/json-simple-1.1.1.jar:lib/junit-platform-console-standalone-6.0.0.jar \
  -d out/test-classes \
  @out/tests.list

java -jar lib/junit-platform-console-standalone-6.0.0.jar execute \
  --class-path out/classes:out/test-classes:src/json-simple-1.1.1.jar \
  --scan-class-path \
  --fail-if-no-tests
```

Compile the application before compiling the tests. The standalone JUnit JAR is an external test dependency and is not bundled with this repository.

## Project Status

This project began as a collaborative course project and is being refined for public release.

JSON validation is being improved incrementally. The loader reports several structural errors explicitly, but complete validation of every definition and cross-reference is still in progress.
