package sg.com.paloit.hashit.model;

public class AnsibleRequest<T> {

    private T extra_vars;

    public T getExtra_vars() {
        return extra_vars;
    }

    public void setExtra_vars(T extra_vars) {
        this.extra_vars = extra_vars;
    }

    public AnsibleRequest(T extra_vars) {
        this.extra_vars = extra_vars;
    }
}
