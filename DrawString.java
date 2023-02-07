package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Optional;

import javax.swing.JComponent;

import gui.util.Direction;
import gui.util.Justify;
import gui.util.Pair;
import gui.util.Positional;

public class DrawString {
    public static final int TAB_SIZE = 4;

    private Graphics g;
    private int tabLength;
    private int height;
    private Rectangle last;

    public DrawString(Graphics g) {
        this.g = g;
        String tab = "";
        for (int i = 0; i < TAB_SIZE; i++) {
            tab += "W|";
        }
        
        tabLength = width(tab)/2;
        height = g.getFontMetrics().getHeight();
        last = new Rectangle();
    }

    /**
     * Getters
     */

     /**
      * @return Bounds of the last drawn item
      */
    public Rectangle last() {
        return last;
    }

    /**
     * @return Height of a line of text
     */
    public int height() {
        return height;
    }

    /**
     * @param text
     * @return Width of <code>text</text>
     */
    public int width(String text) {
        return g.getFontMetrics().stringWidth(text);
    }

    /**
     * Setters
     */

    /**
     * Set how big tabs are
     * 
     * @param length the length
     * @return tab length
     */
    public int setTabLength(int length) {
        tabLength = length;
        return tabLength;
    }

    /**
     * Set how big tabs are
     * 
     * @param tabSizer String with equal width to a tab
     * @return tab length
     */
    public int setTabLength(String tabSizer) {
        tabLength = width(tabSizer);
        return tabLength;
    }

    /**
     * Drawing actions
     */

    /**
     * Draw text positioned relative to a reference
     * 
     * @param dir    Side
     * @param j      Justification (Justify.END aligns rightmost of ref and text NOT right alignment)
     * @param text   Text to draw
     * @param ref    Reference to position against
     * @param parse Whether or not to parse the text
     * @return The rectangle containing this (also saved in <code>last</code>)
     */
    public Rectangle drawPositionedText(Direction dir, Justify j, String text, Rectangle ref, boolean parse) {
        // (Possibly parse lines)
        ArrayList<Pair<String, Point>> lines;
        if (parse) {
            lines = parsedLines(text, new Point(0, 0));

        } else {
            lines = new ArrayList<>();
            lines.add(new Pair<>(text, new Point(0, 0)));
        }

        // get max line width
        int maxWidth = -1;
        for (Pair<String, Point> line : lines) {
            int width = width(line.getKey());
            if (width > maxWidth) {
                maxWidth = width;
            }
        }

        // height =      final line y                              - first line y               + line height
        int height_net = lines.get(lines.size() - 1).getValue().y - lines.get(0).getValue().y + height;

        // Finally position the text relative to ref
        last = Positioner.positioned(new Positional(dir, j), ref, new Dimension(maxWidth, height_net), 0);

        controlledDraw(lines, last.x, last.y);

        return last;
    }

    /**
     * Draw text positioned relative to a reference component. This will only draw
     * if <code>comp</code> is visible
     * 
     * @param dir    Relative direction of the text to comp
     * @param j      Justification to position the text: start, center, end
     * @param text   Test to draw
     * @param comp   Relative component
     * @param parse Whether or not to parse the text
     * @return Rectangle bounding the drawn text, or empty if <code>comp</code> is
     *         not visible
     */
    public Optional<Rectangle> drawPositionedText(Direction dir, Justify j, String text, JComponent comp, boolean parse) {
        if (!comp.isVisible()) {
            return Optional.ofNullable(null);   
        }

        Rectangle ref = comp.getBounds();
        // Have to adjust bc weird spacing and positioning
        ref.translate(0, 3 * height / 4);
        return Optional.of(drawPositionedText(dir, j, text, ref, parse));
    }

    /**
     * Draw flush to the left of a component
     * 
     * @param text
     * @param comp
     * @return Rectangle bounding the drawn text, or empty if <code>comp</code> is
     *         not visible
     */
    public Optional<Rectangle> drawLeft(String text, JComponent comp) {
        return drawPositionedText(Direction.LEFT, Justify.CENTER, text, comp, true);
    }

    /**
     * Draw flush on top of a component
     * 
     * @param text
     * @param comp
     * @return Rectangle bounding the drawn text, or empty if <code>comp</code> is
     *         not visible
     */
    public Optional<Rectangle> drawAbove(String text, JComponent comp) {
        return drawPositionedText(Direction.UP, Justify.CENTER, text, comp, true);
    }

    /**
     * Draw flush to the right of a component
     * 
     * @param text
     * @param comp
     * @return Rectangle bounding the drawn text, or empty if <code>comp</code> is
     *         not visible
     */
    public Optional<Rectangle> drawRight(String text, JComponent comp) {
        return drawPositionedText(Direction.RIGHT, Justify.CENTER, text, comp, true);
    }

    /**
     * Draw flush under a component
     * 
     * @param text
     * @param comp
     * @return Rectangle bounding the drawn text, or empty if <code>comp</code> is
     *         not visible
     */
    public Optional<Rectangle> drawUnder(String text, JComponent comp) {
        return drawPositionedText(Direction.DOWN, Justify.START, text, comp, true);
    }

    /**
     * Draw flush to the left of a reference rectangle
     * 
     * @param text
     * @param ref
     * @return Rectangle bounding the drawn text
     */
    public Rectangle drawLeft(String text, Rectangle ref) {
        return drawPositionedText(Direction.LEFT, Justify.START, text, ref, true);
    }

    /**
     * Draw flush on top of of a reference rectangle
     * 
     * @param text
     * @param ref
     * @return Rectangle bounding the drawn text
     */
    public Rectangle drawAbove(String text, Rectangle ref) {
        return drawPositionedText(Direction.UP, Justify.START, text, ref, true);
    }

    /**
     * Draw flush to the right of a reference rectangle
     * 
     * @param text
     * @param ref
     * @return Rectangle bounding the drawn text
     */
    public Rectangle drawRight(String text, Rectangle ref) {
        return drawPositionedText(Direction.RIGHT, Justify.START, text, ref, true);
    }

    /**
     * Draw flush under a reference rectangle
     * 
     * @param text
     * @param ref
     * @return Rectangle bounding the drawn text
     */
    public Rectangle drawUnder(String text, Rectangle ref) {
        return drawPositionedText(Direction.DOWN, Justify.START, text, ref, true);
    }

    /**
     * Easy, fast way to draw a set of lines
     * @param lines
     * @param start
     * @return Position for a line just under these
     */
    public Point drawLines(String[] lines, Point start) {
        // Could use controlledDraw, but this is more efficient
        for (String line : lines) {
            g.drawString(line, start.x, start.y);
            start.y += height();
        }

        return start;
    }

    /**
     * Easy, fast way to draw a set of lines
     * 
     * @param lines
     * @param x
     * @param y
     * @return Position for a line just under these
     */
    public Point drawLines(String[] lines, int x, int y) {
        return drawLines(lines, new Point(x, y));
    }

    /**
     * Draw text in a parsed way. Note that this is positioned in accordance to
     * normal <code>Graphics</code> draw methods like
     * <code>Graphics.drawRect()</code>. That is to say, it draws from the top left
     * of the text.
     * 
     * @param text Text to draw
     * @param x    Starting x position
     * @param y    Starting y position
     * @return Rectangle bounding the drawn text
     */
    public Rectangle drawString(String text, int x, int y) {
        return controlledDraw(parsedLines(text, new Point(x, y + height)), 0, height);
    }

    /**
     * Private Methods
     */

    /**
     * @param text  Text to parse
     * @param start Starting (x, y)
     * @return List of strings and points representing the text broken up according
     *         to the escaped characters
     */
    private ArrayList<Pair<String, Point>> parsedLines(String text, Point start) {
        Point originalPoint = (Point)start.clone();
        ArrayList<Pair<String, Point>> lines = new ArrayList<>();
        
        for (String line : text.split("\n")) {
            ArrayList<Pair<String, Point>> parsedLines = parseLine(line, start);
            start.move(originalPoint.x, start.y + height);
            
            lines.addAll(parsedLines);
        }
        
        return lines;
    }

    /**
     * Parses the escaped characters of a single line. Presuposes newlines have been
     * removed.
     * 
     * @param line  Line to parse
     * @param start Starting (x, y)
     * @return List of strings and points representing the text broken up according
     *         to the escaped characters
     */
    private ArrayList<Pair<String, Point>> parseLine(String line, Point start) {
        /**
         * Break up most escape sequences (except tab)
         */
        ArrayList<Pair<String, Point>> strings = new ArrayList<Pair<String, Point>>();
        StringBuilder currentWord = new StringBuilder();
        int carrot = 0; // We have to keep track of where things are being added for \b and \r

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            switch (c) {
            // Backspace: just move the cursor back
            case '\b':
                carrot = Math.max(0, carrot - 1); // carrot cannot be < 0
                break;

            // Carriage return: just move to the start
            case '\r':
                carrot = 0;
                break;

            // Line feed: start a new line without moving x
            case '\f':
                // Add line
                String text = currentWord.toString();
                strings.add(new Pair<String,Point>(
                        text, 
                        (Point) start.clone())
                    );

                // Update
                start.translate(width(text), height);   // Move start
                currentWord = new StringBuilder();      // Clear word
                carrot = 0;                             // Note: carrot is always relative to the currentWord
                break;

            // Just insert all other characters at the cursor
            default:
                // Avoid indexOutofBounds errors
                if (currentWord.length() > carrot) {
                    // Overwrite anything currently at cursor
                    currentWord.deleteCharAt(carrot);
                }
                
                currentWord.insert(carrot, c);
                carrot++;
                break;
            }
        }

        // Add remaining text
        strings.add(new Pair<String, Point>(
                currentWord.toString(),
                (Point) start.clone())
            );

        /**
         * Dealing with Tabs
         */
        ArrayList<Pair<String, Point>> res = new ArrayList<Pair<String, Point>>();

        for (Pair<String, Point> pair : strings) {
            start = pair.getValue();
            // TODO: can this be rewritten with `String.split("\t")`

            for (int i = 0; i < pair.getKey().length(); i++) {
                switch (pair.getKey().charAt(i)) {
                case '\t':
                    // Slice out text up to tab
                    String text = pair.getKey().substring(0, i);
                    pair.setKey(pair.getKey().substring(i+1));
                    i = 0;

                    // Add text
                    res.add(new Pair<String,Point>(
                        text, (Point)start.clone()));
                    
                    // adjust start position
                    start.translate(tabLength * (1 + width(text) / tabLength), 0);

                    break;

                default:
                    // only interested in tabs
                    break;
                }
            }

            // Add remaining text
            res.add(new Pair<String,Point>(
                pair.getKey().substring(0, pair.getKey().length()),
                (Point)start.clone()));
        }

        return res;
    }
    
    /**
     * Draw a bunch of lines
     * 
     * @param strings Lines to draw
     * @param offsetX How much to offset the lines in the x direction
     * @param offsetY How much to offset the lines in the y direction
     * @return Rectangle bounding the drawn lines
     */
    private Rectangle controlledDraw(ArrayList<Pair<String, Point>> strings, int offsetX, int offsetY) {
        Pair<String, Point> first = strings.get(0);
        Rectangle container = new Rectangle(
                first.getValue().x, 
                first.getValue().y, 
                width(first.getKey()), 
                height
            );

        for (Pair<String, Point> pair : strings) {
            Point pos = pair.getValue();
            // Update size as needed
            if (pos.y + height > container.y + container.height) {
                container.setSize(container.width, pos.y + height - container.y);
            }
            int testWidth = pos.x + width(pair.getKey());
            if (testWidth > container.x + container.width) {
                container.setSize(testWidth, container.height);
            }

            // Update position as needed
            if (pos.y < container.y) {
                container.setLocation(container.x, pos.y);
            }
            if (pos.x < container.x) {
                container.setLocation(pos.x, container.y);
            }

            g.drawString(
                pair.getKey(), 
                pair.getValue().x + offsetX, 
                pair.getValue().y + offsetY
            );
        }

        return last = container;
    }
}
