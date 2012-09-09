package edu.baylor.cs.csi5321.spdy.frames;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

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
                byte[] nameByte = name.getBytes(SpdyUtil.ENCODING);
                byte[] valueByte = value.getBytes(SpdyUtil.ENCODING);
                out.writeInt(nameByte.length);
                out.write(nameByte);
                out.write(valueByte.length);
                out.write(valueByte);
            }
            byte[] contentArr = bout.toByteArray();
            //we need to compress it
            //let's create compressedContent buffer, that is double size of contentArr
            byte[] compressedContent = new byte[contentArr.length * 2];
            Deflater compresser = new Deflater();
            //set up dictionary as stated in Spdy specification
            compresser.setDictionary(SpdyUtil.SPDY_dictionary_txt);
            compresser.setInput(contentArr);
            compresser.finish();
            int resultLength = compresser.deflate(compressedContent);
            return Arrays.copyOfRange(compressedContent, 0, resultLength);
        } catch (IOException ex) {
            throw new SpdyException(ex);
        }

    }

    public static SpdyNameValueBlock decode(byte[] pairsByte) throws SpdyException {
        try {
            //decompress the content
            SpdyNameValueBlock result = new SpdyNameValueBlock();
            Inflater decompress = new Inflater();
            decompress.setDictionary(SpdyUtil.SPDY_dictionary_txt);
            decompress.setInput(pairsByte, 0, pairsByte.length);
            //let's create buffer that is double the size of pairs
            byte[] buffer = new byte[pairsByte.length * 2];
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int decompressedLength = 0;
            while ((decompressedLength = decompress.inflate(buffer)) > 0) {
                bos.write(buffer, 0, decompressedLength);
            }
            decompress.end();
            byte[] decompressedContent = bos.toByteArray();
            //let's read the content
            DataInputStream dis = new DataInputStream(new ByteArrayInputStream(decompressedContent));
            int numberOfPairs = dis.readInt();
            for (int i = 0; i < numberOfPairs; i++) {
                int nameLength = dis.readInt();
                if (nameLength <= 0) {
                    throw new SpdyException("Header name is a string with 0 length!");
                }
                if (nameLength > Math.pow(2, 24)) {
                    throw new SpdyException("Maximum name length exceeded: " + nameLength);
                }
                byte[] nameArr = new byte[nameLength];
                dis.readFully(nameArr);
                int valueLength = dis.readInt();
                if (valueLength > Math.pow(2, 24)) {
                    throw new SpdyException("Maximum value length exceeded: " + valueLength);
                }
                byte[] valueArr = new byte[valueLength];
                dis.readFully(valueArr);
                String name = new String(nameArr, SpdyUtil.ENCODING);
                String value = new String(valueArr, SpdyUtil.ENCODING);
                if (result.getPairs().containsKey(name)) {
                    throw new SpdyException("Duplicate header name: " + name);
                }
                result.getPairs().put(name, value);

            }
            return result;
        } catch (IOException | DataFormatException ex) {
            throw new SpdyException(ex);
        }
    }
}
