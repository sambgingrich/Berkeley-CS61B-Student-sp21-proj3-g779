package byow.Core;

import byow.TileEngine.TETile;
import static byow.Core.Room.*;
import static byow.Core.TestingMain.RANDOM;

public class Hallway {
    /*
    Draws a hallway wit the first floor tile at position START with length LENGTH and horizontal
    when HORIZONTAL is true and vertical when it is false. The start position is always an opening.
     */
    public static void drawHallway (TETile[][] tiles, boolean horizontal, Position start, int length) {
        Position[] openings = new Position[2];
        if (horizontal) {
            Position startFloor = new Position(start.x, start.y + 1);
            openings[0] = startFloor;
            Position opening2 = randomHallwayOpening(startFloor, true, length);
            openings[1] = opening2;
            drawRoom(tiles, start, length, 3, openings);
        } else {
            Position startFloor = new Position(start.x + 1, start.y);
            openings[0] = startFloor;
            Position opening2 = randomHallwayOpening(startFloor, false, length);
            openings[1] =opening2;
            drawRoom(tiles, start, 3, length, openings);
        }
    }

    /*
    Generates a position for an opening based on a hallway start tile which is always the first floor tile
    and the orientation of the hallway. Always pics an opening on the top or right side of the hallway
    in line with the algorithm 2 of the design doc which builds the world from the bottom left corner.
     */
    public static Position randomHallwayOpening(Position start, boolean horizontal, int length) {
        int topOrRight = RANDOM.nextInt(1);
        if (topOrRight == 0) { // top
            if (horizontal) {
                int openingX = RANDOM.nextInt(length - 2);
                return new Position(start.x + 1 + openingX, start.y + 1);
            } else {
                return new Position(start.x, start.y + length - 1);
            }
        } else { // right
            if (horizontal) {
                return new Position(start.x + length - 1, start.y);
            } else {
                int openingY = RANDOM.nextInt(length - 2);
                return new Position(start.x + 1, start.y + 1 + openingY);
            }
        }
    }
}
