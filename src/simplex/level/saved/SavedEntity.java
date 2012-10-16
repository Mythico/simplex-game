package simplex.level.saved;

import simplex.entity.Entity;

/**
 * An abstract superclass for all SavedEntities that 
 * will be serialized to a YAML objects.
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
     * @param entity  The entity to be saved.
     */
    public SavedEntity(Entity entity) {
        this.id = entity.getId();
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
