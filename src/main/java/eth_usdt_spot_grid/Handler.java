package eth_usdt_spot_grid;

import java.util.Map;
import java.util.function.Function;

class Request {
    private String httpMethod;
    private Map<String, String> headers;
}

class Response {
    private int statusCode;
    private String body;

    public Response(int statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
    }
}

public class Handler implements Function<Request, Response> {
    @Override
    public Response apply(Request request) {
        Main.main(new String[0]);
        return new Response(200, "Hello, thanks i'm fine!");
    }
}