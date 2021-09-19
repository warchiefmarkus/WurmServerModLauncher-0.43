/*     */ package org.fourthline.cling.model;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.model.meta.Device;
/*     */ import org.fourthline.cling.model.meta.Icon;
/*     */ import org.fourthline.cling.model.meta.Service;
/*     */ import org.fourthline.cling.model.resource.Resource;
/*     */ import org.seamless.util.URIUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Namespace
/*     */ {
/*  61 */   private static final Logger log = Logger.getLogger(Namespace.class.getName());
/*     */   
/*     */   public static final String DEVICE = "/dev";
/*     */   
/*     */   public static final String SERVICE = "/svc";
/*     */   public static final String CONTROL = "/action";
/*     */   public static final String EVENTS = "/event";
/*     */   public static final String DESCRIPTOR_FILE = "/desc";
/*     */   public static final String CALLBACK_FILE = "/cb";
/*     */   protected final URI basePath;
/*     */   protected final String decodedPath;
/*     */   
/*     */   public Namespace() {
/*  74 */     this("");
/*     */   }
/*     */   
/*     */   public Namespace(String basePath) {
/*  78 */     this(URI.create(basePath));
/*     */   }
/*     */   
/*     */   public Namespace(URI basePath) {
/*  82 */     this.basePath = basePath;
/*  83 */     this.decodedPath = basePath.getPath();
/*     */   }
/*     */   
/*     */   public URI getBasePath() {
/*  87 */     return this.basePath;
/*     */   }
/*     */   
/*     */   public URI getPath(Device device) {
/*  91 */     return appendPathToBaseURI(getDevicePath(device));
/*     */   }
/*     */   
/*     */   public URI getPath(Service service) {
/*  95 */     return appendPathToBaseURI(getServicePath(service));
/*     */   }
/*     */   
/*     */   public URI getDescriptorPath(Device device) {
/*  99 */     return appendPathToBaseURI(getDevicePath(device.getRoot()) + "/desc");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDescriptorPathString(Device device) {
/* 106 */     return this.decodedPath + getDevicePath(device.getRoot()) + "/desc";
/*     */   }
/*     */   
/*     */   public URI getDescriptorPath(Service service) {
/* 110 */     return appendPathToBaseURI(getServicePath(service) + "/desc");
/*     */   }
/*     */   
/*     */   public URI getControlPath(Service service) {
/* 114 */     return appendPathToBaseURI(getServicePath(service) + "/action");
/*     */   }
/*     */   
/*     */   public URI getIconPath(Icon icon) {
/* 118 */     return appendPathToBaseURI(getDevicePath(icon.getDevice()) + "/" + icon.getUri().toString());
/*     */   }
/*     */   
/*     */   public URI getEventSubscriptionPath(Service service) {
/* 122 */     return appendPathToBaseURI(getServicePath(service) + "/event");
/*     */   }
/*     */   
/*     */   public URI getEventCallbackPath(Service service) {
/* 126 */     return appendPathToBaseURI(getServicePath(service) + "/event" + "/cb");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEventCallbackPathString(Service service) {
/* 133 */     return this.decodedPath + getServicePath(service) + "/event" + "/cb";
/*     */   }
/*     */   
/*     */   public URI prefixIfRelative(Device device, URI uri) {
/* 137 */     if (!uri.isAbsolute() && !uri.getPath().startsWith("/")) {
/* 138 */       return appendPathToBaseURI(getDevicePath(device) + "/" + uri);
/*     */     }
/* 140 */     return uri;
/*     */   }
/*     */   
/*     */   public boolean isControlPath(URI uri) {
/* 144 */     return uri.toString().endsWith("/action");
/*     */   }
/*     */   
/*     */   public boolean isEventSubscriptionPath(URI uri) {
/* 148 */     return uri.toString().endsWith("/event");
/*     */   }
/*     */   
/*     */   public boolean isEventCallbackPath(URI uri) {
/* 152 */     return uri.toString().endsWith("/cb");
/*     */   }
/*     */   
/*     */   public Resource[] getResources(Device device) throws ValidationException {
/* 156 */     if (!device.isRoot()) return null;
/*     */     
/* 158 */     Set<Resource> resources = new HashSet<>();
/* 159 */     List<ValidationError> errors = new ArrayList<>();
/*     */     
/* 161 */     log.fine("Discovering local resources of device graph");
/* 162 */     Resource[] discoveredResources = device.discoverResources(this);
/* 163 */     for (Resource resource : discoveredResources) {
/* 164 */       log.finer("Discovered: " + resource);
/* 165 */       if (!resources.add(resource)) {
/* 166 */         log.finer("Local resource already exists, queueing validation error");
/* 167 */         errors.add(new ValidationError(
/* 168 */               getClass(), "resources", "Local URI namespace conflict between resources of device: " + resource));
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 174 */     if (errors.size() > 0) {
/* 175 */       throw new ValidationException("Validation of device graph failed, call getErrors() on exception", errors);
/*     */     }
/* 177 */     return resources.<Resource>toArray(new Resource[resources.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   protected URI appendPathToBaseURI(String path) {
/*     */     try {
/* 183 */       return new URI(this.basePath
/*     */           
/* 185 */           .getScheme(), null, this.basePath
/*     */           
/* 187 */           .getHost(), this.basePath
/* 188 */           .getPort(), this.decodedPath + path, null, null);
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 193 */     catch (URISyntaxException e) {
/* 194 */       return URI.create(this.basePath + path);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected String getDevicePath(Device device) {
/* 199 */     if (device.getIdentity().getUdn() == null) {
/* 200 */       throw new IllegalStateException("Can't generate local URI prefix without UDN");
/*     */     }
/* 202 */     StringBuilder s = new StringBuilder();
/* 203 */     s.append("/dev").append("/");
/*     */     
/* 205 */     s.append(URIUtil.encodePathSegment(device.getIdentity().getUdn().getIdentifierString()));
/* 206 */     return s.toString();
/*     */   }
/*     */   
/*     */   protected String getServicePath(Service service) {
/* 210 */     if (service.getServiceId() == null) {
/* 211 */       throw new IllegalStateException("Can't generate local URI prefix without service ID");
/*     */     }
/* 213 */     StringBuilder s = new StringBuilder();
/* 214 */     s.append("/svc");
/* 215 */     s.append("/");
/* 216 */     s.append(service.getServiceId().getNamespace());
/* 217 */     s.append("/");
/* 218 */     s.append(service.getServiceId().getId());
/* 219 */     return getDevicePath(service.getDevice()) + s.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\Namespace.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */