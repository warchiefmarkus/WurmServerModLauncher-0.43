package javax.jnlp;

public interface ServiceManagerStub {
  Object lookup(String paramString) throws UnavailableServiceException;
  
  String[] getServiceNames();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\jnlp\ServiceManagerStub.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */