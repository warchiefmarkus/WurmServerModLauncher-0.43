package 1.0.org.apache.xml.resolver;

public class Version {
  public static String getVersion() {
    return getProduct() + " " + getVersionNum();
  }
  
  public static String getProduct() {
    return "XmlResolver";
  }
  
  public static String getVersionNum() {
    return "1.0";
  }
  
  public static void main(String[] paramArrayOfString) {
    System.out.println(getVersion());
  }
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\org\apache\xml\resolver\Version.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */