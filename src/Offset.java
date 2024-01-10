package se.liu.marfr380;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Offset {
    KING(Arrays.asList(1,-1,7,-7,8,-8,9,-9)),
    QUEEN(Arrays.asList(1,-1,7,-7,8,-8,9,-9)),
    KNIGHT(Arrays.asList(6,-6,10,-10,15,-15,17,-17)),
    BISHOP(Arrays.asList(7,-7,9,-9)),
    PAWN(Arrays.asList(7,-7,8,-8,9,-9)),
    ROOK(Arrays.asList(1,-1,8,-8));

    private final List<Integer>offsets;

    Offset(List<Integer>offsets){
        this.offsets = offsets;
    }

    public List<Integer> getOffsets() {
        return new ArrayList<>(offsets);
    }
}
