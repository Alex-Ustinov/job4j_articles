package ru.job4j.articles.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.articles.model.Article;
import ru.job4j.articles.model.Word;
import ru.job4j.articles.service.generator.ArticleGenerator;
import ru.job4j.articles.store.Store;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SimpleArticleService implements ArticleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleArticleService.class.getSimpleName());

    private final ArticleGenerator articleGenerator;

    public SimpleArticleService(ArticleGenerator articleGenerator) {
        this.articleGenerator = articleGenerator;
    }

    @Override
    public void generate(Store<Word> wordStore, int count, Store<Article> articleStore) {
        LOGGER.info("Геренация статей в количестве {}", count);
        var words = wordStore.findAll();
        WeakHashMap articles = new WeakHashMap<SoftReference<Article>, SoftReference<Article>>();
        for (int i = 0; i < count; i++) {
            LOGGER.info("Сгенерирована статья № {}", i);
            Article article = articleGenerator.generate(words);
            SoftReference<Article> softReference = new SoftReference(article);
            articles.put(softReference, softReference);
        }
        for (int h = 0; h < articles.size(); h++) {
            SoftReference<Article> softReference = (SoftReference) articles.get(h);
            Article article = softReference.get();
            articleStore.save(article);
        }
    }
}
