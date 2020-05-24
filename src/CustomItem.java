import org.bukkit.inventory.ItemStack;

public class CustomItem {
    enum NavigationAttribute{
        NONE, PREVIOUS, NEXT, CLOSE
    }


    private ItemStack itemStack;
    private NavigationAttribute attribute;


    public ItemStack getItem(){
        return itemStack;
    }

    public NavigationAttribute getAttribute(){
        return attribute;
    }
}
