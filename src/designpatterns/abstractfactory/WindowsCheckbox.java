package designpatterns.abstractfactory;

public class WindowsCheckbox implements Checkbox {

    @Override
    public void render() {
        System.out.println("[Windows Checkbox] Rendered with Windows styling");
    }

    @Override
    public void onCheck() {
        System.out.println("[Windows Checkbox] Checked — Windows check handler fired");
    }
}
