package ru.job4j.articles.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.articles.model.Article;
import ru.job4j.articles.model.Word;
import ru.job4j.articles.service.generator.ArticleGenerator;
import ru.job4j.articles.store.Store;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
        for (int i = 0; i < count; i++) {
            LOGGER.info("Сгенерирована статья № {}", i);
            Article article = articleGenerator.generate(words);
            ReferenceQueue<? extends Article> referenceQueue = new ReferenceQueue();
            SoftReference<Article> softReference = new SoftReference(article, referenceQueue);
            article = null;
            Article articleFromReference = softReference.get();
            if (articleFromReference != null) {
                articleStore.save(articleFromReference);
                softReference.clear();
            } else {
                articleStore.save((Article) referenceQueue.poll());
            }


            //articleStore.save(article);  не работает  переполнение памяти
            //article = null;


//            не работает  переполнение памяти
//            SoftReference<Article> softReference = new SoftReference<>(article);
//            article = null;
//
//            Article articleFromReference = softReference.get();
//            if (articleFromReference != null) {
//                articleStore.save(articleFromReference);
//                softReference.clear();
//            }



//              не работает  переполнение памяти
//            WeakReference<Article> weakReference = new WeakReference<>(article);
//            article = null;
//
//            Article articleFromReference = weakReference.get();
//            if (articleFromReference != null) {
//                articleStore.save(articleFromReference);
//                weakReference.clear();
//            }

        }
    }
}
