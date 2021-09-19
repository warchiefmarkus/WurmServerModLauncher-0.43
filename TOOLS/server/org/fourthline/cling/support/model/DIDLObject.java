/*     */ package org.fourthline.cling.support.model;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import org.w3c.dom.Element;
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
/*     */ public abstract class DIDLObject
/*     */ {
/*     */   protected String id;
/*     */   protected String parentID;
/*     */   protected String title;
/*     */   protected String creator;
/*     */   
/*     */   public static interface NAMESPACE {}
/*     */   
/*     */   public static abstract class Property<V>
/*     */   {
/*     */     private V value;
/*     */     private final String descriptorName;
/*  39 */     private final List<Property<DIDLAttribute>> attributes = new ArrayList<>();
/*     */     
/*     */     protected Property() {
/*  42 */       this(null, null);
/*     */     }
/*     */     
/*     */     protected Property(String descriptorName) {
/*  46 */       this(null, descriptorName);
/*     */     }
/*     */     
/*     */     protected Property(V value, String descriptorName) {
/*  50 */       this.value = value;
/*     */       
/*  52 */       this
/*  53 */         .descriptorName = (descriptorName == null) ? getClass().getSimpleName().toLowerCase(Locale.ROOT).replace("didlobject$property$upnp$", "") : descriptorName;
/*     */     }
/*     */ 
/*     */     
/*     */     protected Property(V value, String descriptorName, List<Property<DIDLAttribute>> attributes) {
/*  58 */       this.value = value;
/*     */       
/*  60 */       this
/*  61 */         .descriptorName = (descriptorName == null) ? getClass().getSimpleName().toLowerCase(Locale.ROOT).replace("didlobject$property$upnp$", "") : descriptorName;
/*     */       
/*  63 */       this.attributes.addAll(attributes);
/*     */     }
/*     */     
/*     */     public V getValue() {
/*  67 */       return this.value;
/*     */     }
/*     */     
/*     */     public void setValue(V value) {
/*  71 */       this.value = value;
/*     */     }
/*     */     
/*     */     public String getDescriptorName() {
/*  75 */       return this.descriptorName;
/*     */     }
/*     */     
/*     */     public void setOnElement(Element element) {
/*  79 */       element.setTextContent(toString());
/*  80 */       for (Property<DIDLAttribute> attr : this.attributes) {
/*  81 */         element.setAttributeNS(((DIDLAttribute)attr
/*  82 */             .getValue()).getNamespaceURI(), ((DIDLAttribute)attr
/*  83 */             .getValue()).getPrefix() + ':' + attr.getDescriptorName(), ((DIDLAttribute)attr
/*  84 */             .getValue()).getValue());
/*     */       }
/*     */     }
/*     */     
/*     */     public void addAttribute(Property<DIDLAttribute> attr) {
/*  89 */       this.attributes.add(attr);
/*     */     }
/*     */     
/*     */     public void removeAttribute(Property<DIDLAttribute> attr) {
/*  93 */       this.attributes.remove(attr);
/*     */     }
/*     */     
/*     */     public void removeAttribute(String descriptorName) {
/*  97 */       for (Property<DIDLAttribute> attr : this.attributes) {
/*  98 */         if (attr.getDescriptorName().equals(descriptorName)) {
/*  99 */           removeAttribute(attr);
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     public Property<DIDLAttribute> getAttribute(String descriptorName) {
/* 106 */       for (Property<DIDLAttribute> attr : this.attributes) {
/* 107 */         if (attr.getDescriptorName().equals(descriptorName)) {
/* 108 */           return attr;
/*     */         }
/*     */       } 
/* 111 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 116 */       return (getValue() != null) ? getValue().toString() : "";
/*     */     }
/*     */     
/*     */     public static interface NAMESPACE {}
/*     */     
/*     */     public static class PropertyPersonWithRole extends Property<PersonWithRole> {
/*     */       public PropertyPersonWithRole() {}
/*     */       
/*     */       public PropertyPersonWithRole(String descriptorName) {
/* 125 */         super(descriptorName);
/*     */       }
/*     */       
/*     */       public PropertyPersonWithRole(PersonWithRole value, String descriptorName) {
/* 129 */         super(value, descriptorName);
/*     */       }
/*     */ 
/*     */       
/*     */       public void setOnElement(Element element) {
/* 134 */         if (getValue() != null)
/* 135 */           getValue().setOnElement(element); 
/*     */       }
/*     */     }
/*     */     
/*     */     public static class DC {
/*     */       public static interface NAMESPACE
/*     */         extends DIDLObject.Property.NAMESPACE {
/*     */         public static final String URI = "http://purl.org/dc/elements/1.1/";
/*     */       }
/*     */       
/*     */       public static class DESCRIPTION
/*     */         extends DIDLObject.Property<String> implements NAMESPACE {
/*     */         public DESCRIPTION() {}
/*     */         
/*     */         public DESCRIPTION(String value) {
/* 150 */           super(value, null);
/*     */         }
/*     */       }
/*     */       
/*     */       public static class PUBLISHER
/*     */         extends DIDLObject.Property<Person> implements NAMESPACE {
/*     */         public PUBLISHER() {}
/*     */         
/*     */         public PUBLISHER(Person value) {
/* 159 */           super(value, null);
/*     */         }
/*     */       }
/*     */       
/*     */       public static class CONTRIBUTOR
/*     */         extends DIDLObject.Property<Person> implements NAMESPACE {
/*     */         public CONTRIBUTOR() {}
/*     */         
/*     */         public CONTRIBUTOR(Person value) {
/* 168 */           super(value, null);
/*     */         }
/*     */       }
/*     */       
/*     */       public static class DATE
/*     */         extends DIDLObject.Property<String> implements NAMESPACE {
/*     */         public DATE() {}
/*     */         
/*     */         public DATE(String value) {
/* 177 */           super(value, null);
/*     */         }
/*     */       }
/*     */       
/*     */       public static class LANGUAGE
/*     */         extends DIDLObject.Property<String> implements NAMESPACE {
/*     */         public LANGUAGE() {}
/*     */         
/*     */         public LANGUAGE(String value) {
/* 186 */           super(value, null);
/*     */         }
/*     */       }
/*     */       
/*     */       public static class RELATION
/*     */         extends DIDLObject.Property<URI> implements NAMESPACE {
/*     */         public RELATION() {}
/*     */         
/*     */         public RELATION(URI value) {
/* 195 */           super(value, null);
/*     */         }
/*     */       }
/*     */       
/*     */       public static class RIGHTS
/*     */         extends DIDLObject.Property<String> implements NAMESPACE {
/*     */         public RIGHTS() {}
/*     */         
/*     */         public RIGHTS(String value) {
/* 204 */           super(value, null);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public static abstract class SEC {
/*     */       public static interface NAMESPACE
/*     */         extends DIDLObject.Property.NAMESPACE {
/*     */         public static final String URI = "http://www.sec.co.kr/";
/*     */       }
/*     */       
/*     */       public static class CAPTIONINFOEX extends DIDLObject.Property<URI> implements NAMESPACE {
/*     */         public CAPTIONINFOEX() {
/* 217 */           this(null);
/*     */         }
/*     */         
/*     */         public CAPTIONINFOEX(URI value) {
/* 221 */           super(value, "CaptionInfoEx");
/*     */         }
/*     */         
/*     */         public CAPTIONINFOEX(URI value, List<DIDLObject.Property<DIDLAttribute>> attributes) {
/* 225 */           super(value, "CaptionInfoEx", attributes);
/*     */         }
/*     */       }
/*     */       
/*     */       public static class CAPTIONINFO extends DIDLObject.Property<URI> implements NAMESPACE {
/*     */         public CAPTIONINFO() {
/* 231 */           this(null);
/*     */         }
/*     */         
/*     */         public CAPTIONINFO(URI value) {
/* 235 */           super(value, "CaptionInfo");
/*     */         }
/*     */         
/*     */         public CAPTIONINFO(URI value, List<DIDLObject.Property<DIDLAttribute>> attributes) {
/* 239 */           super(value, "CaptionInfo", attributes);
/*     */         }
/*     */       }
/*     */       
/*     */       public static class TYPE extends DIDLObject.Property<DIDLAttribute> implements NAMESPACE {
/*     */         public TYPE() {
/* 245 */           this(null);
/*     */         }
/*     */         
/*     */         public TYPE(DIDLAttribute value) {
/* 249 */           super(value, "type");
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public static abstract class UPNP
/*     */     {
/*     */       public static interface NAMESPACE
/*     */         extends DIDLObject.Property.NAMESPACE {
/*     */         public static final String URI = "urn:schemas-upnp-org:metadata-1-0/upnp/";
/*     */       }
/*     */       
/*     */       public static class ARTIST
/*     */         extends DIDLObject.Property.PropertyPersonWithRole
/*     */         implements NAMESPACE {
/*     */         public ARTIST() {}
/*     */         
/*     */         public ARTIST(PersonWithRole value) {
/* 267 */           super(value, null);
/*     */         }
/*     */       }
/*     */       
/*     */       public static class ACTOR
/*     */         extends DIDLObject.Property.PropertyPersonWithRole implements NAMESPACE {
/*     */         public ACTOR() {}
/*     */         
/*     */         public ACTOR(PersonWithRole value) {
/* 276 */           super(value, null);
/*     */         }
/*     */       }
/*     */       
/*     */       public static class AUTHOR
/*     */         extends DIDLObject.Property.PropertyPersonWithRole implements NAMESPACE {
/*     */         public AUTHOR() {}
/*     */         
/*     */         public AUTHOR(PersonWithRole value) {
/* 285 */           super(value, null);
/*     */         }
/*     */       }
/*     */       
/*     */       public static class PRODUCER
/*     */         extends DIDLObject.Property<Person> implements NAMESPACE {
/*     */         public PRODUCER() {}
/*     */         
/*     */         public PRODUCER(Person value) {
/* 294 */           super(value, null);
/*     */         }
/*     */       }
/*     */       
/*     */       public static class DIRECTOR
/*     */         extends DIDLObject.Property<Person> implements NAMESPACE {
/*     */         public DIRECTOR() {}
/*     */         
/*     */         public DIRECTOR(Person value) {
/* 303 */           super(value, null);
/*     */         }
/*     */       }
/*     */       
/*     */       public static class GENRE
/*     */         extends DIDLObject.Property<String> implements NAMESPACE {
/*     */         public GENRE() {}
/*     */         
/*     */         public GENRE(String value) {
/* 312 */           super(value, null);
/*     */         }
/*     */       }
/*     */       
/*     */       public static class ALBUM
/*     */         extends DIDLObject.Property<String> implements NAMESPACE {
/*     */         public ALBUM() {}
/*     */         
/*     */         public ALBUM(String value) {
/* 321 */           super(value, null);
/*     */         }
/*     */       }
/*     */       
/*     */       public static class PLAYLIST
/*     */         extends DIDLObject.Property<String> implements NAMESPACE {
/*     */         public PLAYLIST() {}
/*     */         
/*     */         public PLAYLIST(String value) {
/* 330 */           super(value, null);
/*     */         }
/*     */       }
/*     */       
/*     */       public static class REGION
/*     */         extends DIDLObject.Property<String> implements NAMESPACE {
/*     */         public REGION() {}
/*     */         
/*     */         public REGION(String value) {
/* 339 */           super(value, null);
/*     */         }
/*     */       }
/*     */       
/*     */       public static class RATING
/*     */         extends DIDLObject.Property<String> implements NAMESPACE {
/*     */         public RATING() {}
/*     */         
/*     */         public RATING(String value) {
/* 348 */           super(value, null);
/*     */         }
/*     */       }
/*     */       
/*     */       public static class TOC
/*     */         extends DIDLObject.Property<String> implements NAMESPACE {
/*     */         public TOC() {}
/*     */         
/*     */         public TOC(String value) {
/* 357 */           super(value, null);
/*     */         }
/*     */       }
/*     */       
/*     */       public static class ALBUM_ART_URI extends DIDLObject.Property<URI> implements NAMESPACE {
/*     */         public ALBUM_ART_URI() {
/* 363 */           this(null);
/*     */         }
/*     */         
/*     */         public ALBUM_ART_URI(URI value) {
/* 367 */           super(value, "albumArtURI");
/*     */         }
/*     */         
/*     */         public ALBUM_ART_URI(URI value, List<DIDLObject.Property<DIDLAttribute>> attributes) {
/* 371 */           super(value, "albumArtURI", attributes);
/*     */         }
/*     */       }
/*     */       
/*     */       public static class ARTIST_DISCO_URI extends DIDLObject.Property<URI> implements NAMESPACE {
/*     */         public ARTIST_DISCO_URI() {
/* 377 */           this(null);
/*     */         }
/*     */         
/*     */         public ARTIST_DISCO_URI(URI value) {
/* 381 */           super(value, "artistDiscographyURI");
/*     */         }
/*     */       }
/*     */       
/*     */       public static class LYRICS_URI extends DIDLObject.Property<URI> implements NAMESPACE {
/*     */         public LYRICS_URI() {
/* 387 */           this(null);
/*     */         }
/*     */         
/*     */         public LYRICS_URI(URI value) {
/* 391 */           super(value, "lyricsURI");
/*     */         }
/*     */       }
/*     */       
/*     */       public static class STORAGE_TOTAL extends DIDLObject.Property<Long> implements NAMESPACE {
/*     */         public STORAGE_TOTAL() {
/* 397 */           this(null);
/*     */         }
/*     */         
/*     */         public STORAGE_TOTAL(Long value) {
/* 401 */           super(value, "storageTotal");
/*     */         }
/*     */       }
/*     */       
/*     */       public static class STORAGE_USED extends DIDLObject.Property<Long> implements NAMESPACE {
/*     */         public STORAGE_USED() {
/* 407 */           this(null);
/*     */         }
/*     */         
/*     */         public STORAGE_USED(Long value) {
/* 411 */           super(value, "storageUsed");
/*     */         }
/*     */       }
/*     */       
/*     */       public static class STORAGE_FREE extends DIDLObject.Property<Long> implements NAMESPACE {
/*     */         public STORAGE_FREE() {
/* 417 */           this(null);
/*     */         }
/*     */         
/*     */         public STORAGE_FREE(Long value) {
/* 421 */           super(value, "storageFree");
/*     */         }
/*     */       }
/*     */       
/*     */       public static class STORAGE_MAX_PARTITION extends DIDLObject.Property<Long> implements NAMESPACE {
/*     */         public STORAGE_MAX_PARTITION() {
/* 427 */           this(null);
/*     */         }
/*     */         
/*     */         public STORAGE_MAX_PARTITION(Long value) {
/* 431 */           super(value, "storageMaxPartition");
/*     */         }
/*     */       }
/*     */       
/*     */       public static class STORAGE_MEDIUM extends DIDLObject.Property<StorageMedium> implements NAMESPACE {
/*     */         public STORAGE_MEDIUM() {
/* 437 */           this(null);
/*     */         }
/*     */         
/*     */         public STORAGE_MEDIUM(StorageMedium value) {
/* 441 */           super(value, "storageMedium");
/*     */         }
/*     */       }
/*     */       
/*     */       public static class LONG_DESCRIPTION extends DIDLObject.Property<String> implements NAMESPACE {
/*     */         public LONG_DESCRIPTION() {
/* 447 */           this(null);
/*     */         }
/*     */         
/*     */         public LONG_DESCRIPTION(String value) {
/* 451 */           super(value, "longDescription");
/*     */         }
/*     */       }
/*     */       
/*     */       public static class ICON extends DIDLObject.Property<URI> implements NAMESPACE {
/*     */         public ICON() {
/* 457 */           this(null);
/*     */         }
/*     */         
/*     */         public ICON(URI value) {
/* 461 */           super(value, "icon");
/*     */         }
/*     */       }
/*     */       
/*     */       public static class RADIO_CALL_SIGN extends DIDLObject.Property<String> implements NAMESPACE {
/*     */         public RADIO_CALL_SIGN() {
/* 467 */           this(null);
/*     */         }
/*     */         
/*     */         public RADIO_CALL_SIGN(String value) {
/* 471 */           super(value, "radioCallSign");
/*     */         }
/*     */       }
/*     */       
/*     */       public static class RADIO_STATION_ID extends DIDLObject.Property<String> implements NAMESPACE {
/*     */         public RADIO_STATION_ID() {
/* 477 */           this(null);
/*     */         }
/*     */         
/*     */         public RADIO_STATION_ID(String value) {
/* 481 */           super(value, "radioStationID");
/*     */         }
/*     */       }
/*     */       
/*     */       public static class RADIO_BAND extends DIDLObject.Property<String> implements NAMESPACE {
/*     */         public RADIO_BAND() {
/* 487 */           this(null);
/*     */         }
/*     */         
/*     */         public RADIO_BAND(String value) {
/* 491 */           super(value, "radioBand");
/*     */         }
/*     */       }
/*     */       
/*     */       public static class CHANNEL_NR extends DIDLObject.Property<Integer> implements NAMESPACE {
/*     */         public CHANNEL_NR() {
/* 497 */           this(null);
/*     */         }
/*     */         
/*     */         public CHANNEL_NR(Integer value) {
/* 501 */           super(value, "channelNr");
/*     */         }
/*     */       }
/*     */       
/*     */       public static class CHANNEL_NAME extends DIDLObject.Property<String> implements NAMESPACE {
/*     */         public CHANNEL_NAME() {
/* 507 */           this(null);
/*     */         }
/*     */         
/*     */         public CHANNEL_NAME(String value) {
/* 511 */           super(value, "channelName");
/*     */         }
/*     */       }
/*     */       
/*     */       public static class SCHEDULED_START_TIME extends DIDLObject.Property<String> implements NAMESPACE {
/*     */         public SCHEDULED_START_TIME() {
/* 517 */           this(null);
/*     */         }
/*     */         
/*     */         public SCHEDULED_START_TIME(String value) {
/* 521 */           super(value, "scheduledStartTime");
/*     */         }
/*     */       }
/*     */       
/*     */       public static class SCHEDULED_END_TIME extends DIDLObject.Property<String> implements NAMESPACE {
/*     */         public SCHEDULED_END_TIME() {
/* 527 */           this(null);
/*     */         }
/*     */         
/*     */         public SCHEDULED_END_TIME(String value) {
/* 531 */           super(value, "scheduledEndTime");
/*     */         }
/*     */       }
/*     */       
/*     */       public static class DVD_REGION_CODE extends DIDLObject.Property<Integer> implements NAMESPACE {
/*     */         public DVD_REGION_CODE() {
/* 537 */           this(null);
/*     */         }
/*     */         
/*     */         public DVD_REGION_CODE(Integer value) {
/* 541 */           super(value, "DVDRegionCode");
/*     */         }
/*     */       }
/*     */       
/*     */       public static class ORIGINAL_TRACK_NUMBER extends DIDLObject.Property<Integer> implements NAMESPACE {
/*     */         public ORIGINAL_TRACK_NUMBER() {
/* 547 */           this(null);
/*     */         }
/*     */         
/*     */         public ORIGINAL_TRACK_NUMBER(Integer value) {
/* 551 */           super(value, "originalTrackNumber");
/*     */         }
/*     */       }
/*     */       
/*     */       public static class USER_ANNOTATION
/*     */         extends DIDLObject.Property<String> implements NAMESPACE {
/*     */         public USER_ANNOTATION() {
/* 558 */           this(null);
/*     */         }
/*     */         
/*     */         public USER_ANNOTATION(String value) {
/* 562 */           super(value, "userAnnotation");
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public static abstract class DLNA
/*     */     {
/*     */       public static class PROFILE_ID
/*     */         extends DIDLObject.Property<DIDLAttribute>
/*     */         implements NAMESPACE
/*     */       {
/*     */         public PROFILE_ID() {
/* 575 */           this(null);
/*     */         }
/*     */         
/*     */         public PROFILE_ID(DIDLAttribute value) {
/* 579 */           super(value, "profileID");
/*     */         }
/*     */       }
/*     */       
/*     */       public static interface NAMESPACE
/*     */         extends DIDLObject.Property.NAMESPACE {
/*     */         public static final String URI = "urn:schemas-dlna-org:metadata-1-0/";
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Class {
/*     */     protected String value;
/*     */     protected String friendlyName;
/*     */     
/*     */     public Class(String value) {
/* 595 */       this.value = value;
/*     */     } protected boolean includeDerived;
/*     */     public Class() {}
/*     */     public Class(String value, String friendlyName) {
/* 599 */       this.value = value;
/* 600 */       this.friendlyName = friendlyName;
/*     */     }
/*     */     
/*     */     public Class(String value, String friendlyName, boolean includeDerived) {
/* 604 */       this.value = value;
/* 605 */       this.friendlyName = friendlyName;
/* 606 */       this.includeDerived = includeDerived;
/*     */     }
/*     */     
/*     */     public String getValue() {
/* 610 */       return this.value;
/*     */     }
/*     */     
/*     */     public void setValue(String value) {
/* 614 */       this.value = value;
/*     */     }
/*     */     
/*     */     public String getFriendlyName() {
/* 618 */       return this.friendlyName;
/*     */     }
/*     */     
/*     */     public void setFriendlyName(String friendlyName) {
/* 622 */       this.friendlyName = friendlyName;
/*     */     }
/*     */     
/*     */     public boolean isIncludeDerived() {
/* 626 */       return this.includeDerived;
/*     */     }
/*     */     
/*     */     public void setIncludeDerived(boolean includeDerived) {
/* 630 */       this.includeDerived = includeDerived;
/*     */     }
/*     */     
/*     */     public boolean equals(DIDLObject instance) {
/* 634 */       return getValue().equals(instance.getClazz().getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean restricted = true;
/*     */ 
/*     */   
/*     */   protected WriteStatus writeStatus;
/*     */ 
/*     */   
/*     */   protected Class clazz;
/*     */ 
/*     */   
/* 649 */   protected List<Res> resources = new ArrayList<>();
/* 650 */   protected List<Property> properties = new ArrayList<>();
/*     */   
/* 652 */   protected List<DescMeta> descMetadata = new ArrayList<>();
/*     */ 
/*     */   
/*     */   protected DIDLObject() {}
/*     */   
/*     */   protected DIDLObject(DIDLObject other) {
/* 658 */     this(other.getId(), other
/* 659 */         .getParentID(), other
/* 660 */         .getTitle(), other
/* 661 */         .getCreator(), other
/* 662 */         .isRestricted(), other
/* 663 */         .getWriteStatus(), other
/* 664 */         .getClazz(), other
/* 665 */         .getResources(), other
/* 666 */         .getProperties(), other
/* 667 */         .getDescMetadata());
/*     */   }
/*     */ 
/*     */   
/*     */   protected DIDLObject(String id, String parentID, String title, String creator, boolean restricted, WriteStatus writeStatus, Class clazz, List<Res> resources, List<Property> properties, List<DescMeta> descMetadata) {
/* 672 */     this.id = id;
/* 673 */     this.parentID = parentID;
/* 674 */     this.title = title;
/* 675 */     this.creator = creator;
/* 676 */     this.restricted = restricted;
/* 677 */     this.writeStatus = writeStatus;
/* 678 */     this.clazz = clazz;
/* 679 */     this.resources = resources;
/* 680 */     this.properties = properties;
/* 681 */     this.descMetadata = descMetadata;
/*     */   }
/*     */   
/*     */   public String getId() {
/* 685 */     return this.id;
/*     */   }
/*     */   
/*     */   public DIDLObject setId(String id) {
/* 689 */     this.id = id;
/* 690 */     return this;
/*     */   }
/*     */   
/*     */   public String getParentID() {
/* 694 */     return this.parentID;
/*     */   }
/*     */   
/*     */   public DIDLObject setParentID(String parentID) {
/* 698 */     this.parentID = parentID;
/* 699 */     return this;
/*     */   }
/*     */   
/*     */   public String getTitle() {
/* 703 */     return this.title;
/*     */   }
/*     */   
/*     */   public DIDLObject setTitle(String title) {
/* 707 */     this.title = title;
/* 708 */     return this;
/*     */   }
/*     */   
/*     */   public String getCreator() {
/* 712 */     return this.creator;
/*     */   }
/*     */   
/*     */   public DIDLObject setCreator(String creator) {
/* 716 */     this.creator = creator;
/* 717 */     return this;
/*     */   }
/*     */   
/*     */   public boolean isRestricted() {
/* 721 */     return this.restricted;
/*     */   }
/*     */   
/*     */   public DIDLObject setRestricted(boolean restricted) {
/* 725 */     this.restricted = restricted;
/* 726 */     return this;
/*     */   }
/*     */   
/*     */   public WriteStatus getWriteStatus() {
/* 730 */     return this.writeStatus;
/*     */   }
/*     */   
/*     */   public DIDLObject setWriteStatus(WriteStatus writeStatus) {
/* 734 */     this.writeStatus = writeStatus;
/* 735 */     return this;
/*     */   }
/*     */   
/*     */   public Res getFirstResource() {
/* 739 */     return (getResources().size() > 0) ? getResources().get(0) : null;
/*     */   }
/*     */   
/*     */   public List<Res> getResources() {
/* 743 */     return this.resources;
/*     */   }
/*     */   
/*     */   public DIDLObject setResources(List<Res> resources) {
/* 747 */     this.resources = resources;
/* 748 */     return this;
/*     */   }
/*     */   
/*     */   public DIDLObject addResource(Res resource) {
/* 752 */     getResources().add(resource);
/* 753 */     return this;
/*     */   }
/*     */   
/*     */   public Class getClazz() {
/* 757 */     return this.clazz;
/*     */   }
/*     */   
/*     */   public DIDLObject setClazz(Class clazz) {
/* 761 */     this.clazz = clazz;
/* 762 */     return this;
/*     */   }
/*     */   
/*     */   public List<Property> getProperties() {
/* 766 */     return this.properties;
/*     */   }
/*     */   
/*     */   public DIDLObject setProperties(List<Property> properties) {
/* 770 */     this.properties = properties;
/* 771 */     return this;
/*     */   }
/*     */   
/*     */   public DIDLObject addProperty(Property property) {
/* 775 */     if (property == null) return this; 
/* 776 */     getProperties().add(property);
/* 777 */     return this;
/*     */   }
/*     */   
/*     */   public DIDLObject replaceFirstProperty(Property property) {
/* 781 */     if (property == null) return this; 
/* 782 */     Iterator<Property> it = getProperties().iterator();
/* 783 */     while (it.hasNext()) {
/* 784 */       Property p = it.next();
/* 785 */       if (p.getClass().isAssignableFrom(property.getClass()))
/* 786 */         it.remove(); 
/*     */     } 
/* 788 */     addProperty(property);
/* 789 */     return this;
/*     */   }
/*     */   
/*     */   public DIDLObject replaceProperties(Class<? extends Property> propertyClass, Property[] properties) {
/* 793 */     if (properties.length == 0) return this; 
/* 794 */     removeProperties(propertyClass);
/* 795 */     return addProperties(properties);
/*     */   }
/*     */   
/*     */   public DIDLObject addProperties(Property[] properties) {
/* 799 */     if (properties == null) return this; 
/* 800 */     for (Property property : properties) {
/* 801 */       addProperty(property);
/*     */     }
/* 803 */     return this;
/*     */   }
/*     */   
/*     */   public DIDLObject removeProperties(Class<? extends Property> propertyClass) {
/* 807 */     Iterator<Property> it = getProperties().iterator();
/* 808 */     while (it.hasNext()) {
/* 809 */       Property property = it.next();
/* 810 */       if (propertyClass.isInstance(property))
/* 811 */         it.remove(); 
/*     */     } 
/* 813 */     return this;
/*     */   }
/*     */   
/*     */   public boolean hasProperty(Class<? extends Property> propertyClass) {
/* 817 */     for (Property property : getProperties()) {
/* 818 */       if (propertyClass.isInstance(property)) return true; 
/*     */     } 
/* 820 */     return false;
/*     */   }
/*     */   
/*     */   public <V> Property<V> getFirstProperty(Class<? extends Property<V>> propertyClass) {
/* 824 */     for (Property<V> property : getProperties()) {
/* 825 */       if (propertyClass.isInstance(property)) return property; 
/*     */     } 
/* 827 */     return null;
/*     */   }
/*     */   
/*     */   public <V> Property<V> getLastProperty(Class<? extends Property<V>> propertyClass) {
/* 831 */     Property<V> found = null;
/* 832 */     for (Property property : getProperties()) {
/* 833 */       if (propertyClass.isInstance(property)) found = property; 
/*     */     } 
/* 835 */     return found;
/*     */   }
/*     */   
/*     */   public <V> Property<V>[] getProperties(Class<? extends Property<V>> propertyClass) {
/* 839 */     List<Property<V>> list = new ArrayList<>();
/* 840 */     for (Property<V> property : getProperties()) {
/* 841 */       if (propertyClass.isInstance(property))
/* 842 */         list.add(property); 
/*     */     } 
/* 844 */     return list.<Property<V>>toArray((Property<V>[])new Property[list.size()]);
/*     */   }
/*     */   
/*     */   public <V> Property<V>[] getPropertiesByNamespace(Class<? extends Property.NAMESPACE> namespace) {
/* 848 */     List<Property<V>> list = new ArrayList<>();
/* 849 */     for (Property<V> property : getProperties()) {
/* 850 */       if (namespace.isInstance(property))
/* 851 */         list.add(property); 
/*     */     } 
/* 853 */     return list.<Property<V>>toArray((Property<V>[])new Property[list.size()]);
/*     */   }
/*     */   
/*     */   public <V> V getFirstPropertyValue(Class<? extends Property<V>> propertyClass) {
/* 857 */     Property<V> prop = getFirstProperty(propertyClass);
/* 858 */     return (prop == null) ? null : prop.getValue();
/*     */   }
/*     */   
/*     */   public <V> List<V> getPropertyValues(Class<? extends Property<V>> propertyClass) {
/* 862 */     List<V> list = new ArrayList<>();
/* 863 */     for (Property<V> property : getProperties(propertyClass)) {
/* 864 */       list.add(property.getValue());
/*     */     }
/* 866 */     return list;
/*     */   }
/*     */   
/*     */   public List<DescMeta> getDescMetadata() {
/* 870 */     return this.descMetadata;
/*     */   }
/*     */   
/*     */   public void setDescMetadata(List<DescMeta> descMetadata) {
/* 874 */     this.descMetadata = descMetadata;
/*     */   }
/*     */   
/*     */   public DIDLObject addDescMetadata(DescMeta descMetadata) {
/* 878 */     getDescMetadata().add(descMetadata);
/* 879 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 884 */     if (this == o) return true; 
/* 885 */     if (o == null || getClass() != o.getClass()) return false;
/*     */     
/* 887 */     DIDLObject that = (DIDLObject)o;
/*     */     
/* 889 */     if (!this.id.equals(that.id)) return false;
/*     */     
/* 891 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 896 */     return this.id.hashCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\DIDLObject.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */