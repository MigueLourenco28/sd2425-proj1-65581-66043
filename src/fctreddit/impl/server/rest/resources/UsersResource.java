package fctreddit.impl.server.rest.resources;

import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

import fctreddit.api.java.Result;
import fctreddit.api.java.Users;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;
import fctreddit.api.User;
import fctreddit.api.rest.RestUsers;
import fctreddit.impl.server.java.JavaUsers;

public class UsersResource implements RestUsers {

	private static Logger Log = Logger.getLogger(UsersResource.class.getName());

	//private Hibernate hibernate;

	private Users impl;
	
	public UsersResource() {
		//hibernate = Hibernate.getInstance();
		impl = new JavaUsers();
	}

	@Override
	public String createUser(User user) {
		Log.info("createUser : " + user);
		
		Result<String> res = impl.createUser(user);
		if(!res.isOK()) {
			throw new WebApplicationException(errorCodeToStatus(res.error()));
		}
		return res.value();
	}

	@Override
	public User getUser(String userId, String password) {
		Log.info("getUser : user = " + userId + "; pwd = " + password);

		Result<User> res = impl.getUser(userId, password);
		if(!res.isOK()) {
			throw new WebApplicationException(errorCodeToStatus(res.error()));
		}
		return res.value();
	}

	@Override
	public User updateUser(String userId, String password, User user) {
		Log.info("updateUser : user = " + userId + "; pwd = " + password + " ; userData = " + user);
		//---------------Added code------------------//
		Result<User> res = impl.updateUser(userId, password, user);
		if(!res.isOK()) {
			throw new WebApplicationException(errorCodeToStatus(res.error()));
		}
		return res.value();
		//---------------End of added code------------------//
	}

	@Override
	public User deleteUser(String userId, String password) {
		Log.info("deleteUser : user = " + userId + "; pwd = " + password);
		//---------------Added code------------------//
		Result<User> res = impl.deleteUser(userId, password);
		if(!res.isOK()) {
			throw new WebApplicationException(errorCodeToStatus(res.error()));
		}
		return res.value();
		//---------------End of added code------------------//
	}

	@Override
	public List<User> searchUsers(String pattern) {
		Log.info("searchUsers : pattern = " + pattern);
		
		Result<List<User>> res = impl.searchUsers(pattern);
		
		if(!res.isOK())
			throw new WebApplicationException(errorCodeToStatus(res.error()));
		
		return res.value();
	}

	/**
	@Override
	public void associateAvatar(String userId, String password, byte[] avatar) {
		Log.info("associate an avatar : user = " + userId + "; pwd = " + password + "; avatarSize = " + avatar.length);

		Result<void> res = impl.associateAvatar(userId, password, avatar);
		if(!res.isOK()) {
			throw new WebApplicationException(errorCodeToStatus(res.error()));
		}
		return res.value();
		
	}

	@Override
	public void removeAvatar(String userId, String password) {
		Log.info("delete an avatar : user = " + userId + "; pwd = " + password);
		
		
	}

	@Override
	public byte[] getAvatar(String userId) {

	}
	*/

	protected static Status errorCodeToStatus( Result.ErrorCode error ) {
    	Status status =  switch( error) {
    	case NOT_FOUND -> Status.NOT_FOUND; 
    	case CONFLICT -> Status.CONFLICT;
    	case FORBIDDEN -> Status.FORBIDDEN;
    	case NOT_IMPLEMENTED -> Status.NOT_IMPLEMENTED;
    	case BAD_REQUEST -> Status.BAD_REQUEST;
    	default -> Status.INTERNAL_SERVER_ERROR;
    	};
    	
    	return status;
    }

}