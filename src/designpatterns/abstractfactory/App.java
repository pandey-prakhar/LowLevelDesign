package designpatterns.abstractfactory;

public class App {

    public static void main(String[] args) {

        // THIS is the only place in the entire codebase that knows the platform.
        // To switch to Mac, comment line below and uncomment the next one.
        // Application.java, Button, Checkbox — none of them change.

        UIFactory factory = new WindowsUIFactory();
        // UIFactory factory = new MacUIFactory();

        Application app = new Application(factory);
        app.buildUI();

        System.out.println("--- Rendering UI ---");
        app.renderUI();

        System.out.println("--- User Interaction ---");
        app.interact();
    }
}
