package cool.structures;

import java.util.*;

import org.antlr.v4.runtime.misc.Pair;

import cool.structures.Symbol.ClassSymbol;
import cool.structures.Symbol.IdSymbol;
import cool.structures.Symbol.MethodSymbol;
import cool.structures.Symbol.VarSymbol;

public class Scope {
    protected Map<String, Symbol> symbols = new LinkedHashMap<>();
    
    protected Scope parent;
    
    public Scope(Scope parent) {
        this.parent = parent;
    }

    // namespace is not allowed to contain underscores in order to prevent name clashes
    private String key(String name, String namespace) {
        return namespace + "_" + name;
    }

    public boolean add(Symbol sym, String namespace) {
        // Reject duplicates in the same scope.
        if (symbols.containsKey(key(sym.getName(), namespace)))
            return false;
        
        symbols.put(key(sym.getName(), namespace), sym);
        
        return true;
    }

    public boolean add(Symbol sym) {
        return add(sym, "");
    }

    public Symbol lookup(String name, String namespace) {
        var sym = symbols.get(key(name, namespace));
        
        if (sym != null)
            return sym;
        
        if (parent != null)
            return parent.lookup(name, namespace);
        
        return null;
    }

    public Symbol lookup(String str) {
        return lookup(str, "");
    }

    public Scope getParent() {
        return parent;
    }

    protected List<Symbol> getNamespace(String namespace) {
        var syms = (parent != null) ? parent.getNamespace(namespace) : new ArrayList<Symbol>();
        
        for (var entry : symbols.entrySet())
            if (entry.getKey().startsWith(namespace))
                syms.add(entry.getValue());
        
        return syms;
    }
    
    @Override
    public String toString() {
        return symbols.keySet().toString() + (parent != null ? " -> " + parent.toString() : "");
    }

    // the scope of a class or any scope nested within a class
    public static class ClassScope extends Scope {
        public final ClassSymbol classSymbol;

        public ClassScope(Scope parent, ClassSymbol classSymbol) {
            super(parent);
            this.classSymbol = classSymbol;
        }

        public ClassSymbol getSymbol() {
            return classSymbol;
        }

        public void setParent(Scope parent) {
            if (this.parent != null)
                throw new RuntimeException("Parent already set");
            this.parent = parent;
            if (parent != null && parent != SymbolTable.globals)
                ((ClassScope)parent).classSymbol.children.add(classSymbol);
        }

        public ClassSymbol getClassParent() {
            return (parent != SymbolTable.globals) ? ((ClassScope)parent).classSymbol : null;
        }

        public List<IdSymbol> getAttributes() {
            var attrs = (parent != SymbolTable.globals) ?
                ((ClassScope)parent).getAttributes() : new ArrayList<IdSymbol>();
            int i = attrs.size();
            for (var entry : symbols.entrySet()) {
                if (!entry.getKey().startsWith(VarSymbol.namespace)) continue;
                var attr = (VarSymbol)entry.getValue();
                attrs.add(attr);
                attr.index = i++;
            }
            return attrs;
        }

        // map method names to pairs of class name and method index
        public Map<String, Pair<String, Integer>> getMethods() {
            var methods = (parent != SymbolTable.globals) ? 
                ((ClassScope)parent).getMethods() : 
                new LinkedHashMap<String, Pair<String, Integer>>();
            int i = methods.size();
            for (var entry : symbols.entrySet()) {
                if (!entry.getKey().startsWith(MethodSymbol.namespace)) continue;
                var method = (MethodSymbol)entry.getValue();
                var pair = methods.get(method.name);
                int index = (pair != null) ? pair.b : i++;
                methods.put(method.name, new Pair<>(classSymbol.name, index));
                method.index = index;
            }
            return methods;
        }
    }

    // a nested scope within a class
    public static class SubClassScope extends ClassScope {
        public SubClassScope(ClassScope parent) {
            super(parent, parent.classSymbol);
        }
    }

    public static class MethodScope extends SubClassScope {
        public final MethodSymbol methodSymbol;

        public MethodScope(ClassScope parent, MethodSymbol methodSymbol) {
            super(parent);
            this.methodSymbol = methodSymbol;
        }
    }
}
