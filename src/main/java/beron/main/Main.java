package beron.main;

import beron.framework.Mandatum;
import beron.framework.TxtConfigLoader;

public class Main {
    public static void main(String[] args) {
        String configPath = args[0];
        Mandatum mandatum = new Mandatum(new TxtConfigLoader(),configPath);
        mandatum.run();
    }
}
