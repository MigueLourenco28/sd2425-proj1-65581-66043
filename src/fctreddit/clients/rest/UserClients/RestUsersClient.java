package fctreddit.clients.rest.UserClients;

import java.net.URI;

import java.util.List;


import fctreddit.clients.java.UsersClient;
import jakarta.ws.rs.core.GenericType;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import fctreddit.api.java.Result;
import fctreddit.api.User;
import fctreddit.api.rest.RestUsers;
import fctreddit.api.java.Result.ErrorCode;

public class RestUsersClient extends UsersClient  {


	protected static final int READ_TIMEOUT = 5000;
	protected static final int CONNECT_TIMEOUT = 5000;

	protected static final int MAX_RETRIES = 10;
	protected static final int RETRY_SLEEP = 5000;

	
	final URI serverURI;
	final Client client;
	final ClientConfig config;

	final WebTarget target;
	
	public RestUsersClient( URI serverURI ) {
		this.serverURI = serverURI;

		this.config = new ClientConfig();
		
		config.property( ClientProperties.READ_TIMEOUT, READ_TIMEOUT);
		config.property( ClientProperties.CONNECT_TIMEOUT, CONNECT_TIMEOUT);

		
		this.client = ClientBuilder.newClient(config);

		target = client.target( serverURI ).path( RestUsers.PATH );
	}

	public Result<String> createUser(User user) {

		Response r = executeOperationsPost(target.request().accept(MediaType.APPLICATION_JSON),
				Entity.entity(user, MediaType.APPLICATION_JSON));

		if(r == null){
			return Result.error(ErrorCode.TIMEOUT);
		}

		int status = r.getStatus();
		if(status != Status.OK.getStatusCode()){
			return Result.error(getErrorCodeFrom(status));
		}else{
			return Result.ok(r.readEntity(String.class));
		}
	}

	public Result<User> getUser(String userId, String pwd) {

		Response r = executeOperationsGet(target.path( userId ).queryParam(RestUsers.PASSWORD, pwd).request()
				.accept(MediaType.APPLICATION_JSON));

		if(r == null){
			return Result.error(ErrorCode.TIMEOUT);
		}

		int status = r.getStatus();
		if(status != Status.OK.getStatusCode()){
			return Result.error(getErrorCodeFrom(status));
		}else{
			return Result.ok(r.readEntity(User.class));
		}
	}



	public Result<User> updateUser(String userId, String password, User user) {

		Response r = executeOperationsPut(target.path( userId ).queryParam(RestUsers.PASSWORD, password).request().accept(MediaType.APPLICATION_JSON),
				Entity.entity(user, MediaType.APPLICATION_JSON));

		if(r == null){
			return Result.error(ErrorCode.TIMEOUT);
		}

		int status = r.getStatus();
		if(status != Status.OK.getStatusCode()){
			return Result.error(getErrorCodeFrom(status));
		}else{
			return Result.ok(r.readEntity(User.class));
		}

	}

	public Result<User> deleteUser(String userId, String password) {
		Response r = executeOperationsDelete(target.path( userId ).queryParam(RestUsers.PASSWORD, password).request()
				.accept(MediaType.APPLICATION_JSON));

		if(r == null){
			return Result.error(ErrorCode.TIMEOUT);
		}

		int status = r.getStatus();
		if(status != Status.OK.getStatusCode()){
			return Result.error(getErrorCodeFrom(status));
		}else{
			return Result.ok(r.readEntity(User.class));
		}
	}


	public Result<List<User>> searchUsers(String pattern) {
		Response r = executeOperationsGet(target.queryParam(RestUsers.QUERY,pattern).request()
				.accept(MediaType.APPLICATION_JSON));

		if(r == null){
			return Result.error(ErrorCode.TIMEOUT);
		}

		int status = r.getStatus();
		if(status != Status.OK.getStatusCode()){
			return Result.error(getErrorCodeFrom(status));
		}else{
			return Result.ok(r.readEntity(new GenericType<List<User>>() {}));
		}

	}


	public static ErrorCode getErrorCodeFrom(int status) {
		return switch (status) {
		case 200, 209 -> ErrorCode.OK;
		case 409 -> ErrorCode.CONFLICT;
		case 403 -> ErrorCode.FORBIDDEN;
		case 404 -> ErrorCode.NOT_FOUND;
		case 400 -> ErrorCode.BAD_REQUEST;
		case 500 -> ErrorCode.INTERNAL_ERROR;
		case 501 -> ErrorCode.NOT_IMPLEMENTED;
		default -> ErrorCode.INTERNAL_ERROR;
		};
	}
}