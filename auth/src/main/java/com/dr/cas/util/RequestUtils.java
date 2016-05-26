package com.dr.cas.util;

import javax.servlet.http.HttpServletRequest;

public class RequestUtils {

  public static String getIPAddress(HttpServletRequest request) {
    String remoteAddr = null;
    // try from true client IP (akamai header)
    remoteAddr = request.getHeader("XOLCIPV");
    if (remoteAddr != null && remoteAddr.length() > 0)
        return cleanIPAddress(remoteAddr);

    // try getting from clientIP (webcache header)
    remoteAddr = request.getHeader("CLIENTIP");

    if (remoteAddr != null && remoteAddr.length() > 0)
        return cleanIPAddress(remoteAddr);
    
    // try getting from X-Real-IP (nginx header)
    remoteAddr = request.getHeader("X-Forwarded-For");

    if (remoteAddr != null && remoteAddr.length() > 0)
        return cleanIPAddress(remoteAddr);
    
    remoteAddr = request.getHeader("X-Real-IP");

    if (remoteAddr != null && remoteAddr.length() > 0)
        return cleanIPAddress(remoteAddr);
    
    // ok.. try from request header
    remoteAddr = request.getHeader("REMOTE_ADDR");

    if (remoteAddr != null && remoteAddr.length() > 0)
        return cleanIPAddress(remoteAddr);

    // finally try the java call which is pretty much equvalent to the above
    // call.
    return cleanIPAddress(request.getRemoteAddr());
  }
  
  public static String cleanIPAddress(String ipAddress) {
    if (ipAddress == null)
        return null;
    String ip = ipAddress.replace("c ", "");
    ip = ipAddress.replace("a ", "");
    return ip;
  }
}
