package bettybook.http.longv;

import static bettybook.http.longv.HttpLong.Http.Http.AcceptR;
import static bettybook.http.longv.HttpLong.Http.Http.Body;
import static bettybook.http.longv.HttpLong.Http.Http.C;
import static bettybook.http.longv.HttpLong.Http.Http.ContentL;
import static bettybook.http.longv.HttpLong.Http.Http.ContentT;
import static bettybook.http.longv.HttpLong.Http.Http.Date;
import static bettybook.http.longv.HttpLong.Http.Http.ETag;
import static bettybook.http.longv.HttpLong.Http.Http.HttpV;
import static bettybook.http.longv.HttpLong.Http.Http.LastM;
import static bettybook.http.longv.HttpLong.Http.Http.S;
import static bettybook.http.longv.HttpLong.Http.Http.Server;
import static bettybook.http.longv.HttpLong.Http.Http.StrictTS;
import static bettybook.http.longv.HttpLong.Http.Http.Vary;
import static bettybook.http.longv.HttpLong.Http.Http.Via;
import static bettybook.http.longv.HttpLong.Http.Http._200;
import static bettybook.http.longv.HttpLong.Http.Http._404;

import org.scribble.net.Buf;
import org.scribble.net.session.MPSTEndpoint;
import org.scribble.net.session.SocketChannelEndpoint;

import bettybook.http.longv.HttpLong.Http.Http;
import bettybook.http.longv.HttpLong.Http.channels.C.EndSocket;
import bettybook.http.longv.HttpLong.Http.channels.C.Http_C_1;
import bettybook.http.longv.HttpLong.Http.channels.C.Http_C_3;
import bettybook.http.longv.HttpLong.Http.channels.C.Http_C_4_Cases;
import bettybook.http.longv.HttpLong.Http.channels.C.Http_C_5;
import bettybook.http.longv.HttpLong.Http.channels.C.Http_C_5_Cases;
import bettybook.http.longv.HttpLong.Http.roles.C;
import bettybook.http.longv.message.Body;
import bettybook.http.longv.message.HttpLongMessageFormatter;
import bettybook.http.longv.message.client.Host;
import bettybook.http.longv.message.client.RequestLine;

public class HttpLongC {

	public static void main(String[] args) throws Exception {
		Http http = new Http();
		try (MPSTEndpoint<Http, C> client = new MPSTEndpoint<>(http, C, new HttpLongMessageFormatter())) {
			String host = "www.doc.ic.ac.uk"; int port = 80;
			//String host = "localhost"; int port = 8080;
		
			client.connect(S, SocketChannelEndpoint::new, host, port);
			new HttpLongC().run(new Http_C_1(client), host);
		}
	}

	public void run(Http_C_1 s1, String host) throws Exception {
			doResponse(doRequest(s1, host));
	}
	
	private Http_C_3 doRequest(Http_C_1 s1, String host) throws Exception {
		return s1.send(S, new RequestLine("/~rhu/", "1.1"))
		         .send(S, new Host(host))
		         .send(S, new Body(""));
	}

	private void doResponse(Http_C_3 s3) throws Exception {
		Http_C_4_Cases cases = s3.async(S, HttpV).branch(S);
		switch (cases.op) {
			case _200: doResponseAux(cases.receive(_200)); break;
			case _404: doResponseAux(cases.receive(_404)); break;
			default: throw new RuntimeException("[TODO]: " + cases.op);
		}
	}

	private EndSocket doResponseAux(Http_C_5 c5) throws Exception {
		Http_C_5_Cases cases = c5.branch(S);
		switch (cases.op) {
			case AcceptR:  return doResponseAux(cases.receive(AcceptR)); 
			case ContentL: return doResponseAux(cases.receive(ContentL));
			case ContentT: return doResponseAux(cases.receive(ContentT));
			case Date:     return doResponseAux(cases.receive(Date));    
			case ETag:     return doResponseAux(cases.receive(ETag));    
			case LastM:    return doResponseAux(cases.receive(LastM));   
			case Server:   return doResponseAux(cases.receive(Server));  
			case StrictTS: return doResponseAux(cases.receive(StrictTS));
			case Vary:     return doResponseAux(cases.receive(Vary));    
			case Via:      return doResponseAux(cases.receive(Via));     
			case Body: {
				Buf<Body> buf_body = new Buf<>();
				EndSocket end = cases.receive(Body, buf_body);
				System.out.println(buf_body.val.getBody());
				return end;
			}
			default: throw new RuntimeException("[TODO]: " + cases.op);
		}
	}
}
