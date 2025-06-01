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

import com.tstamborski.masterofsprites.model.MemoryData;
import com.tstamborski.masterofsprites.model.SpriteProject;

/**
 *
 * @author Tobiasz Stamborski <tstamborski@outlook.com>
 */
public class History {
    private final MemoryData[] list;
    private int index;
    private final boolean[] saved;
    
    public History(SpriteProject project, int capacity) {
        list = new MemoryData[capacity];
        saved = new boolean[capacity];
        index = 0;
        list[index] = project.getMemoryData().deepCopy();
    }
    
    public void push(SpriteProject project) {
        if (index + 1 >= list.length) {
            for (int i = 0; i < index; i++) {
                list[i] = list[i+1];
                saved[i] = saved[i+1];
            }
            index--;
        }
        
        index++;
        list[index] = project.getMemoryData().deepCopy();
        saved[index] = false;
        
        for (int i = index + 1; i < list.length; i++) {
            list[i] = null;
            saved[i] = false;
        }
    }

    public boolean isSaved() {
        return saved[index];
    }

    public void setSaved(boolean is) {
        if (is == true) {
            for (int i = 0; i < saved.length; i++)
                saved[i] = false;
            saved[index] = true;
        } else {
            saved[index] = false;
        }
    }
    
    public boolean hasUndo() {
        return index > 0;
    }
    
    public boolean hasRedo() {
        return index + 1 < list.length && list[index+1] != null;
    }
    
    public void undo(SpriteProject project) {
        if (!hasUndo())
            return;
        
        project.setMemoryData(list[--index].deepCopy());
    }
    
    public void redo(SpriteProject project) {
        if (!hasRedo())
            return;
        
        project.setMemoryData(list[++index].deepCopy());
    }
}
