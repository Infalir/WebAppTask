package main.com.gavruseva.webapp.model;

public abstract class AbstractModel {

  private long entityId;

  public long getModelId() {
    return entityId;
  }

  public void setModelId(long entityId) {
    this.entityId = entityId;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    AbstractModel that = (AbstractModel) object;

    return entityId == that.entityId;
  }

  @Override
  public int hashCode() {
    return Long.hashCode(entityId);
  }

  @Override
  public String toString() {
    return "id = " + entityId;
  }
}
