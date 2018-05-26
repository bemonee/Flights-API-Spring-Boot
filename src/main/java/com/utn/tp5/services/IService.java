package com.utn.tp5.services;

public interface IService <E>{
    public E getAll();
    public E getByID(long id);
    public E save(Object obj);
}
