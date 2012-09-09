package edu.baylor.cs.csi5321.spdy.frames;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Lukas Camra
 */
public class SpdyNameValueBlock {

    private Map<String, String> pairs = new LinkedHashMap<>();

    public Map<String, String> getPairs() {
        return pairs;
    }

    public void setPairs(Map<String, String> pairs) {
        this.pairs = pairs;
    }

    public SpdyNameValueBlock() {
    }

    public SpdyNameValueBlock(Map<String, String> pairs) {
        this.pairs = pairs;
    }

    public byte[] encode() throws SpdyException {
        if (pairs.isEmpty()) {
            return new byte[]{};
        }
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(bout);
            out.writeInt(pairs.size());
            for (String name : pairs.keySet()) {
                String value = pairs.get(name);
                byte[] nameByte = name.getBytes(SpdyFrame.ENCODING);
                byte[] valueByte = value.getBytes(SpdyFrame.ENCODING);
                out.writeInt(nameByte.length);
                out.write(nameByte);
                out.write(valueByte.length);
                out.write(valueByte);
            }
            return bout.toByteArray();
        } catch (IOException ex) {
            throw new SpdyException(ex);
        }

    }
}
