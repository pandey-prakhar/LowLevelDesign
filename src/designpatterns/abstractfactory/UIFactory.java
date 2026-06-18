package designpatterns.abstractfactory;

// The Abstract Factory interface.
// Each method creates one product from the family.
// A concrete factory that implements this is FORCED to return consistent products.
public interface UIFactory {
    Button createButton();
    Checkbox createCheckbox();
}
