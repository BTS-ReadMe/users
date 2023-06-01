package bts.users.responseObject;

public enum Status {

    OK(200, "OK"),
    BAD_REQUEST(400, "BAD_REQUEST"),
    NOT_FOUND(404, "NOT_FOUND"),
    INTERNAL_SERER_ERROR(500, "INTERNAL_SERVER_ERROR");

    Integer statusCode;
    String code;

    Status(Integer statusCode, String code) {
        this.statusCode = statusCode;
        this.code = code;
    }
}
