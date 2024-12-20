/*
 * The MIT License
 *
 * Copyright 2024 Tobiasz Stamborski <tstamborski@outlook.com>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.tstamborski;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.BevelBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;

/**
 *
 * @author Tobiasz Stamborski <tstamborski@outlook.com>
 */
public class ManualDialog extends JDialog {
    private static final int DEFAULT_WIDTH = 650;
    private static final int DEFAULT_HEIGHT = 450;
    
    private final JTextPane textPane;
    private final JScrollPane scrollPane;
    private final JButton closeButton, back2TopButton;
    private final JPanel southPanel;
    
    public ManualDialog(JFrame parent, InputStream istream) throws IOException {
        super(parent);
        
        CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder();
        StringBuilder builder = new StringBuilder();
        byte b[] = new byte[2048];
        
        textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setContentType("text/html");
        textPane.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        while (istream.read(b, 0, b.length) > 0)
            builder.append(decoder.decode(ByteBuffer.wrap(b)));
        textPane.setText(builder.toString());
        
        closeButton = new JButton("Close");
        closeButton.setMnemonic('C');
        back2TopButton = new JButton("Back to Top");
        back2TopButton.setMnemonic('B');
        
        scrollPane = new JScrollPane(textPane, 
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        
        southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout());
        southPanel.add(back2TopButton);
        southPanel.add(closeButton);
        
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
        getRootPane().setDefaultButton(closeButton);
        setResizable(false);
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setTitle("Manual... ");
        
        closeButton.addActionListener(ae -> {
            setVisible(false);
        });
        back2TopButton.addActionListener(ae -> {
            scrollPane.getViewport().setViewPosition(new Point(0, 0));
        });
        textPane.addHyperlinkListener(hle -> {
            if (hle.getEventType() == HyperlinkEvent.EventType.ACTIVATED && hle.getURL() == null) {
                String description = hle.getDescription();
                HTMLDocument doc = (HTMLDocument)textPane.getStyledDocument();
                
                Element e = doc.getElement(doc.getDefaultRootElement(), 
                        HTML.Attribute.NAME, description.substring(1));
                int offset = e.getStartOffset();
                try {
                    Rectangle rect = textPane.modelToView(offset);
                    scrollPane.getViewport().setViewPosition(new Point(0, rect.y));
                } catch (BadLocationException ex) {
                    Util.showError(this, ex.getMessage());
                }
            } else if (hle.getEventType() == HyperlinkEvent.EventType.ACTIVATED && hle.getURL() != null) {
                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().browse(hle.getURL().toURI());
                    } catch (URISyntaxException | IOException ex) {
                        Util.showError(this, ex.getMessage());
                    }
                }
            }
        });
        
        setLocationRelativeTo(parent);
    }
    
    protected JTextPane getTextPane() {
        return textPane;
    }
    
    protected JScrollPane getScrollPane() {
        return scrollPane;
    }
}
