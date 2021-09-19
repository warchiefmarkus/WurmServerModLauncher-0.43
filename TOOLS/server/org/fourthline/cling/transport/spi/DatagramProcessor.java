package org.fourthline.cling.transport.spi;

import java.net.DatagramPacket;
import java.net.InetAddress;
import org.fourthline.cling.model.UnsupportedDataException;
import org.fourthline.cling.model.message.IncomingDatagramMessage;
import org.fourthline.cling.model.message.OutgoingDatagramMessage;

public interface DatagramProcessor {
  IncomingDatagramMessage read(InetAddress paramInetAddress, DatagramPacket paramDatagramPacket) throws UnsupportedDataException;
  
  DatagramPacket write(OutgoingDatagramMessage paramOutgoingDatagramMessage) throws UnsupportedDataException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\transport\spi\DatagramProcessor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */