package ru.job4j.articles.service.generator;

import ru.job4j.articles.model.Article;
import ru.job4j.articles.model.Word;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RandomArticleGenerator implements ArticleGenerator {
    @Override
    public Article generate(List<Word> words) {
        var wordsCopy = new ArrayList<SoftReference<Word>>();
        for (int i = 0; i < words.size(); i++) {
            wordsCopy.add(new SoftReference<Word>(words.get(i)));
            words.remove(i);
        }
        Collections.shuffle(wordsCopy);
        var content = wordsCopy.stream()
                .map(item -> item.get().getValue())
                .collect(Collectors.joining(" "));
        return new Article(content);
    }
}
