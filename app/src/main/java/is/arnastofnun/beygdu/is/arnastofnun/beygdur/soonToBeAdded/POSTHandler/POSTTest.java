package is.arnastofnun.beygdu.is.arnastofnun.beygdur.soonToBeAdded.POSTHandler;

public class POSTTest {

  public static void main(String[] args) {
  
  PostRequestHandler pHandler = new PostRequestHandler("http://skrambi.arnastofnun.is/checkDocument", "soofa", "text/plain", "en-US", false, true, true);
  
  System.out.println(pHandler.sendRequest());
  
  }








}