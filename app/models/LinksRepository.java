package models;

import com.google.inject.ImplementedBy;

import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

@ImplementedBy(JPALinksRepository.class)
public interface LinksRepository {
    CompletionStage<links> add(links links);

    CompletionStage<Stream<links>> list();

    CompletionStage<links> update(links links);
}
