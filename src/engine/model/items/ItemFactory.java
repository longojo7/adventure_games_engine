package engine.model.items;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory for creating and managing game items.
 */
public class ItemFactory {
  private static ItemFactory instance;
  private Map<String, Item> items;

  /**
   * Gets the singleton instance of the item factory.
   *
   * @return the item factory instance
   */
  public static ItemFactory getInstance() {
    if (instance == null)
      instance = new ItemFactory();
    return instance;
  }

  /**
   * Initializes items from JSON array.
   *
   * @param jsonItems the JSON array containing item data
   */
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

  /**
   * Gets an item by name.
   *
   * @param name the item name (case-insensitive)
   * @return the item or null if not found
   */
  public Item getItem(String name) {
    return items.get(name.toLowerCase());
  }
}
