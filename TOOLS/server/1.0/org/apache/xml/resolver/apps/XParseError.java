package 1.0.org.apache.xml.resolver.apps;

import java.net.MalformedURLException;
import java.net.URL;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

public class XParseError implements ErrorHandler {
  private boolean showErrors = true;
  
  private boolean showWarnings = false;
  
  private int maxMessages = 10;
  
  private int fatalCount = 0;
  
  private int errorCount = 0;
  
  private int warningCount = 0;
  
  private String baseURI = "";
  
  public XParseError(boolean paramBoolean1, boolean paramBoolean2) {
    this.showErrors = paramBoolean1;
    this.showWarnings = paramBoolean2;
    String str1 = System.getProperty("user.dir");
    String str2 = "";
    if (str1.endsWith("/")) {
      str2 = "file:" + str1 + "file";
    } else {
      str2 = "file:" + str1 + "/" + str2;
    } 
    try {
      URL uRL = new URL(str2);
      this.baseURI = uRL.toString();
    } catch (MalformedURLException malformedURLException) {}
  }
  
  public int getErrorCount() {
    return this.errorCount;
  }
  
  public int getFatalCount() {
    return this.fatalCount;
  }
  
  public int getWarningCount() {
    return this.warningCount;
  }
  
  public int getMaxMessages() {
    return this.maxMessages;
  }
  
  public void setMaxMessages(int paramInt) {
    this.maxMessages = paramInt;
  }
  
  public void error(SAXParseException paramSAXParseException) {
    if (this.showErrors) {
      if (this.errorCount + this.warningCount < this.maxMessages)
        message("Error", paramSAXParseException); 
      this.errorCount++;
    } 
  }
  
  public void fatalError(SAXParseException paramSAXParseException) {
    if (this.showErrors) {
      if (this.errorCount + this.warningCount < this.maxMessages)
        message("Fatal error", paramSAXParseException); 
      this.errorCount++;
      this.fatalCount++;
    } 
  }
  
  public void warning(SAXParseException paramSAXParseException) {
    if (this.showWarnings) {
      if (this.errorCount + this.warningCount < this.maxMessages)
        message("Warning", paramSAXParseException); 
      this.warningCount++;
    } 
  }
  
  private void message(String paramString, SAXParseException paramSAXParseException) {
    String str = paramSAXParseException.getSystemId();
    if (str.startsWith(this.baseURI))
      str = str.substring(this.baseURI.length()); 
    System.out.print(paramString + ":" + str + ":" + paramSAXParseException.getLineNumber());
    if (paramSAXParseException.getColumnNumber() > 0)
      System.out.print(":" + paramSAXParseException.getColumnNumber()); 
    System.out.println(":" + paramSAXParseException.getMessage());
  }
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\org\apache\xml\resolver\apps\XParseError.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */