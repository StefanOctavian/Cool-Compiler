package cool.codegen;

import java.util.List;

import cool.structures.Symbol.ClassSymbol;

public class ObjectLayout {
    private ClassSymbol classSymbol;
    private List<String> attributes;

    public final String template = "object";

    public ObjectLayout(ClassSymbol classSymbol, List<String> attributes) {
        this.classSymbol = classSymbol;
        this.attributes = attributes;
    }

    public int getTag() {
        return classSymbol.getOrder();
    }

    public int getMemsize() {
        return 3 + attributes.size();
    }

    public String getDispatchTable() {
        return classSymbol.getName() + "_dispTab";
    }

    public List<String> getAttrs() {
        return attributes;
    }
};
