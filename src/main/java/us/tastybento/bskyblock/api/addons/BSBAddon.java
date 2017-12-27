package us.tastybento.bskyblock.api.addons;

import java.io.File;

import us.tastybento.bskyblock.BSkyBlock;
import us.tastybento.bskyblock.api.BSBModule;
import us.tastybento.bskyblock.managers.CommandsManager;
import us.tastybento.bskyblock.managers.LocalesManager;

public abstract class BSBAddon implements BSBModule {

    private File folder;
    private AddonDescription description;
    private AddonState state;

    public abstract void enable();
    public abstract void disable();
    public abstract void load();
    public abstract void reload();

    public AddonDescription getDescription() {
        return description;
    }

    public AddonState getState()    {   return state;                               }
    public boolean isEnabled()      {   return state == AddonState.ENABLED;         }
    public boolean isDisabled()     {   return state == AddonState.DISABLED;        }
    public boolean isIncompatible() {   return state == AddonState.INCOMPATIBLE;    }

    public CommandsManager getCommandsManager() {
        return BSkyBlock.getPlugin().getCommandsManager();
    }

    public LocalesManager getLocalesManager() {
        return BSkyBlock.getPlugin().getLocalesManager();
    }

    @Override
    public final String getIdentifier() {
        return getDescription().getName();
    }

    @Override
    public final boolean isAddon() {
        return true;
    }

    @Override
    public final File getFolder() {
        if (folder == null) {
            folder = new File(BSkyBlock.getPlugin().getFolder() + "/addons/" + getIdentifier());
        }
        return folder;
    }
}