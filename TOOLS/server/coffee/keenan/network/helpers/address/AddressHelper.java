/*     */ package coffee.keenan.network.helpers.address;
/*     */ import coffee.keenan.network.config.DefaultConfiguration;
/*     */ import coffee.keenan.network.config.IConfiguration;
/*     */ import coffee.keenan.network.helpers.ErrorTracking;
/*     */ import coffee.keenan.network.helpers.interfaces.InterfaceHelper;
/*     */ import coffee.keenan.network.validators.address.IAddressValidator;
/*     */ import coffee.keenan.network.validators.address.IP4Validator;
/*     */ import coffee.keenan.network.validators.address.InternetValidator;
/*     */ import java.net.InetAddress;
/*     */ import java.net.NetworkInterface;
/*     */ import java.net.SocketException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Stream;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ import org.jetbrains.annotations.Nullable;
/*     */ 
/*     */ public class AddressHelper extends ErrorTracking {
/*  23 */   private Set<IAddressValidator> addressValidators = new HashSet<>(
/*  24 */       (Collection<? extends IAddressValidator>)Stream.<IAddressValidator>of(new IAddressValidator[] { (IAddressValidator)new IP4Validator(), (IAddressValidator)new InternetValidator() }).collect(Collectors.toList()));
/*     */   
/*     */   private final IConfiguration configuration;
/*     */   
/*     */   public AddressHelper() {
/*  29 */     this.configuration = (IConfiguration)new DefaultConfiguration();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AddressHelper(IConfiguration configuration) {
/*  35 */     this.configuration = configuration;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static InetAddress getFirstValidAddress() {
/*  41 */     return getFirstValidAddress((IConfiguration)new DefaultConfiguration());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static InetAddress getFirstValidAddress(IConfiguration configuration) {
/*  54 */     InterfaceHelper interfaceHelper = new InterfaceHelper(configuration);
/*  55 */     AddressHelper addressHelper = new AddressHelper(configuration);
/*  56 */     List<NetworkInterface> interfaces = null;
/*     */     
/*     */     try {
/*  59 */       interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
/*     */     }
/*  61 */     catch (SocketException e) {
/*     */       
/*  63 */       e.printStackTrace();
/*     */     } 
/*  65 */     for (NetworkInterface networkInterface : Objects.<List>requireNonNull(interfaces)) {
/*     */       
/*  67 */       if (!interfaceHelper.validateInterface(networkInterface))
/*     */         continue; 
/*  69 */       for (InetAddress inetAddress : Collections.<InetAddress>list(networkInterface.getInetAddresses())) {
/*  70 */         if (addressHelper.validateAddress(inetAddress))
/*  71 */           return inetAddress; 
/*     */       } 
/*  73 */     }  return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAddressValidators(@NotNull IAddressValidator... validators) {
/*  79 */     this.addressValidators.addAll(Arrays.asList(validators));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<IAddressValidator> getAddressValidators() {
/*  85 */     return this.addressValidators;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAddressValidators(@NotNull IAddressValidator... validators) {
/*  91 */     this.addressValidators = new HashSet<>(Arrays.asList(validators));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean validateAddress(InetAddress address) {
/* 103 */     for (IAddressValidator validator : getAddressValidators()) {
/*     */       
/* 105 */       if (!validator.validate(address, this.configuration)) {
/*     */         
/* 107 */         addException("address: " + address.toString(), validator.getException());
/* 108 */         return false;
/*     */       } 
/*     */     } 
/* 111 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\coffee\keenan\network\helpers\address\AddressHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */