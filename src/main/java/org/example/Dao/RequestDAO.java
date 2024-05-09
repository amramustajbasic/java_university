package org.example.Dao;
import org.example.Entity.*;
import org.example.Exceptions.UniException;

public interface RequestDAO {
    Request getRequestById(int requestId);
    void addRequest(Request request,int max_id)throws UniException;
    void updateRequest(Request request);
    void deleteRequest(Request request);
    // Other methods related to Request entity
}