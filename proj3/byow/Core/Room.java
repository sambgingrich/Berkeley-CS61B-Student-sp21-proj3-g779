package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import static byow.Core.TestingMain.RANDOM;

public class Room {
    public static class Position {
        int x;
        int y;

        public Position(int x, int y) {
            this.x = x;
            this.y  = y;
        }
    }

    public static void drawRow(TETile[][] tiles, boolean allWall, Position p, int x) {
        if (allWall) {
            for (int dx = 0; dx < x; dx ++) {
                tiles[p.x + dx][p.y] = Tileset.WALL;
            }
        } else {
            tiles[p.x][p.y] = Tileset.WALL;
            for (int dx = 1; dx < x-1; dx++) {
                tiles[p.x + dx][p.y] = Tileset.FLOOR;
            }
            tiles[p.x + x - 1][p.y] = Tileset.WALL;
        }
    }

    public static void drawRoom (TETile[][] tiles, Position start, int x, int y, Position[] openings) {
        drawRow(tiles, true, start, x);
        for (int dy = 1; dy < y -1; dy++) {
            Position p = new Position(start.x, start.y + dy);
            drawRow(tiles, false, p, x);
        }
        Position top = new Position(start.x, start.y + y - 1);
        drawRow(tiles, true, top, x);
        for (Position o : openings) {
            tiles[o.x][o.y] = Tileset.FLOOR;
        }
    }

    public static Position randomRoomOpening(Position start, int x, int y) {
        int topOrRight = RANDOM.nextInt(1);
        if (topOrRight == 0) { // top
            int openingX = RANDOM.nextInt(x - 2);
            return new Position(start.x + 1 + openingX, start.y + y - 1);
        } else { // right
            int openingY = RANDOM.nextInt(y - 2);
            return new Position(start.x + x - 1, start.y + 1 + openingY);
        }
    }
}
