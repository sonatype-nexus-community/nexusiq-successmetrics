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

    private static final X509Certificate[] NO_CERTIFICATES = {};

    private InsecureSSL() {}

    public static void makeSSLConnectionsInsecure()
            throws NoSuchAlgorithmException, KeyManagementException {
        Logger log = LoggerFactory.getLogger(InsecureSSL.class);
        log.warn("Making SSL connections insecure");
        TrustManager[] trustAllCerts =
                new TrustManager[] {
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return NO_CERTIFICATES;
                        }

                        @Override
                        public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                                throws CertificateException {
                            // Not implemented
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] arg0, String arg1)
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
