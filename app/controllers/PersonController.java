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
import scala.collection.immutable.List;

import javax.inject.Inject;
import java.util.*;
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
    public static final char[] array={'q','w','e','1','r','t','y','u','i','o','p','a','3','s','d','f','g','h','j','k','l','z','x','c','v','b','n','m','0','2','5','9','Q','W','E','R','T','4','Y','U','I','O','P','A','S','D','F','G','H','J','K','8','L','Z','X','C','V','6','7','B','N','M'};


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
    //todo httpprotocol
    public CompletionStage<Result> addLinks(){
        links links = formFactory.form(links.class).bindFromRequest().get();
        return linksRepository.add(links).thenApplyAsync(p->{
            return null;
        },ec.current()).thenComposeAsync(a->{
            return linksRepository.list().thenApplyAsync(linksStream -> {
                java.util.List<links> list=linksStream.collect(Collectors.toList());
                String comp;
                java.util.List<links> list2=list.stream().filter(links1 ->compressLongUrl(links.getId()) !=links1.getKeyword()).collect(Collectors.toList());
                /*if (list2.size()>0){
                    //重复
                    comp=compressLongUrl(list.stream().filter(links1 -> links1.getType()!="system").findAny().get().getId());
                }else{
                    //未重复*/
                    comp=compressLongUrl(links.getId());
                //}
                links.setKeyword(comp);
                linksRepository.update(links);
                return ok(views.html.index.render(200,links.getKeyword()));
            },ec.current());
        });
        /*return linksRepository.list().thenApplyAsync(linksStream -> {
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
        });*/
        /*return linksRepository.add(links).thenApplyAsync(p->{
            return ok(views.html.index.render(200));
        },ec.current());*/
    }

    public CompletionStage<Result> getLinks(){
        return linksRepository.list().thenApplyAsync(linksStream -> {
            return ok(views.html.list.render(linksStream.collect(Collectors.toList())));
        },ec.current());
    }

    public CompletionStage<Result> transfer(String url){
        return linksRepository.list().thenApplyAsync(linksStream -> {
            return linksStream.filter(x->x.getKeyword().equals(url)).collect(Collectors.toList());
        },ec.current()).thenComposeAsync(list->{
            if (list==null||list.size()==0){
                return CompletableFuture.supplyAsync(()->notFound());
            }
            else if (list.size()==1){
                return CompletableFuture.supplyAsync(()->found(list.get(0).getUrl()));
            }else{
                return CompletableFuture.supplyAsync(()->internalServerError());
            }
        });
    }

    public String compressLongUrl(Long  id){
        Long rest=id;
        Stack<Character> stack=new Stack<Character>();
        StringBuilder result=new StringBuilder(0);
        while(rest!=0){
            stack.add(array[new Long((rest-(rest/62)*62)).intValue()]);
            rest=rest/62;
        }
        for(;!stack.isEmpty();){
            result.append(stack.pop());
        }
        String key = result.toString();
        while(key.length()<6){
            key="0"+key;
        }
        return key;
    }
}
