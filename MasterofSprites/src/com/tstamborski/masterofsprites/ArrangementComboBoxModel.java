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
package com.tstamborski.masterofsprites;

import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author Tobiasz Stamborski <tstamborski@outlook.com>
 */
public class ArrangementComboBoxModel extends DefaultComboBoxModel {
    private final ArrayList<Arrangement> allArrangements;
    
    public ArrangementComboBoxModel() {
        allArrangements = new ArrayList<>();
        allArrangements.add(new Arrangement(0, 0));
        for (int y = 1; y <= 8; y++)
            for (int x = 1; y*x <= 8; x++)
                allArrangements.add(new Arrangement(x, y));
        
        setSelectionSize(0);
    }
    
    public final void setSelectionSize(int size) {
        Object prevSelected = getSelectedItem();
        
        removeAllElements();
        allArrangements.forEach(a -> {
            if (a.width * a.height <= size)
                addElement(a);
        });
        
        if (prevSelected != allArrangements.get(0) && getIndexOf(prevSelected) >= 0) {
            setSelectedItem(prevSelected);
        } else {
            if (size > 0)
                setSelectedItem(allArrangements.get(1));
        }
    }
}
