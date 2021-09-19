package coffee.keenan.network.validators.address;

import coffee.keenan.network.config.IConfiguration;
import java.net.InetAddress;

public interface IAddressValidator {
  boolean validate(InetAddress paramInetAddress, IConfiguration paramIConfiguration);
  
  Exception getException();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\coffee\keenan\network\validators\address\IAddressValidator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */