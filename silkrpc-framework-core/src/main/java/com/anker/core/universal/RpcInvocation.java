package com.anker.core.universal;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * rpc invoke info
 *
 * @author Anker
 */
public class RpcInvocation implements Serializable {

    private static final long serialVersionUID = 8447213193317732435L;
    /**
     * 目标方法
     */
    private String targetMethod;
    /**
     * 目标服务名
     */
    private String targetServiceName;
    /**
     * 参数
     */
    private Object[] args;
    /**
     * 唯一id
     */
    private String uuid;
    /**
     * 响应结果
     */
    private Object response;
    /**
     * 异常
     */
    private Throwable e;
    /**
     * 重试次数
     */
    private int retry;
    /**
     * 附件
     */
    private Map<String, Object> attachments = new HashMap<>();


    public RpcInvocation() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getTargetMethod() {
        return targetMethod;
    }

    public void setTargetMethod(String targetMethod) {
        this.targetMethod = targetMethod;
    }

    public String getTargetServiceName() {
        return targetServiceName;
    }

    public void setTargetServiceName(String targetServiceName) {
        this.targetServiceName = targetServiceName;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    public Throwable getE() {
        return e;
    }

    public void setE(Throwable e) {
        this.e = e;
    }

    public int getRetry() {
        return retry;
    }

    public void setRetry(int retry) {
        this.retry = retry;
    }

    public Map<String, Object> getAttachments() {
        return attachments;
    }

    public void setAttachments(Map<String, Object> attachments) {
        this.attachments = attachments;
    }
}
