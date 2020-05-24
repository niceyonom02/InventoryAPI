import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;

public class CustomInventoryClickEvent extends Event {
    private Player player;
    private int page;
    private int slot;
    private CustomItem customItem;

    public CustomInventoryClickEvent(Player player, int page, int slot, CustomItem customItem){
        this.player = player;
        this.page = page;
        this.slot = slot;
        this.customItem = customItem;
    }

    private final static HandlerList HANDLER_LIST = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public Player getPlayer() {
        return player;
    }

    public int getPage() {
        return page;
    }

    public int getSlot() {
        return slot;
    }

    public CustomItem getCustomItem() {
        return customItem;
    }
}
