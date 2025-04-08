package model;

public class Arm {

    private String type;
    private boolean isHoldingObject;
    private double maxCapacity;
    private Objecttoget heldObject;

    public Arm(String type, double maxCapacity) {
        this.type = type;
        this.maxCapacity = maxCapacity;
        this.isHoldingObject = false;
        this.heldObject = null;
    }

    // === Core Methods ===

    public boolean grab(Objecttoget obj) {
        if (isHoldingObject) {
            System.out.println("[Arm] Already holding an object.");
            return false;
        }

        if (obj.getWeight() > maxCapacity) {
            System.out.println("[Arm] Object is too heavy: " + obj.getWeight() + "kg > " + maxCapacity + "kg");
            return false;
        }

        this.heldObject = obj;
        this.isHoldingObject = true;
        System.out.println("[Arm] Grabbed object: " + obj.getName());
        return true;
    }

    public boolean release() {
        if (!isHoldingObject) {
            System.out.println("[Arm] No object to release.");
            return false;
        }

        System.out.println("[Arm] Released object: " + heldObject.getName());
        this.heldObject = null;
        this.isHoldingObject = false;
        return true;
    }

    // === Getters and Setters ===

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isHoldingObject() {
        return isHoldingObject;
    }

    public Objecttoget getHeldObject() {
        return heldObject;
    }

    public void setHoldingObject(boolean holdingObject) {
        isHoldingObject = holdingObject;
    }

    public double getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(double maxCapacity) {
        this.maxCapacity = maxCapacity;
    }
}
