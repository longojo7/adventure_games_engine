package engine.model.items;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ItemFactory {
  private static ItemFactory instance;
  private Map<String, Item> items;

  public static ItemFactory getInstance() {
    if (instance == null)
      instance = new ItemFactory();
    return instance;
  }

  public void itemsFromJson(JSONArray jsonItems) {
    items = new HashMap<>();
    for(Object object : jsonItems) {
      JSONObject jsonItem = (JSONObject) object;
      Item item = new Item(
              jsonItem.get("name").toString(),
              Integer.parseInt(jsonItem.get("value").toString()),
              Integer.parseInt(jsonItem.get("weight").toString()),
              jsonItem.get("description").toString(),
              Integer.parseInt(jsonItem.get("max_uses").toString()),
              Integer.parseInt(jsonItem.get("uses_remaining").toString())
      );
      items.put(item.getName().toLowerCase(), item);
    }
  }

  public Item getItem(String name) {
    return items.get(name.toLowerCase());
  }
}
