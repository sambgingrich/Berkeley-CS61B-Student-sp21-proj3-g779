package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import java.util.Random;
import byow.Core.Room.*;
import static byow.Core.Room.drawRoom;
import static byow.Core.Hallway.drawHallway;
import static byow.Core.Room.randomRoomOpening;

public class TestingMain {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;

    public static final long SEED = 2873124;
    public static final Random RANDOM = new Random(SEED);

    /**
     * Fills the given 2D array of tiles with RANDOM tiles.
     * @param tiles
     */
    public static void fillWithNothing(TETile[][] tiles) {
        int height = tiles[0].length;
        int width = tiles.length;
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }

    /*
    This is an example of what you can draw with what I have so far.
    The room only lines up with one of the hallways if the seed is 2873124
     */
    public static void drawWorld(TETile[][] tiles) {
        fillWithNothing(tiles);
        Position start = new Position(20, 20);
        Position opening = randomRoomOpening(start, 10, 4);
        Position[] openings = new Position[1];
        openings[0] = opening;
        drawRoom(tiles, start, 10, 4, openings);
        drawHallway(tiles, false, new Position(22, 24), 2);
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] world = new TETile[WIDTH][HEIGHT];
        drawWorld(world);

        ter.renderFrame(world);
    }
}
