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

    public Rectangle last() {
        return last;
    }

    public int setTabLength(int length) {
        tabLength = length;
        return tabLength;
    }

    public int setTabLength(String tabSizer) {
        tabLength = width(tabSizer);
        return tabLength;
    }

    public int height() {
        return height;
    }

    public int width(String text) {
        return g.getFontMetrics().stringWidth(text);
    }

    /**
     * @param dir   Relative direction of the text to comp
     * @param j     Justification to position the text: start, center, end
     * @param text  Test to draw
     * @param comp   Relative component
     * @return Whether or not the text was drawn, as determined by comp.isVisible()
     */
    public Rectangle positionText(Direction dir, Justify j, String text, Rectangle ref) {
        // Text may need formatting
        ArrayList<Pair<String, Point>> lines = formattedLines(text, 0, 0);

        // Since Positioner.positioned needs the width and height of the target rectangle, we need to find the max width
        int maxWidth = width(lines.get(0).getKey());
        last = Positioner.positioned(
                new Positional(dir, j), 
                ref, 
                new Dimension(width(text), height), 
                0
            );

        for (Pair<String, Point> line : lines) {
            int width = width(line.getKey());
            if (width > maxWidth) {
                maxWidth = width;
            }
        }

        // height = final line y - first line y + additional line height
        int height_net = lines.get(lines.size()-1).getValue().y - lines.get(0).getValue().y + height;

        Rectangle pos = Positioner.positioned(new Positional(dir, j), ref, new Dimension(maxWidth, height_net), 0);

        controlledDraw(lines, pos.x, pos.y);

        return last;
    }
    public Optional<Rectangle> positionText(Direction dir, Justify j, String text, JComponent comp) {
        if (comp.isVisible()) {
            Rectangle ref = comp.getBounds();
            // DrawString works off the bottom-left corner of the text, so we have to move down the height
            // but height actually includes a bit of white space at the top, so to have the
            // text nicely centered in the box, we have to move it up a little; therefore, use 3/4 height
            ref.translate(0, 3 * height / 4);
            return Optional.of(positionText(dir, j, text, ref));
            
        } else {
            return Optional.ofNullable(null);
        }
    }

    public Optional<Rectangle> leftText(String text, JComponent comp) {
        return positionText(Direction.LEFT, Justify.CENTER, text, comp);
    }

    public Optional<Rectangle> topText(String text, JComponent comp) {
        return positionText(Direction.UP, Justify.CENTER, text, comp);
    }

    public Optional<Rectangle> rightText(String text, JComponent comp) {
        return positionText(Direction.RIGHT, Justify.CENTER, text, comp);
    }

    public Optional<Rectangle> bottomText(String text, JComponent comp) {
        return positionText(Direction.DOWN, Justify.START, text, comp);
    }

    public Rectangle leftText(String text, Rectangle comp) {
        return positionText(Direction.LEFT, Justify.START, text, comp);
    }

    public Rectangle topText(String text, Rectangle comp) {
        return positionText(Direction.UP, Justify.START, text, comp);
    }

    public Rectangle rightText(String text, Rectangle comp) {
        return positionText(Direction.RIGHT, Justify.START, text, comp);
    }

    public Rectangle bottomText(String text, Rectangle comp) {
        return positionText(Direction.DOWN, Justify.START, text, comp);
    }

    public Point drawLines(String[] lines, Point start) {
        // Could use controlledDraw, but this is more efficient
        for (String line : lines) {
            g.drawString(line, start.x, start.y);
            start.y += height();
        }

        return start;
    }
    public Point drawLines(String[] lines, int x, int y) {
        return drawLines(lines, new Point(x, y));
    }

    public Point drawLines(ArrayList<String> lines, int x, int y) {
        return drawLines(lines.toArray(new String[0]), x, y);
    }

    public Point drawLines(ArrayList<String> lines, Point p) {
        return drawLines(lines, p);
    }

    public Rectangle drawFormattedLines(String text, int x, int y) {
        return controlledDraw(formattedLines(text, x, y), 0, height);
    }

    private ArrayList<Pair<String, Point>> formattedLines(String text, Point start) {
        Point originalPoint = (Point) start.clone();
        ArrayList<Pair<String, Point>> lines = new ArrayList<Pair<String, Point>>();
        
        for (String line : text.split("\n")) {
            ArrayList<Pair<String, Point>> parsedLines = parseLine(line, start);
            start.move(originalPoint.x, start.y + height);
            
            lines.addAll(parsedLines);
        }
        
        return lines;
    }

    private ArrayList<Pair<String, Point>> formattedLines(String text, int x, int y) {
        return formattedLines(text, new Point(x, y));
    }

    private ArrayList<Pair<String, Point>> parseLine(String line, Point start) {
        /*
         * Break up most escape sequences
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

        /*
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
    public Rectangle controlledDraw(ArrayList<Pair<String, Point>> strings, int offsetX, int offsetY) {
        Pair<String, Point> first = strings.get(0);
        Rectangle container = new Rectangle(
                first.getValue().x, 
                first.getValue().y, 
                width(first.getKey()), 
                height
            );

        for (Pair<String, Point> pair : strings) {
            // ! Leave this for troubleshooting
            // System.out.println("Pair:");
            // System.out.println(pair.getKey() + " <:> " + pair.getValue());

            Point pos = pair.getValue();
            if (pos.y + height > container.y + container.height) {
                container.setSize(container.width, pos.y + height - container.y);
            }

            if (pos.y < container.y) {
                container.setLocation(container.x, pos.y);
            }

            if (pos.x < container.x) {
                container.setLocation(pos.x, container.y);
            }

            int testWidth = pos.x + width(pair.getKey());
            if (testWidth > container.x + container.width) {
                container.setSize(testWidth, container.height);
            }

            g.drawString(
                    pair.getKey(), 
                    pair.getValue().x + offsetX, 
                    pair.getValue().y + offsetY
                );
        }

        last = container;
        return container;
    }

    public Rectangle controlledDraw(ArrayList<Pair<String, Point>> strings) {
        return controlledDraw(strings, 0, 0);
    }
}
