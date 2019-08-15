package sg.com.paloit.hashit.model;

import java.io.Serializable;

public class TotalContractCount implements Serializable {

    private long count;

    public TotalContractCount(long count) {
        this.count = count;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
