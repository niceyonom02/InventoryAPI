import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomInventory {
    private ArrayList<CustomItem> inventoryItemList;
    private HashMap<Integer, CustomItem> unchangeableSlotItem;
    private ArrayList<Inventory> inventoryList;

    public void addItem(CustomItem customItem){
        inventoryItemList.add(customItem);

        if(isLastInventoryFull()){

        }

        inventoryList.get(inventoryList.size() - 1).addItem(customItem.getItem());
    }

    private boolean isLastInventoryFull(){
        Inventory lastInventory = inventoryList.get(inventoryList.size() - 1);
        for(int i = lastInventory.getSize() - 1; i >= 0; i--){
            if(unchangeableSlotItem.containsKey(i)) continue;

            if(lastInventory.getItem(i) == null){
                return false;
            }
        }
        return true;
    }

    private void openInventory(Player player){
        player.openInventory(inventoryList.get(0));
    }

    public void addSlotUnchangeItem(CustomItem customItem, int slot){
        unchangeableSlotItem.put(slot, customItem);

        for(Inventory inventory : inventoryList){
            inventory.setItem(slot, customItem.getItem());
        }
    }

    public void removeItem(int page, int slot){
        inventoryItemList.remove(page * inventoryList.get(page).getSize() + slot);

        int startPage;

        if(slot == maxCount -1){
            startPage = page + 1;
        } else{
            startPage = page;

            Inventory currentInventory = inventoryList.get(startPage);

            for(int index = slot + 1; index < inventoryList.get(startPage).getSize(); index++){
                if(unchangeableSlotItem.containsKey(index)) continue;

                ItemStack fromSlotItem = currentInventory.getItem(index);
                currentInventory.setItem(index - 1, fromSlotItem);
            }

            startPage++;
        }


        for(int pIndex = startPage; pIndex < inventoryList.size(); pIndex++){
            Inventory currentInventory = inventoryList.get(pIndex);

            for(int itemIndex = 0; itemIndex < inventoryList.get(pIndex).getSize(); itemIndex++){
                if(unchangeableSlotItem.containsKey(itemIndex)) continue;

                ItemStack fromSlotItem = currentInventory.getItem(itemIndex);

                if(itemIndex == 0){
                    int lastIndex = -1;

                    Inventory previousPage = inventoryList.get(pIndex - 1);
                    for(int lastPageIndex = previousPage.getSize() - 1; lastPageIndex >= 0; lastPageIndex--){
                        if(unchangeableSlotItem.containsKey(lastPageIndex)) continue;

                        if(previousPage.getItem(lastPageIndex) == null){
                            lastIndex = lastPageIndex;
                            break;
                        }
                    }

                    inventoryList.get(pIndex - 1).setItem(lastIndex, fromSlotItem);
                    currentInventory.setItem(0, null);
                } else{
                    currentInventory.setItem(itemIndex - 1, fromSlotItem);
                    currentInventory.setItem(itemIndex, null);
                }
            }
        }

        if(isLastInventoryEmpty()){
            inventoryList.get(inventoryList.size() - 1).clear();
            inventoryList.remove(inventoryList.size() -1);
        }
    }

    private boolean isLastInventoryEmpty(){
        Inventory inventory = inventoryList.get(inventoryList.size() -1);

        for(int i = 0; i < inventory.getSize(); i++){
            if(unchangeableSlotItem.containsKey(i)) continue;

            if(inventory.getItem(i) != null){
                return false;
            }
        }
        return true;
    }

    public void onClick(InventoryClickEvent e){
        if(e.getWhoClicked() instanceof Player){
            Player player = (Player) e.getWhoClicked();

            Inventory inventory = e.getInventory();
            int page = -1;

            for(int i = 0; i < inventoryList.size(); i++){
                if(inventoryList.get(i).equals(inventory)){
                    page = i;
                    break;
                }
            }

            if(page != -1){
                e.setCancelled(true);
                CustomInventoryClickEvent customInventoryClickEvent = new CustomInventoryClickEvent(player, page, e.getSlot(), getItem(page, e.getSlot()));
                Bukkit.getPluginManager().callEvent(customInventoryClickEvent);
            }
        }
    }

    public CustomItem getItem(int page, int slot){
        return inventoryItemList.get(inventoryIndexToItemIndex(page, slot));
    }

    private int inventoryIndexToItemIndex(int page, int slot){
        int maxCount = inventoryList.get(page).getSize() - unchangeableSlotItem.keySet().size();

        return page * maxCount + slot;
    }
}
