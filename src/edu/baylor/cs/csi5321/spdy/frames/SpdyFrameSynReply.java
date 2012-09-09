package edu.baylor.cs.csi5321.spdy.frames;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 *
 * @author Lukas Camra
 */
public class SpdyFrameSynReply extends SpdyFrameStream {

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

    public SpdyFrameSynReply(boolean controlBit, byte flags, int length) throws SpdyException {
        super(controlBit, flags, length);
    }

    @Override
    public short getType() {
        return SpdyControlFrameType.SYN_REPLY.getType();
    }

    @Override
    public byte[] encode() throws SpdyException {
        
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            bout.write(nameValueBlock.encode());
            byte[] body = bout.toByteArray();
            setLength(body.length); 
            byte[] header = super.encode();
            return SpdyUtil.concatArrays(header, body);
            
        } catch (IOException ex) {
            throw new SpdyException(ex);
        }
    }

    @Override
    public SpdyFrame decode(DataInputStream is) throws SpdyException {
        try {
            SpdyFrameSynReply f = (SpdyFrameSynReply) super.decode(is);
            byte[] pairs = new byte[f.getLength() - HEADER_LENGTH];
            is.readFully(pairs);
            f.setNameValueBlock(SpdyNameValueBlock.decode(pairs));
            return f;
        } catch (IOException ex) {
            throw new SpdyException(ex);
        }
    }

    @Override
    public byte[] getValidFlags() {
        return new byte[]{0x01};
    }
}
