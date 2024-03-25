package API;

public enum RequestTypes {

    MESSAGE(""),
    LOGIN("API:LOGIN:"),
    REGISTER("API:REGISTER:"),
    USER_INFO("API:USER_INFO:");

    private final String identifier;

    RequestTypes(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier(){
        return identifier;
    }

}
