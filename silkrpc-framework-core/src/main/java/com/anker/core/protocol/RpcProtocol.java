package com.anker.core.protocol;



import com.anker.common.constants.CoreConstants;

import java.io.Serializable;
import java.util.Arrays;

/**
 * rpc protocol
 * @author Anker
 */
public class RpcProtocol implements Serializable {
    private static final long serialVersionUID = 5454104442521781820L;

    private short magicNumber = CoreConstants.MAGIC_NUMBER;

    private int contentLength;

    private byte[] content;

    public static RpcProtocol build(){
        return new RpcProtocol();
    }
    public void contentLength(int contentLength){
        this.contentLength = contentLength;
    }

    public void content(byte[] content){
        this.content = content;
    }

    public RpcProtocol() {
    }

    public RpcProtocol(byte[] content) {
        this.contentLength = content.length;
        this.content = content;
    }

    public short getMagicNumber() {
        return magicNumber;
    }

    public void setMagicNumber(short magicNumber) {
        this.magicNumber = magicNumber;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "RpcProtocol{" +
                ", contentLength=" + contentLength +
                ", content=" + Arrays.toString(content) +
                '}';
    }
}
