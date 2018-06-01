package demoApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpHost;
import org.apache.http.HttpVersion;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultProxyAuthenticationHandler;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import com.esri.arcgisruntime.io.RequestConfiguration;




public class MyProxyUtil {
    private static String host = "np1prxy801.corp.halliburton.com";
    private static int port = 80;
    private static String user = "h189867";
    private static String password = "aa";

    
    public static void clearESRIProxy() {
        setupESRIProxy(null, port);
    }

    public static void setupESRIProxy() {
        setupESRIProxy(host, port);
    }
    
    public static void setupESRIProxy(String host, int port) {
        try {
            RequestConfiguration.setProxyInfo(host, port);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
   
    public static void setupProxy() {
        setupProxy(host, port, user, password);
    }
    
    public static void setupProxy(String host, int port, String user, String password) {
        DefaultHttpClient httpclient = null;

       try {
           
               HttpParams params = new BasicHttpParams();
               HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
               httpclient = new DefaultHttpClient(params);

               
               System.setProperty("http.proxySet", "true");
               System.setProperty("http.proxyHost", host);
               System.setProperty("http.proxyPort", String.valueOf(port));
               System.setProperty("https.proxyHost", host);
               System.setProperty("https.proxyPort", String.valueOf(port));               
               System.setProperty("http.proxyUser", user);
               System.setProperty("http.proxyPassword", password);
               System.setProperty("https.proxyUser", user);
               System.setProperty("https.proxyPassword", password);               
               System.setProperty("http.proxyType", "4");
               System.setProperty("http.nonProxyHosts", "localhost|127.0.0.1");
               //Below line added to resolve https service issue with jdk8
               System.setProperty("jdk.tls.client.protocols", "TLSv1");
               


               HttpHost proxy;

               proxy = new HttpHost(host, port);                    

               httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
               DefaultProxyAuthenticationHandler proxyAuthHandler = new DefaultProxyAuthenticationHandler();
               httpclient.setProxyAuthenticationHandler(proxyAuthHandler);
               httpclient.getCredentialsProvider().setCredentials(
                       new AuthScope(host, port),
                       new UsernamePasswordCredentials(user, password));

               if (isValidProxyEntry(httpclient)) {
                   System.out.println("proxy is valid");
               }
               else {
                   System.out.println("no proxy");
               }

       } catch (Exception exp) {
           exp.printStackTrace();
       }       

   }
   
   private static boolean isValidProxyEntry(DefaultHttpClient client){
       try {
           URL urlCon = new URL("http://www.google.com");
           URLConnection yc = urlCon.openConnection();
           BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
           String inputLine;
           String urlContent="";
           if ((inputLine = in.readLine()) != null) {
               urlContent = inputLine;
           }
           in.close();
           if(urlContent==null || urlContent.equals("")){
               return false;
           }
           //check for second url
           else{
               urlContent = "";
               URL urlCon1 = new URL("http://www.yahoo.com");
               URLConnection yc1 = urlCon1.openConnection();
               BufferedReader in_ = new BufferedReader(new InputStreamReader(yc1.getInputStream()));
               if ((inputLine = in_.readLine()) != null) {
                   urlContent = inputLine;
               }
               in_.close();

               if(urlContent==null || urlContent.equals("")){
                   return false;
               }

           }
           return true;
       } catch (Exception e) {
           e.printStackTrace();
       }
       return false;
   }
	
}
