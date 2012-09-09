package edu.baylor.cs.csi5321.spdy.frames;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Lukas Camra
 */
public abstract class SpdyFrame {

    
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

    public static SpdyFrame decode(InputStream in) throws SpdyException {
        try {
            DataInputStream out = new DataInputStream(in);
            //read header of the packet
            int header = out.readInt();
            //read flags and length
            int header2 = out.readInt();
            byte flags = (byte) (header2 >> 24);
            int length = header2 & SpdyUtil.MASK_LENGTH_HEADER;
            //is it control or data frame?
            long controlBit = header >> 31;
            SpdyFrame result = null;
            if (controlBit == 0) {
                //it's data message
                int streamId = header & SpdyUtil.MASK_STREAM_ID_HEADER;
                result = new SpdyDataFrame(streamId, false, flags, length);
            } else if (controlBit == 1) {
                //it's control message
                //load version and type
                short version = (short) (header >> 16 & SpdyUtil.MASK_VERSION_HEADER);
                short type = (short) (header & SpdyUtil.MASK_TYPE_HEADER);
                if (version != SpdyControlFrame.VERSION_CONTROL_FRAME) {
                    throw new SpdyException("Unexpected version of a control frame: " + version);
                }
                //according to type we will decide what concrete implementaiton is going to be created
                SpdyControlFrameType typeEnum = SpdyControlFrameType.getEnumTypeFromType(type);
                if (typeEnum == null) {
                    throw new SpdyException("Control frame type is not supported, type: " + type);
                }
                switch (typeEnum) {
                    case SYN_STREAM:
                        result = new SpdyFrameSynStream(true, flags, length);
                        break;
                    case SYN_REPLY:
                        result = new SpdyFrameSynReply(true, flags, length);
                        break;
                    case RST_STREAM:
                        result = new SpdyFrameRstStream(true, flags, length);
                        break;
                    case GOAWAY:
                        result = new SpdyFrameGoAway(true, flags, length);
                        break;
                    case HEADERS:
                        result = new SpdyFrameHeaders(true, flags, length);
                        break;
                }

            } else {
                //should not occur
                throw new SpdyException("Error reading packet header; the control header has unexpeted value");
            }
            result = result.decode(out);
            return result;
        } catch (IOException ex) {
            throw new SpdyException(ex);
        }

    }

    public abstract SpdyFrame decode(DataInputStream is) throws SpdyException;
}
