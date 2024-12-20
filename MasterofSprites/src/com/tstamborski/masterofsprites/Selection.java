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

import com.tstamborski.masterofsprites.model.SpriteProject;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Tobiasz Stamborski <tstamborski@outlook.com>
 */

public class Selection extends ArrayList<Integer> {
    private final SpriteProject project;
    
    public Selection(SpriteProject proj) {
        super();
        this.project = proj;
    }
    
    public SpriteProject getSpriteProject() {
        return project;
    }
    
    public void invert() {
        int bank_size = project.getMemoryData().size();
        
        Collections.sort(this);
        for (int i = 0; i < bank_size; i++) {
            if (contains(i))
                remove((Integer)i);
            else
                add(i);
        }
    }
    
    public void shift(int c) {
        int bank_size = project.getMemoryData().size();
        for (int n = 0; n < size(); n++) {
            if (get(n) + c >= 0)
                set(n, (get(n) + c) % bank_size);
            else
                set(n, bank_size + ((get(n) + c) % bank_size));
        }
    }
}
