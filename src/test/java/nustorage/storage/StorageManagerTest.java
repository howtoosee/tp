package nustorage.storage;

import static nustorage.testutil.TypicalPersons.getTypicalAddressBook;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import nustorage.commons.core.GuiSettings;
import nustorage.model.AddressBook;
import nustorage.model.ReadOnlyAddressBook;
import nustorage.model.UserPrefs;

public class StorageManagerTest {

    @TempDir
    public Path testFolder;

    private StorageManager storageManager;

    @BeforeEach
    public void setUp() {
        // JsonAddressBookStorage addressBookStorage = new JsonAddressBookStorage(getTempFilePath("ab"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(getTempFilePath("prefs"));

        JsonFinanceAccountStorage financeAccountStorage = new JsonFinanceAccountStorage(getTempFilePath("finance"));
        JsonInventoryStorage inventoryStorage = new JsonInventoryStorage(getTempFilePath("inventory"));

        storageManager = new StorageManager(financeAccountStorage, inventoryStorage, userPrefsStorage);
    }

    private Path getTempFilePath(String fileName) {
        return testFolder.resolve(fileName);
    }

    @Test
    public void prefsReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonUserPrefsStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonUserPrefsStorageTest} class.
         */
        UserPrefs original = new UserPrefs();
        original.setGuiSettings(new GuiSettings(300, 600, 4, 6));
        storageManager.saveUserPrefs(original);
        UserPrefs retrieved = storageManager.readUserPrefs().get();
        assertEquals(original, retrieved);
    }

    // @Test
    // public void addressBookReadSave() throws Exception {
    //     /*
    //      * Note: This is an integration test that verifies the StorageManager is properly wired to the
    //      * {@link JsonAddressBookStorage} class.
    //      * More extensive testing of UserPref saving/reading is done in {@link JsonAddressBookStorageTest} class.
    //      */
    //     AddressBook original = getTypicalAddressBook();
    //     storageManager.saveAddressBook(original);
    //     ReadOnlyAddressBook retrieved = storageManager.readAddressBook().get();
    //     assertEquals(original, new AddressBook(retrieved));
    // }

    // @Test
    // public void getAddressBookFilePath() {
    //     assertNotNull(storageManager.getAddressBookFilePath());
    // }

}
