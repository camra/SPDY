package edu.baylor.cs.csi5321.spdy.frames;

import java.io.ByteArrayInputStream;

/**
 *
 * @author Lukas Camra
 */
public abstract class SpdyFrame {

    public static final String ENCODING = "ASCII";
    protected boolean controlBit;
    protected byte flags;
    protected int length;

    public boolean isControlBit() {
        return controlBit;
    }

    public int getControlBitNumber() {
        return controlBit ? 1 : 0;
    }

    public abstract void setControlBit(boolean controlBit) throws SpdyException;

    public byte getFlags() {
        return flags;
    }

    public final void setFlags(byte flags) {
        this.flags = flags;
    }

    public int getLength() {
        return length;
    }

    public final void setLength(int length) {
        this.length = length;
    }

    public SpdyFrame(boolean controlBit, byte flags, int length) throws SpdyException {
        setControlBit(controlBit);
        setFlags(flags);
        setLength(length);
    }

    public abstract byte[] encode() throws SpdyException;

    public static SpdyFrame decode(byte[] pkt) throws SpdyException {
        ByteArrayInputStream input = new ByteArrayInputStream(pkt);
    }
}
