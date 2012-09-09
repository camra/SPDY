package edu.baylor.cs.csi5321.spdy.frames;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author Lukas Camra
 */
public class SpdyFrameSynStream extends SpdyFrameStream {

    private int associatedToStreamId;
    private byte priority;
    private byte slot;
    private SpdyNameValueBlock nameValueBlock;
    
    private static final int HEADER_LENGTH = 10;

    public int getAssociatedToStreamId() {
        return associatedToStreamId;
    }

    public void setAssociatedToStreamId(int associatedToStreamId) {
        this.associatedToStreamId = associatedToStreamId;
    }

    public byte getPriority() {
        return priority;
    }

    public void setPriority(byte priority) {
        this.priority = priority;
    }

    public byte getSlot() {
        return slot;
    }

    public void setSlot(byte slot) {
        this.slot = slot;
    }

    public SpdyNameValueBlock getNameValueBlock() {
        return nameValueBlock;
    }

    public void setNameValueBlock(SpdyNameValueBlock nameValueBlock) {
        this.nameValueBlock = nameValueBlock;
    }

    public SpdyFrameSynStream(int associatedToStreamId, byte priority, byte slot, SpdyNameValueBlock nameValueBlock, int streamId, short version, boolean controlBit, byte flags, int length) throws SpdyException {
        super(streamId, controlBit, flags, length);
        this.associatedToStreamId = associatedToStreamId;
        this.priority = priority;
        this.slot = slot;
        this.nameValueBlock = nameValueBlock;
    }

    public SpdyFrameSynStream(int streamId, boolean controlBit, byte flags, int length) throws SpdyException {
        super(controlBit, flags, length);
        this.streamId = streamId;
    }

    public SpdyFrameSynStream(boolean controlBit, byte flags, int length) throws SpdyException {
        super(controlBit, flags, length);
    }

    @Override
    public byte[] encode() throws SpdyException {
        byte[] header = super.encode();
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(bout);
        try {
            out.write(header);
            out.write(getAssociatedToStreamId() & 0x7FFFFFFF);
            //writing Pri|Unused|Slot
            out.writeByte(getPriority() << 5);
            //we don't use CREDENTIAL, therefore, let's just write 0
            out.writeByte(0);
            out.write(nameValueBlock.encode());
            return bout.toByteArray();
        } catch (IOException ex) {
            throw new SpdyException(ex);
        }
    }

    @Override
    public short getType() {
        return SpdyControlFrameType.SYN_STREAM.getType();
    }

    @Override
    public SpdyFrame decode(DataInputStream is) throws SpdyException {
        try {
            SpdyFrameSynStream f = (SpdyFrameSynStream) super.decode(is);
            int assoc = is.readInt();
            f.setAssociatedToStreamId(assoc & SpdyUtil.MASK_STREAM_ID_HEADER);
            byte priorityAndUnused = is.readByte();
            setPriority((byte) (priorityAndUnused >> 5));
            byte slot = is.readByte();
            setSlot(slot);
            byte[] pairs = new byte[length - HEADER_LENGTH];
            is.readFully(pairs);
            f.setNameValueBlock(SpdyNameValueBlock.decode(pairs));
            return f;
        } catch (IOException ex) {
            throw new SpdyException(ex);
        }
    }
}
