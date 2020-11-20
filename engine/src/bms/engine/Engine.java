package bms.engine;

public class Engine implements BMSEngine{
    @Override
    public String foo(String word) {
        return word + " in Engine";
    }
}
