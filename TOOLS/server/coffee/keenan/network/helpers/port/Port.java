/*     */ package coffee.keenan.network.helpers.port;
/*     */ import coffee.keenan.network.helpers.ErrorTracking;
/*     */ import coffee.keenan.network.validators.port.IPortValidator;
/*     */ import coffee.keenan.network.validators.port.TCPValidator;
/*     */ import coffee.keenan.network.validators.port.UDPValidator;
/*     */ import java.net.InetAddress;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.fourthline.cling.support.model.PortMapping;
/*     */ 
/*     */ public class Port extends ErrorTracking {
/*  14 */   private final List<Integer> ports = new ArrayList<>();
/*     */   private Integer favortedPort;
/*     */   private final Protocol protocols;
/*     */   private final InetAddress address;
/*  18 */   private final Set<IPortValidator> validators = new HashSet<>();
/*  19 */   private String description = "";
/*     */   
/*     */   private boolean toMap;
/*     */   private int assignedPort;
/*     */   private boolean isMapped;
/*     */   
/*     */   public Port(InetAddress address, Protocol protocol) {
/*  26 */     this.address = address;
/*  27 */     this.protocols = protocol;
/*  28 */     switch (getProtocol()) {
/*     */       
/*     */       case TCP:
/*  31 */         this.validators.add(new TCPValidator());
/*     */         break;
/*     */       case UDP:
/*  34 */         this.validators.add(new UDPValidator());
/*     */         break;
/*     */       case Both:
/*  37 */         this.validators.add(new TCPValidator());
/*  38 */         this.validators.add(new UDPValidator());
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Port setFavoredPort(int port) {
/*  45 */     this.favortedPort = Integer.valueOf(port);
/*  46 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Integer getFavoredPort() {
/*  51 */     return this.favortedPort;
/*     */   }
/*     */ 
/*     */   
/*     */   public Port addPort(int port) {
/*  56 */     this.ports.add(Integer.valueOf(port));
/*  57 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Port addPortRange(int start, int end) {
/*  62 */     for (int i = start; i <= end; i++)
/*  63 */       this.ports.add(Integer.valueOf(i)); 
/*  64 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Port addPorts(Integer... ports) {
/*  70 */     Collections.addAll(this.ports, ports);
/*  71 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Port addPorts(List<Integer> ports) {
/*  77 */     this.ports.addAll(ports);
/*  78 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Port toMap() {
/*  83 */     return toMap(true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Port toMap(boolean toMap) {
/*  89 */     this.toMap = toMap;
/*  90 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Integer> getPorts() {
/*  96 */     return this.ports;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Protocol getProtocol() {
/* 102 */     return this.protocols;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDescription() {
/* 108 */     return this.description;
/*     */   }
/*     */ 
/*     */   
/*     */   public Port setDescription(String description) {
/* 113 */     this.description = description;
/* 114 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isToMap() {
/* 119 */     return this.toMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isMapped() {
/* 124 */     return this.isMapped;
/*     */   }
/*     */ 
/*     */   
/*     */   public Port setMapped(boolean value) {
/* 129 */     this.isMapped = value;
/* 130 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getAssignedPort() {
/* 135 */     return this.assignedPort;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Port setAssignedPort(int assignedPort) {
/* 141 */     this.assignedPort = assignedPort;
/* 142 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public InetAddress getAddress() {
/* 147 */     return this.address;
/*     */   }
/*     */ 
/*     */   
/*     */   public PortMapping[] getMappings() {
/* 152 */     if (!isToMap()) return new PortMapping[0];
/*     */     
/* 154 */     List<PortMapping> mappings = new ArrayList<>();
/* 155 */     switch (getProtocol()) {
/*     */ 
/*     */       
/*     */       case TCP:
/* 159 */         mappings.add(new PortMapping(getAssignedPort(), this.address.getHostAddress(), PortMapping.Protocol.TCP, getDescription()));
/*     */         break;
/*     */       case UDP:
/* 162 */         mappings.add(new PortMapping(getAssignedPort(), this.address.getHostAddress(), PortMapping.Protocol.UDP, getDescription()));
/*     */         break;
/*     */       case Both:
/* 165 */         mappings.add(new PortMapping(getAssignedPort(), this.address.getHostAddress(), PortMapping.Protocol.UDP, getDescription()));
/* 166 */         mappings.add(new PortMapping(getAssignedPort(), this.address.getHostAddress(), PortMapping.Protocol.TCP, getDescription()));
/*     */         break;
/*     */     } 
/* 169 */     return mappings.<PortMapping>toArray(new PortMapping[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<IPortValidator> getValidators() {
/* 174 */     return this.validators;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Port addValidators(IPortValidator... validators) {
/* 180 */     this.validators.addAll(Arrays.asList(validators));
/* 181 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 187 */     return getDescription() + " (" + getAddress().getHostAddress() + ":" + getAssignedPort() + ")";
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\coffee\keenan\network\helpers\port\Port.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */