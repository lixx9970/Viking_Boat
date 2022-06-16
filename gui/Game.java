package comp1110.ass1.gui;

import comp1110.ass1.*;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Game extends Application {
    private static final int TILE_SPACING = 150;
    private static final int TILE_DIAMETER = 200;
    private static final int MARGIN_X = 30;
    private static final int MARGIN_Y = 30;
    private static final int BOARD_MARGIN = 71;
    private static final int BOARD_WIDTH = TILE_SPACING * 3 + BOARD_MARGIN * 2;
    private static final int BOARD_HEIGHT = TILE_SPACING * 3 + BOARD_MARGIN * 2;
    private static final int BOARD_Y = MARGIN_Y;
    private static final int BOARD_X = MARGIN_X;
    private static final int CONTROLS_HEIGHT = 50;
    private static final int GAME_WIDTH = BOARD_X + BOARD_WIDTH + MARGIN_X;
    private static final int GAME_HEIGHT = BOARD_Y + BOARD_HEIGHT + CONTROLS_HEIGHT + MARGIN_X;
    private static final long ROTATION_THRESHOLD = 50; // Allow rotation every 50 ms
    private static final String validTiles = "BGRYNO";

    /* node groups */
    private final Group root = new Group();
    private final Group seaTiles = new Group();
    private final Group boatTiles = new Group();
    private final Group board = new Group();
    private final Group controls = new Group();
    private final Group exposed = new Group();
    private final Group objective = new Group();

    private static String solutionString;

    /* where to find media assets */
    private static final String URI_BASE = "assets/";

    /* Loop in public domain CC 0 http://www.freesound.org/people/oceanictrancer/sounds/211684/ */
    private static final String LOOP_URI = Game.class.getResource(URI_BASE + "211684__oceanictrancer__classic-house-loop-128-bpm.wav").toString();
    private AudioClip loop;

    /* game variables */
    private boolean loopPlaying = false;

    /**
     * the difficulty slider
     */
    private final Slider difficulty = new Slider();

    /**
     * message on completion
     */
    private final Text completionText = new Text("Well done!");

    /**
     * The underlying game
     */
    Vikings vikingsGame;

    /**
     * The current state of the game.
     */
    String currentBoardString;

    /* Define a drop shadow effect that will apply to tiles */
    private static DropShadow dropShadow;

    /** Static initializer to initialize dropShadow */
    static {
        dropShadow = new DropShadow();
        dropShadow.setOffsetX(2.0);
        dropShadow.setOffsetY(2.0);
        dropShadow.setColor(Color.color(0, 0, 0, .4));
    }

    /**
     * Graphical representations of tiles
     */
    class GTile extends ImageView {
        char tile;

        /**
         * Construct a particular playing tile
         *
         * @param tile the letter representing the tile to be created.
         */
        GTile(char tile) {
            if (validTiles.indexOf(tile) < 0) {
                throw new IllegalArgumentException("Bad tile: \"" + tile + "\"");
            }
            this.tile = tile;
            setImage(new Image(Game.class.getResource(URI_BASE + tile + ".png").toString()));
        }
    }

    class SeaTile extends GTile {
        Tile tile;

        // This caters for mice which send multiple scroll events per tick.
        long lastRotationTime = System.currentTimeMillis(); // only allow rotation every ROTATION_THRESHOLD (ms)

        /**
         * Construct a playing tile, which is placed on the board at the start of the game.
         *
         * @param tileType    The letter representing the type of tile, either 'N' or 'O'
         * @param orientation The orientation of the tile, 0-3
         */
        SeaTile(char tileType, int orientation, int position) {
            super(tileType);
            this.tile = new Tile(TileType.fromChar(tileType), orientation, position);
            setImage(new Image(Game.class.getResource(URI_BASE + tileType + ".png").toString()));
            setFitHeight(TILE_DIAMETER);
            setFitWidth(TILE_DIAMETER);
            setEffect(dropShadow);
            setRotate(orientation * 90);
            int locX = position % 3;
            int locY = position / 3;
            setLayoutX(locX * TILE_SPACING);
            setLayoutY(locY * TILE_SPACING);

            setOnScroll(event -> { // click to change orientation
                rotate90(event.getSceneX(), event.getSceneY());
            });

            setOnMouseClicked(event -> { // scroll to change orientation
                rotate90(event.getSceneX(), event.getSceneY());
            });
        }

        /**
         * If the mouse is within half the tile radius from the centre of this tile,
         * rotate this tile 90 degrees clockwise.
         *
         * @param mouseX the x location of the mouse when rotation was attempted
         * @param mouseY the y location of the mouse when rotation was attempted
         */
        private void rotate90(double mouseX, double mouseY) {
            int locationX = tile.getPosition() % 3;
            int locationY = tile.getPosition() / 3;
            if (System.currentTimeMillis() - lastRotationTime > ROTATION_THRESHOLD) {
                lastRotationTime = System.currentTimeMillis();
                int tileCentreX = MARGIN_X + TILE_SPACING * (locationX + 1);
                int tileCentreY = MARGIN_Y + TILE_SPACING * (locationY + 1);
                int targetRadius = TILE_SPACING / 2;
                double xDist = tileCentreX - mouseX;
                double yDist = tileCentreY - mouseY;
                if (xDist * xDist + yDist * yDist < targetRadius * targetRadius) {
                    if (Vikings.canRotateTile(currentBoardString, tile.getPosition())) {
                        int newOrientation = (tile.getOrientation() + 1) % 4;
                        currentBoardString = Vikings.rotateTile(currentBoardString, tile.getPosition());
                        System.out.print("" + tile.getPosition() + newOrientation);
                        System.out.flush();
                        setRotate(getRotate() + 90);
                        tile.setOrientation(newOrientation);
                        for (int i = Tile.NUM_POSITIONS * 2; i < currentBoardString.length(); i += 2) {
                            char boatColourChar = currentBoardString.charAt(i);
                            for (Node boatNode : boatTiles.getChildren()) {
                                if (boatNode instanceof BoatTile) {
                                    BoatTile boatTile = (BoatTile) (boatNode);
                                    if (boatTile.tile == boatColourChar) {
                                        char newLocation = currentBoardString.charAt(i + 1);
                                        boatTile.moveToLocation(newLocation);
                                    }
                                }
                            }
                        }
                    } else {
                        // briefly fade the tile to indicate that it can't be rotated
                        FadeTransition ft = new FadeTransition(Duration.millis(200), this);
                        ft.setFromValue(1.0);
                        ft.setToValue(0.1);
                        ft.setCycleCount(2);
                        ft.setAutoReverse(true);
                        ft.play();
                    }

                }
                hideCompletion();
                checkCompletion();
            }
        }
    }

    /**
     * This class extends Tile with the capacity for it to be dragged and dropped,
     * and snap-to-grid.
     */
    class BoatTile extends GTile {
        char edge;

        /**
         * Construct a draggable tile
         *
         * @param tile The tile identifier ('a' - 'f')
         */
        BoatTile(char tile, char edge) {
            super(tile);
            int orientation = 0;
            if (edge < 'a' || edge > 'x') {
                throw new IllegalArgumentException("Bad edge: \"" + edge + "\"");
            }

            setFitHeight(TILE_DIAMETER * 0.62);
            setFitWidth(TILE_DIAMETER * 0.25);

            moveToLocation(edge);
        }

        public void moveToLocation(char edge) {
            int orientation;
            this.edge = edge;
            Location loc = Location.fromEdge(edge);
            if (loc.getX() % 2 == 1) orientation = 1;
            else orientation = 0;
            double x = loc.getX();
            double y = loc.getY() - 0.5;

            setLayoutX(x * TILE_SPACING / 2);
            setLayoutY(y * TILE_SPACING / 2);
            setRotate(90 * orientation);
        }

        /**
         * @return the boat placement represented as a string
         */
        public String toString() {
            return String.valueOf(tile) + edge;
        }
    }

    /**
     * Set up event handlers for the main game
     *
     * @param scene The Scene used by the game.
     */
    private void setUpHandlers(Scene scene) {
        /* create handlers for key press and release events */
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.M) {
                toggleSoundLoop();
                event.consume();
            } else if (event.getCode() == KeyCode.Q) {
                Platform.exit();
                event.consume();
            } else if (event.getCode() == KeyCode.SLASH) {
                // TODO hint
                //solution.setOpacity(1.0);
                seaTiles.setOpacity(0);
                event.consume();
            }
        });
        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.SLASH) {
                // TODO hint
                //solution.setOpacity(0);
                seaTiles.setOpacity(1.0);
                event.consume();
            }
        });
    }


    /**
     * Set up the sound loop (to play when the 'M' key is pressed)
     */
    private void setUpSoundLoop() {
        try {
            loop = new AudioClip(LOOP_URI);
            loop.setCycleCount(AudioClip.INDEFINITE);
        } catch (Exception e) {
            System.err.println(":-( something bad happened (" + LOOP_URI + "): " + e);
        }
    }


    /**
     * Turn the sound loop on or off
     */
    private void toggleSoundLoop() {
        if (loopPlaying)
            loop.stop();
        else
            loop.play();
        loopPlaying = !loopPlaying;
    }

    /**
     * Set up the group that represents the solution (and make it transparent)
     *
     * @param solution The solution as an array of chars.
     */
    private void makeSolution(String solution) {

    }

    /**
     * Set up the group that represents the graphical elements that make up the game board
     */
    private void makeBoard() {
        board.getChildren().clear();

        Rectangle baseboard = new Rectangle();
        baseboard.setWidth(BOARD_WIDTH);
        baseboard.setHeight(BOARD_HEIGHT);
        baseboard.setLayoutX(BOARD_X);
        baseboard.setLayoutY(BOARD_Y);
        baseboard.setArcWidth(20.0);
        baseboard.setArcHeight(20.0);
        baseboard.setFill(Color.CORNFLOWERBLUE);
        baseboard.setStroke(Color.CYAN);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(2.0);
        dropShadow.setOffsetY(2.0);
        dropShadow.setColor(Color.color(0, 0, 0, .4));
        baseboard.setEffect(dropShadow);

        board.getChildren().add(baseboard);
        seaTiles.setLayoutX(BOARD_MARGIN);
        seaTiles.setLayoutY(BOARD_MARGIN);
        board.getChildren().add(seaTiles);
        boatTiles.setLayoutX(BOARD_MARGIN);
        boatTiles.setLayoutY(BOARD_MARGIN);
        board.getChildren().add(boatTiles);

        board.toBack();
    }

    /**
     * Create each of the nine tiles
     */
    private void makeTiles(String boardString) {
        seaTiles.getChildren().clear();
        for (int i = 0; i < 18; i += 2) {
            char tile = boardString.charAt(i);
            int orientation = boardString.charAt(i + 1) - '0';
            seaTiles.getChildren().add(new SeaTile(tile, orientation, i / 2));
        }
        boatTiles.getChildren().clear();
        for (int i = 18; i < boardString.length(); i += 2) {
            char boatTile = boardString.charAt(i);
            char edge = boardString.charAt(i + 1);
            boatTiles.getChildren().add(new BoatTile(boatTile, edge));
        }
    }

    /**
     * Add the objective to the board
     */
    private void addObjectiveToBoard() {
        objective.getChildren().clear();
        String placement = vikingsGame.getObjective().getTargetPlacement();
        for (int i = 0; i < placement.length(); i += 2) {
            Boat target = Boat.fromString(placement.substring(i, i + 2));
            Polygon triangleMarker = new Polygon();
            triangleMarker.getPoints().setAll(
                    -30.0, 0.0,
                    30.0, 0.0,
                    0.0, 30.0
            );
            Location loc = target.getLocation();

            double x = loc.getX() + 1.25;
            double y = loc.getY() + 1.25;
            if (loc.getX() == 0) {
                x -= 0.75;
                triangleMarker.setRotate(270);
            } else if (loc.getX() == 6) {
                x += 1;
                triangleMarker.setRotate(90);
            } else if (loc.getY() == 0) {
                y -= 1;
            } else if (loc.getY() == 6) {
                y += 0.75;
                triangleMarker.setRotate(180);
            }

            triangleMarker.setLayoutX(x * TILE_SPACING / 2);
            triangleMarker.setLayoutY(y * TILE_SPACING / 2);
            triangleMarker.setFill(getPaintColor(target.getColour()));
            objective.getChildren().add(triangleMarker);
        }

        //objective.getChildren().add(new GTile(dinosaursGame.getObjective().getProblemNumber(), OBJECTIVE_MARGIN_X, OBJECTIVE_MARGIN_Y));
    }

    private Color getPaintColor(Colour c) {
        Color result;
        switch (c) {
            case GREEN:
                result = Color.LIME;
                break;
            case BLUE:
                result = Color.BLUE;
                break;
            case RED:
                result = Color.RED;
                break;
            case YELLOW:
                result = Color.YELLOW;
                break;
            default:
                throw new IllegalArgumentException();
        }
        return result;
    }

    private boolean checkBoardString() {
        if (!Vikings.isBoardStringValid(currentBoardString)) {
            System.err.println("Vikings.isBoardStringValid(\"" + currentBoardString + "\") returned false!");
            return false;
        }
        return true;
    }

    /**
     * Check game completion and update status
     */
    private void checkCompletion() {
        if (checkBoardString()) {
            String boatPlacement = currentBoardString.substring(18);
            if (boatPlacement.equals(vikingsGame.getObjective().getTargetPlacement()))
                showCompletion();
        }
    }

    /**
     * Create the controls that allow the game to be restarted and the difficulty
     * level set.
     */
    private void makeControls() {
        Button button = new Button("Restart");
        button.setLayoutX(BOARD_X + BOARD_MARGIN + 240);
        button.setLayoutY(GAME_HEIGHT - 55);
        button.setOnAction(e -> newGame());
        controls.getChildren().add(button);

        difficulty.setMin(1);
        difficulty.setMax(4);
        difficulty.setValue(0);
        difficulty.setShowTickLabels(true);
        difficulty.setShowTickMarks(true);
        difficulty.setMajorTickUnit(1);
        difficulty.setMinorTickCount(1);
        difficulty.setSnapToTicks(true);

        difficulty.setLayoutX(BOARD_X + BOARD_MARGIN + 70);
        difficulty.setLayoutY(GAME_HEIGHT - 50);
        controls.getChildren().add(difficulty);

        final Label difficultyCaption = new Label("Difficulty:");
        difficultyCaption.setTextFill(Color.GREY);
        difficultyCaption.setLayoutX(BOARD_X + BOARD_MARGIN);
        difficultyCaption.setLayoutY(GAME_HEIGHT - 50);
        controls.getChildren().add(difficultyCaption);
    }


    /**
     * Create the message to be displayed when the player completes the puzzle.
     */
    private void makeCompletion() {
        completionText.setFill(Color.WHITE);
        completionText.setEffect(dropShadow);
        completionText.setCache(true);
        completionText.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 80));
        completionText.setLayoutX(140);
        completionText.setLayoutY(350);
        completionText.setTextAlignment(TextAlignment.CENTER);
        root.getChildren().add(completionText);
    }


    /**
     * Show the completion message
     */
    private void showCompletion() {
        completionText.toFront();
        completionText.setOpacity(1);
    }


    /**
     * Hide the completion message
     */
    private void hideCompletion() {
        completionText.toBack();
        completionText.setOpacity(0);
    }


    /**
     * Start a new game, resetting everything as necessary
     */
    private void newGame() {
        try {
            hideCompletion();
            vikingsGame = new Vikings((int) difficulty.getValue() - 1);
            String sol = Vikings.findSolution(vikingsGame.getObjective());
            if (sol != null)
                makeSolution(sol);
            System.out.println(vikingsGame.getObjective());
            makeTiles(vikingsGame.getObjective().getInitialState());
            addObjectiveToBoard();
            currentBoardString = vikingsGame.getObjective().getInitialState();
            checkBoardString();
        } catch (IllegalArgumentException e) {
            System.err.println("Uh oh. " + e);
            e.printStackTrace();
            Platform.exit();
        }
    }


    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("VIKINGS Brainstorm");
        Scene scene = new Scene(root, GAME_WIDTH, GAME_HEIGHT);

        root.getChildren().add(board);
        root.getChildren().add(controls);
        root.getChildren().add(exposed);
        root.getChildren().add(objective);

        setUpHandlers(scene);
        setUpSoundLoop();
        makeBoard();
        makeControls();
        makeCompletion();

        newGame();

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
