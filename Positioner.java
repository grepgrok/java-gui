package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;

import gui.util.Corner;
import gui.util.Direction;
import gui.util.Justify;
import gui.util.Positional;

/**
 * Used to get rectangles variously positioned relative to others
 */
public class Positioner {
    /**
     * Width of a standard rectangle;
     * what <code>Positioner</code> defaults to for width
     */
    public static final int WIDTH = 100;
    /**
     * Height of a standard rectangle;
     * what <code>Positioner</code> defaults to for height
     */
    public static final int HEIGHT = 30;
    /**
     * Standard amount of space between things;
     * what <code>Positioner</code> defaults to for space
     */
    public static final int SPACER = 10;

    /**
     * @param dim the dimensions of the new rectangle
     * @return rectangle positioned at (0, 0)
     */
    public static Rectangle rectOf(Dimension dim) {
        return new Rectangle(0, 0, dim.width, dim.height);
    }

    /**
     * Main controller for getting a rectangle positioned relative to another
     * 
     * @param p      Specification for side and alignment
     * @param ref    Reference to position against
     * @param dim    Dimensions of the new rectangle
     * @param spacer Space between ref and new rectangle
     * @return rectangle as specified above
     */
    public static Rectangle positioned(Positional p, Rectangle ref, Dimension dim, int spacer) {
        int x = 0;
        int y = 0;
        switch (p.direction) {
        case UP:
            // Place above ref
            y = ref.y - dim.height - spacer;
            switch (p.justification) {
            case START:
                // Left aligned
                x = ref.x;
                break;

            case CENTER:
                // Centered
                x = ref.x + ref.width/2 - dim.width/2;
                break;
            
            case END:
                // Right aligned
                x = ref.x + ref.width - dim.width;
                break;
            }
            break;
            
        case DOWN:
            // Place below ref
            switch (p.justification) {
            case START:
                x = ref.x;
                break;

            case CENTER:
                x = ref.x + ref.width / 2 - dim.width / 2;
                break;

            case END:
                x = ref.x + ref.width - dim.width;
                break;
            }
            y = ref.y + ref.height + spacer;
            break;
        
        case LEFT:
            // Place left of ref
            x = ref.x - dim.width - spacer;
            switch (p.justification) {
            case START:
                // top aligned
                y = ref.y;
                break;

            case CENTER:
                // centered
                y = ref.y + ref.height / 2 - dim.height / 2;
                break;

            case END:
                // bottom aligned
                y = ref.y + ref.height - dim.height;
                break;
            }
            break;

        case RIGHT:
            // Place right of ref
            x = ref.x + ref.width + spacer;
            switch (p.justification) {
            case START:
                y = ref.y;
                break;

            case CENTER:
                y = ref.y + ref.height / 2 - dim.height / 2;
                break;

            case END:
                y = ref.y + ref.height - dim.height;
                break;
            }
            break;
        }
        return new Rectangle(x, y,dim.width, dim.height);
    }
    
    /**
     * @param comp Relative component
     * @return Standard rectangle positioned under and left-aligned with comp
     */
    public static Rectangle under(Component comp) {
        return positioned(new Positional(Direction.DOWN, Justify.START), comp.getBounds(), new Dimension(WIDTH, HEIGHT), SPACER);
    }

    /**
     * @param comp   Relative component
     * @param width  Width of the desired rectangle
     * @param height Height of the desired rectangle
     * @param spacer Space between comp and new rectangle
     * @return Rectangle as specified positioned under and left-aligned with comp
     */
    public static Rectangle under(Component comp, int width, int height, int spacer) {
        return positioned(new Positional(Direction.DOWN, Justify.START), comp.getBounds(), new Dimension(width, height), spacer);
    }

    /**
     * @param comp Relative component
     * @return Standard rectangle positioned on top of and left-aligned with comp
     */
    public static Rectangle over(Component comp) {
        return positioned(new Positional(Direction.UP, Justify.START), comp.getBounds(), new Dimension(WIDTH, HEIGHT), SPACER);
    }

    /**
     * @param comp   Relative component
     * @param width  Width of the desired rectangle
     * @param height Height of the desired rectangle
     * @param spacer Space between comp and new rectangle
     * @return Rectangle as specified positioned on top of and left-aligned with
     *         comp
     */
    public static Rectangle over(Component comp, int width, int height, int spacer) {
        return positioned(new Positional(Direction.UP, Justify.START), comp.getBounds(), new Dimension(width, height), spacer);
    }
    
    /**
     * @param comp Relative component
     * @return Standard rectangle positioned left of and top-aligned with comp
     */
    public static Rectangle left(Component comp) {
        return positioned(new Positional(Direction.LEFT, Justify.START), comp.getBounds(), new Dimension(WIDTH, HEIGHT), SPACER);
    }

    /**
     * @param comp   Relative component
     * @param width  Width of the desired rectangle
     * @param height Height of the desired rectangle
     * @param spacer Space between comp and new rectangle
     * @return Rectangle as specified positioned left of and top-aligned with comp
     */
    public static Rectangle left(Component comp, int width, int height, int spacer) {
        return positioned(new Positional(Direction.LEFT, Justify.START), comp.getBounds(), new Dimension(width, height), spacer);
    }

    /**
     * @param comp Relative component
     * @return Standard rectangle positioned right of and bottom-aligned with comp
     */
    public static Rectangle right(Component c) {
        return positioned(new Positional(Direction.RIGHT, Justify.START), c.getBounds(), new Dimension(WIDTH, HEIGHT), SPACER);
    }

    /**
     * @param comp   Relative component
     * @param width  Width of the desired rectangle
     * @param height Height of the desired rectangle
     * @param spacer Space between comp and new rectangle
     * @return Rectangle as specified positioned right of and bottom-aligned with
     *         comp
     */
    public static Rectangle right(Component c, int w, int h, int s) {
        return positioned(new Positional(Direction.RIGHT, Justify.START), c.getBounds(), new Dimension(w, h), s);
    }

    /**
     * Get the position within another and centered along a side
     * 
     * @param dir    Which edge to center along (e.g. <code>Direction.LEFT</code>
     *               for left edge)
     * @param ref    Relative rectangle to position against
     * @param dim    Dimensions of the new rectangle
     * @param spacer Space between ref and new rectangle
     * @return Rectangle as specified above [Note: this will always be <i>within</i>
     *         ref]
     */
    public static Rectangle center(Direction dir, Rectangle ref, Dimension dim, int spacer) {
        int x = ref.x;
        int y = ref.y;

        switch(dir) {
        case UP:
            // Center along top
            x += ref.width / 2 - dim.width / 2;
            y += spacer;
            break;

        case RIGHT:
            // Center along right
            x += ref.width - dim.width - spacer;
            y += ref.height / 2 - dim.height / 2;
            break;

        case DOWN:
            // Center along bottom
            x += ref.width / 2 - dim.width / 2;
            y += ref.height - dim.height - spacer;
            break;

        case LEFT:
            // Center along left
            x += spacer;
            y += ref.height / 2 - dim.height / 2;
            break;
        }
        return new Rectangle(x, y, dim.width, dim.height);
    }

    /**
     * @param ref    Relative rectangle
     * @param width  Width of the new rectangle
     * @param height Height of the new rectangle
     * @return Rectangle as specified centered along the left edge of ref
     */
    public static Rectangle centerLeft(Rectangle ref, int width, int height) {
        return center(Direction.LEFT, ref, new Dimension(width, height), SPACER);
    }

    /**
     * @param dim Dimensions of outer panel
     * @return Standard rectangle centered along the left edge of a panel with
     *         dimensions dim
     */
    public static Rectangle centerLeft(Dimension dim) {
        return center(Direction.LEFT, rectOf(dim), new Dimension(WIDTH, HEIGHT), SPACER);
    }

    /**
     * @param ref    Relative rectangle
     * @param width  Width of the new rectangle
     * @param height Height of the new rectangle
     * @return Rectangle as specified centered along the right edge of ref
     */
    public static Rectangle centerRight(Rectangle ref, int width, int height) {
        return center(Direction.RIGHT, ref, new Dimension(width, height), SPACER);
    }

    /**
     * @param dim Dimensions of outer panel
     * @return Standard rectangle centered along the right edge of a panel with
     *         dimensions dim
     */
    public static Rectangle centerRight(Dimension dim) {
        return center(Direction.RIGHT, new Rectangle(0, 0, dim.width, dim.height), new Dimension(WIDTH, HEIGHT), SPACER);
    }

    /**
     * @param ref    Relative rectangle
     * @param width  Width of the new rectangle
     * @param height Height of the new rectangle
     * @return Rectangle as specified centered along the top edge of ref
     */
    public static Rectangle centerTop(Rectangle ref, int width, int height) {
        return center(Direction.UP, ref, new Dimension(width, height), SPACER);
    }

    /**
     * @param dim Dimensions of outer panel
     * @return Standard rectangle centered along the top edge of a panel with
     *         dimensions dim
     */
    public static Rectangle centerTop(Dimension dim) {
        return center(Direction.UP, new Rectangle(0, 0, dim.width, dim.height), new Dimension(WIDTH, HEIGHT), SPACER);
    }

    /**
     * @param ref    Relative rectangle
     * @param width  Width of the new rectangle
     * @param height Height of the new rectangle
     * @return Rectangle as specified centered along the bottom edge of ref
     */
    public static Rectangle centerBottom(Rectangle ref, int width, int height) {
        return center(Direction.DOWN, ref, new Dimension(width, height), SPACER);
    }

    /**
     * @param dim Dimensions of outer panel
     * @return Standard rectangle centered along the bottom edge of a panel with
     *         dimensions dim
     */
    public static Rectangle centerBottom(Dimension dim) {
        return center(Direction.DOWN, new Rectangle(0, 0, dim.width, dim.height), new Dimension(WIDTH, HEIGHT), SPACER);
    }

    /**
     * Center vertically and horizontally
     * 
     * @param ref Relative rectangle
     * @param dim Dimensions of new rectangle
     * @return Rectangle as specified positioned in the center of ref
     */
    public static Rectangle center(Rectangle ref, Dimension dim) {
        return new Rectangle(
            ref.x + ref.width / 2 - dim.width / 2,
            ref.y + ref.height / 2 - dim.height / 2,
            dim.width, dim.height
        );
    }

    /**
     * @param ref    Relative rectangle
     * @param width  Width of new rectangle
     * @param height Height of new rectangle
     * @return Rectangle as specified positioned in the center of ref
     */
    public static Rectangle center(Rectangle ref, int width, int height) {
        return center(ref, new Dimension(width, height));
    }

    /**
     * @param dim Dimensions of outer panel
     * @return Standard rectangle center in panel with dimensions dim
     */
    public static Rectangle center(Dimension dim) {
        return center(rectOf(dim), WIDTH, HEIGHT);
    }

    /**
     * Position in the corners of a reference
     * 
     * @param dim     width x height dimensions of the rectangle
     * @param ref     reference rectangle
     * @param corner  corner of ref
     * @param spacerX space from edge of ref in x direction
     * @param spacerY space from edge of ref in y direction
     * @return rectangle with dimensions dim and position calculated from ref,
     *         corner, spacerX, and spacerY
     */
    public static Rectangle corner(Rectangle ref, Dimension dim, Corner corner, int spacerX, int spacerY) {
        // Already covering top left
        int x = ref.x + spacerX;
        int y = ref.y + spacerY;

        switch (corner) {
            case TOP_RIGHT:
                x = ref.x + ref.width - dim.width - spacerX;
                break;
            case BOTTOM_LEFT:
                y = ref.y + ref.height - dim.height - spacerY;
                break;
            case BOTTOM_RIGHT:
                x = ref.x + ref.width - dim.width - spacerX;
                y = ref.y + ref.height - dim.height - spacerY;
                break;
            default:
                break;
        }

        return new Rectangle(x, y, dim.width, dim.height);
    }

    /**
     * @param dim    width x height dimensions of the rectangle
     * @param ref    reference rectangle
     * @param corner corner of ref
     * @param spacer space from edge of ref in x and y direction
     * @return rectangle with dimensions dim and position calculated from ref,
     *         corner, and spacer
     */
    public static Rectangle corner(Rectangle ref, Dimension dim, Corner cor, int spacer) {
        return corner(ref, dim, cor, spacer, spacer);
    }

    /**
     * @param ref    Reference to place in
     * @param width  Width of rectangle
     * @param height Height of rectangle
     * @return rectangle of dimensions width x height in the top-left corner of ref
     */
    public static Rectangle topLeft(Rectangle ref, int width, int height) {
        return corner(ref, new Dimension(width, height), Corner.TOP_LEFT, SPACER);
    }
    
    /**
     * @param ref    Reference to place in
     * @param width  Width of rectangle
     * @param height Height of rectangle
     * @return rectangle of dimensions width x height in the top-right corner of ref
     */
    public static Rectangle topRight(Rectangle ref, int width, int height) {
        return corner(ref, new Dimension(width, height), Corner.TOP_RIGHT, SPACER);
    }
    
    /**
     * @param ref    Reference to place in
     * @param width  Width of rectangle
     * @param height Height of rectangle
     * @return rectangle of dimensions width x height in the bottom-left corner of
     *         ref
     */
    public static Rectangle bottomLeft(Rectangle ref, int width, int height) {
        return corner(ref, new Dimension(width, height), Corner.BOTTOM_LEFT, SPACER);
    }
    
    /**
     * @param ref    Reference to place in
     * @param width  Width of rectangle
     * @param height Height of rectangle
     * @return rectangle of dimensions width x height in the bottom-right corner of
     *         ref
     */
    public static Rectangle bottomRight(Rectangle ref, int width, int height) {
        return corner(ref, new Dimension(width, height), Corner.BOTTOM_RIGHT, SPACER);
    }

    /**
     * @param ref    Reference to place in
     * @return Standard rectangle in the top-left corner of ref
     */
    public static Rectangle topLeft(Rectangle ref) {
        return corner(ref, new Dimension(WIDTH, HEIGHT), Corner.TOP_LEFT, SPACER);
    }
    
    /**
     * @param ref    Reference to place in
     * @return Standard rectangle in the top-right corner of ref
     */
    public static Rectangle topRight(Rectangle ref) {
        return corner(ref, new Dimension(WIDTH, HEIGHT), Corner.TOP_RIGHT, SPACER);
    }
    
    /**
     * @param ref    Reference to place in
     * @return Standard rectangle in the bottom-left corner of ref
     */
    public static Rectangle bottomLeft(Rectangle ref) {
        return corner(ref, new Dimension(WIDTH, HEIGHT), Corner.BOTTOM_LEFT, SPACER);
    }
    
    /**
     * @param ref    Reference to place in
     * @return Standard rectangle in the bottom-right corner of ref
     */
    public static Rectangle bottomRight(Rectangle ref) {
        return corner(ref, new Dimension(WIDTH, HEIGHT), Corner.BOTTOM_RIGHT, SPACER);
    }
}
