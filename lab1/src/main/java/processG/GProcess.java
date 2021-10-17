package processG;

import com.computation.ComputationClient;

import java.io.IOException;

public class GProcess {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Connected, starting G process...");
        ComputationClient g = new ComputationClient('G');
        g.compute(args[0]);
    }
}
