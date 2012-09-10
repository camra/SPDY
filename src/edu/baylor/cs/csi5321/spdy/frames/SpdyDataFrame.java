package edu.baylor.cs.csi5321.spdy.frames;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 *
 * @author Lukas Camra
 */
public class SpdyDataFrame extends SpdyFrame {

    private int streamId;
    private byte[] data;

    public SpdyDataFrame(int streamId, byte[] data, boolean controlBit, byte flags, int length) throws SpdyException {
        super(controlBit, flags, length);
        setStreamId(streamId);
        this.data = data;
    }

    public SpdyDataFrame(int streamId, boolean controlBit, byte flags, int length) throws SpdyException {
        super(controlBit, flags, length);
        setStreamId(streamId);
    }

    public int getStreamId() {
        return streamId;
    }

    public void setStreamId(int streamId) throws SpdyException {
        if(streamId < 0) {
            throw new SpdyException("Stream ID must be 31-bit number within 32-bit integer, that is, it must not be a negative number");
        }
        this.streamId = streamId;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public void setControlBit(boolean controlBit) throws SpdyException {
        if (isControlBit()) {
            throw new SpdyException("For data frame the control bit must be 0");
        }
        super.controlBit = controlBit;
    }

    @Override
    public byte[] encode() throws SpdyException {
        try {
            setLength(data == null ? 0 : data.length);
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bout);
            if (isControlBit()) {
                throw new SpdyException("For data frame the control bit must be 0");
            }
            dos.writeInt((getControlBitNumber() << 31 | (getStreamId() & SpdyUtil.MASK_STREAM_ID_HEADER)));
            dos.writeInt(getFlags() << 24 | (getLength() & SpdyUtil.MASK_LENGTH_HEADER));
            dos.write(data);
            dos.close();
            return bout.toByteArray();
        } catch (IOException ex) {
            throw new SpdyException(ex);
        }
    }

    @Override
    public SpdyFrame decode(DataInputStream is) throws SpdyException {
        try {
            byte[] dat = new byte[getLength()];
            is.readFully(dat);
            this.data = dat;
            return this;
        } catch (IOException ex) {
            throw new SpdyException(ex);
        }
    }

    @Override
    public Byte[] getValidFlags() {
        return new Byte[]{0x01};
    }

    @Override
    public boolean equals(Object obj) {
        if(!super.equals(obj)) {
            return false;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SpdyDataFrame other = (SpdyDataFrame) obj;
        if (this.streamId != other.streamId) {
            return false;
        }
        if (!Arrays.equals(this.data, other.data)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5 * super.hashCode();
        hash = 31 * hash + this.streamId;
        hash = 31 * hash + Arrays.hashCode(this.data);
        return hash;
    }
    
    
}
