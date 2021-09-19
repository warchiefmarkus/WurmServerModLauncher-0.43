/*    */ package com.wurmonline.server.webinterface;
/*    */ 
/*    */ import java.net.InetAddress;
/*    */ import java.rmi.AlreadyBoundException;
/*    */ import java.rmi.RemoteException;
/*    */ import java.rmi.registry.LocateRegistry;
/*    */ import java.rmi.registry.Registry;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class RegistryStarter
/*    */ {
/*    */   public static final String namingInterface = "wuinterface";
/*    */   
/*    */   public static void startRegistry(WebInterfaceImpl webInterface, InetAddress inetaddress, int rmiPort) throws RemoteException, AlreadyBoundException {
/* 41 */     AnchorSocketFactory sf = new AnchorSocketFactory(inetaddress);
/* 42 */     Registry registry = LocateRegistry.createRegistry(rmiPort, null, sf);
/* 43 */     registry.bind("wuinterface", webInterface);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\webinterface\RegistryStarter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */