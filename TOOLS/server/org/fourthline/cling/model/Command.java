package org.fourthline.cling.model;

public interface Command<T> {
  void execute(ServiceManager<T> paramServiceManager) throws Exception;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\Command.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */