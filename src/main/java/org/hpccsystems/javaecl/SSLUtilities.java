package org.hpccsystems.javaecl;

import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * This class provide various static methods that relax X509 certificate and
 * hostname verification while using the SSL over the HTTP protocol.
 *  
 * @author Jiramot.info
 */
public final class SSLUtilities {

 
  private static HostnameVerifier _hostnameVerifier;
  /**
   * Thrust managers.
   */
  private static TrustManager[] _trustManagers;

  /**
   * Set the default Hostname Verifier to an instance of a fake class that
   * trust all hostnames.
   */
  private static void _trustAllHostnames() {
      // Create a trust manager that does not validate certificate chains
      if (_hostnameVerifier == null) {
          _hostnameVerifier = new SSLUtilities.FakeHostnameVerifier();
      } // if
      // Install the all-trusting host name verifier:
      HttpsURLConnection.setDefaultHostnameVerifier(_hostnameVerifier);
  } // _trustAllHttpsCertificates

  /**
   * Set the default X509 Trust Manager to an instance of a fake class that
   * trust all certificates, even the self-signed ones.
   */
  private static void _trustAllHttpsCertificates() {
    SSLContext context;

      // Create a trust manager that does not validate certificate chains
      if (_trustManagers == null) {
          _trustManagers = new TrustManager[]{new SSLUtilities.FakeX509TrustManager()};
      } // if
      // Install the all-trusting trust manager:
      try {
          context = SSLContext.getInstance("SSL");
          context.init(null, _trustManagers, new SecureRandom());
      } catch (GeneralSecurityException gse) {
          throw new IllegalStateException(gse.getMessage());
      } // catch
      HttpsURLConnection.setDefaultSSLSocketFactory(context
            .getSocketFactory());
  } // _trustAllHttpsCertificates

  /**
   * Set the default Hostname Verifier to an instance of a fake class that
   * trust all hostnames.
   */
  public static void trustAllHostnames() {
          _trustAllHostnames();
  }

  /**
   * Set the default X509 Trust Manager to an instance of a fake class that
   * trust all certificates, even the self-signed ones.
   */
  public static void trustAllHttpsCertificates() {
        _trustAllHttpsCertificates();
  } 

 

  /**
   * This class implements a fake hostname verificator, trusting any host
   * name.
   *
   * @author Jiramot.info
   */
  public static class FakeHostnameVerifier implements HostnameVerifier {

    /**
     * Always return true, indicating that the host name is an acceptable
     * match with the server's authentication scheme.
     *
     * @param hostname the host name.
     * @param session the SSL session used on the connection to host.
     * @return the true boolean value indicating the host name is trusted.
     */
    public boolean verify(String hostname, javax.net.ssl.SSLSession session) {
        return (true);
    } // verify
  } 

  /**
   * This class allow any X509 certificates to be used to authenticate the
   * remote side of a secure socket, including self-signed certificates.
   *
   * @author Jiramot.info
   */
  public static class FakeX509TrustManager implements X509TrustManager {

    /**
     * Empty array of certificate authority certificates.
     */
    private static final X509Certificate[] _AcceptedIssuers = new X509Certificate[]{};

    /**
     * Always trust for client SSL chain peer certificate chain with any
     * authType authentication types.
     *
     * @param chain the peer certificate chain.
     * @param authType the authentication type based on the client
     * certificate.
     */
    public void checkClientTrusted(X509Certificate[] chain, String authType) {
    } 
    /**
     * Always trust for server SSL chain peer certificate chain with any
     * authType exchange algorithm types.
     *
     * @param chain the peer certificate chain.
     * @param authType the key exchange algorithm used.
     */
    public void checkServerTrusted(X509Certificate[] chain, String authType) {
    } 

    /**
     * Return an empty array of certificate authority certificates which are
     * trusted for authenticating peers.
     *
     * @return a empty array of issuer certificates.
     */
    public X509Certificate[] getAcceptedIssuers() {
        return (_AcceptedIssuers);
    } 
  } 
}