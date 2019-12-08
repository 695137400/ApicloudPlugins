package com.apicloud.plugin.ui.apicloudInfo;

/**
 * Created with IntelliJ IDEA.<br/>
 * User: lizhichao<br/>
 * Date: 2018-6-18-0018<br/>
 * Time: 17:10:40<br/>
 * Author:lizhichao<br/>
 * Description: <span style="color:#63D3E9">权限</span><br/>
 */
public class Permission {
    /**
     * 允许该应用访问设备的电话功能。此权限可以让该应用确定本机号码和设备ID、是否处于通话状态以及拨打的号码
     */
    boolean readPhoneState = false;
    /**
     * 允许应用在用户未执行操作的情况下直接拨打电话号码。此权限可能会导致意外收费或呼叫。此权限不允许该应用拨打紧急电话号码。
     */
    boolean call = false;
    /**
     * 允许该应用在用户不知情的情况下直接发送短信。此权限可能会导致意外收费。
     */
    boolean sms = false;
    /**
     * 允许该应用使用相机拍摄照片和视频。此权限可以让应用随时使用相机，而无需用户确认。
     */
    boolean camera = false;
    /**
     * 允许该应用访问设备的麦克风，并进行录音。该权限可能导致用户隐私的泄露。
     */
    boolean record = false;
    /**
     * 访问地理位置信息
     */
    boolean location = false;
    /**
     * 访问文件系统
     */
    boolean fileSystem = false;
    /**
     * 完全的访问网络权限
     */
    boolean internetl = false;
    /**
     * 开机启动
     */
    boolean bootCompleted = false;
    /**
     * 控制振动/闪光灯/屏幕休眠等硬件设备
     */
    boolean hardware = false;
    /**
     * 访问设备通讯录
     */
    boolean contact = false;


    public boolean isReadPhoneState() {
        return readPhoneState;
    }

    public void setReadPhoneState(boolean readPhoneState) {
        this.readPhoneState = readPhoneState;
    }

    public boolean isCall() {
        return call;
    }

    public void setCall(boolean call) {
        this.call = call;
    }

    public boolean isSms() {
        return sms;
    }

    public void setSms(boolean sms) {
        this.sms = sms;
    }

    public boolean isCamera() {
        return camera;
    }

    public void setCamera(boolean camera) {
        this.camera = camera;
    }

    public boolean isRecord() {
        return record;
    }

    public void setRecord(boolean record) {
        this.record = record;
    }

    public boolean isLocation() {
        return location;
    }

    public void setLocation(boolean location) {
        this.location = location;
    }

    public boolean isFileSystem() {
        return fileSystem;
    }

    public void setFileSystem(boolean fileSystem) {
        this.fileSystem = fileSystem;
    }

    public boolean isInternetl() {
        return internetl;
    }

    public void setInternetl(boolean internetl) {
        this.internetl = internetl;
    }

    public boolean isBootCompleted() {
        return bootCompleted;
    }

    public void setBootCompleted(boolean bootCompleted) {
        this.bootCompleted = bootCompleted;
    }

    public boolean isHardware() {
        return hardware;
    }

    public void setHardware(boolean hardware) {
        this.hardware = hardware;
    }

    public boolean isContact() {
        return contact;
    }

    public void setContact(boolean contact) {
        this.contact = contact;
    }
}
