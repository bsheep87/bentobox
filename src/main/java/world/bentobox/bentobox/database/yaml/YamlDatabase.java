package world.bentobox.bentobox.database.yaml;

import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.database.AbstractDatabaseHandler;
import world.bentobox.bentobox.database.DatabaseSetup;

public class YamlDatabase implements DatabaseSetup {

    /**
     * Get the config
     * @param <T> - Class type
     * @param type - config object type
     * @return - the config handler
     */
    public <T> AbstractDatabaseHandler<T> getConfig(Class<T> type) {
        return new ConfigHandler<>(BentoBox.getInstance(), type, new YamlDatabaseConnector(BentoBox.getInstance()));
    }

    @Override
    public <T> AbstractDatabaseHandler<T> getHandler(Class<T> type) {
        return new YamlDatabaseHandler<>(BentoBox.getInstance(), type, new YamlDatabaseConnector(BentoBox.getInstance()));
    }

}
