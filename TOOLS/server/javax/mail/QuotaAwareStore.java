package javax.mail;

public interface QuotaAwareStore {
  Quota[] getQuota(String paramString) throws MessagingException;
  
  void setQuota(Quota paramQuota) throws MessagingException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\mail\QuotaAwareStore.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */