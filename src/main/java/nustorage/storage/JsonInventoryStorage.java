package nustorage.storage;


import static java.util.Objects.requireNonNull;
import static nustorage.commons.util.CollectionUtil.requireAllNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import nustorage.commons.core.LogsCenter;
import nustorage.commons.exceptions.DataConversionException;
import nustorage.commons.exceptions.IllegalValueException;
import nustorage.commons.util.FileUtil;
import nustorage.commons.util.JsonUtil;
import nustorage.model.Inventory;


/**
 * A class to access Inventory data stored as a json file on the hard disk.
 */
public class JsonInventoryStorage implements InventoryStorage {

    private static final Logger logger = LogsCenter.getLogger(JsonInventoryStorage.class);

    private Path filePath;


    /**
     * Creates a new inventory storage object.
     *
     * @param filePath file path to storage file.
     */
    public JsonInventoryStorage(Path filePath) {
        this.filePath = filePath;
    }


    @Override
    public Path getInventoryFilePath() {
        return this.filePath;
    }


    @Override
    public Optional<Inventory> readInventory() throws DataConversionException, IOException {
        return readInventory(filePath);
    }


    @Override
    public Optional<Inventory> readInventory(Path filePath) throws DataConversionException, IOException {
        requireNonNull(filePath);

        // TODO: THIS LINE IS CAUSING A LOADING ERROR
        Optional<JsonSerializableInventory> jsonInventory = JsonUtil.readJsonFile(
                filePath, JsonSerializableInventory.class);

        if (jsonInventory.isEmpty()) {
            return Optional.empty();
        }

        try {
            return Optional.of(jsonInventory.get().toModelType());
        } catch (IllegalValueException e) {
            logger.info("Illegal values found in " + filePath + ": " + e.getMessage());
            throw new DataConversionException(e);
        }
    }


    @Override
    public void saveInventory(Inventory inventory) throws IOException {
        saveInventory(inventory, filePath);
    }


    @Override
    public void saveInventory(Inventory inventory, Path filePath) throws IOException {
        requireAllNonNull(inventory, filePath);

        FileUtil.createIfMissing(filePath);

        JsonUtil.saveJsonFile(new JsonSerializableInventory(inventory), filePath);
    }

}