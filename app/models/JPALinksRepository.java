package models;

import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class JPALinksRepository implements LinksRepository{

    private final JPAApi jpaApi;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public JPALinksRepository(JPAApi jpaApi, DatabaseExecutionContext executionContext) {
        this.jpaApi = jpaApi;
        this.executionContext = executionContext;
    }
    
    @Override
    public CompletionStage<links> add(links links) {
        return supplyAsync(() -> wrap(em -> insert(em, links)), executionContext);
    }

    @Override
    public CompletionStage<Stream<links>> list() {
        return supplyAsync(() -> wrap(em -> list(em)), executionContext);
    }

    @Override
    public CompletionStage<links> update(links links){
        return supplyAsync(()->wrap(em->merge(em,links)),executionContext);
    }

    @Override
    public CompletionStage<links> delete (links links){
        return supplyAsync(()->wrap(em->remove(em,links)),executionContext);
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }

    private links merge(EntityManager em, links links){
        em.merge(links);
        return links;
    }

    private links insert(EntityManager em, links links) {
        em.persist(links);
        return links;
    }

    private links remove(EntityManager em,links links){
        em.remove(links);
        return links;
    }

    private Stream<links> list(EntityManager em) {
        List<links> links = em.createQuery("select p from links p", links.class).getResultList();
        return links.stream();
    }
}
