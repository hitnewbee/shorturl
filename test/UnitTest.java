import controllers.LinksController;
import models.LinksRepository;
import models.links;
import org.junit.Test;
import play.data.FormFactory;
import play.data.format.Formatters;
import play.i18n.MessagesApi;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Http;
import play.mvc.Result;
import play.twirl.api.Content;

import javax.validation.Validator;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ForkJoinPool;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static play.mvc.Http.Status.OK;
import static play.mvc.Http.Status.SEE_OTHER;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.invokeWithContext;

/**
 *
 */
public class UnitTest {

    @Test
    public void checkIndex() {
        LinksRepository repository = mock(LinksRepository.class);
        FormFactory formFactory = mock(FormFactory.class);
        HttpExecutionContext ec = new HttpExecutionContext(ForkJoinPool.commonPool());
        final LinksController controller = new LinksController(formFactory, ec, repository);
        final Result result = controller.index();

        assertThat(result.status()).isEqualTo(OK);
    }

    @Test
    public void checkTemplate() {
        Content html = views.html.index.render(null,null);
        assertThat(html.contentType()).isEqualTo("text/html");
        assertThat(contentAsString(html)).contains("links");
    }



}
