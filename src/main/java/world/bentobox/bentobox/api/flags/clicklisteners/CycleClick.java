package world.bentobox.bentobox.api.flags.clicklisteners;

import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;

import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.api.flags.Flag;
import world.bentobox.bentobox.api.localization.TextVariables;
import world.bentobox.bentobox.api.panels.Panel;
import world.bentobox.bentobox.api.panels.PanelItem;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.bentobox.database.objects.Island;
import world.bentobox.bentobox.managers.RanksManager;
import world.bentobox.bentobox.util.Util;

/**
 * Left Clicks increase rank, right clicks lower rank
 * @author tastybento
 *
 */
public class CycleClick implements PanelItem.ClickHandler {

    private BentoBox plugin = BentoBox.getInstance();
    private final String id;
    private int minRank = RanksManager.VISITOR_RANK;
    private int maxRank = RanksManager.OWNER_RANK;

    /**
     * Construct default cycle clicker with min rank of {@link RanksManager#VISITOR_RANK}
     * and max rank of {@link RanksManager#OWNER_RANK}
     * @param id - the flag id that will be adjusted by this click
     */
    public CycleClick(String id) {
        this.id = id;
    }

    /**
     * Construct a cycle clicker with a min and max rank
     * @param id
     * @param minRank
     * @param maxRank
     */
    public CycleClick(String id, int minRank, int maxRank) {
        this.id = id;
        this.minRank = minRank;
        this.maxRank = maxRank;
    }

    @Override
    public boolean onClick(Panel panel, User user, ClickType click, int slot) {
        // Get the world
        if (!plugin.getIWM().inWorld(user.getLocation())) {
            user.sendMessage("general.errors.wrong-world");
            return true;
        }
        String reqPerm = plugin.getIWM().getPermissionPrefix(Util.getWorld(user.getWorld())) + ".settings." + id;
        String allPerms = plugin.getIWM().getPermissionPrefix(Util.getWorld(user.getWorld())) + ".settings.*";
        if (!user.hasPermission(reqPerm) && !user.hasPermission(allPerms)) {
            user.sendMessage("general.errors.no-permission", TextVariables.PERMISSION, reqPerm);
            user.getPlayer().playSound(user.getLocation(), Sound.BLOCK_METAL_HIT, 1F, 1F);
            return true;
        }
        // Left clicking increases the rank required
        // Right clicking decreases the rank required
        // Get the user's island
        Island island = plugin.getIslands().getIsland(user.getWorld(), user.getUniqueId());
        if (island != null && island.getOwner().equals(user.getUniqueId())) {
            RanksManager rm = plugin.getRanksManager();
            Flag flag = plugin.getFlagsManager().getFlagByID(id);
            int currentRank = island.getFlag(flag);
            if (click.equals(ClickType.LEFT)) {
                if (currentRank >= maxRank) {
                    island.setFlag(flag, minRank);
                } else {
                    island.setFlag(flag, rm.getRankUpValue(currentRank));
                }
                user.getPlayer().playSound(user.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 1F, 1F);
            } else if (click.equals(ClickType.RIGHT)) {
                if (currentRank <= minRank) {
                    island.setFlag(flag, maxRank);
                } else {
                    island.setFlag(flag, rm.getRankDownValue(currentRank));
                }
                user.getPlayer().playSound(user.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 1F, 1F);
            }
            // Apply change to panel
            panel.getInventory().setItem(slot, flag.toPanelItem(plugin, user).getItem());
        } else {
            user.getPlayer().playSound(user.getLocation(), Sound.BLOCK_METAL_HIT, 1F, 1F);
        }
        return true;
    }

    /**
     * @param minRank the minRank to set
     */
    public void setMinRank(int minRank) {
        this.minRank = minRank;
    }

    /**
     * @param maxRank the maxRank to set
     */
    public void setMaxRank(int maxRank) {
        this.maxRank = maxRank;
    }

}
