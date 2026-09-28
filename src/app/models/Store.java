package app.models;

import java.util.Objects;

public class Store {
    private int storeNumber;
    private String address;
    private byte checkouts;
    private boolean hasSelfCheckout;
    private byte selfCheckouts;
    private float workingHours;

    public Store(int storeNumber, String address, byte checkouts, boolean hasSelfCheckout, byte selfCheckouts, float workingHours) {
        this.storeNumber = storeNumber;
        this.address = address;
        this.checkouts = checkouts;
        this.hasSelfCheckout = hasSelfCheckout;
        this.selfCheckouts = selfCheckouts;
        this.workingHours = workingHours;
    }

    public Store() {
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Store)) return false;
        Store store = (Store) o;
        return storeNumber == store.storeNumber && checkouts == store.checkouts && hasSelfCheckout == store.hasSelfCheckout && selfCheckouts == store.selfCheckouts && Float.compare(workingHours, store.workingHours) == 0 && Objects.equals(address, store.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeNumber, address, checkouts, hasSelfCheckout, selfCheckouts, workingHours);
    }

    @Override
    public String toString() {
        return "Store{" +
                "storeNumber=" + storeNumber +
                ", address='" + address + '\'' +
                ", checkouts=" + checkouts +
                ", hasSelfCheckout=" + hasSelfCheckout +
                ", selfCheckouts=" + selfCheckouts +
                ", workingHours=" + workingHours +
                '}';
    }

    public int getStoreNumber() {
        return storeNumber;
    }

    public void setStoreNumber(int storeNumber) {
        this.storeNumber = storeNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public byte getCheckouts() {
        return checkouts;
    }

    public void setCheckouts(byte checkouts) {
        this.checkouts = checkouts;
    }

    public boolean isHasSelfCheckout() {
        return hasSelfCheckout;
    }

    public void setHasSelfCheckout(boolean hasSelfCheckout) {
        this.hasSelfCheckout = hasSelfCheckout;
    }

    public byte getSelfCheckouts() {
        return selfCheckouts;
    }

    public void setSelfCheckouts(byte selfCheckouts) {
        this.selfCheckouts = selfCheckouts;
    }

    public float getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(float workingHours) {
        this.workingHours = workingHours;
    }
}
