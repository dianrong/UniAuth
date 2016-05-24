package com.dr.cas.util;


import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * Hostname verifier that performs no host name verification for an SSL peer
 * such that all hosts are allowed.
 *
 */
public final class AnyHostnameVerifier implements HostnameVerifier {

    public boolean verify(final String hostname, final SSLSession session) {
        return true;
    }

}