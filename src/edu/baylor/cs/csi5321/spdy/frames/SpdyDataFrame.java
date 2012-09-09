package edu.baylor.cs.csi5321.spdy.frames;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 *
 * @author Lukas Camra
 */
public class SpdyDataFrame extends SpdyFrame {

    private int streamId;
    private byte[] data;

    public SpdyDataFrame(int streamId, byte[] data, boolean controlBit, byte flags, int length) throws SpdyException {
        super(controlBit, flags, length);
        this.streamId = streamId;
        this.data = data;
    }

    public SpdyDataFrame(int streamId, boolean controlBit, byte flags, int length) throws SpdyException {
        super(controlBit, flags, length);
        this.streamId = streamId;
    }

    public int getStreamId() {
        return streamId;
    }

    public void setStreamId(int streamId) {
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
        if (controlBit) {
            throw new SpdyException("For data frame the control bit must be 0");
        }
        this.controlBit = controlBit;
    }

    @Override
    public byte[] encode() throws SpdyException {
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            bout.write((getControlBitNumber() << 31 | (getStreamId() & 0x7FFFFFFF)));
            bout.write(getFlags() << 24 | (getLength() & 0x00FFFFFF));
            bout.write(data);
            return bout.toByteArray();
        } catch (IOException ex) {
            throw new SpdyException(ex);
        }
    }

    @Override
    public SpdyFrame decode(DataInputStream is) throws SpdyException {
        try {
            byte[] dat = new byte[length];
            is.readFully(dat);
            this.data = dat;
            return this;
        } catch (IOException ex) {
            throw new SpdyException(ex);
        }
    }
}
