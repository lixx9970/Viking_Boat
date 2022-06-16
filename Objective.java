package comp1110.ass1;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Objective {
    private int problemNumber;          // The problem number from the original board game
    private String targetPlacement;    // The target placement for the boats
    private String initialState;        // The list of initial tile placements and boat placements

    /**
     * This array defines a set of 60 pre-defined puzzle objectives.
     * <p>
     * There are 4 categories of objective, according to 4 difficulty levels, with
     * 20 objectives per difficulty level, organized within the array as follows:
     * <p>
     * Starter: 0-14
     * Junior: 15-29
     * Expert: 30-44
     * Master: 45-59
     * <p>
     * Each objective is encoded in terms of:
     * 1 - A string representing the necessary inter-island connections
     * 2 - A string representing the tiles which are already placed on the board when the game starts
     * 3 - An objective problemNumber corresponding to the
     * objective problem number used in the original game.
     */
    static final Objective[] OBJECTIVES = {
            // STARTER 0-14 (0)
            new Objective("Rv", "N0O1N1N0O0O1N0N3N1Rt", 0),
            new Objective("Gu", "O0O1O0N3N1N2N3N2N2Gs", 1),
            new Objective("Yg", "O0N1O0N2N1N2N2O0N2Ye", 2),
            new Objective("Rr", "O0O0N0N3N3N0N3N3O0Ru", 3),
            new Objective("Bc", "O0N1N1N2N1O0N2O0N2Be", 4),
            new Objective("GcRb", "O0O0N2O1N2N2N3N2N2GdRe", 5),
            new Objective("BgRcYk", "O1O0N1O1N2N2N3N2N2BhRaYo", 6),
            new Objective("BdYn", "O0O1N1N3O1N2N3N3N2BbYp", 7),
            new Objective("GnRgYu", "N0N3N1O0O1O0N3N3N2GtRfYn", 8),
            new Objective("BaGx", "O1N1O0N3N1N2N3N3O1BlGh", 9),
            new Objective("GuRaYx", "O0O1N1N3O1N1N3N3N3GiRpYb", 10),
            new Objective("BxGdRbYk", "O0O1N1N3N0N1N3O0N2BtGbRmYq", 11),
            new Objective("GbRaYu", "N0O1N1N0O1O0N3N2N2GpRsYo", 12),
            new Objective("BkRbYx", "O0O1N2N3N3N2N3O1N2BiRwYb", 13),
            new Objective("BwGkRbYn", "N3O1N1N3N3O0N3O1N2BnGaRmYb", 14),
            // JUNIOR 15-29 (1)
            new Objective("BnGdRb", "O0O0O1N2N2N2N3N3N3BjGuRc", 15),
            new Objective("BnGkRb", "O1O0N1N3N3O1N2N2N2BrGaRh", 16),
            new Objective("BnRaYv", "O1O0N1N0N2N1O0N2N2BfRrYq", 17),
            new Objective("BdGgYu", "O1N2N1N3O1N1N2N2O0BpGaYu", 18),
            new Objective("GcYa", "O1N1N1N0O1N2N0N3O1GpYx", 19),
            new Objective("BwRvYx", "O0N0N3N3N0O1N3O0N3BdRsYg", 20),
            new Objective("BuGbRaYn", "N0O1N1N0O0O1N0N3N2BlGtRvYb", 21),
            new Objective("BxGbRaYw", "O0N0N0N3O1N1N3N0O1BdGgRiYx", 22),
            new Objective("GkRbYu", "O1O0N2N3N3O1N2N2N2GrRaYh", 23),
            new Objective("BwGkRbYn", "O1N1N1N0N0O0N2O0N3BaGrRoYn", 24),
            new Objective("BcGx", "O1O0N2N3N3O1N2N2N2BaGq", 25),
            new Objective("GnRgYu", "O0N0O1N3N3N1N3O1N1GdRcYm", 26),
            new Objective("BxGvRbYw", "N1O0N1N1N2O0N3O1N2BkGfRnYw", 27),
            new Objective("GrRk", "O1N1O0N0O1N2N3N3N3GpRa", 28),
            new Objective("BgGkYu", "O1O0O0N3N3N3N2N2N2BaGhYr", 29),
            // EXPERT 30-44 (2)
            new Objective("RbYr", "N0N1N1O1O0O1N3N3N2ReYt", 30),
            new Objective("GrRd", "N3O1N1N3O0O1N3N3N2GbRt", 31),
            new Objective("BrGgRcYk", "O0O1N2O1N2N2N1N2N2BcGvRrYi", 32),
            new Objective("RgYu", "O1N1O0N0N0N2N0O1N2RmYa", 33),
            new Objective("BvGdRcYn", "O0O1N1N2N2N1O1N2N2BqGiRbYv", 34),
            new Objective("BkRg", "O0O1N1N3N0N0N3O0N2BdRn", 35),
            new Objective("BxGgRcYu", "O0N0N3N3O0O1N2N2N2BrGgRlYd", 36),
            new Objective("BnRgYv", "N1N1N1O0O1O0N3N2N2BsRdYn", 37),
            new Objective("BuGkRbYn", "N0N0N3O0O0O1N2N2N2BrGgRlYk", 38),
            new Objective("BxGnRdYw", "O0N1O0N3N1N2N2O0N2BdGtRgYe", 39),
            new Objective("BuGgRaYk", "O0N0O0N3O0N3N2N2N2BrGdRgYl", 40),
            new Objective("BxGdRaYu", "N0N0N3O1O0O1N1N2N2BrGgRcYq", 41),
            new Objective("BuRb", "O1N2N1N3O1O0N2N2N2BnRa", 42),
            new Objective("BxGdRcYk", "O0N0O0N3N0N3N3N0O1BdGgRfYn", 43),
            new Objective("BnGv", "O0O0N1N2N2N2O1N2N2BvGj", 44),
            // MASTER 45- 59 (3)
            new Objective("BrGnRg", "O1N1O0N0O1N2N3N3N3BpGaRg", 45),
            new Objective("BwGnRd", "O0O1N1N3N2N1N2N2O0BdGlRu", 46),
            new Objective("BrGcRbYd", "O1O0N1N1N2O0N3N3N3BfGnRkYu", 47),
            new Objective("BrGdRcYn", "O1O0O1N1N2N2N3N3N3BuGcRjYk", 48),
            new Objective("BwGkRbYn", "N0N0N3O1O0O1N1N2N2BgGqRcYr", 49),
            new Objective("RdYg", "N0O1N2N0N3O1N0O1N2RqYi", 50),
            new Objective("BkRbGcYd", "O0O1N1N3N2N1N2N2O0BuGlRrYd", 51),
            new Objective("RrYu", "O0N0N0N3N0O0N3O0N2RsYd", 52),
            new Objective("GvRg", "O0O0O1N2N2N2N3N3N3GjRc", 53),
            new Objective("BwGnRgYv", "N3O1N1N3N0O0N3O0N3BbGnRaYs", 54),
            new Objective("BuGdRb", "O0O0N0N2N2N2N3O1N2BjGwRg", 55),
            new Objective("BvGnRaYu", "O1N1N2N0O1N2N3N3O1BcGpRxYa", 56),
            new Objective("BxGuRaYw", "O1O0N1N3O1N1N3N3N3BhGaRpYf", 57),
            new Objective("BuGgRbYk", "O1O0O1N3N3N2N2N2N2BmGaRhYr", 58),
            new Objective("BdRcYr", "O0N0N3N3O0O1N3N3N3BdRlYg", 59)
    };

    static final Objective DEFAULT_OBJECTIVE = new Objective("Rv", "N0O1N1N0O0O1N0N3N1Rt", 0);

    /**
     * Given the two parts of a game objective and a problem number, constructs an `Objective` object
     *
     * @param targetPlacement A string representing the target placement for the boats
     * @param initialState    A string representing the list of initial tile placements
     * @param problemNumber   The problem number from the original board game,
     *                        a value from 1 to 60.
     */
    public Objective(String targetPlacement, String initialState, int problemNumber) {
        assert problemNumber >= 0 && problemNumber <= 59;
        this.targetPlacement = targetPlacement;
        this.initialState = initialState;
        this.problemNumber = problemNumber;
    }

    /**
     * Choose a new objective, given a difficulty level.
     * <p>
     * The method should select a randomized objective from the 60 pre-defined solutions,
     * being sure to select an objective with the correct level of difficulty.
     * <p>
     * (See the comments above the declaration of the OBJECTIVES array.)
     * <p>
     * So, for example, if the difficulty is 0 (starter), then this function should use a randomized
     * index between 0 and 14 (inclusive) to return an objective from the OBJECTIVES array that is
     * level 0 difficulty.  On the other hand, if the difficulty is 3 (master), then this function
     * should use a randomized index between 45 and 59 (inclusive) to return an objective from the
     * OBJECTIVES array that is level 3 difficulty.
     * <p>
     * The original code below simply returns OBJECTIVES[0], which neither respects the difficulty
     * (it always returns a level 0 objective), nor is it randomized (it always returns the same
     * objective).
     *
     * @param difficulty The difficulty of the game (0 - starter, 1 - junior, 2 - expert, 3 - master)
     * @return An objective at the appropriate level of difficulty.
     */
    public static Objective newObjective(int difficulty) {
        int randomInt;

        //use the library of ThreadLocalRandom to get a random number from a given range
        //return the corresponding OBJECTIVE with given difficulty level

        if(difficulty == 0){
            randomInt = ThreadLocalRandom.current().nextInt(0,15);
            return OBJECTIVES[randomInt];
        }else if(difficulty == 1){
            randomInt = ThreadLocalRandom.current().nextInt(15,30);
            return OBJECTIVES[randomInt];
        }else if(difficulty == 2){
            randomInt = ThreadLocalRandom.current().nextInt(30,45);
            return OBJECTIVES[randomInt];
        }else{
            randomInt = ThreadLocalRandom.current().nextInt(45,60);
            return OBJECTIVES[randomInt];
        }
    }

    public String getTargetPlacement() {
        return targetPlacement;
    }

    public String getInitialState() {
        return initialState;
    }

    public int getProblemNumber() {
        return problemNumber;
    }

    @Override
    public String toString() {
        return "Objective " + getProblemNumber() + ": " + getInitialState() + " -> " + getTargetPlacement();
    }
}
