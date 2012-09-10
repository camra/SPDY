package edu.baylor.cs.csi5321.spdy.frames;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Lukas Camra
 */
public abstract class SpdyFrame {

    protected boolean controlBit;
    private byte flags;
    private int length;

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

    public final void setFlags(byte flags) throws SpdyException {
        List<Byte> list = Arrays.asList(getValidFlags());
        if (flags != 0 && !list.contains(flags)) { 
            throw new SpdyException("Invalid flag for this type of frame: " + flags);
        }
        this.flags = flags;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) throws SpdyException {
        if (length > Math.pow(2, 24)) {
            throw new SpdyException("Maximum length of 2^24 exceeded: " + length);
        }
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
            byte flags = (byte) ((header2 >> 24) & 0x00FFFFFF);
            int length = header2 & SpdyUtil.MASK_LENGTH_HEADER;
            //is it control or data frame?
            byte controlBit = (byte) ((header >> 31) & 0x1);
            //let's read the packet
            byte[] packet = new byte[length];
            out.readFully(packet);
            DataInputStream packetOut = new DataInputStream(new ByteArrayInputStream(packet));
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
                throw new SpdyException("Error reading packet header; the control header has unexpected control bit: " + controlBit);
            }
            result = result.decode(packetOut);
            if (packetOut.read() != -1) {
                throw new SpdyException("End of packet was expected!");
            }
            packetOut.close();
            return result;
        } catch (IOException ex) {
            throw new SpdyException(ex);
        }

    }

    public abstract SpdyFrame decode(DataInputStream is) throws SpdyException;

    public abstract Byte[] getValidFlags();

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SpdyFrame other = (SpdyFrame) obj;
        if (this.controlBit != other.controlBit) {
            return false;
        }
        if (this.flags != other.flags) {
            return false;
        }
        if (this.length != other.length) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + (this.controlBit ? 1 : 0);
        hash = 73 * hash + this.flags;
        hash = 73 * hash + this.length;
        return hash;
    }
}
