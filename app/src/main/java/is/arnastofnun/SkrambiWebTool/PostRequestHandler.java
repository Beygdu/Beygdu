package is.arnastofnun.SkrambiWebTool;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Arnar Jonsson
 * @since 10.3.2015
 * @version 0.5
 */
public class PostRequestHandler {

    private URL url;
    private HttpURLConnection conn = null;
    private String targetUrl;
    private String urlParam;
    private String contentType;
    private String contentLanguage;
    private Boolean useCaches;
    private Boolean doInput;
    private Boolean doOutput;

    private String contentLength;

    /**
     * PostRequestHandler - A handler class for POST Requests
     * Configures a POST request from the parameters and readies it for use
     * @param targetUrl Taget server url
     * @param urlParam Target server POST argument
     * @param contentType Content Type of response
     * @param contentLanguage Content Language of the response
     * @param useCaches Toggle Caches
     * @param doInput Toggle input
     * @param doOutput Toggle output
     */
    public PostRequestHandler(String targetUrl, String urlParam, String contentType, String contentLanguage, Boolean useCaches, Boolean doInput, Boolean doOutput) {

        this.targetUrl = targetUrl;
        this.urlParam = urlParam;
        this.contentType = contentType;
        this.contentLanguage = contentLanguage;
        this.useCaches = useCaches;
        this.doInput = doInput;
        this.doOutput = doOutput;

        int contLength = urlParam.getBytes().length;
        this.contentLength = Integer.toString(contLength);

    }

    /**
     * sendRequest - Send a POST request constructed by the constructor params
     * @return String representation of the server response if the constructor
     * configuration allows for a legal POST request. Returns an error string if the
     * request miss fired
     */
    public String sendRequest() {

        String responseLine;
        StringBuffer result;
        try {
            this.url = new URL(this.targetUrl);
            this.conn = (HttpURLConnection) url.openConnection();
            this.conn.setRequestMethod("POST");

            this.conn.setRequestProperty("Content-Type", this.contentType);
            this.conn.setRequestProperty("Content-Type", this.contentLength);
            this.conn.setRequestProperty("Content-Language", this.contentLanguage);
            this.conn.setUseCaches(this.useCaches);
            this.conn.setDoInput(this.doInput);
            this.conn.setDoOutput(this.doOutput);





            DataOutputStream outStream = new DataOutputStream(this.conn.getOutputStream());
            outStream.writeBytes(this.urlParam);
            outStream.flush();
            outStream.close();





            InputStream inStream = this.conn.getInputStream();
            BufferedReader bR = new BufferedReader(new InputStreamReader(inStream));

            result = new StringBuffer();
            while( (responseLine = bR.readLine()) != null ) {

                result.append(responseLine);
                result.append('\r');

            }
            bR.close();

            return result.toString();

        }
        catch( Exception e ) {
            return "failed to reach host";
        }
        finally {
            if(this.conn != null) {
                this.conn.disconnect();
            }
        }
    }


}
