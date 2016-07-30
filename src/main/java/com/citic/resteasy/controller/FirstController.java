package com.citic.resteasy.controller;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import org.springframework.stereotype.Controller;

import com.citic.resteasy.controller.request.Person;
import com.citic.resteasy.pojo.response.HelloWorld;

@Controller
@Path("/hello")
public class FirstController {

	@GET
	@Path("/world")
	@Produces("application/json")
	public HelloWorld helloworld(@QueryParam("message") String message) throws Exception {
		
		return new HelloWorld(message);
	}

	@GET
	@Path("/auth")
	@Produces("application/json")
	public HelloWorld auth(@Context SecurityContext context) {
		return new HelloWorld(context.getUserPrincipal().getName());
	}

    /**
     * 因为自动转换为实例出错，所有直接获取参数，再创建实例
     * @param name
     * @param id
     * @return
     */
    @POST
    @Path("/form")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Produces({ MediaType.APPLICATION_JSON })
	public Person save(@FormParam("name") String name, @FormParam("id") int id) {
    	
		return new Person(id, name);
	}

	@POST
	@Path("/json")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Person> save(List<Person> articles) {
		
		return articles;
	}

}
