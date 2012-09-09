package edu.baylor.cs.csi5321.spdy.frames;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author Lukas Camra
 */
public class SpdyFrameSynStream extends SpdyFrameStream {
    
    public static final short TYPE = 1;

    private int associatedToStreamId;
    private short priority;
    private short slot;
    private SpdyNameValueBlock nameValueBlock;

    public int getAssociatedToStreamId() {
        return associatedToStreamId;
    }

    public void setAssociatedToStreamId(int associatedToStreamId) {
        this.associatedToStreamId = associatedToStreamId;
    }

    public short getPriority() {
        return priority;
    }

    public void setPriority(short priority) {
        this.priority = priority;
    }

    public short getSlot() {
        return slot;
    }

    public void setSlot(short slot) {
        this.slot = slot;
    }

    public SpdyNameValueBlock getNameValueBlock() {
        return nameValueBlock;
    }

    public void setNameValueBlock(SpdyNameValueBlock nameValueBlock) {
        this.nameValueBlock = nameValueBlock;
    }

    public SpdyFrameSynStream(int associatedToStreamId, short priority, short slot, SpdyNameValueBlock nameValueBlock, int streamId, short version, boolean controlBit, byte flags, int length) throws SpdyException {
        super(streamId, controlBit, flags, length);
        this.associatedToStreamId = associatedToStreamId;
        this.priority = priority;
        this.slot = slot;
        this.nameValueBlock = nameValueBlock;
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
        } catch(IOException ex) {
            throw new SpdyException(ex);
        }
    }

    @Override
    public short getType() {
        return TYPE;
    }

    
}
