package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.LinksRepository;
import models.links;
import play.data.FormFactory;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static play.mvc.Http.MimeTypes.JSON;


/**
 * The controller keeps all database operations behind the repository, and uses
 * {@link play.libs.concurrent.HttpExecutionContext} to provide access to the
 * {@link play.mvc.Http.Context} methods like {@code request()} and {@code flash()}.
 */
public class LinksController extends Controller {

    private final FormFactory formFactory;
    private final LinksRepository linksRepository;
    private final HttpExecutionContext ec;
    public static final char[] array={'q','w','e','1','r','t','y','u','i','o','p','a','3','s','d','f','g','h','j','k','l','z','x','c','v','b','n','m','0','2','5','9','Q','W','E','R','T','4','Y','U','I','O','P','A','S','D','F','G','H','J','K','8','L','Z','X','C','V','6','7','B','N','M'};
    private static final SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    private static Pattern pattern=Pattern.compile("[a-zA-Z0-9]{6,6}");

    @Inject
    public LinksController(FormFactory formFactory, HttpExecutionContext ec,LinksRepository linksRepository) {
        this.formFactory = formFactory;
        this.linksRepository=linksRepository;
        this.ec = ec;

    }

    public Result index() {
        return ok(views.html.index.render(null,null));
    }



    public CompletionStage<Result> addLinks(){
        links links = formFactory.form(links.class).bindFromRequest().get();
        return addLogic(links,false);
    }

    public CompletionStage<Result> addLinksRest(){
        JsonNode jsonNode=request().body().asJson();
        links links=new links();
        links.setUrl(jsonNode.get("url").asText());
        if(jsonNode.has("keyword")){
            links.setKeyword(jsonNode.get("keyword").asText());
        }
        return addLogic(links,true);
    }

    public CompletionStage<Result> addLogic(links links,boolean ifrest){
        return CompletableFuture.supplyAsync(()->{
            Date date = new Date(System.currentTimeMillis());
            links.setInsert_at(formatter.format(date));
            return links;
        }).thenComposeAsync(links1 -> {
            //systemtype
            if (null==links1.getKeyword()||links1.getKeyword().equals("")){
                links1.setType("system");
                return linksRepository.add(links1).thenComposeAsync(x->{
                    return linksRepository.list();
                }).thenApplyAsync(linksStream -> {
                    java.util.List<links> list=linksStream.collect(Collectors.toList());
                    String comp;
                    java.util.List<links> list2=list
                            .stream()
                            .filter(x ->compressLongUrl(links.getId()).equals(x.getKeyword()))
                            .filter(x -> links.getId()==x.getId())
                            .collect(Collectors.toList());
                    if (list2.size()>0){
                        //重复或已自定义
                        comp=compressLongUrl(list.stream().filter(x -> x.getType()!="system").findAny().get().getId());
                    }else{
                        //未重复
                        comp=compressLongUrl(links.getId());
                    }
                    links.setKeyword(comp);
                    linksRepository.update(links);
                    return ifrest? Results.status(Http.Status.OK, Json.toJson(links)).as(JSON):ok(views.html.index.render("create short url success!",links.getKeyword()));
                },ec.current());
            }else{
                //正则匹配
                if (pattern.matcher(links1.getKeyword()).matches()){
                    links1.setType("custom");
                    return linksRepository.list().thenComposeAsync(linksStream -> {
                        java.util.List<links> linksList=linksStream.filter(x->x.getKeyword().equals(links1.getKeyword())).collect(Collectors.toList());
                        if (linksList.size()>0){
                            String tip="this URL has been owned ,plz try another";
                            String message=String.format("{\"message\":%s}",tip);
                            return CompletableFuture.supplyAsync(()->ifrest?Results.status(Http.Status.BAD_REQUEST,message ).as(JSON):ok(views.html.index.render(tip,null)));
                        }else{
                            return  linksRepository.add(links1).thenApplyAsync(list->ifrest?Results.status(Http.Status.OK, Json.toJson(links)).as(JSON):ok(views.html.index.render("create short url success!You must paste URL to address bar and run youself to test function","http://localhost:9000/transfer/"+links1.getKeyword())));
                        }
                    });
                }else{
                    String tip="your short url not match rules";
                    String message=String.format("{\"message\":%s}",tip);
                    return CompletableFuture.supplyAsync(()->ifrest?Results.status(Http.Status.BAD_REQUEST, message).as(JSON):ok(views.html.index.render(tip,null)));
                }
            }
        });
    }

    public CompletionStage<Result> getLinks(){
        return getListLogic(false);
    }

    public CompletionStage<Result> getLinksRest(){
        return getListLogic(true);
    }

    public CompletionStage<Result> getListLogic(boolean ifrest){
        return linksRepository.list().thenApplyAsync(linksStream -> {
            List<links> list =linksStream.collect(Collectors.toList());
            return ifrest?Results.status(Http.Status.OK,Json.toJson(list) ).as(JSON):ok(views.html.list.render(list));
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
                return CompletableFuture.supplyAsync(()->{
                    //访问计数
                    links links = list.get(0);
                    links.setClickcount(links.getClickcount()+1);
                    linksRepository.update(links);
                    return found(list.get(0).getUrl());
                });
            }else{
                return CompletableFuture.supplyAsync(()->internalServerError());
            }
        });
    }

    private String compressLongUrl(Long  id){
        Long rest=id;
        Stack<Character> stack=new Stack<>();
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
