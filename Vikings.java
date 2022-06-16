package comp1110.ass1;

/**
 * This class represents the Vikings - Brainstorm game.
 * <p>
 * The board state is represented by a single string.
 * The first 18 characters represent the placement of the nine tiles in
 * order from left to right and top to bottom.
 * <p>
 * 0   1   2
 * 3   4   5
 * 6   7   8
 * <p>
 * The two tile types are represented by the uppercase characters 'N'
 * and 'O', with the four rotations represented by the digits 0-3.
 * The four boats are represented by an uppercase character which is the
 * first letter of the name of the colour i.e. 'B','G','R','Y'.
 * Edges are represented by a lowercase character 'a'-'x'.
 * <p>
 * <pre>
 *   a   b   c
 * d 0 e 1 f 2 g
 *   h   i   j
 * k 3 l 4 m 5 n
 *   o   p   q
 * r 6 s 7 t 8 u
 *   v   w   x
 * </pre>
 * <p>
 * Thus the string 'Bl' represents a blue boat at edge 'l' (in between
 * tiles '3' and '4'.
 * <p>
 * The first objective in the game book is
 * "N0O1N1N0O0O1N0N3N1Rt", "Rv"
 * Where "N0O1N1N0O0O1N0N3N1Rt" represents the initial boardState and "Rv" represents the objective (ie:
 * the red boat get to edge v)
 */
public class Vikings {
    /**
     * An array of the nine tiles on the Vikings game board.
     * Since this data structure reflects the board state,
     * it is initially empty until we initialize a game.
     */
    final Tile[] tiles;

    /**
     * An array of boats for the current game,
     * containing at least 1 and no more than 4 boats.
     */
    final Boat[] boats;

    /**
     * The objective of this game
     */
    private Objective objective;

    public Vikings(Objective objective) {
        this.objective = objective;
        String boardString = objective.getInitialState();
        tiles = Tile.fromBoardString(boardString);
        boats = Boat.fromBoardString(boardString);
    }

    /**
     * Construct a game for a given level of difficulty.
     * This chooses a new objective and creates a new instance of
     * the game at the given level of difficulty.
     *
     * @param difficulty The difficulty of the game.
     */
    public Vikings(int difficulty) {
        this(Objective.newObjective(difficulty));
    }

    public Objective getObjective() {
        return objective;
    }

    /**
     * A boardString is well-formed if it contains:
     * - nine tiles, each with:
     * - a correct type for each tile;
     * - 6 'N' tiles and 3 'O' tiles;
     * - the correct rotation of tiles (0-3);
     * - at least 1 boat at a valid edge; and
     * - no more than 1 boat of each colour, in order B-G-R-Y.
     * Note: a well-formed boardString is not necessarily a valid boardString!
     * (For example, tiles and boats may overlap.)
     *
     * @param boardString A string representing the board
     * @return true if boardString is well-formed, false if boardString is not well-formed.
     */
    public static boolean isBoardStringWellFormed(String boardString) {


        //Initialize all the counters for Tile type and Boat color, and other increments

        int i,j,k;
        int O_counter = 0, N_counter = 0;
        int B_counter = 0, G_counter = 0, R_counter = 0, Y_counter = 0;

        //First to check if the boardstring with correct length

        if(boardString.length() < 20 || boardString.length() > 26){
            return false;
        }else {
            //Partition of whole boardstring to two parts, Tiles and Boats
            //Count the number of Tile types are correct or not
            String Boat = boardString.substring(18);
            for (i = 0; i < 18; i = i + 2) {
                if (boardString.charAt(i) != 'O' && boardString.charAt(i) != 'N') {
                    return false;
                }
                if (boardString.charAt(i) == 'O')
                    O_counter++;
                if (boardString.charAt(i) == 'N')
                    N_counter++;
            }
            //Call the helper method to determine if the orientation is valid or not

            for (j = 1; j < 18; j = j + 2) {
                if (!isInRange(boardString.charAt(j)))
                    return false;
            }
            if (O_counter != 3 || N_counter != 6)
                return false;

            //Test for Color with right number and right order
            for (k = 0; k < Boat.length(); k = k + 2) {
                if (Boat.charAt(k) != 'B' && Boat.charAt(k) != 'G' && Boat.charAt(k) != 'R' && Boat.charAt(k) != 'Y')
                    return false;
                if (Boat.charAt(k) == 'B')
                    B_counter++;
                if (Boat.charAt(k) == 'G')
                    G_counter++;
                if (Boat.charAt(k) == 'R')
                    R_counter++;
                if (Boat.charAt(k) == 'Y')
                    Y_counter++;
            }

            if (B_counter > 1 || G_counter > 1 || R_counter > 1 || Y_counter > 1)
                return false;

            if (Boat.contains("B") && Boat.contains("G") && Boat.contains("R") && Boat.contains("Y")) {
                if (Boat.indexOf("B") > Boat.indexOf("G") || Boat.indexOf("B") > Boat.indexOf("R") || Boat.indexOf("B") > Boat.indexOf("Y")) {
                    return false;
                } else if (Boat.indexOf("G") > Boat.indexOf("R") || Boat.indexOf("G") > Boat.indexOf("Y")) {
                    return false;
                } else if (Boat.indexOf("R") > Boat.indexOf("Y"))
                    return false;
            } else if (Boat.contains("G") && Boat.contains("R") && Boat.contains("Y")) {
                if (Boat.indexOf("Y") < Boat.indexOf("G") || Boat.indexOf("R") < Boat.indexOf("G")) {
                    return false;
                } else if (Boat.indexOf("Y") < Boat.indexOf("R"))
                    return false;
            }
        }
        return true;
    }
    /**
     * The helper method of determining if the orientation is valid or not
     * if within 0 and 3, orientation is valid
     * otherwise, invalid
     *
     * @param c character typed orientation
     */
    public static boolean isInRange(char c){
        int target = Character.getNumericValue(c);
        if (target < 0 && target > 3){
            return false;
        }
        return true;
    }

    /**
     * @param boardString a well-formed board string
     * @param position1   Position of tile 1
     * @param position2   Position of tile 2
     * @return true if Tiles overlap, False if tiles do not overlap
     */
    public static boolean doTilesOverlap(String boardString, int position1, int position2) {
        //Get the tile lists by using fromBoardstring

        Tile [] tiles = Tile.fromBoardString(boardString);
        int temp1,temp2;

        //To handle the condition of 7 and 4 sames as 4 and 7
        if(position2 < position1){
          temp1 = position2;
          temp2 = position1;
          position1 = temp1;
          position2 = temp2;
        }
        Tile t1 = tiles[position1];
        Tile t2 = tiles[position2];


        //Test when two position are vertically next to each other
        //According to different conditions to determine whether they are overlapped or not

        if(Math.abs(position1 - position2) ==3){
            if(t1.getTileType() == TileType.O && t2.getTileType() == TileType.O){
                if(t1.getOrientation() == 0 && t2.getOrientation() == 2){
                    return true;
                }
                else if(t1.getOrientation() == 2 && t2.getOrientation() ==0){
                    return true;
                }
                else if(t1.getOrientation() == 0 && t2.getOrientation() == 0){
                    return true;
                } else if (t1.getOrientation() == 2 && t2.getOrientation() == 2) {

                    return true;
                }else{
                    return false;
                }
            }
            else if(t1.getTileType() == TileType.N && t2.getTileType() == TileType.N){
                if(t1.getOrientation() == 2 && t2.getOrientation() == 0){
                    return true;
                }
                else if(t1.getOrientation() == 2 && t2.getOrientation() == 1){
                    return true;
                }
                else if(t1.getOrientation() == 3 && t2.getOrientation() == 0){
                    return true;
                }
                else if(t1.getOrientation() == 3 && t2.getOrientation() == 1){
                    return true;
                }else{
                    return false;
                }
            }
            else if(t1.getTileType() == TileType.O && t2.getTileType() == TileType.N){
                if(t1.getOrientation() == 0 && t2.getOrientation() == 0){
                    return true;
                }
                else if(t1.getOrientation() == 0 && t2.getOrientation() == 1){
                    return true;
                }
                else if(t1.getOrientation() == 2 && t2.getOrientation() == 0){
                    return true;
                }
                else if(t1.getOrientation() == 2 && t2.getOrientation() == 1){
                    return true;
                }else{
                    return false;
                }
            }
            else if(t1.getTileType() == TileType.N && t2.getTileType() == TileType.O){
                if(t1.getOrientation() == 2 && t2.getOrientation() ==0){
                    return true;
                }
                else if(t1.getOrientation() == 2 && t2.getOrientation() ==2){
                    return true;
                }
                else if(t1.getOrientation() == 3 && t2.getOrientation() == 0){
                    return true;
                }
                else if(t1.getOrientation() == 3 && t2.getOrientation() == 2){
                    return true;
                }else{
                    return false;
                }
            }
            //To test when two positions are horizontally next to each other
            //Determine whether they are overlapped by different conditions

        }else if(Math.abs(position1 - position2) == 1){
            if((position1 == 2 && position2 == 3) || (position1 == 5 && position2 == 6) )
                return false;
            if(t1.getTileType() == TileType.O && t2.getTileType() == TileType.O){
                if(t1.getOrientation() == 1 && t2.getOrientation() == 1){
                    return true;
                }
                else if(t1.getOrientation() == 3 && t2.getOrientation() ==3){
                    return true;
                }
                else if(t1.getOrientation() == 1 && t2.getOrientation() == 3){
                    return true;
                } else if (t1.getOrientation() == 3 && t2.getOrientation() == 1) {

                    return true;
                }else{
                    return false;
                }
            }
            else if(t1.getTileType() == TileType.N && t2.getTileType() == TileType.N){
                if(t1.getOrientation() == 1 && t2.getOrientation() == 0){
                    return true;
                }
                else if(t1.getOrientation() == 1 && t2.getOrientation() == 3){
                    return true;
                }
                else if(t1.getOrientation() == 2 && t2.getOrientation() == 0){
                    return true;
                }
                else if(t1.getOrientation() == 2 && t2.getOrientation() == 3){
                    return true;
                }else{
                    return false;
                }
            }
            else if(t1.getTileType() == TileType.O && t2.getTileType() == TileType.N){
                if(t1.getOrientation() == 1 && t2.getOrientation() == 0){
                    return true;
                }
                else if(t1.getOrientation() == 1 && t2.getOrientation() == 3){
                    return true;
                }
                else if(t1.getOrientation() == 3 && t2.getOrientation() == 0){
                    return true;
                }
                else if(t1.getOrientation() == 3 && t2.getOrientation() == 3){
                    return true;
                }else{
                    return false;
                }
            }
            else if(t1.getTileType() == TileType.N && t2.getTileType() == TileType.O){
                if(t1.getOrientation() == 1 && t2.getOrientation() ==1){
                    return true;
                }
                else if(t1.getOrientation() == 2 && t2.getOrientation() ==1){
                    return true;
                }
                else if(t1.getOrientation() == 1 && t2.getOrientation() == 3){
                    return true;
                }
                else if(t1.getOrientation() == 2 && t2.getOrientation() == 3){
                    return true;
                }else{
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * A board string is valid if it is well-formed,
     * there are no two boats on the same edge, and no two pieces overlapping.
     *
     * @param boardString a board string
     * @return True if valid, false if invalid.
     */
    public static boolean isBoardStringValid(String boardString) {

        //Initialize variables so that we can re-assign the value to them later

        boolean check_boat = true;

        Boat [] Boats = Boat.fromBoardString(boardString);
        char first_loc = Boats[0].getLocation().toEdge();

        //Check if two boats on the same edge

        for(int z = 1; z < Boats.length; z++){
            if(Boats[z].getLocation().toEdge() == first_loc){
                check_boat = false;
                break;
            }else{
                first_loc = Boats[z].getLocation().toEdge();
            }
        }
        //Check if the string is well formed

        boolean check_form = isBoardStringWellFormed(boardString);
        boolean check_overlap = true;
        boolean result;

        //Check if it's overlapping

        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                if(i == j){
                    continue;
                }
                if(doTilesOverlap(boardString, i, j)){
                    check_overlap = false;
                    break;
                }
            }
        }

        //If none of those problems above occured, then the string is valid
        //Otherwise, it's invalid

        if(check_form && check_overlap && check_boat){
            result = true;
        }else
            result = false;


        return result;
    }

    /**
     * Given two adjacent tiles, decide whether they interlock.
     * Two tiles interlock if there is no gap on the edge between them.
     * For example: in OBJECTIVES[0], Tile "O0" in position "4" interlocks with Tile "O1" in position "2".
     * However, Tile "O0" in position "4" does not interlock with Tile "N0" in position "3".
     *
     * @param position1 int representing the position of tile 1
     * @param position2 int representing the position of tile 2
     * @return True if tiles interlock, False if they do not interlock.
     */
    public static boolean doTilesInterlock(String boardString, int position1, int position2) {

        //Obtain the list of tiles from boardstring

        Tile [] tiles = Tile.fromBoardString(boardString);
        int temp1, temp2;

        //Handle the condition when testing 7 and 4 but actually test 4 and 7

        if(position2 < position1){
            temp1 = position2;
            temp2 = position1;
            position1 = temp1;
            position2 = temp2;
        }
        Tile t1 = tiles[position1];
        Tile t2 = tiles[position2];

        //Test when two positions are horizontally next to each other and determine it's interlock by several conditions

        if(Math.abs(position1 - position2) == 1){
            if(t1.getTileType() == TileType.O && t2.getTileType() == TileType.O){
                if(t1.getOrientation() == 0 && t2.getOrientation() == 0){
                    return false;
                }else if(t1.getOrientation() == 2 && t2.getOrientation() == 2){
                    return  false;
                }
            }
            else if(t1.getTileType() == TileType.N && t2.getTileType() == TileType.N){
                if(t1.getOrientation() == 0 && t2.getOrientation() == 1){
                    return false;
                }
                else if(t1.getOrientation() == 0 && t2.getOrientation() == 2){
                    return false;
                }
                else if(t1.getOrientation() == 3 && t2.getOrientation() == 1){
                    return false;
                }
                else if(t1.getOrientation() == 3 && t2.getOrientation() == 2){
                    return false;
                }
            }
            else if(t1.getTileType() == TileType.N && t2.getTileType() == TileType.O){
                if(t1.getOrientation() == 0 && t2.getOrientation() == 0){
                    return false;
                }
                else if(t1.getOrientation() == 0 && t2.getOrientation() == 2){
                    return false;
                }
                else if(t1.getOrientation() == 3 && t2.getOrientation() == 0){
                    return false;
                }
                else if(t1.getOrientation() == 3 && t2.getOrientation() == 2){
                    return false;
                }
            }
            else if(t1.getTileType() == TileType.O && t2.getTileType() == TileType.N){
                if(t1.getOrientation() == 0 && t2.getOrientation() == 1){
                    return false;
                }
                else if(t1.getOrientation() == 2 && t2.getOrientation() == 1){
                    return false;
                }
                else if(t1.getOrientation() == 0 && t2.getOrientation() == 2){
                    return false;
                }
                else if(t1.getOrientation() == 2 && t2.getOrientation() == 2){
                    return false;
                }
            }

            //Test when two positions are vertically next to each other and determine it's interlock by several conditions


        }else if(Math.abs(position1 - position2) ==3){
            if(t1.getTileType() == TileType.O && t2.getTileType() == TileType.O){
                if(t1.getOrientation() == 1 && t2.getOrientation() == 1){
                    return false;
                }
                else if(t1.getOrientation() == 1 && t2.getOrientation() ==3){
                    return false;
                }
                else if(t1.getOrientation() == 3 && t2.getOrientation() == 1){
                    return false;
                } else if (t1.getOrientation() == 3 && t2.getOrientation() == 3) {

                    return false;
                }
            }
            else if(t1.getTileType() == TileType.N && t2.getTileType() == TileType.N){
                if(t1.getOrientation() == 0 && t2.getOrientation() == 2){
                    return false;
                }
                else if(t1.getOrientation() == 1 && t2.getOrientation() == 2){
                    return false;
                }
                else if(t1.getOrientation() == 0 && t2.getOrientation() == 3){
                    return false;
                }
                else if(t1.getOrientation() == 1 && t2.getOrientation() == 3){
                    return false;
                }
            }
            else if(t1.getTileType() == TileType.O && t2.getTileType() == TileType.N){
                if(t1.getOrientation() == 1 && t2.getOrientation() == 2){
                    return false;
                }
                else if(t1.getOrientation() == 1 && t2.getOrientation() == 3){
                    return false;
                }
                else if(t1.getOrientation() == 3 && t2.getOrientation() == 2){
                    return false;
                }
                else if(t1.getOrientation() == 3 && t2.getOrientation() == 3){
                    return false;
                }
            }
            else if(t1.getTileType() == TileType.N && t2.getTileType() == TileType.O){
                if(t1.getOrientation() == 0 && t2.getOrientation() ==1){
                    return false;
                }
                else if(t1.getOrientation() == 1 && t2.getOrientation() ==1){
                    return false;
                }
                else if(t1.getOrientation() == 0 && t2.getOrientation() == 3){
                    return false;
                }
                else if(t1.getOrientation() == 1 && t2.getOrientation() == 3){
                    return false;
                }
            }
        }


        return true;
    }

    /**
     * Returns true if the given tile is currently able to be rotated
     * i.e. it has a boat on at least one edge, and its rotation is not
     * blocked by any other tile.
     *
     * @param boardString a valid board string
     * @param position    '0'-'8' represents the position of the tile to be rotated
     * @return True if tile can be rotated, otherwise return false (tile can't be rotated)
     */

    public static boolean canRotateTile(String boardString, int position) {

        //obtain the target tile from a list of tiles by using given position

        Tile[] tiles = Tile.fromBoardString(boardString);
        Tile target = tiles[position];

        //Get a list of boats from boardstring and location of the target tiles

        Boat[] boats = Boat.fromBoardString(boardString);
        Location[] cur_loc = Location.getEdgeLocationsForTilePosition(target.getPosition());

        //Initialize some variables so that we can re-assign the value to them

        boolean boat_on_border = false;
        String new_boardstring;

        //To check one boat on at least one edge of the target tile


        for (int i = 0; i < boats.length; i++) {
            Location boat_loc = boats[i].getLocation();
            for (int j = 0; j < cur_loc.length; j++) {
                if (cur_loc[j].isEqual(boat_loc)) {
                    boat_on_border = true;
                    break;
                }
            }
            if (boat_on_border == true) {
                break;
            }
        }

        //To change the old boardstring to the new version of boardstring with new orientation for the given target tile
        //That will help me to determine if it can be rotated by comparing the state of after rotation to its surrounding
        //tiles


        if (target.getOrientation() != 3) {
            String new_o = Integer.toString(Character.getNumericValue(boardString.charAt(position * 2 + 1)) + 1);
            new_boardstring = boardString.substring(0, position * 2 + 1).concat(new_o).concat(boardString.substring(position * 2 + 2));
        } else {
            new_boardstring = boardString.substring(0, position * 2 + 1).concat(Integer.toString(0)).concat(boardString.substring(position * 2 + 2));
        }
        boardString = new_boardstring;


        //To test if the tiles after rotation overlap or interlock with other surrounding tiles


        if(boat_on_border == true) {
            if (position == 0) {
                if (!doTilesOverlap(boardString, 0, 1) && !doTilesOverlap(boardString, 0, 3)) {
                    if(!doTilesInterlock(boardString,0,1) || doTilesInterlock(boardString,0,3))
                        return true;
                }
            } else if (position == 1) {
                if (!doTilesOverlap(boardString, 1, 2) && !doTilesOverlap(boardString, 1, 4) && !doTilesOverlap(boardString,1,0)) {
                    return true;
                }
            } else if (position == 2) {
                if (!doTilesOverlap(boardString, 2, 5) && !doTilesOverlap(boardString,2,1))
                    return true;
            } else if (position == 3) {
                if (!doTilesOverlap(boardString, 3, 4) && !doTilesOverlap(boardString, 3, 6) && !doTilesOverlap(boardString,3,0)) {
                    if(!doTilesInterlock(boardString,3,4) || doTilesInterlock(boardString,3,6))
                    return true;
                }
            } else if (position == 4) {
                if (!doTilesOverlap(boardString, 4, 5) && !doTilesOverlap(boardString, 4, 7) && !doTilesOverlap(boardString,4,1) && !doTilesOverlap(boardString,4,3)) {
                    if(!doTilesInterlock(boardString,4,5))
                        return true;
                }
            } else if (position == 5) {
                if (!doTilesOverlap(boardString, 5, 8) && !doTilesOverlap(boardString,5,2) && !doTilesOverlap(boardString,5,4)) {
                    return true;
                }
            } else if (position == 6) {
                if (!doTilesOverlap(boardString, 6, 7) && !doTilesOverlap(boardString,6,3)) {
                    if(!doTilesInterlock(boardString,6,3))
                        return true;
                }
            } else if (position == 7) {
                if (!doTilesOverlap(boardString, 7, 8) && !doTilesOverlap(boardString,7,4) && !doTilesOverlap(boardString,7,6)) {
                    if(target.getTileType() == TileType.O)
                        return true;
                }
            }else if(position == 8){
                if(!doTilesOverlap(boardString,8,7) && !doTilesOverlap(boardString,8,5)){
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Rotate the specified tile one quarter-turn (90 degrees) clockwise.
     *
     * @param boardString String representing the board
     * @param pos         position of the tile to be rotated
     * @return An updated boardString that reflects the rotation
     */
    public static String rotateTile(String boardString, int pos) {

        //Initialize several variables so that we can re-assign the value to them later
        //nbs stands for new board string, new_o stands for new orientation
        //s stands for the final string after rotation
        //n stands for the new edge that boat is on after the rotation

        String nbs, b, new_o = "";
        Tile[] tiles = Tile.fromBoardString(boardString);
        Tile target = tiles[pos];
        Location l = new Location(0,0);
        char newedge, n = ' ';
        String s="";

        //Seperating the board string into two parts, one is Tile with orientation, the other one is boat with edge

        Boat[] boats = Boat.fromBoardString(boardString);
        Location[] cur_loc = Location.getEdgeLocationsForTilePosition(target.getPosition());
        b = boardString.substring(18);

        //To change the orientation to the new one after rotation then concatenate the string with the change

        if (target.getOrientation() != 3) {
            new_o = Integer.toString(Character.getNumericValue(boardString.charAt(pos * 2 + 1)) + 1);
            nbs = boardString.substring(0, pos * 2 + 1).concat(new_o).concat(boardString.substring(pos * 2 + 2,18));
        } else {
            nbs = boardString.substring(0, pos * 2 + 1).concat(Integer.toString(0)).concat(boardString.substring(pos * 2 + 2,18));
        }


        //To change the edge for each boat

        for (int i = 0; i < boats.length; i++) {
            Location boat_loc = boats[i].getLocation();
            for (int j = 0; j < cur_loc.length; j++) {
                if (cur_loc[j].isEqual(boat_loc)) {

                    l = new Location(cur_loc[j].getX(),cur_loc[j].getY());
                    newedge = l.toEdge();
                    if(pos == 0){
                        char [] c1 = {'a','e','h','d'};
                        for(int z=0;z<c1.length;z++){
                            if(newedge == c1[z])
                                if(z+1 >= c1.length)
                                    n = c1[0];
                                else
                                    n = c1[z+1];
                        }
                    }else if(pos == 1){
                        char [] c1 = {'b','f','i','e'};
                        for(int z=0;z<c1.length;z++){
                            if(newedge == c1[z])
                                if(z+1 >= c1.length)
                                    n = c1[0];
                                else
                                    n = c1[z+1];
                        }
                    }else if(pos == 2){
                            char [] c1 = {'c','g','j','f'};
                            for(int z=0;z<c1.length;z++){
                                if(newedge == c1[z])
                                    if(z+1 >= c1.length)
                                        n = c1[0];
                                    else
                                        n = c1[z+1];
                        }
                    }else if(pos == 3) {
                        char[] c1 = {'h', 'l', 'o', 'k'};
                        for (int z = 0; z < c1.length; z++) {
                            if (newedge == c1[z])
                                if (z + 1 >= c1.length)
                                    n = c1[0];
                                else
                                    n = c1[z + 1];
                        }
                    }else if(pos ==4) {
                        char[] c1 = {'i', 'm', 'p', 'l'};
                        for (int z = 0; z < c1.length; z++) {
                            if (newedge == c1[z])
                                if (z + 1 >= c1.length)
                                    n = c1[0];
                                else
                                    n = c1[z + 1];
                        }
                    }else if(pos == 5) {
                        char[] c1 = {'j', 'n', 'q', 'm'};
                        for (int z = 0; z < c1.length; z++) {
                            if (newedge == c1[z])
                                if (z + 1 >= c1.length)
                                    n = c1[0];
                                else
                                    n = c1[z + 1];
                        }
                    }else if(pos == 6) {
                        char[] c1 = {'o', 's', 'v', 'r'};
                        for (int z = 0; z < c1.length; z++) {
                            if (newedge == c1[z])
                                if (z + 1 >= c1.length)
                                    n = c1[0];
                                else
                                    n = c1[z + 1];
                        }
                    }else if(pos == 7) {
                        char[] c1 = {'p', 't', 'w', 's'};
                        for (int z = 0; z < c1.length; z++) {
                            if (newedge == c1[z])
                                if (z + 1 >= c1.length)
                                    n = c1[0];
                                else
                                    n = c1[z + 1];
                        }
                    }else {
                        char[] c1 = {'q', 'u', 'x', 't'};
                        for (int z = 0; z < c1.length; z++) {
                            if (newedge == c1[z])
                                if (z + 1 >= c1.length)
                                    n = c1[0];
                                else
                                    n = c1[z + 1];
                        }
                    }

                    boats[i] = new Boat(boats[i].getColour(),Location.fromEdge(n));
                }
            }

        }

        //produce the new boat string with new edge after rotation

        for(int i = 0; i < boats.length;i++){
            s=s+boats[i].toString();
        }

        nbs = nbs +s;

        return nbs;
    }

    /**
     * Given an objective, return a sequence of rotations that solves it.
     * The sequence of rotations is a String in which each character is an
     * integer representing the position of the tile to be rotated.
     * All rotations are clockwise quarter-turns (90 degrees).
     * For example, the String "8887" represents three clockwise quarter-
     * turns of tile number eight, followed by a single turn of tile
     * number 7.
     *
     * @param objective an objective for the Vikings game
     * @return a String representing the sequence of rotations to solve the objective,
     * or an empty String if no solution exists
     */
    public static String findSolution(comp1110.ass1.Objective objective) {
        // FIXME Task 11
        return "";
    }
}
