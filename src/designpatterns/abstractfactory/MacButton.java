package designpatterns.abstractfactory;

public class MacButton implements Button {

    @Override
    public void render() {
        System.out.println("[Mac Button] Rendered with Mac styling");
    }

    @Override
    public void onClick() {
        System.out.println("[Mac Button] Clicked — Mac click handler fired");
    }
}
