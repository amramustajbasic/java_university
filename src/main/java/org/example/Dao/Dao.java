package org.example.Dao;

import org.example.Exceptions.UniException;

import java.sql.SQLException;
import java.util.List;
public interface Dao<T>{
    T getById(int id) throws UniException;
    T add(T item) throws UniException;
    T update(T item) throws UniException;
    void delete(int id) throws UniException;
    List<T> getAll() throws SQLException, UniException;
}





