package fctreddit.impl.server.java;

import fctreddit.Discovery;
import fctreddit.api.User;
import fctreddit.api.java.Image;
import fctreddit.api.java.Result;

import fctreddit.clients.java.UsersClient;
import fctreddit.clients.rest.UserClients.RestUsersClient;
import fctreddit.impl.server.persistence.Hibernate;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.net.URI;
import java.util.UUID;
import java.util.logging.Logger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JavaImage implements Image {

    public static Discovery discovery;

    private static Logger Log = Logger.getLogger(JavaUsers.class.getName());

    private Hibernate hibernate;

    public JavaImage() {
        hibernate = Hibernate.getInstance();
    }

    @Override
    public Result<String> createImage(String userId, byte[] imageContents, String password) {

        if (imageContents.length == 0 || password == null) {
            Log.info("Image or password null.");
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        URI[] uri = discovery.knownUrisOf("Users", 1);
        UsersClient client = new RestUsersClient(uri[0]);
        User user = client.getUser(userId, password).value();

        if (user == null) {
            Log.info("User does not exist.");
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        String pwd = user.getPassword();

        if(!pwd.equals(password)) {
            Log.info("Password is incorrect.");
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }

        String imageId = UUID.randomUUID().toString();

        try {
            Path imagePath = Paths.get("images/" + imageId + ".jpg");
            Files.createDirectories(imagePath.getParent());
            Files.write(imagePath, imageContents);
        } catch (IOException e) {
            Log.severe("Error saving image: " + e.getMessage());
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }

        String imageUrl = uri[0] + imageId + ".jpg";


        return Result.ok(imageUrl);
    }

    @Override
    public Result<byte[]> getImage(String userId, String imageId) {
        return null;
    }

    @Override
    public Result<Void> deleteImage(String userId, String imageId, String password) {
        return null;
    }
}
