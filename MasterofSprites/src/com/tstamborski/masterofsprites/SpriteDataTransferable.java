/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tstamborski.masterofsprites;

import com.tstamborski.masterofsprites.model.SpriteData;
import java.awt.datatransfer.DataFlavor;
import java.util.ArrayList;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 *
 * @author Tobiasz Stamborski <tstamborski@outlook.com>
 */
public class SpriteDataTransferable extends ArrayList<SpriteData> implements Transferable {
    public static final DataFlavor C64_SPRITEDATA_FLAVOR = new DataFlavor("x-c64/x-spritedata", 
            "Commodore 64 sprite data");
    
    @Override
    public DataFlavor[] getTransferDataFlavors() {
        DataFlavor[] flavors = new DataFlavor[1];
        flavors[0] = C64_SPRITEDATA_FLAVOR;
        return flavors;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor.equals(C64_SPRITEDATA_FLAVOR);
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (flavor.equals(C64_SPRITEDATA_FLAVOR))
            return this;
        else
            throw new UnsupportedFlavorException(flavor);
    }
    
}
