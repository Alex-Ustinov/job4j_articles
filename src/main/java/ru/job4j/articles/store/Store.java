package ru.job4j.articles.store;

import ru.job4j.articles.model.Article;

import java.util.List;

public interface Store<T> {
    T save(T model);
    List<T> findAll();
}
