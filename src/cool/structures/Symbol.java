package cool.structures;

import java.util.ArrayList;
import java.util.List;

import cool.structures.Scope.*;

public abstract class Symbol {
    protected String name;
    
    public Symbol(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        return getName();
    }

    public static class IdSymbol extends Symbol {
        private ClassSymbol classSymbol;
        private boolean hasSelfType = false;

        public IdSymbol(String name) {
            super(name);
        }

        // returns the type of the variable or attribute resolving SELF_TYPE in the given scope
        public TypeSymbol getType(ClassScope scope) {
            if (hasSelfType)
                return scope.classSymbol.selfType;
            return classSymbol;
        }

        public IdSymbol setType(ClassSymbol classSymbol) {
            this.classSymbol = classSymbol;
            return this;
        }

        public IdSymbol setSelfType() {
            hasSelfType = true;
            return this;
        }

        // returns the type of the variable or attribute if it's not SELF_TYPE, otherwise null
        // useful for formal arguments which can never be SELF_TYPE
        public ClassSymbol getClassType() {
            return classSymbol;
        }
    }

    // applies to attributes, local variables and formal arguments
    public static class VarSymbol extends IdSymbol {
        public static final String namespace = "VAR";

        protected int index = -1;
        public final String store;

        public VarSymbol(String name, String store) {
            super(name);
            this.store = store;
        }

        public int getIndex() {
            return index;
        }
    }

    public static class AttrSymbol extends VarSymbol {
        public static final String attrStore = "attrStore";
        public AttrSymbol(String name) {
            super(name, attrStore);
        }

        public void setIndex(int index) {
            if (this.index != -1)
                throw new RuntimeException("Index already set");
            this.index = index;
        }

        @Override
        public AttrSymbol setType(ClassSymbol classSymbol) {
            super.setType(classSymbol);
            return this;
        }

        @Override
        public AttrSymbol setSelfType() {
            super.setSelfType();
            return this;
        }
    }

    public static class LocalSymbol extends VarSymbol {
        public static final String localStore = "localStore";
        public LocalSymbol(String name) {
            super(name, localStore);
        }

        public void setIndex(int index) {
            if (this.index != -1)
                throw new RuntimeException("Index already set");
            this.index = index;
        }
    }

    public static class ArgSymbol extends VarSymbol {
        public static final String argStore = "argStore";
        public ArgSymbol(String name, int index) {
            super(name, argStore);
            this.index = index;
        }

        @Override
        public ArgSymbol setType(ClassSymbol classSymbol) {
            super.setType(classSymbol);
            return this;
        }

        @Override
        public ArgSymbol setSelfType() {
            super.setSelfType();
            return this;
        }
    }

    public static abstract class TypeSymbol extends Symbol {
        public TypeSymbol(String name) {
            super(name);
        }
        public abstract boolean isSubtypeOf(TypeSymbol other);
        public abstract TypeSymbol leastUpperBound(TypeSymbol other);
    };

    public static class ClassSymbol extends TypeSymbol {
        // classes are implicitly in a different namespace, since their names start with an uppercase letter
        public static final String namespace = "";

        public final ClassScope classScope;
        public final List<ClassSymbol> children;
        public final IdSymbol self;
        public final SelfTypeSymbol selfType;

        private int order = -1;

        public ClassSymbol(String name) {
            super(name);
            classScope = new ClassScope(null, this);
            children = new ArrayList<>();
            selfType = new SelfTypeSymbol(this);
            self = new IdSymbol("");
            self.setSelfType();
        }

        public ClassScope getScope() {
            return classScope;
        }

        public ClassSymbol getParent() {
            return classScope.getClassParent();
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            if (this.order != -1)
                throw new RuntimeException("Order already set");
            this.order = order;
        }

        public boolean isSubtypeOf(TypeSymbol other) {
            if (other instanceof SelfTypeSymbol)
                return false;
            
            ClassSymbol superClass = this;
            while (superClass != other && superClass != null)
                superClass = superClass.getParent();
            return (superClass == other);
        }

        public TypeSymbol leastUpperBound(TypeSymbol other) {
            if (other instanceof SelfTypeSymbol)
                return leastUpperBound(((SelfTypeSymbol)other).classSymbol);
            
            if (this == other) return this;
            if (this.isSubtypeOf(other)) return other;
            if (other.isSubtypeOf(this)) return this;
            return this.getParent().leastUpperBound(((ClassSymbol)other).getParent());
        }
    }

    public static class SelfTypeSymbol extends TypeSymbol {
        public final ClassSymbol classSymbol;

        public SelfTypeSymbol(ClassSymbol classSymbol) {
            super("SELF_TYPE");
            this.classSymbol = classSymbol;
        }

        public boolean isSubtypeOf(TypeSymbol other) {
            if (other instanceof SelfTypeSymbol)
                return this.classSymbol == ((SelfTypeSymbol)other).classSymbol;
            return classSymbol.isSubtypeOf(other);
        }

        public TypeSymbol leastUpperBound(TypeSymbol other) {
            if (other instanceof SelfTypeSymbol) {
                SelfTypeSymbol otherSelf = (SelfTypeSymbol)other;
                if (this.classSymbol == otherSelf.classSymbol)
                    return this;
                return this.classSymbol.leastUpperBound(otherSelf.classSymbol);
            }
                
            return classSymbol.leastUpperBound(other);
        }
    }

    public static class MethodSymbol extends IdSymbol {
        public static final String namespace = "MET";

        public final List<ArgSymbol> argsList;
        public final MethodScope methodScope;

        public int index = -1;

        public MethodSymbol(String name, ClassScope classScope) {
            super(name);
            this.argsList = new ArrayList<>();
            this.methodScope = new MethodScope(classScope, this);
        }

        // chainable methods

        @Override
        public MethodSymbol setType(ClassSymbol classSymbol) {
            super.setType(classSymbol);
            return this;
        }

        @Override
        public MethodSymbol setSelfType() {
            super.setSelfType();
            return this;
        }

        public MethodSymbol addArg(ArgSymbol arg) {
            argsList.add(arg);
            return this;
        }
    }
}