package org.sonatype.cs.getmetrics.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class InsecureSSL {

    private static final Logger log = LoggerFactory.getLogger(InsecureSSL.class);
    private static final X509Certificate[] NO_CERTIFICATES = {};

    private InsecureSSL() {
        // Private constructor to prevent instantiation
    }

    public static void makeSSLConnectionsInsecure()
            throws NoSuchAlgorithmException, KeyManagementException {
        log.warn("Making SSL connections insecure");

        TrustManager[] trustAllCerts =
                new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return NO_CERTIFICATES;
                        }

                        @Override
                        public void checkClientTrusted(X509Certificate[] certs, String authType)
                                throws CertificateException {
                            // Not implemented
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] certs, String authType)
                                throws CertificateException {
                            // Not implemented
                        }
                    }
                };

        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        log.warn("SSL connections are now insecure");
    }
}
