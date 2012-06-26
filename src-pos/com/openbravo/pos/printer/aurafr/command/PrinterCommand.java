package com.openbravo.pos.printer.aurafr.command;

import com.openbravo.pos.util.ByteArrayUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bcd4j.PackedBCD;

public abstract class PrinterCommand {

    ByteArrayOutputStream lineout = new ByteArrayOutputStream();
    private static final byte ETX = 0x03;
    private static final byte STX = 0x02;
    private static final byte DLE = 0x10;

    /**
     * Creates a new instance of PrinterCommand
     */
    public byte[] createMessage(String sCashierPassword) {
        PackedBCD PackBCD = new PackedBCD(new BigInteger(sCashierPassword), 4);
        byte[] bCashierPassword = PackBCD.toByteArray();
        try {
            lineout.write(STX);
            lineout.write(bCashierPassword);
            lineout.write(getCode());
           
            if (getMessageDataSize() != 0) {
                /*
                 * data[N] Количество байт данных (N) не должно превышать:
                 * Аура-01ФР-KZ N≤66; PayVKP-80KZ N≤66.
                 *
                 * Внимание: Байты данных, равные DLE и ETX, передаются как
                 * последовательность двух байт: 0x10, как <DLE> <DLE>; 0x03,
                 * как <DLE> <ETX>. Все остальные байты передаются просто как
                 * один байт.
                 */

                byte[] bMSG = getMessageData();
                for (int i = 0; i < bMSG.length; i++) {
                    if (bMSG[i] == ETX || bMSG[i] == DLE) {
                        lineout.write(DLE);
                    }
                    lineout.write(bMSG[i]);
                }
            }
            lineout.write(ETX);
            lineout.write(calcCheckSumCRC(lineout.toByteArray()));
        } catch (IOException ex) {
            Logger.getLogger(PrinterCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lineout.toByteArray();
    }
    
    public void readAnswer(byte[] inputdata) {

        byte[] bANSW = new byte[inputdata.length - 3];

        for (int i = 1; i < inputdata.length - 2; i++) {
            bANSW[i-1] = inputdata[i];
        }

        readAnswerData(bANSW);
    }

    public abstract int getCode();

    public abstract byte[] getMessageData();

    public abstract int getMessageDataSize();

    public abstract String getText();

    public abstract boolean isAnswer();

    public abstract void readAnswerData(byte[] inputdata);

    //Формирование контрольной суммы
    private byte calcCheckSumCRC(byte[] adata) {
        int isum = 0;
        for (int i = 1; i < adata.length; i++) {
            isum = isum ^ adata[i];
        }
        byte result = (byte) isum;
        return result;
    }
}
