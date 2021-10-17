package os.lab1.compfuncs.basic;

import java.util.Optional;

public class Disjunction {
    public static Optional<Integer> trialF(Integer x){
        return Optional.of(x-1 != 0 ? 1 : 0);
    }

    public static Optional<Integer> trialG(Integer x){
        return Optional.of(x+1 == 1 ? 1 : 0);
    }
}
