package comp1110.ass1;

public enum TileType {
    N, O;

    public static TileType fromChar(char t) {
        if (t == 'N') return N;
        else if (t == 'O') return O;
        else throw new IllegalArgumentException("Invalid tile type: " + t);
    }
}
