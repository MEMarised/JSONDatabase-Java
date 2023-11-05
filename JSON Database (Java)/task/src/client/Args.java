package client;

import com.beust.jcommander.Parameter;

/**
 * @author namvdo
 */
public class Args {
    @Parameter(
            names = "-t",
            description = "The type of the command"
    )
    public String type;

    @Parameter(
            names = "-k",
            description = "K is the key"
    )
    public String index;

    @Parameter(
            names = "-v",
            description = "V is the value to set"
    )
    public String message;
    @Parameter(
            names = "-in",
            description = "The json input file"
    )
    public String jsonFile;
}