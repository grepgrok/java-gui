package gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Rectangle;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Components {
    /**
     * Sets up a new JTextField
     * 
     * @param bounds The bounds of the field
     * @param parent The parent container (e.g. the JPanel)
     * @return The initialized and set up field
     */
    public static JTextField textField(Rectangle bounds, Container parent) {
        JTextField res = new JTextField();
        res.setBounds(bounds);
        parent.add(res);
        return res;
    }

    /**
     * Sets up a new JButton. For simplicity, when calling from <code>class MyClass
     * extends JPanel implements ActionListener</code>, you can use
     * 
     * <pre>
     * Components.button("My Name", &lt;bounds&gt;, this, this);
     * </pre>
     * 
     * @param name     What to label the button
     * @param bounds   The bounds
     * @param parent   The parent container (e.g. the JPanel)
     * @param listener What listens to the buttons actions
     * @return Initialized and set up button.
     */
    public static JButton button(String name, Rectangle bounds, Container parent, ActionListener listener) {
        JButton res = new JButton(name);
        res.setBounds(bounds);
        res.addActionListener(listener);
        parent.add(res);
        return res;
    }

    /**
     * Sets up a scrollable JTextArea. To get the text area use the following (or
     * similar);
     * 
     * <pre>
     * JScrollPane myPane = Components.scrollPane("initial text", new Dimension(200,
     * 250), &lt;bounds&gt;, this);
     * <p>
     * JTextArea myTextArea = (JTextArea) Components.paneComponent(myPane);
     * myTextArea.setText("new text");
     * </pre>
     * 
     * @param text   Initial text for the text area
     * @param dim    Dimensions of the text area as Columns x Rows
     * @param bounds The bounds
     * @param parent The parent container (e.g. the JPanel)
     * @return Initialized and set up scrollPane
     * @see paneComponent needs explicit cast <code>(JTextArea)</code>
     */
    public static JScrollPane scrollPane(String initialText, Rectangle paneBounds, Container parent) {
        JTextArea textArea = new JTextArea(initialText);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(paneBounds);

        parent.add(scrollPane);

        return scrollPane;
    }

    /**
     * @param pane
     * @return Child of pane. Should cast to relevant class
     *         (e.g. <code>(JTextArea)Components.paneComponent(myJScrollPane)</code>)
     */
    public static Component paneComponent(JScrollPane pane) {
        return pane.getViewport().getView();
    }
}