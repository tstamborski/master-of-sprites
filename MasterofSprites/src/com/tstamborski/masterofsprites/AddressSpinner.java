/*
 * The MIT License
 *
 * Copyright 2023 Tobiasz Stamborski <tstamborski@outlook.com>.
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

import java.text.ParseException;
import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;

/**
 *
 * @author Tobiasz Stamborski <tstamborski@outlook.com>
 */

public class AddressSpinner extends JSpinner {
    public AddressSpinner() {
        super(new SpinnerAddressModel());
        
        JFormattedTextField tfield = ((JSpinner.DefaultEditor)getEditor()).getTextField();
        tfield.setFormatterFactory(new AddressFormatterFactory());
        tfield.setEditable(true);
    }
}

class SpinnerAddressModel extends AbstractSpinnerModel {
    
    @Override
    public Object getValue() {
        return address;
    }

    @Override
    public void setValue(Object o) {
        address = ((Integer)o) & 0xffff;
        address = address - address%step;
        fireStateChanged();
    }

    @Override
    public Object getNextValue() {
        setValue(address+step);
        return address;
    }

    @Override
    public Object getPreviousValue() {
        setValue(address-step);
        return address;
    }
    
    private int address;
    private final int step = 0x40;
    
}

class AddressFormatterFactory extends DefaultFormatterFactory {

    @Override
    public JFormattedTextField.AbstractFormatter getDefaultFormatter() {
        return new AddressFormatter();
    }
    
}

class AddressFormatter extends JFormattedTextField.AbstractFormatter {

    @Override
    public Object stringToValue(String text) throws ParseException {
        int value;
        
        try {
            value = Integer.parseInt(text, 16);
        }
        catch (NumberFormatException e) {
            throw new ParseException(text,0);
        }
        
        return value;
    }

    @Override
    public String valueToString(Object value) throws ParseException {
        return String.format("%04X", value);
    }
    
}
