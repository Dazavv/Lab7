package me.dasha.lab7.utility;

import me.dasha.lab7.collectionClasses.SpaceMarine;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;

public class Response implements Serializable {
    private ResponseStatus responseStatus;
    private String response = "";
    private Collection<SpaceMarine> collection;

    public Response() {
    }

    public Response(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }

    public Response(ResponseStatus responseStatus, String response) {
        this.responseStatus = responseStatus;
        this.response = response.trim() + "\n";
    }

    public Response(ResponseStatus responseStatus, String response, Collection<SpaceMarine> collection) {
        this.responseStatus = responseStatus;
        this.response = response.trim();
        this.collection = collection.stream()
                .sorted(Comparator.comparing(SpaceMarine::getId))
                .toList();
    }
    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }

    public String getResponse() {
        return response;
    }

    public Collection<SpaceMarine> getCollection() {
        return collection;
    }
    @Override
    public String toString(){
        return "Response[" + responseStatus +
                (response.isEmpty()
                        ? ""
                        :',' + response) +
                (collection == null
                        ? ']'
                        : ',' + collection.toString() + ']');
    }

    @Override
    public int hashCode() {
        return Objects.hash(responseStatus, response, collection);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Response response1)) return false;
        return responseStatus == response1.responseStatus && Objects.equals(response, response1.response) && Objects.equals(collection, response1.collection);
    }
}
