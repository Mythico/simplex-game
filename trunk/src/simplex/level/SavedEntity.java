package simplex.level;

import simplex.entity.Entity;

/**
 * An abstract superclass for all SavedObjects that 
 * will be saved in the level file.
 * 
 * @author Emil
 * @author Samuel
 */
public abstract class SavedEntity {
    private int id;

    /**
     * Creates a new empty SavedEntity.
     * This constructor should only be used during the deserialization.
     */
    public SavedEntity() {
    }

    /**
     * Create a new SavedEntity with an id.
     * @param id The id of the SavedEntity
     */
    public SavedEntity(int id) {
        this.id = id;
    }

    /**
     * Create a Entity from the SavedEntity
     * @return A new Entity
     */
    public abstract Entity create();

    /**
     * Get the id.
     * @return The id of the object.
     */
    public int getId() {
        return id;
    }

    /**
     * Set the id.
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }
    
}
