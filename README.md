# Adventure Game Engine (Model, Controller, and Parser)

At this level of the project, we implemented a JSON parser for our data to fit our model, and we
also connected a controller that communicates with the model, which the user can
interact with. We now support loading in-game data from valid JSON files and improper game files
that omit certain aspects. We now construct items loaded from the JSON into a
dedicated factory, which the Item class uses to call particular items. Lastly, we have an
interactive game controller that users can use to play the game in the command line or with a 
string.

Player remained as our first and central abstraction, since it has its own data and behaviors. Giving it its own supertype will also allow for future or alternate versions of the
player (NPCs, etc.), hence the IPlayer interface. A player has to other key abstractiosn in our model. We made IHealth its own interface and Player has an IHealth. This way, how
Health is handled could be changed in different circumstances or updated in the future. Our concrete implementation is the ThresholdHealth class which allows health behavior to vary
without modifying the Player class. 

Player still has an IInventory. IInventory is our interface for storing and transferring items.
Rooms and Players both use them, although they use different implementations. Rooms use an 
InfiniteInventory does not care about the weight of Items, while the Player’s inventory is 
weight-constrained through our LimitedInventory class. 

IRoom is implemented by Room, which includes its own InfiniteInventory and references to
Obstacles and Connections. Connections are how Rooms connect to each other. They also track if
they are open or closed so that Connections can be “unlocked” by actions in the game. IObstacle
is the protocol for Puzzles and Monsters. It has methods for affecting rooms and being solved.
IObstacles also use an observer pattern, where SolveListeners can be notified when a Puzzle is
solved. Monsters are like Puzzles, but with the added functionality of damaging the player.
ISolution allows for multiple types of solutions, in our case, ItemSolution and 
PasswordSolution, which can check a potential solution against theirs.

We added a full JSON parser that loads data--rooms, items, puzzles, monsters--and constructs
the model using the IGameBuilder abstraction. The parser reads structured JSON and delegates
object creation to factories. The builder creates all the rooms, obstacles, health, inventory,
etc., and wires everything up.

The Item Factory is responsible for producing Item objects based on their IDs which allows these
items to evolve without changing parsing logic or controller code. The model interacts with
items through the factory. 

We added a GameController (with an accompanying interface to allow future variations). The
controller reads user input, parses commands, and forwards those commands to the IEngineModel.
It then prints the model’s responses, acting as the bridge between the user and the game logic.
