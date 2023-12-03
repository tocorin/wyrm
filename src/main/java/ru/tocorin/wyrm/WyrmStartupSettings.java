package ru.tocorin.wyrm;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class WyrmStartupSettings {
    private final CommandLine cmd;
    public WyrmStartupSettings(String[] args) throws ParseException {
        DefaultParser parser = new DefaultParser();
        cmd = parser.parse(getOptions(), args);
    }

    private Options getOptions() {
        final Options options = new Options();
        options.addOption("p", true, "port");
        return options;
    }

    /**
     * Get server port
     * @return port number, if undefined that returns 8080 by default
     */
    public int getPort(){
        String value = cmd.getOptionValue("p", "8080");
        return Integer.parseInt(value);
    }
}
