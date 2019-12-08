package com.apicloud.plugin.ui.apicloudInfo;

/**
 * Created with IntelliJ IDEA.<br/>
 * User: lizhichao<br/>
 * Date: 2018-6-18-0018<br/>
 * Time: 17:58:00<br/>
 * Author:lizhichao<br/>
 * Description: <span style="color:#63D3E9">偏好属性</span><br/>
 */
public class Preference {

    /**
     * App全局背景
     */
    String appBackground = "";
    /**
     * Window默认背景
     */
    String windowBackground = "";
    /**
     * Frame默认背景
     */
    String frameBackgroundColor = "";
    /**
     * 页面是否弹动
     */
    boolean pageBounce = true;
    /**
     * 横向滚动条
     */
    boolean hScrollBarEnabled = true;
    /**
     * 竖直滚动条
     */
    boolean vScrollBarEnabled = true;
    /**
     * 启动页是否自动隐藏
     */
    boolean autoLaunch = true;
    /**
     * 沉浸式效果
     */
    boolean statusBarAppearance = true;
    /**
     * 应用是否全屏运行
     */
    boolean fullScreen = false;
    /**
     * 应用是否自动检测更新
     */
    boolean autoUpdate = true;
    /**
     * 应用是否支持增量更新、云修复
     */
    boolean smartUpdate = true;
    /**
     * 应用开启/关闭调试模式
     */
    boolean debug = false;
    /**
     * 是否允许使用第三方键盘
     */
    boolean allowKeyboardExtension = true;
    /**
     * 键盘弹出方式
     */
    String softInputMode = "auto";
    /**
     * 是否显示键盘上方的工具条
     */
    boolean softInputBarEnabled = true;
    /**
     * 是否允许页面默认拖拽行为
     */
    boolean dragAndDrop = true;
    /**
     * 字体
     */
    String font = "";
    /**
     * 后台运行
     */
    String backgroundMode = "";
    /**
     * 配置应用的URL Scheme，该scheme用于从浏览器或其他应用中启动本应用，并且可以传递参数数据。此字段云编译有效。
     */
    String urlScheme = "";
    /**
     * 可被检测的URL Scheme
     */
    String querySchemes = "";
    /**
     * 网页里被禁止跳转的URL Scheme
     */
    String forbiddenSchemes = "";
    /**
     * 用于配置User Agent，配置后会更改页面里的User Agent以及ajax请求的User Agent
     */
    String userAgent = "";
    /**
     * iTunes文件共享
     */
    boolean fileShare = false;
    /**
     * 自定义下拉刷新模块
     */
    String customRefreshHeader = "";
    /**
     * 是否在桌面显示应用图标
     */
    boolean launcher = true;
    /**
     *否检查https证书是受信任的
     */
    boolean checkSslTrusted = true;
    /**
     * 是否校验应用证书
     */
    boolean appCertificateVerify = true;

    public String getAppBackground() {
        return appBackground;
    }

    public void setAppBackground(String appBackground) {
        this.appBackground = appBackground;
    }

    public String getWindowBackground() {
        return windowBackground;
    }

    public void setWindowBackground(String windowBackground) {
        this.windowBackground = windowBackground;
    }

    public String getFrameBackgroundColor() {
        return frameBackgroundColor;
    }

    public void setFrameBackgroundColor(String frameBackgroundColor) {
        this.frameBackgroundColor = frameBackgroundColor;
    }

    public boolean isPageBounce() {
        return pageBounce;
    }

    public void setPageBounce(boolean pageBounce) {
        this.pageBounce = pageBounce;
    }

    public boolean ishScrollBarEnabled() {
        return hScrollBarEnabled;
    }

    public void sethScrollBarEnabled(boolean hScrollBarEnabled) {
        this.hScrollBarEnabled = hScrollBarEnabled;
    }

    public boolean isvScrollBarEnabled() {
        return vScrollBarEnabled;
    }

    public void setvScrollBarEnabled(boolean vScrollBarEnabled) {
        this.vScrollBarEnabled = vScrollBarEnabled;
    }

    public boolean isAutoLaunch() {
        return autoLaunch;
    }

    public void setAutoLaunch(boolean autoLaunch) {
        this.autoLaunch = autoLaunch;
    }

    public boolean isStatusBarAppearance() {
        return statusBarAppearance;
    }

    public void setStatusBarAppearance(boolean statusBarAppearance) {
        this.statusBarAppearance = statusBarAppearance;
    }

    public boolean isFullScreen() {
        return fullScreen;
    }

    public void setFullScreen(boolean fullScreen) {
        this.fullScreen = fullScreen;
    }

    public boolean isAutoUpdate() {
        return autoUpdate;
    }

    public void setAutoUpdate(boolean autoUpdate) {
        this.autoUpdate = autoUpdate;
    }

    public boolean isSmartUpdate() {
        return smartUpdate;
    }

    public void setSmartUpdate(boolean smartUpdate) {
        this.smartUpdate = smartUpdate;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean isAllowKeyboardExtension() {
        return allowKeyboardExtension;
    }

    public void setAllowKeyboardExtension(boolean allowKeyboardExtension) {
        this.allowKeyboardExtension = allowKeyboardExtension;
    }

    public String getSoftInputMode() {
        return softInputMode;
    }

    public void setSoftInputMode(String softInputMode) {
        this.softInputMode = softInputMode;
    }

    public boolean isSoftInputBarEnabled() {
        return softInputBarEnabled;
    }

    public void setSoftInputBarEnabled(boolean softInputBarEnabled) {
        this.softInputBarEnabled = softInputBarEnabled;
    }

    public boolean isDragAndDrop() {
        return dragAndDrop;
    }

    public void setDragAndDrop(boolean dragAndDrop) {
        this.dragAndDrop = dragAndDrop;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public String getBackgroundMode() {
        return backgroundMode;
    }

    public void setBackgroundMode(String backgroundMode) {
        this.backgroundMode = backgroundMode;
    }

    public String getUrlScheme() {
        return urlScheme;
    }

    public void setUrlScheme(String urlScheme) {
        this.urlScheme = urlScheme;
    }

    public String getQuerySchemes() {
        return querySchemes;
    }

    public void setQuerySchemes(String querySchemes) {
        this.querySchemes = querySchemes;
    }

    public String getForbiddenSchemes() {
        return forbiddenSchemes;
    }

    public void setForbiddenSchemes(String forbiddenSchemes) {
        this.forbiddenSchemes = forbiddenSchemes;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public boolean isFileShare() {
        return fileShare;
    }

    public void setFileShare(boolean fileShare) {
        this.fileShare = fileShare;
    }

    public String getCustomRefreshHeader() {
        return customRefreshHeader;
    }

    public void setCustomRefreshHeader(String customRefreshHeader) {
        this.customRefreshHeader = customRefreshHeader;
    }

    public boolean isLauncher() {
        return launcher;
    }

    public void setLauncher(boolean launcher) {
        this.launcher = launcher;
    }

    public boolean isCheckSslTrusted() {
        return checkSslTrusted;
    }

    public void setCheckSslTrusted(boolean checkSslTrusted) {
        this.checkSslTrusted = checkSslTrusted;
    }

    public boolean isAppCertificateVerify() {
        return appCertificateVerify;
    }

    public void setAppCertificateVerify(boolean appCertificateVerify) {
        this.appCertificateVerify = appCertificateVerify;
    }
}
