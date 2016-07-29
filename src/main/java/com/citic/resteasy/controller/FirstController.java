package com.citic.resteasy.controller;

import java.util.List;

import javax.ws.rs.*;
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
	public HelloWorld helloworld() throws Exception {
		return new HelloWorld("Welcome, HelloWorld");
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
	public List<Person> save(@QueryParam("multi") boolean isMulti, List<Person> articles) {
		
		return articles;
	}

}
