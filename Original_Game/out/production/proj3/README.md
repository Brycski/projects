# Build Your Own World Design Document

**Partner 1:**
Nupur Agarwal
**Partner 2:**
Bryce Szarzynski

## Classes and Data Structures
**Class: Player**
. TETile playerSkin;
. Point position;
. private static TETile[][] worldTiles;
. int width;
. int height;

**Class: Stone**
. Point position;
. private static TETile[][] worldTiles;
. int width;
. int height;

**Class: World**
. TETile[][] map;
. int width;
. int height;
. private static Random RANDOM;
. static LinkedList<Stone> infinityStones = new LinkedList<>();
. Player player;
. Enemy enemy;

**Class: Enemy**
. Stone preferredStone;
. Point position;
. private static TETile[][] worldTiles;
. int width;
. int height;
. Point[] Path;

**Class: Point**
. int x;
. int y

## Algorithms
**Class: Player**
. Spawn = spawns player in a random floor tile

**Class: Stone**
. Spawn = spawns stone in a random floor tile

**Class: World**
. getNewWorld = new TETile[][] of rooms, hallways, player, enemy, and stones
. playerMove = moves player from one spot in world map to given second spot

**Class: Enemy**
. Spawn = spawns enemy in a random floor tile equal distance away from a randomly desired stone that the player is
. Path = gets the enemy path to its preferred stone

## Persistence


Overall Ideas
Collect 6 stones
Beat Avenger to it

TO DO:
*- HUD (with hover over tile feature with mouse and stones)
*- Enemy Movement
- Encounter: create new world if they hit
  - Rock Paper Scissors (Shield, Web-slinger, Bow n Arrow)
      - if tie, restart
      - if lose, game over
      - if win continue and plus stone
- Add game-over feature (if all stones or npc gets one stone) (on hold)
- Choose skin menu option (on hold)
- Maybe sound effects/background music OR clicking in menu options with mouse (on hold)
- Menu option to change name which is in HUD (on hold)
- EC: Count down timer? If you trap enemy? 