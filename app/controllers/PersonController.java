package controllers;

import models.LinksRepository;
import models.Person;
import models.PersonRepository;
import models.links;
import play.data.FormFactory;
import play.http.HttpEntity;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Call;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static play.libs.Json.toJson;

/**
 * The controller keeps all database operations behind the repository, and uses
 * {@link play.libs.concurrent.HttpExecutionContext} to provide access to the
 * {@link play.mvc.Http.Context} methods like {@code request()} and {@code flash()}.
 */
public class PersonController extends Controller {

    private final FormFactory formFactory;
    private final PersonRepository personRepository;
    private final LinksRepository linksRepository;
    private final HttpExecutionContext ec;

    @Inject
    public PersonController(FormFactory formFactory, PersonRepository personRepository, HttpExecutionContext ec,LinksRepository linksRepository) {
        this.formFactory = formFactory;
        this.linksRepository=linksRepository;
        this.personRepository = personRepository;
        this.ec = ec;
    }

    public Result index() {
        return ok(views.html.index.render(200,null));
    }

    public CompletionStage<Result> addPerson() {
        Person person = formFactory.form(Person.class).bindFromRequest().get();

        return personRepository.add(person).thenApplyAsync(p -> {
            return redirect(routes.PersonController.index());
        }, ec.current());
    }

    public CompletionStage<Result> getPersons() {
        return personRepository.list().thenApplyAsync(personStream ->
             found("http://www.baidu.com")
        , ec.current());
    }

    public CompletionStage<Result> addLinks(){
        links links = formFactory.form(links.class).bindFromRequest().get();
        links.setKeyword("http://localhost:9000/1112");
        return linksRepository.list().thenApplyAsync(linksStream -> {
            return linksStream.filter(link->link.getUrl()==links.getUrl()).collect(Collectors.toList());
        },ec.current()).thenComposeAsync(list->{
            if (list==null||list.size()==0){
                //无短地址，新建并返回
                return linksRepository.add(links).thenApplyAsync(p->{
                    return ok(views.html.index.render(200,links.getKeyword()));
                },ec.current());
            }else{
                //已有短地址
                return CompletableFuture.supplyAsync(()->ok(views.html.index.render(200,list.get(0).getKeyword())));
            }
        });
        /*return linksRepository.add(links).thenApplyAsync(p->{
            return ok(views.html.index.render(200));
        },ec.current());*/
    }

    public CompletionStage<Result> getLinks(){
        return linksRepository.list().thenApplyAsync(linksStream -> {
            return ok(toJson(linksStream.collect(Collectors.toList())));
        },ec.current());
    }

}
