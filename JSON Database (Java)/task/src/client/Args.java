package client;

import com.beust.jcommander.Parameter;

public class Args {

    @Parameter(names = "-t", required = true)
    private String type;

    @Parameter(names = "-i")
    private String index;

    @Parameter(names = "-m")
    private String value;

    public String getRequestString() {
        if (type.equals("set")) {
            return type + " " + index + " " + value;
        } else {
            return type + " " + index;
        }
    }
}