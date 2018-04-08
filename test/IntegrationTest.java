import com.fasterxml.jackson.databind.JsonNode;
import models.LinksRepository;
import models.links;
import org.junit.Assert;
import org.junit.Test;
import play.libs.Json;
import play.libs.ws.WS;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithServer;

import java.util.concurrent.CompletionStage;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static play.test.Helpers.*;

/**
 *
 */
public class IntegrationTest extends WithServer {

    //测试主页面接口
    @Test
    public void testInServerThroughApp() throws Exception {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/");

        Result result = route(app, request);
        final String body = contentAsString(result);
        assertThat(body, containsString("links"));
    }

    //测试列表api
    @Test
    public void testList() {
        LinksRepository repository = app.injector().instanceOf(LinksRepository.class);
        links links = new links();
        links.setUrl("http://www.baidu.com");
        repository.add(links);

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/listrest");

        Result result = route(app, request);
        final String body = contentAsString(result);
        Assert.assertThat(body, containsString("http://www.baidu.com"));
    }

    //测试系统添加api
    @Test
    public void  testAddLinksRest(){
        LinksRepository repository = app.injector().instanceOf(LinksRepository.class);
        links links = new links();
        links.setUrl("http://www.baidu.com");
        links.setKeyword("");
        JsonNode jsonNode = Json.toJson(links);

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .bodyJson(jsonNode)
                .uri("/linksRest");

        Result result = route(app, request);
        Http.RequestBuilder requestresult = new Http.RequestBuilder()
                .method(GET)
                .uri("/listrest");
        Result result1 = route(app,requestresult);
        final String body = contentAsString(result1);
        Assert.assertThat(body, containsString("\"url\":\"http://www.baidu.com\""));
    }

    //测试自定义api
    @Test
    public void  testAddDIYLinksRest(){
        LinksRepository repository = app.injector().instanceOf(LinksRepository.class);
        links links = new links();
        links.setUrl("http://www.baidu.com");
        links.setKeyword("123456");
        JsonNode jsonNode = Json.toJson(links);

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .bodyJson(jsonNode)
                .uri("/linksRest");

        Result result = route(app, request);
        final String body = contentAsString(result);
        Assert.assertThat(body, containsString("\"keyword\":\"123456\""));
    }

    //测试自定义api规则
    @Test
    public void  testAddDIYLinksRestRule(){
        LinksRepository repository = app.injector().instanceOf(LinksRepository.class);
        links links = new links();
        links.setUrl("http://www.baidu.com");
        links.setKeyword("1234567");
        JsonNode jsonNode = Json.toJson(links);

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .bodyJson(jsonNode)
                .uri("/linksRest");

        Result result = route(app, request);
        final String body = contentAsString(result);
        Assert.assertEquals(BAD_REQUEST,result.status());
    }




}
