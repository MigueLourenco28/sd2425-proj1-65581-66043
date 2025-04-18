package fctreddit.clients.rest.ContentClients;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Logger;

import fctreddit.Discovery;

public class GetUpVotesClient {
	
	private static Logger Log = Logger.getLogger(GetUpVotesClient.class.getName());


	public static void main(String[] args) throws IOException {
		
		if( args.length != 1) {
			System.err.println( "Use: java " + GetUpVotesClient.class.getCanonicalName() + " postId");
			return;
		}
		
		Discovery discovery = new Discovery(Discovery.DISCOVERY_ADDR);
		discovery.start();

		URI[] uris = discovery.knownUrisOf("Content", 1);

		String postId = args[0];
		
		var client = new RestContentClient( URI.create( uris[0].toString() ) );
			
		var result = client.getupVotes(postId);
		if( result.isOK()  )
			Log.info("Get UpVotes:" + result.value() );
		else
			Log.info("Get UpVotes failed with error: " + result.error());
		
	}
	
}