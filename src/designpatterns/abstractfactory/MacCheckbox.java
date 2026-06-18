package designpatterns.abstractfactory;

public class MacCheckbox implements Checkbox {

    @Override
    public void render() {
        System.out.println("[Mac Checkbox] Rendered with Mac styling");
    }

    @Override
    public void onCheck() {
        System.out.println("[Mac Checkbox] Checked — Mac check handler fired");
    }
}
