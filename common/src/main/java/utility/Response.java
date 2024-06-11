package utility;

import models.StudyGroup;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;

public class Response implements Serializable {
    private final ResponseStatus responseStatus;
    private String response;
    private Collection<StudyGroup> collection;
    public Response(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }
    public Response(ResponseStatus responseStatus, String response) {
        this.responseStatus = responseStatus;
        this.response = response;
    }

    public Response(ResponseStatus responseStatus, String response, Collection<StudyGroup> collection) {
        this.responseStatus = responseStatus;
        this.response = response.trim();
        this.collection = collection.stream()
                .sorted(Comparator.comparing(StudyGroup::getDistance)) // клиенту передаётся коллекция, упорядоченная по местоположению
                .toList();
    }

    public Collection<StudyGroup> getCollection() {
        return collection;
    }

    public void setCollection(Collection<StudyGroup> collection) {
        this.collection = collection;
    }

    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Response response1 = (Response) o;
        return responseStatus == response1.responseStatus && Objects.equals(response, response1.response);
    }

    @Override
    public int hashCode() {
        return Objects.hash(responseStatus, response);
    }

    @Override
    public String toString() {
        return "Response{" +
                "responseStatus=" + responseStatus +
                ", response='" + response + '\'' +
                '}';
    }
}
