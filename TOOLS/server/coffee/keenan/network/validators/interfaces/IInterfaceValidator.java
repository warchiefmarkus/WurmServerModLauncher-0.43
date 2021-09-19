package coffee.keenan.network.validators.interfaces;

import coffee.keenan.network.config.IConfiguration;
import java.net.NetworkInterface;

public interface IInterfaceValidator {
  boolean validate(NetworkInterface paramNetworkInterface, IConfiguration paramIConfiguration);
  
  Exception getException();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\coffee\keenan\network\validators\interfaces\IInterfaceValidator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */