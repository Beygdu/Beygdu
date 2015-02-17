package is.arnastofnun.beygdu.is.arnastofnun.beygdur.soonToBeAdded.POSTHandler;

import java.net.*;
import java.io.*;

//Hof Arnar Jonsson
//OfurEinfaldur post request handler, verdur endurbaettur i spretti 2
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






































