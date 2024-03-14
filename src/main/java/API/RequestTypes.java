package API;

public enum RequestTypes {

    MESSAGE(""),
    LOGIN("API:LOGIN:"),
    REGISTER("API:REGISTER:");

    private final String identifier;

    RequestTypes(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier(){
        return identifier;
    }

}
