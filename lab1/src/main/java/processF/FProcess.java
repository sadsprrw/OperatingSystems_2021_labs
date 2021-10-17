package processF;

import com.computation.ComputationClient;

import java.io.IOException;

public class FProcess {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Connected, starting F process...");
        ComputationClient f = new ComputationClient('F');
        f.compute(args[0]);
    }
}
