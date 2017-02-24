package io.miti.restify.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * The list of servers used by the Servers tab.
 */
public final class ServerCache {
  
  /** The one instance of this class. */
  private static final ServerCache cache;
  
  /** Map of server name to URL */
  private Map<String, String> map;
  
  static {
    cache = new ServerCache();
    cache.map = new HashMap<>(5);
  }

  private ServerCache() {
    super();
  }
  
  public static ServerCache getInstance() {
    return cache;
  }
  
  public String getServerUrl(final String name) {
    return map.get(name);
  }
  
  public void add(final String name, final String url) {
    map.put(name, url);
  }

  public Set<Entry<String,String>> getMap() {
    return map.entrySet();
  }
  
  public void setMap(Map<String, String> setMap) {
    
    map.clear();
    if ((setMap != null) && !setMap.isEmpty()) {
      for (Entry<String, String> entry : setMap.entrySet()) {
        map.put(entry.getKey(), entry.getValue());
      }
    }
  }

  public void deleteServer(String selectedName) {
    map.remove(selectedName);
  }
}
