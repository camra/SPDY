package edu.baylor.cs.csi5321.spdy.frames;

import java.io.ByteArrayOutputStream;

/**
 *
 * @author Lukas Camra
 */
public abstract class SpdyControlFrame extends SpdyFrame {

    public static final short VERSION_CONTROL_FRAME = 3;
    
    public static final int HEADER_LENGTH = 8;

    public SpdyControlFrame(boolean controlBit, byte flags, int length) throws SpdyException  {
        super(controlBit, flags, length);
    }
    
    public abstract short getType();

    public short getVersion() {
        return VERSION_CONTROL_FRAME;
    }

    
    @Override
    public void setControlBit(boolean controlBit) throws SpdyException {
        if(!controlBit) {
            throw new SpdyException("Control bit for control frames must be 1");
        }
        setControlBit(controlBit);
    }
    
    @Override
    public byte[] encode() throws SpdyException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream(HEADER_LENGTH);
        bout.write(getControlBitNumber() << 31 | (getVersion() << 16 & 0x7F) | getType());
        bout.write(getFlags() << 24 | (getLength() & 0x00FFFFFF));
        return bout.toByteArray();
    }
    
    
    


}
