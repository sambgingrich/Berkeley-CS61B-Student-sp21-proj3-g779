package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import java.util.Random;
import byow.Core.Room.*;
import com.sun.tools.hat.internal.parser.PositionInputStream;

import static byow.Core.Room.drawRoom;

public class TestingMain {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;

    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);

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

    public static void drawWorld(TETile[][] tiles) {
        fillWithNothing(tiles);
        Position start = new Room.Position(20, 20);
        Position opening = new Room.Position(22, 20);
        Position[] openings = new Position[1];
        openings[0] = opening;
        drawRoom(tiles, start, 10, 4, openings);
        //drawRow(tiles, true, start, 7);
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] world = new TETile[WIDTH][HEIGHT];
        drawWorld(world);

        ter.renderFrame(world);
    }
}
