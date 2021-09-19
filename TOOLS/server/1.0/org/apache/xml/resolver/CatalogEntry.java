package 1.0.org.apache.xml.resolver;

import java.util.Hashtable;
import java.util.Vector;
import org.apache.xml.resolver.CatalogException;

public class CatalogEntry {
  protected static int nextEntry = 0;
  
  protected static Hashtable entryTypes = new Hashtable();
  
  protected static Vector entryArgs = new Vector();
  
  protected int entryType = 0;
  
  protected Vector args = null;
  
  public static int addEntryType(String paramString, int paramInt) {
    entryTypes.put(paramString, new Integer(nextEntry));
    entryArgs.add(nextEntry, new Integer(paramInt));
    nextEntry++;
    return nextEntry - 1;
  }
  
  public static int getEntryType(String paramString) throws CatalogException {
    if (!entryTypes.containsKey(paramString))
      throw new CatalogException(3); 
    Integer integer = (Integer)entryTypes.get(paramString);
    if (integer == null)
      throw new CatalogException(3); 
    return integer.intValue();
  }
  
  public static int getEntryArgCount(String paramString) throws CatalogException {
    return getEntryArgCount(getEntryType(paramString));
  }
  
  public static int getEntryArgCount(int paramInt) throws CatalogException {
    try {
      Integer integer = entryArgs.get(paramInt);
      return integer.intValue();
    } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
      throw new CatalogException(3);
    } 
  }
  
  public CatalogEntry() {}
  
  public CatalogEntry(String paramString, Vector paramVector) throws CatalogException {
    Integer integer = (Integer)entryTypes.get(paramString);
    if (integer == null)
      throw new CatalogException(3); 
    int i = integer.intValue();
    try {
      Integer integer1 = entryArgs.get(i);
      if (integer1.intValue() != paramVector.size())
        throw new CatalogException(2); 
    } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
      throw new CatalogException(3);
    } 
    this.entryType = i;
    this.args = paramVector;
  }
  
  public CatalogEntry(int paramInt, Vector paramVector) throws CatalogException {
    try {
      Integer integer = entryArgs.get(paramInt);
      if (integer.intValue() != paramVector.size())
        throw new CatalogException(2); 
    } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
      throw new CatalogException(3);
    } 
    this.entryType = paramInt;
    this.args = paramVector;
  }
  
  public int getEntryType() {
    return this.entryType;
  }
  
  public String getEntryArg(int paramInt) {
    try {
      return this.args.get(paramInt);
    } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
      return null;
    } 
  }
  
  public void setEntryArg(int paramInt, String paramString) throws ArrayIndexOutOfBoundsException {
    this.args.set(paramInt, paramString);
  }
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\org\apache\xml\resolver\CatalogEntry.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */