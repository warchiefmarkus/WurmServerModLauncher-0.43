package javax.mail.search;

import java.io.Serializable;
import javax.mail.Message;

public abstract class SearchTerm implements Serializable {
  private static final long serialVersionUID = -6652358452205992789L;
  
  public abstract boolean match(Message paramMessage);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\mail\search\SearchTerm.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */