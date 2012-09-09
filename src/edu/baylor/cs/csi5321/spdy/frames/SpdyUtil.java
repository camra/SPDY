package edu.baylor.cs.csi5321.spdy.frames;

/**
 *
 * @author ICPCDev
 */
public class SpdyUtil {
 /**
     * Converts integer to byte array
     * @param i integer to be converted
     * @return Returns byte array of length 4 that contains byte representation
     * of integer i
     */
    public static byte[] int2byte(int i) {
        byte[] buf = new byte[4];
        buf[0] = (byte) ((i >> 24));
        buf[1] = (byte) ((i >> 16));
        buf[2] = (byte) ((i >> 8));
        buf[3] = (byte) ((i));
        return buf;
    }    
    
    /**
     * Converts int to unsigned int
     * @param input int to be converted
     * @return Returns long representing unsigned int
     */
    public static long convertToUnsignedInt(int input) {
        return input & 0xFFFFFFFFL;
    }
    
    /**
     * Converts byte to unsigned byte represented by short
     * @param input byte to be converted
     * @return Returns converted byte to unsigned byte represented by short
     */
    public static short convertToUnsignedByte(byte input) {
        return (short) (input & 0xFF);
    }
}
