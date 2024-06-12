package managers;

import utility.Response;

import java.io.ObjectOutputStream;

public class ConnectionManagerPool {
    private Response response;
    private ObjectOutputStream objectOutputStream;

    public ConnectionManagerPool(Response response, ObjectOutputStream objectOutputStream) {
        this.response = response;
        this.objectOutputStream = objectOutputStream;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public ObjectOutputStream getObjectOutputStream() {
        return objectOutputStream;
    }

    public void setObjectOutputStream(ObjectOutputStream objectOutputStream) {
        this.objectOutputStream = objectOutputStream;
    }
}
