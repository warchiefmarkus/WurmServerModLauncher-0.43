package org.fourthline.cling.protocol;

import java.net.URL;
import org.fourthline.cling.UpnpService;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.gena.LocalGENASubscription;
import org.fourthline.cling.model.gena.RemoteGENASubscription;
import org.fourthline.cling.model.message.IncomingDatagramMessage;
import org.fourthline.cling.model.message.StreamRequestMessage;
import org.fourthline.cling.model.message.header.UpnpHeader;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.protocol.async.SendingNotificationAlive;
import org.fourthline.cling.protocol.async.SendingNotificationByebye;
import org.fourthline.cling.protocol.async.SendingSearch;
import org.fourthline.cling.protocol.sync.SendingAction;
import org.fourthline.cling.protocol.sync.SendingEvent;
import org.fourthline.cling.protocol.sync.SendingRenewal;
import org.fourthline.cling.protocol.sync.SendingSubscribe;
import org.fourthline.cling.protocol.sync.SendingUnsubscribe;

public interface ProtocolFactory {
  UpnpService getUpnpService();
  
  ReceivingAsync createReceivingAsync(IncomingDatagramMessage paramIncomingDatagramMessage) throws ProtocolCreationException;
  
  ReceivingSync createReceivingSync(StreamRequestMessage paramStreamRequestMessage) throws ProtocolCreationException;
  
  SendingNotificationAlive createSendingNotificationAlive(LocalDevice paramLocalDevice);
  
  SendingNotificationByebye createSendingNotificationByebye(LocalDevice paramLocalDevice);
  
  SendingSearch createSendingSearch(UpnpHeader paramUpnpHeader, int paramInt);
  
  SendingAction createSendingAction(ActionInvocation paramActionInvocation, URL paramURL);
  
  SendingSubscribe createSendingSubscribe(RemoteGENASubscription paramRemoteGENASubscription) throws ProtocolCreationException;
  
  SendingRenewal createSendingRenewal(RemoteGENASubscription paramRemoteGENASubscription);
  
  SendingUnsubscribe createSendingUnsubscribe(RemoteGENASubscription paramRemoteGENASubscription);
  
  SendingEvent createSendingEvent(LocalGENASubscription paramLocalGENASubscription);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\protocol\ProtocolFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */