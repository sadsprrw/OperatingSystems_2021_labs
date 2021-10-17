package os.lab1.compfuncs.basic;

import java.util.Optional;

public class Conjunction {
    public static Optional<Integer> trialF(Integer x){
        return Optional.of(x != 0 ? 1 : 0);
    }

    public static Optional<Integer> trialG(Integer x){
        return Optional.of(x + 5 != 0 ? 1 : 0);
    }
}
