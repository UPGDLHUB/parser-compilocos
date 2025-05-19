import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

public class FirstsSet {
    public static final Map<String, Set<String>> FIRST_MAP = createFirstMap();

    private static Map<String, Set<String>> createFirstMap() {
        Map<String, Set<String>> map = new HashMap<>();
        map.put("RULE_PROGRAM", program());
        map.put("RULE_METHODS", methods());
        map.put("RULE_TYPES", types());
        map.put("RULE_GLOBAL_ATTRIBUTE", globalAttribute());
        map.put("RULE_PARAMS", params());
        map.put("RULE_ASSIGNMENT", assignment());
        map.put("RULE_VARIABLE", variable());
        map.put("RULE_RETURN", _return());
        map.put("RULE_CALL", call());
        map.put("RULE_ARGUMENTS", arguments());
        map.put("RULE_FOR", _for());
        map.put("RULE_SWITCH", _switch());
        map.put("RULE_DOWHILE", doWhile());
        map.put("RULE_IF", _if());
        map.put("RULE_WHILE", _while());
        map.put("RULE_BODY", body());
        map.put("RULE_EXPRESSION", expression());
        map.put("RULE_X", x());
        map.put("RULE_Y", y());
        map.put("RULE_R", R());
        map.put("RULE_E", E());
        map.put("RULE_A", A());
        map.put("RULE_B", B());
        map.put("RULE_C", C());
        return map;
    }

    public static Set<String> program() {
        return Set.of("class");
    }

    public static Set<String> methods() {
        return types();
    }

    public static Set<String> types() {
        return Set.of("int", "float", "boolean", "char", "string", "void");
    }

    public static Set<String> globalAttribute() {
        return types();
    }

    public static Set<String> params() {
        return types();
    }

    public static Set<String> assignment() {
        return Set.of("IDENTIFIER");
    }

    public static Set<String> variable() {
        return types();
    }

    public static Set<String> _return() {
        return Set.of("return");
    }

    public static Set<String> call() {
        return Set.of("IDENTIFIER");
    }

    public static Set<String> arguments() {
        return expression();
    }

    public static Set<String> _for() {
        return Set.of("for");
    }

    public static Set<String> _switch() {
        return Set.of("switch");
    }

    public static Set<String> doWhile() {
        return Set.of("do");
    }

    public static Set<String> _if() {
        return Set.of("if");
    }

    public static Set<String> _while() {
        return Set.of("while");
    }

    public static Set<String> body() {
        Set<String> set = new HashSet<>();
        set.addAll(types());
        set.add("IDENTIFIER");
        set.add("return");
        set.add("if");
        set.add("while");
        set.add("for");
        set.add("switch");
        set.add("do");
        set.add(";");
        return set;
    }

    public static Set<String> expression() {
        return x();
    }

    public static Set<String> x() {
        return y();
    }

    public static Set<String> y() {
        Set<String> set = new HashSet<>();
        set.addAll(R());
        set.add("!");
        return set;
    }

    public static Set<String> R() {
        return E();
    }

    public static Set<String> E() {
        return A();
    }

    public static Set<String> A() {
        return B();
    }

    public static Set<String> B() {
        Set<String> set = new HashSet<>();
        set.add("-");
        set.addAll(C());
        return set;
    }

    public static Set<String> C() {
        Set<String> set = new HashSet<>();
        set.add("IDENTIFIER");
        set.add("INTEGER");
        set.add("FLOAT");
        set.add("OCTAL");
        set.add("HEXADECIMAL");
        set.add("BINARY");
        set.add("STRING");
        set.add("CHAR");
        set.add("true");
        set.add("false");
        set.add("(");
        return set;
    }
}

