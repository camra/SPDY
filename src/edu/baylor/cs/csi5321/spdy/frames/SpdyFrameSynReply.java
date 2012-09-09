package edu.baylor.cs.csi5321.spdy.frames;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *
 * @author Lukas Camra
 */
public class SpdyFrameSynReply extends SpdyFrameStream {

    public static final short TYPE = 2;
    private SpdyNameValueBlock nameValueBlock;

    public SpdyNameValueBlock getNameValueBlock() {
        return nameValueBlock;
    }

    public void setNameValueBlock(SpdyNameValueBlock nameValueBlock) {
        this.nameValueBlock = nameValueBlock;
    }

    public SpdyFrameSynReply(SpdyNameValueBlock nameValueBlock, int streamId, boolean controlBit, byte flags, int length) throws SpdyException {
        super(streamId, controlBit, flags, length);
        this.nameValueBlock = nameValueBlock;
    }

    @Override
    public short getType() {
        return TYPE;
    }

    @Override
    public byte[] encode() throws SpdyException {
        byte[] header = super.encode();
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            bout.write(header);
            bout.write(nameValueBlock.encode());
            return bout.toByteArray();
        } catch (IOException ex) {
            throw new SpdyException(ex);
        }
    }
}
