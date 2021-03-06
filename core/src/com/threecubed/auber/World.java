package com.threecubed.auber;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.threecubed.auber.entities.*;
import com.threecubed.auber.pathfinding.NavigationMesh;
import com.threecubed.auber.screens.GameOverScreen;
import com.threecubed.auber.screens.GameScreen;
import com.threecubed.auber.Abilities;
import com.threecubed.auber.ui.Difficulties;

import java.util.*;
import java.util.stream.Collectors;


/**
 * The world class stores information related to what is happening within the game world.
 * It should only be used within the GameScreen screen.
 *
 * @author Daniel O'Brien
 * @version 1.0
 * @since 1.0
 * */
public class World implements Json.Serializable {
  private AuberGame game;

  public Player player;
  public int infiltratorCount;

  public boolean demoMode = false;

  /** Number of infiltrators added, including defeated ones. */
  public int infiltratorsAddedCount = 0;

  private List<GameEntity> entities = new ArrayList<>();
  public List<GameEntity> newEntities = new ArrayList<>();
  public List<GameEntity> oldEntities = new ArrayList<>();

  public OrthographicCamera camera = new OrthographicCamera();

  public static final TiledMap map = new TmxMapLoader().load("map.tmx");
  public static final TiledMapTileSet tileset = map.getTileSets().getTileSet(0);
  public static TextureAtlas atlas;

  public OrthogonalTiledMapRenderer renderer;

  public ArrayList<RectangleMapObject> systems = new ArrayList<>();
  public RectangleMapObject medbay;
  public ArrayList<float[]> spawnLocations = new ArrayList<>();

  public static Random randomNumberGenerator = new Random();

  // ------------------NAVIGATION----------------
  public static final NavigationMesh navigationMesh = new NavigationMesh(
      (TiledMapTileLayer) map.getLayers().get("navigation_layer")
      );
  public ArrayList<float[]> fleePoints = new ArrayList<>();

  //<editor-fold desc="Constants">
  /** Coordinates for the bottom left and top right tiles of the brig. */
  public static final float[][] BRIG_BOUNDS = {{240f, 608f}, {352f, 640f}};
  /** Coordinates for the medbay teleporter. */
  public static final float[] MEDBAY_COORDINATES = {96f, 640f};

  // --------------------AUBER-------------------
  public float auberTeleporterCharge = 0f;
  /** The rate at which the teleporter ray charges. */
  public static final float AUBER_CHARGE_RATE = 0.05f;
  /** The rate at which the teleporter ray charges during the insta_beam ability. */
  public static final float AUBER_INSTA_BEAM_RATE = 1f;
  /** The number of buffs which should spawn when a system is attacked. (Affected by game difficulty)*/
  public int BUFFS_ON_ATTACK = 5;
  /** The time the ray should visibly render for. */
  public static final float AUBER_RAY_TIME = 0.25f;
  /** The time a debuff should last for (with the exception of blindness). */
  public static final float AUBER_DEBUFF_TIME = 5f;
  /** The time a buff should last for. */
  public float AUBER_BUFF_TIME = 10f;
  /** The rate at which auber should heal. */
  public static final float AUBER_HEAL_RATE = 0.005f;
  public static final Color rayColorA = new Color(0.106f, 0.71f, 0.714f, 1f);
  public static final Color rayColorB = new Color(0.212f, 1f, 1f, 0.7f);
  //</editor-fold>

  //<editor-fold desc="Rendering">
  // ------------------RENDERING-----------------
  /** IDs of layers that should be rendered behind entities. */
  public final int[] backgroundLayersIds = {
    map.getLayers().getIndex("background_layer"),
    };

  /** IDs of layers that should be rendered infront of entities. */
  public final int[] foregroundLayersIds = {
    map.getLayers().getIndex("foreground_layer"),
    map.getLayers().getIndex("collision_layer")
    };
  //</editor-fold>


  /** An enum containing information about all dynamic/frequently accessed tiles. */
  public static enum Tiles {
    WALL_SYSTEM(38),
    STANDALONE_SYSTEM(62),
    STANDALONE_SYSTEM_LIGHT(50),

    WALL_SYSTEM_ATTACKED(40),
    STANDALONE_SYSTEM_ATTACKED(64),
    STANDALONE_SYSTEM_LIGHT_ATTACKED(52),

    WALL_SYSTEM_DESTROYED(42),
    STANDALONE_SYSTEM_DESTROYED(66),
    STANDALONE_SYSTEM_LIGHT_DESTROYED(54),

    // Pathfinding tiles
    FLEE_POINT(57);



    public final int tileId;

    Tiles(int tileId) {
      this.tileId = tileId;
    }

    /**
     * Return a cell object for a given tile type via its tileId.
     *
     * @return A cell object for the given tile ID
     * */
    public Cell getCell() {
      Cell output = new Cell();
      output.setTile(tileset.getTile(tileId));
      return output;
    }

    /**
     * Return a tile from this enum by a given tile ID.
     *
     * @param id The ID of the {@link Tiles} object to return
     * @return A {@link Tiles} object of the given ID
     * @throws IllegalArgumentException Thrown if no tile could be found with the given ID
     * */
    public static Tiles getTileById(int id) {
      for (Tiles tile : Tiles.values()) {
        if (tile.tileId == id) {
          return tile;
        }
      }
      throw new IllegalArgumentException("Tile of given ID not found.");
    }
  }

  //<editor-fold desc="Constants">
  /** The amount of time it takes for an infiltrator to sabotage a system. */
  public static final float SYSTEM_BREAK_TIME = 5f;
  /** The chance an infiltrator will sabotage after pathfinding to a system. */
  public float SYSTEM_SABOTAGE_CHANCE = 0.6f;
  /** The distance the infiltrator can see. Default: 5 tiles */
  public static final float INFILTRATOR_SIGHT_RANGE = 80f;
  /** The speed at which infiltrator projectiles should travel. */
  public static final float INFILTRATOR_PROJECTILE_SPEED = 4f;
  /** Maximum infiltrators in a full game of Auber (including defated ones). */
  public static final int MAX_INFILTRATORS = 8;
  /** The interval at which the infiltrator should attack the player when exposed. */
  public float INFILTRATOR_FIRING_INTERVAL = 5f;
  /** The damage a projectile should do. */
  public static final float INFILTRATOR_PROJECTILE_DAMAGE = 0.1f;
  /**
   * Max infiltrators alive at a given point, Should always be less than
   * {@link World#MAX_INFILTRATORS}.
   * */
  public static final int MAX_INFILTRATORS_IN_GAME = 3;

  /** The amount of variance there should be between the speeds of different NPCs. */
  public static final float[] NPC_SPEED_VARIANCE = {0.8f, 1.2f};
  /** The maximum amount of time (in seconds) an NPC should flee for. */
  public static final float NPC_FLEE_TIME = 10f;
  /** The speed multiplier an NPC should receive when fleeing. */
  public static final float NPC_FLEE_MULTIPLIER = 1.2f;
  /** The shortest distance an NPC should move from its current position when fleeing. */
  public static final float NPC_MIN_FLEE_DISTANCE = 80f;
  /** The distance an NPC can here the teleporter ray shoot from. */
  public static final float NPC_EAR_STRENGTH = 80f;
  /** The number of NPCs in the game. */
  public static final int NPC_COUNT = 24;
  //</editor-fold>

  public static enum SystemStates {
    WORKING,
    ATTACKED,
    DESTROYED
  }

  @Override
  public void write(Json json) {
    json.writeValue("randomNumberGenerator", randomNumberGenerator);
    json.writeValue("cameraPosition", camera.position);
    json.writeValue("player", player);
    json.writeValue("infiltratorCount", infiltratorCount);
    json.writeValue("demoMode", demoMode);
    json.writeValue("infiltratorsAddedCount", infiltratorsAddedCount);
    json.writeValue("entities", entities.stream().filter(x -> !(x instanceof Player)).collect(Collectors.toList()));
    json.writeValue("buffsOnAttack", this.BUFFS_ON_ATTACK);
    json.writeValue("auberBuffTime", this.AUBER_BUFF_TIME);
    json.writeValue("infiltratorFiringInterval", this.INFILTRATOR_FIRING_INTERVAL);
    json.writeValue("systemSabotageChance", this.SYSTEM_SABOTAGE_CHANCE);
    json.writeValue("systems", systems);
  }

  @Override
  @SuppressWarnings("unchecked")
  public void read(Json json, JsonValue jsonData) {
    this.randomNumberGenerator = json.readValue("randomNumberGenerator", Random.class, jsonData);
    this.camera.position.set(json.readValue("cameraPosition", Vector3.class, jsonData));
    Gdx.app.log("load", jsonData.get("player").toString());
    json.readFields(this.player, jsonData.get("player"));
    this.entities.add(this.player);
    this.infiltratorCount = json.readValue("infiltratorCount", Integer.class, jsonData);
    this.demoMode = json.readValue("demoMode", Boolean.class, jsonData);
    this.infiltratorsAddedCount = json.readValue("infiltratorsAddedCount", Integer.class, jsonData);
    this.entities = json.readValue("entities", List.class, jsonData);
    this.BUFFS_ON_ATTACK = json.readValue("buffsOnAttack", Integer.class, jsonData);
    this.AUBER_BUFF_TIME = json.readValue("auberBuffTime", Float.class, jsonData);
    this.INFILTRATOR_FIRING_INTERVAL = json.readValue("infiltratorFiringInterval", Float.class, jsonData);
    this.SYSTEM_SABOTAGE_CHANCE = json.readValue("systemSabotageChance", Float.class, jsonData);
    this.systems = json.readValue("systems", ArrayList.class, jsonData);
    for (GameEntity e : entities) {
      if (e instanceof PowerUp) {
        ((PowerUp) e).world = this;
        ((PowerUp) e).recreateSprite();
      }
    }
  }

  /**
   * Initialise the game world with a given difficulty.
   *
   * @param game The game object.
   * @param diff The difficulty of the game {@link Difficulties}.
   * */
  public World(AuberGame game, Difficulties diff) {
    this.game = game;

    if (Gdx.app.getType() != Application.ApplicationType.HeadlessDesktop) {
      this.renderer = new OrthogonalTiledMapRenderer(map);
    }

    // Configure the camera
    camera.setToOrtho(false, 480, 270);
    camera.update();

    Player player = new Player(64f, 64f);
    queueEntityAdd(player);
    this.player = player;

    if (diff == Difficulties.Hard) {
      // Game in hard mode
      // Less buffs are dropped for auber
      this.BUFFS_ON_ATTACK = 2;
      // Buffs last less time
      this.AUBER_BUFF_TIME = 6f;
      // Infiltrators attack more often
      this.INFILTRATOR_FIRING_INTERVAL = 3f;
      // Infiltrators attempt to sabotage more
      this.SYSTEM_SABOTAGE_CHANCE = 0.8f;
    }
    // Easy by default

    MapObjects objects = map.getLayers().get("object_layer").getObjects();
    for (MapObject object : objects) {
      if (object instanceof RectangleMapObject) {
        RectangleMapObject rectangularObject = (RectangleMapObject) object;
        switch (rectangularObject.getProperties().get("type", String.class)) {
          case "system":
            systems.add(rectangularObject);
            break;
          case "medbay":
            medbay = rectangularObject;
            break;
          default:
            break;
        }
      }
    }

    TiledMapTileLayer navigationLayer = (TiledMapTileLayer) map.getLayers().get("navigation_layer");
    for (int y = 0; y < navigationLayer.getHeight(); y++) {
      for (int x = 0; x < navigationLayer.getWidth(); x++) {
        Cell currentCell = navigationLayer.getCell(x, y);
        float[] cellCoordinates = {x * navigationLayer.getTileWidth(),
                y * navigationLayer.getTileHeight()};
        if (currentCell != null) {
          spawnLocations.add(cellCoordinates);
          if (currentCell.getTile().getId() == Tiles.FLEE_POINT.tileId) {
            fleePoints.add(cellCoordinates);
          }
        }
      }
    }
  }

  /**
   * Initialise the game world with an easy difficulty (default).
   *
   * @param game The game object.
   * */
  public World(AuberGame game) {
    this(game, Difficulties.Easy);
  }

  /**
   * Initialise an instance of the world with the given game object.
   * Demo mode locks the player to an NPC.
   *
   * @param game The game object
   * @param demoMode Whether to run the game in demo mode
   * */
  public World(AuberGame game, boolean demoMode) {
    this(game);
    this.demoMode = demoMode;
    if (demoMode) {
      camera.setToOrtho(false, 480, 270);
      camera.update();
    }
  }

  /**
   * Saves the state of the game to a file - ".auber/save.json" in the user's home directory.
   */
  public void saveGame() {
    Json json = new Json();
    json.setOutputType(JsonWriter.OutputType.json);
    FileHandle file = Gdx.files.external(".auber/save.json");
    file.writeString(
        json.toJson(this),
        false
    );
  }

  public void addEntity(GameEntity entity) {
    entities.add(entity);
  }

  public List<GameEntity> getEntities() {
    return entities;
  }

  /**
   * Queue an entity to be added.
   *
   * @param entity The entity to queue
   * */
  public void queueEntityAdd(GameEntity entity) {
    newEntities.add(entity);
  }

  /**
   * Queue an entity to be removed.
   *
   * @param entity The entity to queue
   * */
  public void queueEntityRemove(GameEntity entity) {
    oldEntities.add(entity);
  }

  /**
   * Apply any queued entity removals/additions to the world.
   * */
  public void updateEntities() {
    entities.addAll(newEntities);
    entities.removeAll(oldEntities);
    newEntities.clear();
    oldEntities.clear();
  }

  /**
   * Update the sprite of a system to match a new state.
   *
   * @param x The x coordinate of the system object (not the tile)
   * @param y The y coordinate of the system object (not the tile)
   * @param newState The new state of the system
   **/
  public void updateSystemState(float x, float y, SystemStates newState) {
    TiledMapTileLayer collisionLayer = (TiledMapTileLayer) World.map.getLayers()
        .get("collision_layer");

    int[] systemPosition = {
      (int) x / collisionLayer.getTileWidth(),
      (int) (y / collisionLayer.getTileHeight()) + 1
      };

    Cell attackedSystemCell = collisionLayer.getCell(systemPosition[0], systemPosition[1]);
    int targetTileId = attackedSystemCell.getTile().getId();

    if (targetTileId == World.Tiles.STANDALONE_SYSTEM.tileId
        || targetTileId == World.Tiles.STANDALONE_SYSTEM_ATTACKED.tileId
        || targetTileId == World.Tiles.STANDALONE_SYSTEM_DESTROYED.tileId) {
      Cell newSystem;
      Cell newSystemLight;

      switch (newState) {
        case WORKING:
          newSystem = World.Tiles.STANDALONE_SYSTEM.getCell();
          newSystemLight = World.Tiles.STANDALONE_SYSTEM_LIGHT.getCell();
          break;
        case ATTACKED:
          newSystem = World.Tiles.STANDALONE_SYSTEM_ATTACKED.getCell();
          newSystemLight = World.Tiles.STANDALONE_SYSTEM_LIGHT_ATTACKED.getCell();
          break;
        case DESTROYED:
          newSystem = World.Tiles.STANDALONE_SYSTEM_DESTROYED.getCell();
          newSystemLight = World.Tiles.STANDALONE_SYSTEM_LIGHT_DESTROYED.getCell();
          break;
        default:
          // This line will never fire, However it needs to be here otherwise Java
          // will think that theres a chance newSystem and newSystemLight may not have been
          // initiated even though every enum state is covered
          return;
      }
      collisionLayer.setCell(systemPosition[0], systemPosition[1], newSystem);

      TiledMapTileLayer foregroundLayer = (TiledMapTileLayer) map.getLayers()
          .get("foreground_layer");
      foregroundLayer.setCell(systemPosition[0], systemPosition[1] + 1, newSystemLight);
    } else {
      Cell newSystem;
      switch (newState) {
        case WORKING:
          newSystem = World.Tiles.WALL_SYSTEM.getCell();
          break;
        case ATTACKED:
          newSystem = World.Tiles.WALL_SYSTEM_ATTACKED.getCell();
          break;
        case DESTROYED:
          newSystem = World.Tiles.WALL_SYSTEM_DESTROYED.getCell();
          break;
        default:
          // See above
          return;
      }
      collisionLayer.setCell(systemPosition[0], systemPosition[1], newSystem);
    }

    if (newState == SystemStates.DESTROYED) {
      for (RectangleMapObject system : systems) {
        if (system.getRectangle().getX() == x
            && system.getRectangle().getY() == y) {
          systems.remove(system);
          break;
        }
      }
    }
  }

  /**
   * Return the state of a system, given the coordinates of the system object (not the tile).
   *
   * @param x The x coordinate to check
   * @param y The y coordinate to check
   * @return A {@link SystemStates} representing the state of the system
   * @throws IllegalArgumentException if a standalone system light is at the coordinates provided
   * */
  public SystemStates getSystemState(float x, float y) {
    TiledMapTileLayer collisionLayer = (TiledMapTileLayer) World.map.getLayers()
        .get("collision_layer");

    int[] systemPosition = {
      (int) x / collisionLayer.getTileWidth(),
      (int) (y / collisionLayer.getTileHeight()) + 1
      };

    Cell attackedSystemCell = collisionLayer.getCell(systemPosition[0], systemPosition[1]);
    if (attackedSystemCell == null) {
      throw new IllegalArgumentException("No tile at given coordinates");
    }

    Tiles targetTile = Tiles.getTileById(attackedSystemCell.getTile().getId());

    switch (targetTile) {
      case WALL_SYSTEM:
      case STANDALONE_SYSTEM:
        return SystemStates.WORKING;

      case WALL_SYSTEM_ATTACKED:
      case STANDALONE_SYSTEM_ATTACKED:
        return SystemStates.ATTACKED;

      case WALL_SYSTEM_DESTROYED:
      case STANDALONE_SYSTEM_DESTROYED:
        return SystemStates.DESTROYED;

      default:
        throw new IllegalArgumentException("Use the coordinates of the System object on the"
                                           .concat("tilemap - not the system tile."));
    }
  }

  public SystemStates getSystemState(RectangleMapObject system) {
    return getSystemState(system.getRectangle().x, system.getRectangle().y);
  }

  /**
   * Check to see if any of the end conditions have been met, if so update the screen.
   * */
  public void checkForEndState() {
    if (systems.isEmpty()) {
      if (!demoMode) {
        game.setScreen(new GameOverScreen(game, false));
      } else {
        game.setScreen(new GameScreen(game, true));
      }
    } else if (infiltratorCount <= 0) {
      game.setScreen(new GameOverScreen(game, true));
    }
  }

  /**
   * Spawns in a random buff in the position of a random entity (Civilians only)
   * */

  public void spawnBuff() {
    GameEntity ent;
    boolean civExist = false;
    for (GameEntity entity : this.getEntities()) {
      if (entity instanceof Civilian) {
        civExist = true;
        break;
      }
    }
    if (civExist) {
      do {
        ent = this.getEntities().get(this.randomNumberGenerator.nextInt(this.getEntities().size()));
      } while (!(ent instanceof Civilian));
      Vector2 spawnPos = ent.position;
      PowerUp powerUpTest = new PowerUp(spawnPos.x, spawnPos.y, this, Abilities.randomAbility());
      queueEntityAdd(powerUpTest);
    }
    else {
      // No spawn possible
    }
  }
}
