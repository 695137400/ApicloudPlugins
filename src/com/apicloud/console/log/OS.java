package com.apicloud.console.log;

/**
 * Created with IntelliJ IDEA.<br/>
 * User: lizhichao<br/>
 * Date: 2018-5-13-0013<br/>
 * Time: 16:25:01<br/>
 * Author:lizhichao<br/>
 * Description: <span style="color:#63D3E9"></span><br/>
 */
public class OS {
    private static String OS = System.getProperty("os.name").toLowerCase();
    public static boolean isMacOS() {
        return OS.indexOf("mac") >= 0 && OS.indexOf("os") > 0;
    }
    private static OSType osType;

    static {
        osType = OSType.OSUndefined;
    }

    public OS() {
    }

    public static final boolean isWindows() {
        return getOSType() == OSType.OSWindows;
    }

    public static final boolean isMacintosh() {
        return getOSType() == OSType.OSMacintosh;
    }

    public static final boolean isLinux() {
        return getOSType() == OSType.OSLinux;
    }

    private static final OSType getOSType() {
        if (osType == OSType.OSUndefined) {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.startsWith("windows")) {
                osType = OSType.OSWindows;
            } else if (os.startsWith("linux")) {
                osType = OSType.OSLinux;
            } else if (os.startsWith("mac")) {
                osType = OSType.OSMacintosh;
            } else {
                osType = OSType.OSUnknown;
            }
        }

        return osType;
    }

    private static enum OSType {
        OSUndefined,
        OSLinux,
        OSWindows,
        OSMacintosh,
        OSUnknown;

        private OSType() {
        }
    }
}
