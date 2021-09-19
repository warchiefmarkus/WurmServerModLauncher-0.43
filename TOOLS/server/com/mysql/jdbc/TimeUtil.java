/*      */ package com.mysql.jdbc;
/*      */ 
/*      */ import java.sql.Date;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.Time;
/*      */ import java.sql.Timestamp;
/*      */ import java.util.Calendar;
/*      */ import java.util.Collections;
/*      */ import java.util.Date;
/*      */ import java.util.GregorianCalendar;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.TimeZone;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class TimeUtil
/*      */ {
/*      */   static final Map ABBREVIATED_TIMEZONES;
/*   47 */   static final TimeZone GMT_TIMEZONE = TimeZone.getTimeZone("GMT");
/*      */   
/*      */   static final Map TIMEZONE_MAPPINGS;
/*      */   
/*      */   static {
/*   52 */     HashMap tempMap = new HashMap();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   57 */     tempMap.put("Romance", "Europe/Paris");
/*   58 */     tempMap.put("Romance Standard Time", "Europe/Paris");
/*   59 */     tempMap.put("Warsaw", "Europe/Warsaw");
/*   60 */     tempMap.put("Central Europe", "Europe/Prague");
/*   61 */     tempMap.put("Central Europe Standard Time", "Europe/Prague");
/*   62 */     tempMap.put("Prague Bratislava", "Europe/Prague");
/*   63 */     tempMap.put("W. Central Africa Standard Time", "Africa/Luanda");
/*   64 */     tempMap.put("FLE", "Europe/Helsinki");
/*   65 */     tempMap.put("FLE Standard Time", "Europe/Helsinki");
/*   66 */     tempMap.put("GFT", "Europe/Athens");
/*   67 */     tempMap.put("GFT Standard Time", "Europe/Athens");
/*   68 */     tempMap.put("GTB", "Europe/Athens");
/*   69 */     tempMap.put("GTB Standard Time", "Europe/Athens");
/*   70 */     tempMap.put("Israel", "Asia/Jerusalem");
/*   71 */     tempMap.put("Israel Standard Time", "Asia/Jerusalem");
/*   72 */     tempMap.put("Arab", "Asia/Riyadh");
/*   73 */     tempMap.put("Arab Standard Time", "Asia/Riyadh");
/*   74 */     tempMap.put("Arabic Standard Time", "Asia/Baghdad");
/*   75 */     tempMap.put("E. Africa", "Africa/Nairobi");
/*   76 */     tempMap.put("E. Africa Standard Time", "Africa/Nairobi");
/*   77 */     tempMap.put("Saudi Arabia", "Asia/Riyadh");
/*   78 */     tempMap.put("Saudi Arabia Standard Time", "Asia/Riyadh");
/*   79 */     tempMap.put("Iran", "Asia/Tehran");
/*   80 */     tempMap.put("Iran Standard Time", "Asia/Tehran");
/*   81 */     tempMap.put("Afghanistan", "Asia/Kabul");
/*   82 */     tempMap.put("Afghanistan Standard Time", "Asia/Kabul");
/*   83 */     tempMap.put("India", "Asia/Calcutta");
/*   84 */     tempMap.put("India Standard Time", "Asia/Calcutta");
/*   85 */     tempMap.put("Myanmar Standard Time", "Asia/Rangoon");
/*   86 */     tempMap.put("Nepal Standard Time", "Asia/Katmandu");
/*   87 */     tempMap.put("Sri Lanka", "Asia/Colombo");
/*   88 */     tempMap.put("Sri Lanka Standard Time", "Asia/Colombo");
/*   89 */     tempMap.put("Beijing", "Asia/Shanghai");
/*   90 */     tempMap.put("China", "Asia/Shanghai");
/*   91 */     tempMap.put("China Standard Time", "Asia/Shanghai");
/*   92 */     tempMap.put("AUS Central", "Australia/Darwin");
/*   93 */     tempMap.put("AUS Central Standard Time", "Australia/Darwin");
/*   94 */     tempMap.put("Cen. Australia", "Australia/Adelaide");
/*   95 */     tempMap.put("Cen. Australia Standard Time", "Australia/Adelaide");
/*   96 */     tempMap.put("Vladivostok", "Asia/Vladivostok");
/*   97 */     tempMap.put("Vladivostok Standard Time", "Asia/Vladivostok");
/*   98 */     tempMap.put("West Pacific", "Pacific/Guam");
/*   99 */     tempMap.put("West Pacific Standard Time", "Pacific/Guam");
/*  100 */     tempMap.put("E. South America", "America/Sao_Paulo");
/*  101 */     tempMap.put("E. South America Standard Time", "America/Sao_Paulo");
/*  102 */     tempMap.put("Greenland Standard Time", "America/Godthab");
/*  103 */     tempMap.put("Newfoundland", "America/St_Johns");
/*  104 */     tempMap.put("Newfoundland Standard Time", "America/St_Johns");
/*  105 */     tempMap.put("Pacific SA", "America/Caracas");
/*  106 */     tempMap.put("Pacific SA Standard Time", "America/Caracas");
/*  107 */     tempMap.put("SA Western", "America/Caracas");
/*  108 */     tempMap.put("SA Western Standard Time", "America/Caracas");
/*  109 */     tempMap.put("SA Pacific", "America/Bogota");
/*  110 */     tempMap.put("SA Pacific Standard Time", "America/Bogota");
/*  111 */     tempMap.put("US Eastern", "America/Indianapolis");
/*  112 */     tempMap.put("US Eastern Standard Time", "America/Indianapolis");
/*  113 */     tempMap.put("Central America Standard Time", "America/Regina");
/*  114 */     tempMap.put("Mexico", "America/Mexico_City");
/*  115 */     tempMap.put("Mexico Standard Time", "America/Mexico_City");
/*  116 */     tempMap.put("Canada Central", "America/Regina");
/*  117 */     tempMap.put("Canada Central Standard Time", "America/Regina");
/*  118 */     tempMap.put("US Mountain", "America/Phoenix");
/*  119 */     tempMap.put("US Mountain Standard Time", "America/Phoenix");
/*  120 */     tempMap.put("GMT", "GMT");
/*  121 */     tempMap.put("Ekaterinburg", "Asia/Yekaterinburg");
/*  122 */     tempMap.put("Ekaterinburg Standard Time", "Asia/Yekaterinburg");
/*  123 */     tempMap.put("West Asia", "Asia/Karachi");
/*  124 */     tempMap.put("West Asia Standard Time", "Asia/Karachi");
/*  125 */     tempMap.put("Central Asia", "Asia/Dhaka");
/*  126 */     tempMap.put("Central Asia Standard Time", "Asia/Dhaka");
/*  127 */     tempMap.put("N. Central Asia Standard Time", "Asia/Novosibirsk");
/*  128 */     tempMap.put("Bangkok", "Asia/Bangkok");
/*  129 */     tempMap.put("Bangkok Standard Time", "Asia/Bangkok");
/*  130 */     tempMap.put("North Asia Standard Time", "Asia/Krasnoyarsk");
/*  131 */     tempMap.put("SE Asia", "Asia/Bangkok");
/*  132 */     tempMap.put("SE Asia Standard Time", "Asia/Bangkok");
/*  133 */     tempMap.put("North Asia East Standard Time", "Asia/Ulaanbaatar");
/*  134 */     tempMap.put("Singapore", "Asia/Singapore");
/*  135 */     tempMap.put("Singapore Standard Time", "Asia/Singapore");
/*  136 */     tempMap.put("Taipei", "Asia/Taipei");
/*  137 */     tempMap.put("Taipei Standard Time", "Asia/Taipei");
/*  138 */     tempMap.put("W. Australia", "Australia/Perth");
/*  139 */     tempMap.put("W. Australia Standard Time", "Australia/Perth");
/*  140 */     tempMap.put("Korea", "Asia/Seoul");
/*  141 */     tempMap.put("Korea Standard Time", "Asia/Seoul");
/*  142 */     tempMap.put("Tokyo", "Asia/Tokyo");
/*  143 */     tempMap.put("Tokyo Standard Time", "Asia/Tokyo");
/*  144 */     tempMap.put("Yakutsk", "Asia/Yakutsk");
/*  145 */     tempMap.put("Yakutsk Standard Time", "Asia/Yakutsk");
/*  146 */     tempMap.put("Central European", "Europe/Belgrade");
/*  147 */     tempMap.put("Central European Standard Time", "Europe/Belgrade");
/*  148 */     tempMap.put("W. Europe", "Europe/Berlin");
/*  149 */     tempMap.put("W. Europe Standard Time", "Europe/Berlin");
/*  150 */     tempMap.put("Tasmania", "Australia/Hobart");
/*  151 */     tempMap.put("Tasmania Standard Time", "Australia/Hobart");
/*  152 */     tempMap.put("AUS Eastern", "Australia/Sydney");
/*  153 */     tempMap.put("AUS Eastern Standard Time", "Australia/Sydney");
/*  154 */     tempMap.put("E. Australia", "Australia/Brisbane");
/*  155 */     tempMap.put("E. Australia Standard Time", "Australia/Brisbane");
/*  156 */     tempMap.put("Sydney Standard Time", "Australia/Sydney");
/*  157 */     tempMap.put("Central Pacific", "Pacific/Guadalcanal");
/*  158 */     tempMap.put("Central Pacific Standard Time", "Pacific/Guadalcanal");
/*  159 */     tempMap.put("Dateline", "Pacific/Majuro");
/*  160 */     tempMap.put("Dateline Standard Time", "Pacific/Majuro");
/*  161 */     tempMap.put("Fiji", "Pacific/Fiji");
/*  162 */     tempMap.put("Fiji Standard Time", "Pacific/Fiji");
/*  163 */     tempMap.put("Samoa", "Pacific/Apia");
/*  164 */     tempMap.put("Samoa Standard Time", "Pacific/Apia");
/*  165 */     tempMap.put("Hawaiian", "Pacific/Honolulu");
/*  166 */     tempMap.put("Hawaiian Standard Time", "Pacific/Honolulu");
/*  167 */     tempMap.put("Alaskan", "America/Anchorage");
/*  168 */     tempMap.put("Alaskan Standard Time", "America/Anchorage");
/*  169 */     tempMap.put("Pacific", "America/Los_Angeles");
/*  170 */     tempMap.put("Pacific Standard Time", "America/Los_Angeles");
/*  171 */     tempMap.put("Mexico Standard Time 2", "America/Chihuahua");
/*  172 */     tempMap.put("Mountain", "America/Denver");
/*  173 */     tempMap.put("Mountain Standard Time", "America/Denver");
/*  174 */     tempMap.put("Central", "America/Chicago");
/*  175 */     tempMap.put("Central Standard Time", "America/Chicago");
/*  176 */     tempMap.put("Eastern", "America/New_York");
/*  177 */     tempMap.put("Eastern Standard Time", "America/New_York");
/*  178 */     tempMap.put("E. Europe", "Europe/Bucharest");
/*  179 */     tempMap.put("E. Europe Standard Time", "Europe/Bucharest");
/*  180 */     tempMap.put("Egypt", "Africa/Cairo");
/*  181 */     tempMap.put("Egypt Standard Time", "Africa/Cairo");
/*  182 */     tempMap.put("South Africa", "Africa/Harare");
/*  183 */     tempMap.put("South Africa Standard Time", "Africa/Harare");
/*  184 */     tempMap.put("Atlantic", "America/Halifax");
/*  185 */     tempMap.put("Atlantic Standard Time", "America/Halifax");
/*  186 */     tempMap.put("SA Eastern", "America/Buenos_Aires");
/*  187 */     tempMap.put("SA Eastern Standard Time", "America/Buenos_Aires");
/*  188 */     tempMap.put("Mid-Atlantic", "Atlantic/South_Georgia");
/*  189 */     tempMap.put("Mid-Atlantic Standard Time", "Atlantic/South_Georgia");
/*  190 */     tempMap.put("Azores", "Atlantic/Azores");
/*  191 */     tempMap.put("Azores Standard Time", "Atlantic/Azores");
/*  192 */     tempMap.put("Cape Verde Standard Time", "Atlantic/Cape_Verde");
/*  193 */     tempMap.put("Russian", "Europe/Moscow");
/*  194 */     tempMap.put("Russian Standard Time", "Europe/Moscow");
/*  195 */     tempMap.put("New Zealand", "Pacific/Auckland");
/*  196 */     tempMap.put("New Zealand Standard Time", "Pacific/Auckland");
/*  197 */     tempMap.put("Tonga Standard Time", "Pacific/Tongatapu");
/*  198 */     tempMap.put("Arabian", "Asia/Muscat");
/*  199 */     tempMap.put("Arabian Standard Time", "Asia/Muscat");
/*  200 */     tempMap.put("Caucasus", "Asia/Tbilisi");
/*  201 */     tempMap.put("Caucasus Standard Time", "Asia/Tbilisi");
/*  202 */     tempMap.put("GMT Standard Time", "GMT");
/*  203 */     tempMap.put("Greenwich", "GMT");
/*  204 */     tempMap.put("Greenwich Standard Time", "GMT");
/*  205 */     tempMap.put("UTC", "GMT");
/*      */ 
/*      */     
/*  208 */     Iterator entries = tempMap.entrySet().iterator();
/*  209 */     Map entryMap = new HashMap(tempMap.size());
/*      */     
/*  211 */     while (entries.hasNext()) {
/*  212 */       String name = ((Map.Entry)entries.next()).getValue().toString();
/*  213 */       entryMap.put(name, name);
/*      */     } 
/*      */     
/*  216 */     tempMap.putAll(entryMap);
/*      */     
/*  218 */     TIMEZONE_MAPPINGS = Collections.unmodifiableMap(tempMap);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  223 */     tempMap = new HashMap();
/*      */     
/*  225 */     tempMap.put("ACST", new String[] { "America/Porto_Acre" });
/*  226 */     tempMap.put("ACT", new String[] { "America/Porto_Acre" });
/*  227 */     tempMap.put("ADDT", new String[] { "America/Pangnirtung" });
/*  228 */     tempMap.put("ADMT", new String[] { "Africa/Asmera", "Africa/Addis_Ababa" });
/*      */     
/*  230 */     tempMap.put("ADT", new String[] { "Atlantic/Bermuda", "Asia/Baghdad", "America/Thule", "America/Goose_Bay", "America/Halifax", "America/Glace_Bay", "America/Pangnirtung", "America/Barbados", "America/Martinique" });
/*      */ 
/*      */ 
/*      */     
/*  234 */     tempMap.put("AFT", new String[] { "Asia/Kabul" });
/*  235 */     tempMap.put("AHDT", new String[] { "America/Anchorage" });
/*  236 */     tempMap.put("AHST", new String[] { "America/Anchorage" });
/*  237 */     tempMap.put("AHWT", new String[] { "America/Anchorage" });
/*  238 */     tempMap.put("AKDT", new String[] { "America/Juneau", "America/Yakutat", "America/Anchorage", "America/Nome" });
/*      */     
/*  240 */     tempMap.put("AKST", new String[] { "Asia/Aqtobe", "America/Juneau", "America/Yakutat", "America/Anchorage", "America/Nome" });
/*      */     
/*  242 */     tempMap.put("AKT", new String[] { "Asia/Aqtobe" });
/*  243 */     tempMap.put("AKTST", new String[] { "Asia/Aqtobe" });
/*  244 */     tempMap.put("AKWT", new String[] { "America/Juneau", "America/Yakutat", "America/Anchorage", "America/Nome" });
/*      */     
/*  246 */     tempMap.put("ALMST", new String[] { "Asia/Almaty" });
/*  247 */     tempMap.put("ALMT", new String[] { "Asia/Almaty" });
/*  248 */     tempMap.put("AMST", new String[] { "Asia/Yerevan", "America/Cuiaba", "America/Porto_Velho", "America/Boa_Vista", "America/Manaus" });
/*      */     
/*  250 */     tempMap.put("AMT", new String[] { "Europe/Athens", "Europe/Amsterdam", "Asia/Yerevan", "Africa/Asmera", "America/Cuiaba", "America/Porto_Velho", "America/Boa_Vista", "America/Manaus", "America/Asuncion" });
/*      */ 
/*      */ 
/*      */     
/*  254 */     tempMap.put("ANAMT", new String[] { "Asia/Anadyr" });
/*  255 */     tempMap.put("ANAST", new String[] { "Asia/Anadyr" });
/*  256 */     tempMap.put("ANAT", new String[] { "Asia/Anadyr" });
/*  257 */     tempMap.put("ANT", new String[] { "America/Aruba", "America/Curacao" });
/*  258 */     tempMap.put("AQTST", new String[] { "Asia/Aqtobe", "Asia/Aqtau" });
/*  259 */     tempMap.put("AQTT", new String[] { "Asia/Aqtobe", "Asia/Aqtau" });
/*  260 */     tempMap.put("ARST", new String[] { "Antarctica/Palmer", "America/Buenos_Aires", "America/Rosario", "America/Cordoba", "America/Jujuy", "America/Catamarca", "America/Mendoza" });
/*      */ 
/*      */     
/*  263 */     tempMap.put("ART", new String[] { "Antarctica/Palmer", "America/Buenos_Aires", "America/Rosario", "America/Cordoba", "America/Jujuy", "America/Catamarca", "America/Mendoza" });
/*      */ 
/*      */     
/*  266 */     tempMap.put("ASHST", new String[] { "Asia/Ashkhabad" });
/*  267 */     tempMap.put("ASHT", new String[] { "Asia/Ashkhabad" });
/*  268 */     tempMap.put("AST", new String[] { "Atlantic/Bermuda", "Asia/Bahrain", "Asia/Baghdad", "Asia/Kuwait", "Asia/Qatar", "Asia/Riyadh", "Asia/Aden", "America/Thule", "America/Goose_Bay", "America/Halifax", "America/Glace_Bay", "America/Pangnirtung", "America/Anguilla", "America/Antigua", "America/Barbados", "America/Dominica", "America/Santo_Domingo", "America/Grenada", "America/Guadeloupe", "America/Martinique", "America/Montserrat", "America/Puerto_Rico", "America/St_Kitts", "America/St_Lucia", "America/Miquelon", "America/St_Vincent", "America/Tortola", "America/St_Thomas", "America/Aruba", "America/Curacao", "America/Port_of_Spain" });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  279 */     tempMap.put("AWT", new String[] { "America/Puerto_Rico" });
/*  280 */     tempMap.put("AZOST", new String[] { "Atlantic/Azores" });
/*  281 */     tempMap.put("AZOT", new String[] { "Atlantic/Azores" });
/*  282 */     tempMap.put("AZST", new String[] { "Asia/Baku" });
/*  283 */     tempMap.put("AZT", new String[] { "Asia/Baku" });
/*  284 */     tempMap.put("BAKST", new String[] { "Asia/Baku" });
/*  285 */     tempMap.put("BAKT", new String[] { "Asia/Baku" });
/*  286 */     tempMap.put("BDT", new String[] { "Asia/Dacca", "America/Nome", "America/Adak" });
/*      */     
/*  288 */     tempMap.put("BEAT", new String[] { "Africa/Nairobi", "Africa/Mogadishu", "Africa/Kampala" });
/*      */     
/*  290 */     tempMap.put("BEAUT", new String[] { "Africa/Nairobi", "Africa/Dar_es_Salaam", "Africa/Kampala" });
/*      */     
/*  292 */     tempMap.put("BMT", new String[] { "Europe/Brussels", "Europe/Chisinau", "Europe/Tiraspol", "Europe/Bucharest", "Europe/Zurich", "Asia/Baghdad", "Asia/Bangkok", "Africa/Banjul", "America/Barbados", "America/Bogota" });
/*      */ 
/*      */ 
/*      */     
/*  296 */     tempMap.put("BNT", new String[] { "Asia/Brunei" });
/*  297 */     tempMap.put("BORT", new String[] { "Asia/Ujung_Pandang", "Asia/Kuching" });
/*      */     
/*  299 */     tempMap.put("BOST", new String[] { "America/La_Paz" });
/*  300 */     tempMap.put("BOT", new String[] { "America/La_Paz" });
/*  301 */     tempMap.put("BRST", new String[] { "America/Belem", "America/Fortaleza", "America/Araguaina", "America/Maceio", "America/Sao_Paulo" });
/*      */ 
/*      */     
/*  304 */     tempMap.put("BRT", new String[] { "America/Belem", "America/Fortaleza", "America/Araguaina", "America/Maceio", "America/Sao_Paulo" });
/*      */     
/*  306 */     tempMap.put("BST", new String[] { "Europe/London", "Europe/Belfast", "Europe/Dublin", "Europe/Gibraltar", "Pacific/Pago_Pago", "Pacific/Midway", "America/Nome", "America/Adak" });
/*      */ 
/*      */     
/*  309 */     tempMap.put("BTT", new String[] { "Asia/Thimbu" });
/*  310 */     tempMap.put("BURT", new String[] { "Asia/Dacca", "Asia/Rangoon", "Asia/Calcutta" });
/*      */     
/*  312 */     tempMap.put("BWT", new String[] { "America/Nome", "America/Adak" });
/*  313 */     tempMap.put("CANT", new String[] { "Atlantic/Canary" });
/*  314 */     tempMap.put("CAST", new String[] { "Africa/Gaborone", "Africa/Khartoum" });
/*      */     
/*  316 */     tempMap.put("CAT", new String[] { "Africa/Gaborone", "Africa/Bujumbura", "Africa/Lubumbashi", "Africa/Blantyre", "Africa/Maputo", "Africa/Windhoek", "Africa/Kigali", "Africa/Khartoum", "Africa/Lusaka", "Africa/Harare", "America/Anchorage" });
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  321 */     tempMap.put("CCT", new String[] { "Indian/Cocos" });
/*  322 */     tempMap.put("CDDT", new String[] { "America/Rankin_Inlet" });
/*  323 */     tempMap.put("CDT", new String[] { "Asia/Harbin", "Asia/Shanghai", "Asia/Chungking", "Asia/Urumqi", "Asia/Kashgar", "Asia/Taipei", "Asia/Macao", "America/Chicago", "America/Indianapolis", "America/Indiana/Marengo", "America/Indiana/Knox", "America/Indiana/Vevay", "America/Louisville", "America/Menominee", "America/Rainy_River", "America/Winnipeg", "America/Pangnirtung", "America/Iqaluit", "America/Rankin_Inlet", "America/Cambridge_Bay", "America/Cancun", "America/Mexico_City", "America/Chihuahua", "America/Belize", "America/Costa_Rica", "America/Havana", "America/El_Salvador", "America/Guatemala", "America/Tegucigalpa", "America/Managua" });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  335 */     tempMap.put("CEST", new String[] { "Europe/Tirane", "Europe/Andorra", "Europe/Vienna", "Europe/Minsk", "Europe/Brussels", "Europe/Sofia", "Europe/Prague", "Europe/Copenhagen", "Europe/Tallinn", "Europe/Berlin", "Europe/Gibraltar", "Europe/Athens", "Europe/Budapest", "Europe/Rome", "Europe/Riga", "Europe/Vaduz", "Europe/Vilnius", "Europe/Luxembourg", "Europe/Malta", "Europe/Chisinau", "Europe/Tiraspol", "Europe/Monaco", "Europe/Amsterdam", "Europe/Oslo", "Europe/Warsaw", "Europe/Lisbon", "Europe/Kaliningrad", "Europe/Madrid", "Europe/Stockholm", "Europe/Zurich", "Europe/Kiev", "Europe/Uzhgorod", "Europe/Zaporozhye", "Europe/Simferopol", "Europe/Belgrade", "Africa/Algiers", "Africa/Tripoli", "Africa/Tunis", "Africa/Ceuta" });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  349 */     tempMap.put("CET", new String[] { "Europe/Tirane", "Europe/Andorra", "Europe/Vienna", "Europe/Minsk", "Europe/Brussels", "Europe/Sofia", "Europe/Prague", "Europe/Copenhagen", "Europe/Tallinn", "Europe/Berlin", "Europe/Gibraltar", "Europe/Athens", "Europe/Budapest", "Europe/Rome", "Europe/Riga", "Europe/Vaduz", "Europe/Vilnius", "Europe/Luxembourg", "Europe/Malta", "Europe/Chisinau", "Europe/Tiraspol", "Europe/Monaco", "Europe/Amsterdam", "Europe/Oslo", "Europe/Warsaw", "Europe/Lisbon", "Europe/Kaliningrad", "Europe/Madrid", "Europe/Stockholm", "Europe/Zurich", "Europe/Kiev", "Europe/Uzhgorod", "Europe/Zaporozhye", "Europe/Simferopol", "Europe/Belgrade", "Africa/Algiers", "Africa/Tripoli", "Africa/Casablanca", "Africa/Tunis", "Africa/Ceuta" });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  363 */     tempMap.put("CGST", new String[] { "America/Scoresbysund" });
/*  364 */     tempMap.put("CGT", new String[] { "America/Scoresbysund" });
/*  365 */     tempMap.put("CHDT", new String[] { "America/Belize" });
/*  366 */     tempMap.put("CHUT", new String[] { "Asia/Chungking" });
/*  367 */     tempMap.put("CJT", new String[] { "Asia/Tokyo" });
/*  368 */     tempMap.put("CKHST", new String[] { "Pacific/Rarotonga" });
/*  369 */     tempMap.put("CKT", new String[] { "Pacific/Rarotonga" });
/*  370 */     tempMap.put("CLST", new String[] { "Antarctica/Palmer", "America/Santiago" });
/*      */     
/*  372 */     tempMap.put("CLT", new String[] { "Antarctica/Palmer", "America/Santiago" });
/*      */     
/*  374 */     tempMap.put("CMT", new String[] { "Europe/Copenhagen", "Europe/Chisinau", "Europe/Tiraspol", "America/St_Lucia", "America/Buenos_Aires", "America/Rosario", "America/Cordoba", "America/Jujuy", "America/Catamarca", "America/Mendoza", "America/Caracas" });
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  379 */     tempMap.put("COST", new String[] { "America/Bogota" });
/*  380 */     tempMap.put("COT", new String[] { "America/Bogota" });
/*  381 */     tempMap.put("CST", new String[] { "Asia/Harbin", "Asia/Shanghai", "Asia/Chungking", "Asia/Urumqi", "Asia/Kashgar", "Asia/Taipei", "Asia/Macao", "Asia/Jayapura", "Australia/Darwin", "Australia/Adelaide", "Australia/Broken_Hill", "America/Chicago", "America/Indianapolis", "America/Indiana/Marengo", "America/Indiana/Knox", "America/Indiana/Vevay", "America/Louisville", "America/Detroit", "America/Menominee", "America/Rainy_River", "America/Winnipeg", "America/Regina", "America/Swift_Current", "America/Pangnirtung", "America/Iqaluit", "America/Rankin_Inlet", "America/Cambridge_Bay", "America/Cancun", "America/Mexico_City", "America/Chihuahua", "America/Hermosillo", "America/Mazatlan", "America/Belize", "America/Costa_Rica", "America/Havana", "America/El_Salvador", "America/Guatemala", "America/Tegucigalpa", "America/Managua" });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  401 */     tempMap.put("CUT", new String[] { "Europe/Zaporozhye" });
/*  402 */     tempMap.put("CVST", new String[] { "Atlantic/Cape_Verde" });
/*  403 */     tempMap.put("CVT", new String[] { "Atlantic/Cape_Verde" });
/*  404 */     tempMap.put("CWT", new String[] { "America/Chicago", "America/Indianapolis", "America/Indiana/Marengo", "America/Indiana/Knox", "America/Indiana/Vevay", "America/Louisville", "America/Menominee" });
/*      */ 
/*      */ 
/*      */     
/*  408 */     tempMap.put("CXT", new String[] { "Indian/Christmas" });
/*  409 */     tempMap.put("DACT", new String[] { "Asia/Dacca" });
/*  410 */     tempMap.put("DAVT", new String[] { "Antarctica/Davis" });
/*  411 */     tempMap.put("DDUT", new String[] { "Antarctica/DumontDUrville" });
/*  412 */     tempMap.put("DFT", new String[] { "Europe/Oslo", "Europe/Paris" });
/*  413 */     tempMap.put("DMT", new String[] { "Europe/Belfast", "Europe/Dublin" });
/*  414 */     tempMap.put("DUSST", new String[] { "Asia/Dushanbe" });
/*  415 */     tempMap.put("DUST", new String[] { "Asia/Dushanbe" });
/*  416 */     tempMap.put("EASST", new String[] { "Pacific/Easter" });
/*  417 */     tempMap.put("EAST", new String[] { "Indian/Antananarivo", "Pacific/Easter" });
/*      */     
/*  419 */     tempMap.put("EAT", new String[] { "Indian/Comoro", "Indian/Antananarivo", "Indian/Mayotte", "Africa/Djibouti", "Africa/Asmera", "Africa/Addis_Ababa", "Africa/Nairobi", "Africa/Mogadishu", "Africa/Khartoum", "Africa/Dar_es_Salaam", "Africa/Kampala" });
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  424 */     tempMap.put("ECT", new String[] { "Pacific/Galapagos", "America/Guayaquil" });
/*      */     
/*  426 */     tempMap.put("EDDT", new String[] { "America/Iqaluit" });
/*  427 */     tempMap.put("EDT", new String[] { "America/New_York", "America/Indianapolis", "America/Indiana/Marengo", "America/Indiana/Vevay", "America/Louisville", "America/Detroit", "America/Montreal", "America/Thunder_Bay", "America/Nipigon", "America/Pangnirtung", "America/Iqaluit", "America/Cancun", "America/Nassau", "America/Santo_Domingo", "America/Port-au-Prince", "America/Jamaica", "America/Grand_Turk" });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  435 */     tempMap.put("EEMT", new String[] { "Europe/Minsk", "Europe/Chisinau", "Europe/Tiraspol", "Europe/Kaliningrad", "Europe/Moscow" });
/*      */     
/*  437 */     tempMap.put("EEST", new String[] { "Europe/Minsk", "Europe/Sofia", "Europe/Tallinn", "Europe/Helsinki", "Europe/Athens", "Europe/Riga", "Europe/Vilnius", "Europe/Chisinau", "Europe/Tiraspol", "Europe/Warsaw", "Europe/Bucharest", "Europe/Kaliningrad", "Europe/Moscow", "Europe/Istanbul", "Europe/Kiev", "Europe/Uzhgorod", "Europe/Zaporozhye", "Asia/Nicosia", "Asia/Amman", "Asia/Beirut", "Asia/Gaza", "Asia/Damascus", "Africa/Cairo" });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  445 */     tempMap.put("EET", new String[] { "Europe/Minsk", "Europe/Sofia", "Europe/Tallinn", "Europe/Helsinki", "Europe/Athens", "Europe/Riga", "Europe/Vilnius", "Europe/Chisinau", "Europe/Tiraspol", "Europe/Warsaw", "Europe/Bucharest", "Europe/Kaliningrad", "Europe/Moscow", "Europe/Istanbul", "Europe/Kiev", "Europe/Uzhgorod", "Europe/Zaporozhye", "Europe/Simferopol", "Asia/Nicosia", "Asia/Amman", "Asia/Beirut", "Asia/Gaza", "Asia/Damascus", "Africa/Cairo", "Africa/Tripoli" });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  454 */     tempMap.put("EGST", new String[] { "America/Scoresbysund" });
/*  455 */     tempMap.put("EGT", new String[] { "Atlantic/Jan_Mayen", "America/Scoresbysund" });
/*      */     
/*  457 */     tempMap.put("EHDT", new String[] { "America/Santo_Domingo" });
/*  458 */     tempMap.put("EST", new String[] { "Australia/Brisbane", "Australia/Lindeman", "Australia/Hobart", "Australia/Melbourne", "Australia/Sydney", "Australia/Broken_Hill", "Australia/Lord_Howe", "America/New_York", "America/Chicago", "America/Indianapolis", "America/Indiana/Marengo", "America/Indiana/Knox", "America/Indiana/Vevay", "America/Louisville", "America/Detroit", "America/Menominee", "America/Montreal", "America/Thunder_Bay", "America/Nipigon", "America/Pangnirtung", "America/Iqaluit", "America/Cancun", "America/Antigua", "America/Nassau", "America/Cayman", "America/Santo_Domingo", "America/Port-au-Prince", "America/Jamaica", "America/Managua", "America/Panama", "America/Grand_Turk" });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  472 */     tempMap.put("EWT", new String[] { "America/New_York", "America/Indianapolis", "America/Indiana/Marengo", "America/Indiana/Vevay", "America/Louisville", "America/Detroit", "America/Jamaica" });
/*      */ 
/*      */ 
/*      */     
/*  476 */     tempMap.put("FFMT", new String[] { "America/Martinique" });
/*  477 */     tempMap.put("FJST", new String[] { "Pacific/Fiji" });
/*  478 */     tempMap.put("FJT", new String[] { "Pacific/Fiji" });
/*  479 */     tempMap.put("FKST", new String[] { "Atlantic/Stanley" });
/*  480 */     tempMap.put("FKT", new String[] { "Atlantic/Stanley" });
/*  481 */     tempMap.put("FMT", new String[] { "Atlantic/Madeira", "Africa/Freetown" });
/*      */     
/*  483 */     tempMap.put("FNST", new String[] { "America/Noronha" });
/*  484 */     tempMap.put("FNT", new String[] { "America/Noronha" });
/*  485 */     tempMap.put("FRUST", new String[] { "Asia/Bishkek" });
/*  486 */     tempMap.put("FRUT", new String[] { "Asia/Bishkek" });
/*  487 */     tempMap.put("GALT", new String[] { "Pacific/Galapagos" });
/*  488 */     tempMap.put("GAMT", new String[] { "Pacific/Gambier" });
/*  489 */     tempMap.put("GBGT", new String[] { "America/Guyana" });
/*  490 */     tempMap.put("GEST", new String[] { "Asia/Tbilisi" });
/*  491 */     tempMap.put("GET", new String[] { "Asia/Tbilisi" });
/*  492 */     tempMap.put("GFT", new String[] { "America/Cayenne" });
/*  493 */     tempMap.put("GHST", new String[] { "Africa/Accra" });
/*  494 */     tempMap.put("GILT", new String[] { "Pacific/Tarawa" });
/*  495 */     tempMap.put("GMT", new String[] { "Atlantic/St_Helena", "Atlantic/Reykjavik", "Europe/London", "Europe/Belfast", "Europe/Dublin", "Europe/Gibraltar", "Africa/Porto-Novo", "Africa/Ouagadougou", "Africa/Abidjan", "Africa/Malabo", "Africa/Banjul", "Africa/Accra", "Africa/Conakry", "Africa/Bissau", "Africa/Monrovia", "Africa/Bamako", "Africa/Timbuktu", "Africa/Nouakchott", "Africa/Niamey", "Africa/Sao_Tome", "Africa/Dakar", "Africa/Freetown", "Africa/Lome" });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  504 */     tempMap.put("GST", new String[] { "Atlantic/South_Georgia", "Asia/Bahrain", "Asia/Muscat", "Asia/Qatar", "Asia/Dubai", "Pacific/Guam" });
/*      */ 
/*      */     
/*  507 */     tempMap.put("GYT", new String[] { "America/Guyana" });
/*  508 */     tempMap.put("HADT", new String[] { "America/Adak" });
/*  509 */     tempMap.put("HART", new String[] { "Asia/Harbin" });
/*  510 */     tempMap.put("HAST", new String[] { "America/Adak" });
/*  511 */     tempMap.put("HAWT", new String[] { "America/Adak" });
/*  512 */     tempMap.put("HDT", new String[] { "Pacific/Honolulu" });
/*  513 */     tempMap.put("HKST", new String[] { "Asia/Hong_Kong" });
/*  514 */     tempMap.put("HKT", new String[] { "Asia/Hong_Kong" });
/*  515 */     tempMap.put("HMT", new String[] { "Atlantic/Azores", "Europe/Helsinki", "Asia/Dacca", "Asia/Calcutta", "America/Havana" });
/*      */     
/*  517 */     tempMap.put("HOVST", new String[] { "Asia/Hovd" });
/*  518 */     tempMap.put("HOVT", new String[] { "Asia/Hovd" });
/*  519 */     tempMap.put("HST", new String[] { "Pacific/Johnston", "Pacific/Honolulu" });
/*      */     
/*  521 */     tempMap.put("HWT", new String[] { "Pacific/Honolulu" });
/*  522 */     tempMap.put("ICT", new String[] { "Asia/Phnom_Penh", "Asia/Vientiane", "Asia/Bangkok", "Asia/Saigon" });
/*      */     
/*  524 */     tempMap.put("IDDT", new String[] { "Asia/Jerusalem", "Asia/Gaza" });
/*  525 */     tempMap.put("IDT", new String[] { "Asia/Jerusalem", "Asia/Gaza" });
/*  526 */     tempMap.put("IHST", new String[] { "Asia/Colombo" });
/*  527 */     tempMap.put("IMT", new String[] { "Europe/Sofia", "Europe/Istanbul", "Asia/Irkutsk" });
/*      */     
/*  529 */     tempMap.put("IOT", new String[] { "Indian/Chagos" });
/*  530 */     tempMap.put("IRKMT", new String[] { "Asia/Irkutsk" });
/*  531 */     tempMap.put("IRKST", new String[] { "Asia/Irkutsk" });
/*  532 */     tempMap.put("IRKT", new String[] { "Asia/Irkutsk" });
/*  533 */     tempMap.put("IRST", new String[] { "Asia/Tehran" });
/*  534 */     tempMap.put("IRT", new String[] { "Asia/Tehran" });
/*  535 */     tempMap.put("ISST", new String[] { "Atlantic/Reykjavik" });
/*  536 */     tempMap.put("IST", new String[] { "Atlantic/Reykjavik", "Europe/Belfast", "Europe/Dublin", "Asia/Dacca", "Asia/Thimbu", "Asia/Calcutta", "Asia/Jerusalem", "Asia/Katmandu", "Asia/Karachi", "Asia/Gaza", "Asia/Colombo" });
/*      */ 
/*      */ 
/*      */     
/*  540 */     tempMap.put("JAYT", new String[] { "Asia/Jayapura" });
/*  541 */     tempMap.put("JMT", new String[] { "Atlantic/St_Helena", "Asia/Jerusalem" });
/*      */     
/*  543 */     tempMap.put("JST", new String[] { "Asia/Rangoon", "Asia/Dili", "Asia/Ujung_Pandang", "Asia/Tokyo", "Asia/Kuala_Lumpur", "Asia/Kuching", "Asia/Manila", "Asia/Singapore", "Pacific/Nauru" });
/*      */ 
/*      */ 
/*      */     
/*  547 */     tempMap.put("KART", new String[] { "Asia/Karachi" });
/*  548 */     tempMap.put("KAST", new String[] { "Asia/Kashgar" });
/*  549 */     tempMap.put("KDT", new String[] { "Asia/Seoul" });
/*  550 */     tempMap.put("KGST", new String[] { "Asia/Bishkek" });
/*  551 */     tempMap.put("KGT", new String[] { "Asia/Bishkek" });
/*  552 */     tempMap.put("KMT", new String[] { "Europe/Vilnius", "Europe/Kiev", "America/Cayman", "America/Jamaica", "America/St_Vincent", "America/Grand_Turk" });
/*      */ 
/*      */     
/*  555 */     tempMap.put("KOST", new String[] { "Pacific/Kosrae" });
/*  556 */     tempMap.put("KRAMT", new String[] { "Asia/Krasnoyarsk" });
/*  557 */     tempMap.put("KRAST", new String[] { "Asia/Krasnoyarsk" });
/*  558 */     tempMap.put("KRAT", new String[] { "Asia/Krasnoyarsk" });
/*  559 */     tempMap.put("KST", new String[] { "Asia/Seoul", "Asia/Pyongyang" });
/*  560 */     tempMap.put("KUYMT", new String[] { "Europe/Samara" });
/*  561 */     tempMap.put("KUYST", new String[] { "Europe/Samara" });
/*  562 */     tempMap.put("KUYT", new String[] { "Europe/Samara" });
/*  563 */     tempMap.put("KWAT", new String[] { "Pacific/Kwajalein" });
/*  564 */     tempMap.put("LHST", new String[] { "Australia/Lord_Howe" });
/*  565 */     tempMap.put("LINT", new String[] { "Pacific/Kiritimati" });
/*  566 */     tempMap.put("LKT", new String[] { "Asia/Colombo" });
/*  567 */     tempMap.put("LPMT", new String[] { "America/La_Paz" });
/*  568 */     tempMap.put("LRT", new String[] { "Africa/Monrovia" });
/*  569 */     tempMap.put("LST", new String[] { "Europe/Riga" });
/*  570 */     tempMap.put("M", new String[] { "Europe/Moscow" });
/*  571 */     tempMap.put("MADST", new String[] { "Atlantic/Madeira" });
/*  572 */     tempMap.put("MAGMT", new String[] { "Asia/Magadan" });
/*  573 */     tempMap.put("MAGST", new String[] { "Asia/Magadan" });
/*  574 */     tempMap.put("MAGT", new String[] { "Asia/Magadan" });
/*  575 */     tempMap.put("MALT", new String[] { "Asia/Kuala_Lumpur", "Asia/Singapore" });
/*      */     
/*  577 */     tempMap.put("MART", new String[] { "Pacific/Marquesas" });
/*  578 */     tempMap.put("MAWT", new String[] { "Antarctica/Mawson" });
/*  579 */     tempMap.put("MDDT", new String[] { "America/Cambridge_Bay", "America/Yellowknife", "America/Inuvik" });
/*      */     
/*  581 */     tempMap.put("MDST", new String[] { "Europe/Moscow" });
/*  582 */     tempMap.put("MDT", new String[] { "America/Denver", "America/Phoenix", "America/Boise", "America/Regina", "America/Swift_Current", "America/Edmonton", "America/Cambridge_Bay", "America/Yellowknife", "America/Inuvik", "America/Chihuahua", "America/Hermosillo", "America/Mazatlan" });
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  587 */     tempMap.put("MET", new String[] { "Europe/Tirane", "Europe/Andorra", "Europe/Vienna", "Europe/Minsk", "Europe/Brussels", "Europe/Sofia", "Europe/Prague", "Europe/Copenhagen", "Europe/Tallinn", "Europe/Berlin", "Europe/Gibraltar", "Europe/Athens", "Europe/Budapest", "Europe/Rome", "Europe/Riga", "Europe/Vaduz", "Europe/Vilnius", "Europe/Luxembourg", "Europe/Malta", "Europe/Chisinau", "Europe/Tiraspol", "Europe/Monaco", "Europe/Amsterdam", "Europe/Oslo", "Europe/Warsaw", "Europe/Lisbon", "Europe/Kaliningrad", "Europe/Madrid", "Europe/Stockholm", "Europe/Zurich", "Europe/Kiev", "Europe/Uzhgorod", "Europe/Zaporozhye", "Europe/Simferopol", "Europe/Belgrade", "Africa/Algiers", "Africa/Tripoli", "Africa/Casablanca", "Africa/Tunis", "Africa/Ceuta" });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  601 */     tempMap.put("MHT", new String[] { "Pacific/Majuro", "Pacific/Kwajalein" });
/*      */     
/*  603 */     tempMap.put("MMT", new String[] { "Indian/Maldives", "Europe/Minsk", "Europe/Moscow", "Asia/Rangoon", "Asia/Ujung_Pandang", "Asia/Colombo", "Pacific/Easter", "Africa/Monrovia", "America/Managua", "America/Montevideo" });
/*      */ 
/*      */ 
/*      */     
/*  607 */     tempMap.put("MOST", new String[] { "Asia/Macao" });
/*  608 */     tempMap.put("MOT", new String[] { "Asia/Macao" });
/*  609 */     tempMap.put("MPT", new String[] { "Pacific/Saipan" });
/*  610 */     tempMap.put("MSK", new String[] { "Europe/Minsk", "Europe/Tallinn", "Europe/Riga", "Europe/Vilnius", "Europe/Chisinau", "Europe/Kiev", "Europe/Uzhgorod", "Europe/Zaporozhye", "Europe/Simferopol" });
/*      */ 
/*      */ 
/*      */     
/*  614 */     tempMap.put("MST", new String[] { "Europe/Moscow", "America/Denver", "America/Phoenix", "America/Boise", "America/Regina", "America/Swift_Current", "America/Edmonton", "America/Dawson_Creek", "America/Cambridge_Bay", "America/Yellowknife", "America/Inuvik", "America/Mexico_City", "America/Chihuahua", "America/Hermosillo", "America/Mazatlan", "America/Tijuana" });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  621 */     tempMap.put("MUT", new String[] { "Indian/Mauritius" });
/*  622 */     tempMap.put("MVT", new String[] { "Indian/Maldives" });
/*  623 */     tempMap.put("MWT", new String[] { "America/Denver", "America/Phoenix", "America/Boise" });
/*      */     
/*  625 */     tempMap.put("MYT", new String[] { "Asia/Kuala_Lumpur", "Asia/Kuching" });
/*      */ 
/*      */     
/*  628 */     tempMap.put("NCST", new String[] { "Pacific/Noumea" });
/*  629 */     tempMap.put("NCT", new String[] { "Pacific/Noumea" });
/*  630 */     tempMap.put("NDT", new String[] { "America/Nome", "America/Adak", "America/St_Johns", "America/Goose_Bay" });
/*      */     
/*  632 */     tempMap.put("NEGT", new String[] { "America/Paramaribo" });
/*  633 */     tempMap.put("NFT", new String[] { "Europe/Paris", "Europe/Oslo", "Pacific/Norfolk" });
/*      */     
/*  635 */     tempMap.put("NMT", new String[] { "Pacific/Norfolk" });
/*  636 */     tempMap.put("NOVMT", new String[] { "Asia/Novosibirsk" });
/*  637 */     tempMap.put("NOVST", new String[] { "Asia/Novosibirsk" });
/*  638 */     tempMap.put("NOVT", new String[] { "Asia/Novosibirsk" });
/*  639 */     tempMap.put("NPT", new String[] { "Asia/Katmandu" });
/*  640 */     tempMap.put("NRT", new String[] { "Pacific/Nauru" });
/*  641 */     tempMap.put("NST", new String[] { "Europe/Amsterdam", "Pacific/Pago_Pago", "Pacific/Midway", "America/Nome", "America/Adak", "America/St_Johns", "America/Goose_Bay" });
/*      */ 
/*      */     
/*  644 */     tempMap.put("NUT", new String[] { "Pacific/Niue" });
/*  645 */     tempMap.put("NWT", new String[] { "America/Nome", "America/Adak" });
/*  646 */     tempMap.put("NZDT", new String[] { "Antarctica/McMurdo" });
/*  647 */     tempMap.put("NZHDT", new String[] { "Pacific/Auckland" });
/*  648 */     tempMap.put("NZST", new String[] { "Antarctica/McMurdo", "Pacific/Auckland" });
/*      */     
/*  650 */     tempMap.put("OMSMT", new String[] { "Asia/Omsk" });
/*  651 */     tempMap.put("OMSST", new String[] { "Asia/Omsk" });
/*  652 */     tempMap.put("OMST", new String[] { "Asia/Omsk" });
/*  653 */     tempMap.put("PDDT", new String[] { "America/Inuvik", "America/Whitehorse", "America/Dawson" });
/*      */     
/*  655 */     tempMap.put("PDT", new String[] { "America/Los_Angeles", "America/Juneau", "America/Boise", "America/Vancouver", "America/Dawson_Creek", "America/Inuvik", "America/Whitehorse", "America/Dawson", "America/Tijuana" });
/*      */ 
/*      */ 
/*      */     
/*  659 */     tempMap.put("PEST", new String[] { "America/Lima" });
/*  660 */     tempMap.put("PET", new String[] { "America/Lima" });
/*  661 */     tempMap.put("PETMT", new String[] { "Asia/Kamchatka" });
/*  662 */     tempMap.put("PETST", new String[] { "Asia/Kamchatka" });
/*  663 */     tempMap.put("PETT", new String[] { "Asia/Kamchatka" });
/*  664 */     tempMap.put("PGT", new String[] { "Pacific/Port_Moresby" });
/*  665 */     tempMap.put("PHOT", new String[] { "Pacific/Enderbury" });
/*  666 */     tempMap.put("PHST", new String[] { "Asia/Manila" });
/*  667 */     tempMap.put("PHT", new String[] { "Asia/Manila" });
/*  668 */     tempMap.put("PKT", new String[] { "Asia/Karachi" });
/*  669 */     tempMap.put("PMDT", new String[] { "America/Miquelon" });
/*  670 */     tempMap.put("PMMT", new String[] { "Pacific/Port_Moresby" });
/*  671 */     tempMap.put("PMST", new String[] { "America/Miquelon" });
/*  672 */     tempMap.put("PMT", new String[] { "Antarctica/DumontDUrville", "Europe/Prague", "Europe/Paris", "Europe/Monaco", "Africa/Algiers", "Africa/Tunis", "America/Panama", "America/Paramaribo" });
/*      */ 
/*      */ 
/*      */     
/*  676 */     tempMap.put("PNT", new String[] { "Pacific/Pitcairn" });
/*  677 */     tempMap.put("PONT", new String[] { "Pacific/Ponape" });
/*  678 */     tempMap.put("PPMT", new String[] { "America/Port-au-Prince" });
/*  679 */     tempMap.put("PST", new String[] { "Pacific/Pitcairn", "America/Los_Angeles", "America/Juneau", "America/Boise", "America/Vancouver", "America/Dawson_Creek", "America/Inuvik", "America/Whitehorse", "America/Dawson", "America/Hermosillo", "America/Mazatlan", "America/Tijuana" });
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  684 */     tempMap.put("PWT", new String[] { "Pacific/Palau", "America/Los_Angeles", "America/Juneau", "America/Boise", "America/Tijuana" });
/*      */ 
/*      */     
/*  687 */     tempMap.put("PYST", new String[] { "America/Asuncion" });
/*  688 */     tempMap.put("PYT", new String[] { "America/Asuncion" });
/*  689 */     tempMap.put("QMT", new String[] { "America/Guayaquil" });
/*  690 */     tempMap.put("RET", new String[] { "Indian/Reunion" });
/*  691 */     tempMap.put("RMT", new String[] { "Atlantic/Reykjavik", "Europe/Rome", "Europe/Riga", "Asia/Rangoon" });
/*      */     
/*  693 */     tempMap.put("S", new String[] { "Europe/Moscow" });
/*  694 */     tempMap.put("SAMMT", new String[] { "Europe/Samara" });
/*  695 */     tempMap.put("SAMST", new String[] { "Europe/Samara", "Asia/Samarkand" });
/*      */ 
/*      */     
/*  698 */     tempMap.put("SAMT", new String[] { "Europe/Samara", "Asia/Samarkand", "Pacific/Pago_Pago", "Pacific/Apia" });
/*      */     
/*  700 */     tempMap.put("SAST", new String[] { "Africa/Maseru", "Africa/Windhoek", "Africa/Johannesburg", "Africa/Mbabane" });
/*      */     
/*  702 */     tempMap.put("SBT", new String[] { "Pacific/Guadalcanal" });
/*  703 */     tempMap.put("SCT", new String[] { "Indian/Mahe" });
/*  704 */     tempMap.put("SDMT", new String[] { "America/Santo_Domingo" });
/*  705 */     tempMap.put("SGT", new String[] { "Asia/Singapore" });
/*  706 */     tempMap.put("SHEST", new String[] { "Asia/Aqtau" });
/*  707 */     tempMap.put("SHET", new String[] { "Asia/Aqtau" });
/*  708 */     tempMap.put("SJMT", new String[] { "America/Costa_Rica" });
/*  709 */     tempMap.put("SLST", new String[] { "Africa/Freetown" });
/*  710 */     tempMap.put("SMT", new String[] { "Atlantic/Stanley", "Europe/Stockholm", "Europe/Simferopol", "Asia/Phnom_Penh", "Asia/Vientiane", "Asia/Kuala_Lumpur", "Asia/Singapore", "Asia/Saigon", "America/Santiago" });
/*      */ 
/*      */ 
/*      */     
/*  714 */     tempMap.put("SRT", new String[] { "America/Paramaribo" });
/*  715 */     tempMap.put("SST", new String[] { "Pacific/Pago_Pago", "Pacific/Midway" });
/*      */     
/*  717 */     tempMap.put("SVEMT", new String[] { "Asia/Yekaterinburg" });
/*  718 */     tempMap.put("SVEST", new String[] { "Asia/Yekaterinburg" });
/*  719 */     tempMap.put("SVET", new String[] { "Asia/Yekaterinburg" });
/*  720 */     tempMap.put("SWAT", new String[] { "Africa/Windhoek" });
/*  721 */     tempMap.put("SYOT", new String[] { "Antarctica/Syowa" });
/*  722 */     tempMap.put("TAHT", new String[] { "Pacific/Tahiti" });
/*  723 */     tempMap.put("TASST", new String[] { "Asia/Samarkand", "Asia/Tashkent" });
/*      */ 
/*      */     
/*  726 */     tempMap.put("TAST", new String[] { "Asia/Samarkand", "Asia/Tashkent" });
/*  727 */     tempMap.put("TBIST", new String[] { "Asia/Tbilisi" });
/*  728 */     tempMap.put("TBIT", new String[] { "Asia/Tbilisi" });
/*  729 */     tempMap.put("TBMT", new String[] { "Asia/Tbilisi" });
/*  730 */     tempMap.put("TFT", new String[] { "Indian/Kerguelen" });
/*  731 */     tempMap.put("TJT", new String[] { "Asia/Dushanbe" });
/*  732 */     tempMap.put("TKT", new String[] { "Pacific/Fakaofo" });
/*  733 */     tempMap.put("TMST", new String[] { "Asia/Ashkhabad" });
/*  734 */     tempMap.put("TMT", new String[] { "Europe/Tallinn", "Asia/Tehran", "Asia/Ashkhabad" });
/*      */     
/*  736 */     tempMap.put("TOST", new String[] { "Pacific/Tongatapu" });
/*  737 */     tempMap.put("TOT", new String[] { "Pacific/Tongatapu" });
/*  738 */     tempMap.put("TPT", new String[] { "Asia/Dili" });
/*  739 */     tempMap.put("TRST", new String[] { "Europe/Istanbul" });
/*  740 */     tempMap.put("TRT", new String[] { "Europe/Istanbul" });
/*  741 */     tempMap.put("TRUT", new String[] { "Pacific/Truk" });
/*  742 */     tempMap.put("TVT", new String[] { "Pacific/Funafuti" });
/*  743 */     tempMap.put("ULAST", new String[] { "Asia/Ulaanbaatar" });
/*  744 */     tempMap.put("ULAT", new String[] { "Asia/Ulaanbaatar" });
/*  745 */     tempMap.put("URUT", new String[] { "Asia/Urumqi" });
/*  746 */     tempMap.put("UYHST", new String[] { "America/Montevideo" });
/*  747 */     tempMap.put("UYT", new String[] { "America/Montevideo" });
/*  748 */     tempMap.put("UZST", new String[] { "Asia/Samarkand", "Asia/Tashkent" });
/*  749 */     tempMap.put("UZT", new String[] { "Asia/Samarkand", "Asia/Tashkent" });
/*  750 */     tempMap.put("VET", new String[] { "America/Caracas" });
/*  751 */     tempMap.put("VLAMT", new String[] { "Asia/Vladivostok" });
/*  752 */     tempMap.put("VLAST", new String[] { "Asia/Vladivostok" });
/*  753 */     tempMap.put("VLAT", new String[] { "Asia/Vladivostok" });
/*  754 */     tempMap.put("VUST", new String[] { "Pacific/Efate" });
/*  755 */     tempMap.put("VUT", new String[] { "Pacific/Efate" });
/*  756 */     tempMap.put("WAKT", new String[] { "Pacific/Wake" });
/*  757 */     tempMap.put("WARST", new String[] { "America/Jujuy", "America/Mendoza" });
/*      */     
/*  759 */     tempMap.put("WART", new String[] { "America/Jujuy", "America/Mendoza" });
/*      */ 
/*      */     
/*  762 */     tempMap.put("WAST", new String[] { "Africa/Ndjamena", "Africa/Windhoek" });
/*      */     
/*  764 */     tempMap.put("WAT", new String[] { "Africa/Luanda", "Africa/Porto-Novo", "Africa/Douala", "Africa/Bangui", "Africa/Ndjamena", "Africa/Kinshasa", "Africa/Brazzaville", "Africa/Malabo", "Africa/Libreville", "Africa/Banjul", "Africa/Conakry", "Africa/Bissau", "Africa/Bamako", "Africa/Nouakchott", "Africa/El_Aaiun", "Africa/Windhoek", "Africa/Niamey", "Africa/Lagos", "Africa/Dakar", "Africa/Freetown" });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  771 */     tempMap.put("WEST", new String[] { "Atlantic/Faeroe", "Atlantic/Azores", "Atlantic/Madeira", "Atlantic/Canary", "Europe/Brussels", "Europe/Luxembourg", "Europe/Monaco", "Europe/Lisbon", "Europe/Madrid", "Africa/Algiers", "Africa/Casablanca", "Africa/Ceuta" });
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  776 */     tempMap.put("WET", new String[] { "Atlantic/Faeroe", "Atlantic/Azores", "Atlantic/Madeira", "Atlantic/Canary", "Europe/Andorra", "Europe/Brussels", "Europe/Luxembourg", "Europe/Monaco", "Europe/Lisbon", "Europe/Madrid", "Africa/Algiers", "Africa/Casablanca", "Africa/El_Aaiun", "Africa/Ceuta" });
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  781 */     tempMap.put("WFT", new String[] { "Pacific/Wallis" });
/*  782 */     tempMap.put("WGST", new String[] { "America/Godthab" });
/*  783 */     tempMap.put("WGT", new String[] { "America/Godthab" });
/*  784 */     tempMap.put("WMT", new String[] { "Europe/Vilnius", "Europe/Warsaw" });
/*  785 */     tempMap.put("WST", new String[] { "Antarctica/Casey", "Pacific/Apia", "Australia/Perth" });
/*      */     
/*  787 */     tempMap.put("YAKMT", new String[] { "Asia/Yakutsk" });
/*  788 */     tempMap.put("YAKST", new String[] { "Asia/Yakutsk" });
/*  789 */     tempMap.put("YAKT", new String[] { "Asia/Yakutsk" });
/*  790 */     tempMap.put("YAPT", new String[] { "Pacific/Yap" });
/*  791 */     tempMap.put("YDDT", new String[] { "America/Whitehorse", "America/Dawson" });
/*      */     
/*  793 */     tempMap.put("YDT", new String[] { "America/Yakutat", "America/Whitehorse", "America/Dawson" });
/*      */     
/*  795 */     tempMap.put("YEKMT", new String[] { "Asia/Yekaterinburg" });
/*  796 */     tempMap.put("YEKST", new String[] { "Asia/Yekaterinburg" });
/*  797 */     tempMap.put("YEKT", new String[] { "Asia/Yekaterinburg" });
/*  798 */     tempMap.put("YERST", new String[] { "Asia/Yerevan" });
/*  799 */     tempMap.put("YERT", new String[] { "Asia/Yerevan" });
/*  800 */     tempMap.put("YST", new String[] { "America/Yakutat", "America/Whitehorse", "America/Dawson" });
/*      */     
/*  802 */     tempMap.put("YWT", new String[] { "America/Yakutat" });
/*      */     
/*  804 */     ABBREVIATED_TIMEZONES = Collections.unmodifiableMap(tempMap);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Time changeTimezone(ConnectionImpl conn, Calendar sessionCalendar, Calendar targetCalendar, Time t, TimeZone fromTz, TimeZone toTz, boolean rollForward) {
/*  828 */     if (conn != null) {
/*  829 */       if (conn.getUseTimezone() && !conn.getNoTimezoneConversionForTimeType()) {
/*      */ 
/*      */         
/*  832 */         Calendar fromCal = Calendar.getInstance(fromTz);
/*  833 */         fromCal.setTime(t);
/*      */         
/*  835 */         int fromOffset = fromCal.get(15) + fromCal.get(16);
/*      */         
/*  837 */         Calendar toCal = Calendar.getInstance(toTz);
/*  838 */         toCal.setTime(t);
/*      */         
/*  840 */         int toOffset = toCal.get(15) + toCal.get(16);
/*      */         
/*  842 */         int offsetDiff = fromOffset - toOffset;
/*  843 */         long toTime = toCal.getTime().getTime();
/*      */         
/*  845 */         if (rollForward || (conn.isServerTzUTC() && !conn.isClientTzUTC())) {
/*  846 */           toTime += offsetDiff;
/*      */         } else {
/*  848 */           toTime -= offsetDiff;
/*      */         } 
/*      */         
/*  851 */         Time changedTime = new Time(toTime);
/*      */         
/*  853 */         return changedTime;
/*  854 */       }  if (conn.getUseJDBCCompliantTimezoneShift() && 
/*  855 */         targetCalendar != null) {
/*      */         
/*  857 */         Time adjustedTime = new Time(jdbcCompliantZoneShift(sessionCalendar, targetCalendar, t));
/*      */ 
/*      */ 
/*      */         
/*  861 */         return adjustedTime;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  866 */     return t;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Timestamp changeTimezone(ConnectionImpl conn, Calendar sessionCalendar, Calendar targetCalendar, Timestamp tstamp, TimeZone fromTz, TimeZone toTz, boolean rollForward) {
/*  890 */     if (conn != null) {
/*  891 */       if (conn.getUseTimezone()) {
/*      */         
/*  893 */         Calendar fromCal = Calendar.getInstance(fromTz);
/*  894 */         fromCal.setTime(tstamp);
/*      */         
/*  896 */         int fromOffset = fromCal.get(15) + fromCal.get(16);
/*      */         
/*  898 */         Calendar toCal = Calendar.getInstance(toTz);
/*  899 */         toCal.setTime(tstamp);
/*      */         
/*  901 */         int toOffset = toCal.get(15) + toCal.get(16);
/*      */         
/*  903 */         int offsetDiff = fromOffset - toOffset;
/*  904 */         long toTime = toCal.getTime().getTime();
/*      */         
/*  906 */         if (rollForward || (conn.isServerTzUTC() && !conn.isClientTzUTC())) {
/*  907 */           toTime += offsetDiff;
/*      */         } else {
/*  909 */           toTime -= offsetDiff;
/*      */         } 
/*      */         
/*  912 */         Timestamp changedTimestamp = new Timestamp(toTime);
/*      */         
/*  914 */         return changedTimestamp;
/*  915 */       }  if (conn.getUseJDBCCompliantTimezoneShift() && 
/*  916 */         targetCalendar != null) {
/*      */         
/*  918 */         Timestamp adjustedTimestamp = new Timestamp(jdbcCompliantZoneShift(sessionCalendar, targetCalendar, tstamp));
/*      */ 
/*      */ 
/*      */         
/*  922 */         adjustedTimestamp.setNanos(tstamp.getNanos());
/*      */         
/*  924 */         return adjustedTimestamp;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  929 */     return tstamp;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static long jdbcCompliantZoneShift(Calendar sessionCalendar, Calendar targetCalendar, Date dt) {
/*  935 */     if (sessionCalendar == null) {
/*  936 */       sessionCalendar = new GregorianCalendar();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  943 */     Date origCalDate = targetCalendar.getTime();
/*  944 */     Date origSessionDate = sessionCalendar.getTime();
/*      */     
/*      */     try {
/*  947 */       sessionCalendar.setTime(dt);
/*      */       
/*  949 */       targetCalendar.set(1, sessionCalendar.get(1));
/*  950 */       targetCalendar.set(2, sessionCalendar.get(2));
/*  951 */       targetCalendar.set(5, sessionCalendar.get(5));
/*      */       
/*  953 */       targetCalendar.set(11, sessionCalendar.get(11));
/*  954 */       targetCalendar.set(12, sessionCalendar.get(12));
/*  955 */       targetCalendar.set(13, sessionCalendar.get(13));
/*  956 */       targetCalendar.set(14, sessionCalendar.get(14));
/*      */       
/*  958 */       return targetCalendar.getTime().getTime();
/*      */     } finally {
/*      */       
/*  961 */       sessionCalendar.setTime(origSessionDate);
/*  962 */       targetCalendar.setTime(origCalDate);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final Date fastDateCreate(boolean useGmtConversion, Calendar gmtCalIfNeeded, Calendar cal, int year, int month, int day) {
/*  974 */     Calendar dateCal = cal;
/*      */     
/*  976 */     if (useGmtConversion) {
/*      */       
/*  978 */       if (gmtCalIfNeeded == null) {
/*  979 */         gmtCalIfNeeded = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
/*      */       }
/*  981 */       gmtCalIfNeeded.clear();
/*      */       
/*  983 */       dateCal = gmtCalIfNeeded;
/*      */     } 
/*      */     
/*  986 */     dateCal.clear();
/*  987 */     dateCal.set(14, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  992 */     dateCal.set(year, month - 1, day, 0, 0, 0);
/*      */     
/*  994 */     long dateAsMillis = 0L;
/*      */     
/*      */     try {
/*  997 */       dateAsMillis = dateCal.getTimeInMillis();
/*  998 */     } catch (IllegalAccessError iae) {
/*      */       
/* 1000 */       dateAsMillis = dateCal.getTime().getTime();
/*      */     } 
/*      */     
/* 1003 */     return new Date(dateAsMillis);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static final Date fastDateCreate(int year, int month, int day, Calendar targetCalendar) {
/* 1009 */     Calendar dateCal = (targetCalendar == null) ? new GregorianCalendar() : targetCalendar;
/*      */     
/* 1011 */     dateCal.clear();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1017 */     dateCal.set(year, month - 1, day, 0, 0, 0);
/* 1018 */     dateCal.set(14, 0);
/*      */     
/* 1020 */     long dateAsMillis = 0L;
/*      */     
/*      */     try {
/* 1023 */       dateAsMillis = dateCal.getTimeInMillis();
/* 1024 */     } catch (IllegalAccessError iae) {
/*      */       
/* 1026 */       dateAsMillis = dateCal.getTime().getTime();
/*      */     } 
/*      */     
/* 1029 */     return new Date(dateAsMillis);
/*      */   }
/*      */ 
/*      */   
/*      */   static final Time fastTimeCreate(Calendar cal, int hour, int minute, int second, ExceptionInterceptor exceptionInterceptor) throws SQLException {
/* 1034 */     if (hour < 0 || hour > 24) {
/* 1035 */       throw SQLError.createSQLException("Illegal hour value '" + hour + "' for java.sql.Time type in value '" + timeFormattedString(hour, minute, second) + ".", "S1009", exceptionInterceptor);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1040 */     if (minute < 0 || minute > 59) {
/* 1041 */       throw SQLError.createSQLException("Illegal minute value '" + minute + "'" + "' for java.sql.Time type in value '" + timeFormattedString(hour, minute, second) + ".", "S1009", exceptionInterceptor);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1046 */     if (second < 0 || second > 59) {
/* 1047 */       throw SQLError.createSQLException("Illegal minute value '" + second + "'" + "' for java.sql.Time type in value '" + timeFormattedString(hour, minute, second) + ".", "S1009", exceptionInterceptor);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1052 */     cal.clear();
/*      */ 
/*      */     
/* 1055 */     cal.set(1970, 0, 1, hour, minute, second);
/*      */     
/* 1057 */     long timeAsMillis = 0L;
/*      */     
/*      */     try {
/* 1060 */       timeAsMillis = cal.getTimeInMillis();
/* 1061 */     } catch (IllegalAccessError iae) {
/*      */       
/* 1063 */       timeAsMillis = cal.getTime().getTime();
/*      */     } 
/*      */     
/* 1066 */     return new Time(timeAsMillis);
/*      */   }
/*      */ 
/*      */   
/*      */   static final Time fastTimeCreate(int hour, int minute, int second, Calendar targetCalendar, ExceptionInterceptor exceptionInterceptor) throws SQLException {
/* 1071 */     if (hour < 0 || hour > 23) {
/* 1072 */       throw SQLError.createSQLException("Illegal hour value '" + hour + "' for java.sql.Time type in value '" + timeFormattedString(hour, minute, second) + ".", "S1009", exceptionInterceptor);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1077 */     if (minute < 0 || minute > 59) {
/* 1078 */       throw SQLError.createSQLException("Illegal minute value '" + minute + "'" + "' for java.sql.Time type in value '" + timeFormattedString(hour, minute, second) + ".", "S1009", exceptionInterceptor);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1083 */     if (second < 0 || second > 59) {
/* 1084 */       throw SQLError.createSQLException("Illegal minute value '" + second + "'" + "' for java.sql.Time type in value '" + timeFormattedString(hour, minute, second) + ".", "S1009", exceptionInterceptor);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1089 */     Calendar cal = (targetCalendar == null) ? new GregorianCalendar() : targetCalendar;
/* 1090 */     cal.clear();
/*      */ 
/*      */     
/* 1093 */     cal.set(1970, 0, 1, hour, minute, second);
/*      */     
/* 1095 */     long timeAsMillis = 0L;
/*      */     
/*      */     try {
/* 1098 */       timeAsMillis = cal.getTimeInMillis();
/* 1099 */     } catch (IllegalAccessError iae) {
/*      */       
/* 1101 */       timeAsMillis = cal.getTime().getTime();
/*      */     } 
/*      */     
/* 1104 */     return new Time(timeAsMillis);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final Timestamp fastTimestampCreate(boolean useGmtConversion, Calendar gmtCalIfNeeded, Calendar cal, int year, int month, int day, int hour, int minute, int seconds, int secondsPart) {
/* 1112 */     cal.clear();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1117 */     cal.set(year, month - 1, day, hour, minute, seconds);
/*      */     
/* 1119 */     int offsetDiff = 0;
/*      */     
/* 1121 */     if (useGmtConversion) {
/* 1122 */       int fromOffset = cal.get(15) + cal.get(16);
/*      */ 
/*      */       
/* 1125 */       if (gmtCalIfNeeded == null) {
/* 1126 */         gmtCalIfNeeded = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
/*      */       }
/* 1128 */       gmtCalIfNeeded.clear();
/*      */       
/* 1130 */       gmtCalIfNeeded.setTimeInMillis(cal.getTimeInMillis());
/*      */       
/* 1132 */       int toOffset = gmtCalIfNeeded.get(15) + gmtCalIfNeeded.get(16);
/*      */       
/* 1134 */       offsetDiff = fromOffset - toOffset;
/*      */     } 
/*      */     
/* 1137 */     if (secondsPart != 0) {
/* 1138 */       cal.set(14, secondsPart / 1000000);
/*      */     }
/*      */     
/* 1141 */     long tsAsMillis = 0L;
/*      */ 
/*      */     
/*      */     try {
/* 1145 */       tsAsMillis = cal.getTimeInMillis();
/* 1146 */     } catch (IllegalAccessError iae) {
/*      */       
/* 1148 */       tsAsMillis = cal.getTime().getTime();
/*      */     } 
/*      */     
/* 1151 */     Timestamp ts = new Timestamp(tsAsMillis + offsetDiff);
/*      */     
/* 1153 */     ts.setNanos(secondsPart);
/*      */     
/* 1155 */     return ts;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static final Timestamp fastTimestampCreate(TimeZone tz, int year, int month, int day, int hour, int minute, int seconds, int secondsPart) {
/* 1161 */     Calendar cal = (tz == null) ? new GregorianCalendar() : new GregorianCalendar(tz);
/* 1162 */     cal.clear();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1167 */     cal.set(year, month - 1, day, hour, minute, seconds);
/*      */     
/* 1169 */     long tsAsMillis = 0L;
/*      */     
/*      */     try {
/* 1172 */       tsAsMillis = cal.getTimeInMillis();
/* 1173 */     } catch (IllegalAccessError iae) {
/*      */       
/* 1175 */       tsAsMillis = cal.getTime().getTime();
/*      */     } 
/*      */     
/* 1178 */     Timestamp ts = new Timestamp(tsAsMillis);
/* 1179 */     ts.setNanos(secondsPart);
/*      */     
/* 1181 */     return ts;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getCanoncialTimezone(String timezoneStr, ExceptionInterceptor exceptionInterceptor) throws SQLException {
/* 1197 */     if (timezoneStr == null) {
/* 1198 */       return null;
/*      */     }
/*      */     
/* 1201 */     timezoneStr = timezoneStr.trim();
/*      */ 
/*      */ 
/*      */     
/* 1205 */     if (timezoneStr.length() > 2 && (
/* 1206 */       timezoneStr.charAt(0) == '+' || timezoneStr.charAt(0) == '-') && Character.isDigit(timezoneStr.charAt(1)))
/*      */     {
/* 1208 */       return "GMT" + timezoneStr;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1213 */     int daylightIndex = StringUtils.indexOfIgnoreCase(timezoneStr, "DAYLIGHT");
/*      */ 
/*      */     
/* 1216 */     if (daylightIndex != -1) {
/* 1217 */       StringBuffer timezoneBuf = new StringBuffer();
/* 1218 */       timezoneBuf.append(timezoneStr.substring(0, daylightIndex));
/* 1219 */       timezoneBuf.append("Standard");
/* 1220 */       timezoneBuf.append(timezoneStr.substring(daylightIndex + "DAYLIGHT".length(), timezoneStr.length()));
/*      */       
/* 1222 */       timezoneStr = timezoneBuf.toString();
/*      */     } 
/*      */     
/* 1225 */     String canonicalTz = (String)TIMEZONE_MAPPINGS.get(timezoneStr);
/*      */ 
/*      */     
/* 1228 */     if (canonicalTz == null) {
/* 1229 */       String[] abbreviatedTimezone = (String[])ABBREVIATED_TIMEZONES.get(timezoneStr);
/*      */ 
/*      */       
/* 1232 */       if (abbreviatedTimezone != null)
/*      */       {
/* 1234 */         if (abbreviatedTimezone.length == 1) {
/* 1235 */           canonicalTz = abbreviatedTimezone[0];
/*      */         } else {
/* 1237 */           StringBuffer possibleTimezones = new StringBuffer(128);
/*      */           
/* 1239 */           possibleTimezones.append(abbreviatedTimezone[0]);
/*      */           
/* 1241 */           for (int i = 1; i < abbreviatedTimezone.length; i++) {
/* 1242 */             possibleTimezones.append(", ");
/* 1243 */             possibleTimezones.append(abbreviatedTimezone[i]);
/*      */           } 
/*      */           
/* 1246 */           throw SQLError.createSQLException(Messages.getString("TimeUtil.TooGenericTimezoneId", new Object[] { timezoneStr, possibleTimezones }), "01S00", exceptionInterceptor);
/*      */         } 
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1252 */     return canonicalTz;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String timeFormattedString(int hours, int minutes, int seconds) {
/* 1261 */     StringBuffer buf = new StringBuffer(8);
/* 1262 */     if (hours < 10) {
/* 1263 */       buf.append("0");
/*      */     }
/*      */     
/* 1266 */     buf.append(hours);
/* 1267 */     buf.append(":");
/*      */     
/* 1269 */     if (minutes < 10) {
/* 1270 */       buf.append("0");
/*      */     }
/*      */     
/* 1273 */     buf.append(minutes);
/* 1274 */     buf.append(":");
/*      */     
/* 1276 */     if (seconds < 10) {
/* 1277 */       buf.append("0");
/*      */     }
/*      */     
/* 1280 */     buf.append(seconds);
/*      */     
/* 1282 */     return buf.toString();
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdbc\TimeUtil.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */