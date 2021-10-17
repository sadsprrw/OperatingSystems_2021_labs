package os.lab1.compfuncs.basic;

import java.util.Optional;

public class DoubleOps {
    public static Optional<Double> trialF(Integer x){
        return Optional.of((double)x/3);
    }

    public static Optional<Double> trialG(Integer x){
        return Optional.of((double)3/x);
    }
}
