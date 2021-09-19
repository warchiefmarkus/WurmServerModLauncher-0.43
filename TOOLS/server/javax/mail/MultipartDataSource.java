package javax.mail;

import javax.activation.DataSource;

public interface MultipartDataSource extends DataSource {
  int getCount();
  
  BodyPart getBodyPart(int paramInt) throws MessagingException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\mail\MultipartDataSource.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */