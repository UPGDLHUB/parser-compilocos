import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;

public class FollowsSet {

    // Map of rule names to their FOLLOW sets
    public static final Map<String, Set<String>> FOLLOW_MAP = createFollowMap();

    private static Map<String, Set<String>> createFollowMap() {
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
        return Set.of("$", "}");
    }

    public static Set<String> methods() {
        return Set.of("int", "float", "boolean", "char", "string", "void", "}");
    }

    public static Set<String> types() {
        return Set.of("IDENTIFIER");
    }

    public static Set<String> globalAttribute() {
        return methods();
    }

    public static Set<String> params() {
        return Set.of(")", ",");
    }

    public static Set<String> assignment() {
        return Set.of(";", ")", ",", ":");
    }

    public static Set<String> variable() {
        return Set.of(";", ")", ",", ":");
    }

    public static Set<String> _return() {
        return body();
    }

    public static Set<String> call() {
        return Set.of(";");
    }

    public static Set<String> arguments() {
        return Set.of(")");
    }

    public static Set<String> _for() {
        return body();
    }

    public static Set<String> _switch() {
        return body();
    }

    public static Set<String> doWhile() {
        return body();
    }

    public static Set<String> _if() {
        return body();
    }

    public static Set<String> _while() {
        return body();
    }

    public static Set<String> body() {
        Set<String> set = new HashSet<>();
        set.addAll(FirstsSet.body());
        set.add("}");
        set.add("case");
        set.add("default");
        set.add("else");
        return set;
    }

    public static Set<String> expression() {
        return Set.of(")", ";", ",", ":", "}");
    }

    public static Set<String> x() {
        Set<String> set = new HashSet<>();
        set.addAll(Set.of("||"));
        set.addAll(expression());
        return set;
    }

    public static Set<String> y() {
        Set<String> set = new HashSet<>();
        set.addAll(Set.of("&&"));
        set.addAll(x());
        return set;
    }

    public static Set<String> R() {
        Set<String> set = new HashSet<>();
        set.addAll(Set.of("!=","==",">","<"));
        set.addAll(y());
        return set;
    }

    public static Set<String> E() {
        Set<String> set = new HashSet<>();
        set.addAll(Set.of("+","-"));
        set.addAll(R());
        return set;
    }

    public static Set<String> A() {
        Set<String> set = new HashSet<>();
        set.addAll(Set.of("*","/"));
        set.addAll(E());
        return set;
    }

    public static Set<String> B() {
        return A();
    }

    public static Set<String> C() {
        return A();
    }
}