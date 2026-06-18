package designpatterns.abstractfactory;

// This class has ZERO knowledge of Windows or Mac.
// It only depends on the UIFactory interface and the product interfaces.
// Swap the factory injected into it — the entire UI changes consistently.
public class Application {

    private UIFactory factory;
    private Button button;
    private Checkbox checkbox;

    public Application(UIFactory factory) {
        this.factory = factory;
    }

    public void buildUI() {
        button   = factory.createButton();
        checkbox = factory.createCheckbox();
    }

    public void renderUI() {
        button.render();
        checkbox.render();
    }

    public void interact() {
        button.onClick();
        checkbox.onCheck();
    }
}
