package gui.util;

/**
 * Holds a direction and justify. Together describes position relative to a
 * rectangle
 */
public class Positional {
    public Direction direction;
    public Justify justification;

    public Positional(Direction direction, Justify justification) {
        this.direction = direction;
        this.justification = justification;
    }
}
