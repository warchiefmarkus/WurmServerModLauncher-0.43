package coffee.keenan.network.validators.port;

import coffee.keenan.network.config.IConfiguration;
import java.net.InetAddress;

public interface IPortValidator {
  boolean validate(InetAddress paramInetAddress, IConfiguration paramIConfiguration, int paramInt);
  
  Exception getException();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\coffee\keenan\network\validators\port\IPortValidator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */