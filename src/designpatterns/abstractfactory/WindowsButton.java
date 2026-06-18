package designpatterns.abstractfactory;

public class WindowsButton implements Button {

    @Override
    public void render() {
        System.out.println("[Windows Button] Rendered with Windows styling");
    }

    @Override
    public void onClick() {
        System.out.println("[Windows Button] Clicked — Windows click handler fired");
    }
}
