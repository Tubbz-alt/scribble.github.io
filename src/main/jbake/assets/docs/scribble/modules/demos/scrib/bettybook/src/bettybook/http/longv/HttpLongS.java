package bettybook.http.longv;

import static bettybook.http.longv.HttpLong.Http.Http.Accept;
import static bettybook.http.longv.HttpLong.Http.Http.AcceptE;
import static bettybook.http.longv.HttpLong.Http.Http.AcceptL;
import static bettybook.http.longv.HttpLong.Http.Http.Body;
import static bettybook.http.longv.HttpLong.Http.Http.C;
import static bettybook.http.longv.HttpLong.Http.Http.Connection;
import static bettybook.http.longv.HttpLong.Http.Http.DNT;
import static bettybook.http.longv.HttpLong.Http.Http.Host;
import static bettybook.http.longv.HttpLong.Http.Http.RequestL;
import static bettybook.http.longv.HttpLong.Http.Http.S;
import static bettybook.http.longv.HttpLong.Http.Http.UserA;
import static bettybook.http.longv.HttpLong.Http.Http.UpgradeIR;

import java.io.IOException;

import org.scribble.main.ScribbleRuntimeException;
import org.scribble.net.Buf;
import org.scribble.net.scribsock.ScribServerSocket;
import org.scribble.net.scribsock.SocketChannelServer;
import org.scribble.net.session.MPSTEndpoint;

import bettybook.http.longv.HttpLong.Http.Http;
import bettybook.http.longv.HttpLong.Http.channels.S.Http_S_1;
import bettybook.http.longv.HttpLong.Http.channels.S.Http_S_2;
import bettybook.http.longv.HttpLong.Http.channels.S.Http_S_2_Cases;
import bettybook.http.longv.HttpLong.Http.roles.S;
import bettybook.http.longv.message.Body;
import bettybook.http.longv.message.HttpLongMessageFormatter;
import bettybook.http.longv.message.server.ContentLength;
import bettybook.http.longv.message.server.HttpVersion;
import bettybook.http.longv.message.server._200;

public class HttpLongS
{
	public static void main(String[] args) throws Exception
	{
		try (ScribServerSocket ss = new SocketChannelServer(8080)) {
			while (true)	{
				Http http = new Http();
				try (MPSTEndpoint<Http, S> server = new MPSTEndpoint<>(http, S, new HttpLongMessageFormatter())) {
					server.accept(ss, C);
				
					run(new Http_S_1(server));
				}
				catch (IOException | ClassNotFoundException | ScribbleRuntimeException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	private static void run(Http_S_1 s1) throws ClassNotFoundException, ScribbleRuntimeException, IOException {
		Buf<Object> buf = new Buf<>();
		
		Http_S_2 s2 = s1.receive(C, RequestL, buf);
		System.out.println("Requested: " + buf.val);
		
		Y: while (true) {
			Http_S_2_Cases cases = s2.branch(C);
			switch (cases.op) {
				case Accept:  s2 = cases.receive(Accept, buf); break;
				case AcceptE: s2 = cases.receive(AcceptE, buf); break;
				case AcceptL: s2 = cases.receive(AcceptL, buf); break;
				case Body:
				{
					String body = "<html><body>Hello, World!</body></html>";
					cases.receive(Body, buf)
						.send(C, new HttpVersion("1.1"))
						.send(C, new _200("OK"))
						.send(C, new ContentLength(body.length()))
						.send(C, new Body(body));
					break Y;
				}
				case Connection: s2 = cases.receive(Connection, buf); break;
				case DNT:        s2 = cases.receive(DNT, buf); break;
				case UpgradeIR:  s2 = cases.receive(UpgradeIR, buf); break;
				case Host:       s2 = cases.receive(Host, buf); break;
				case UserA:      s2 = cases.receive(UserA, buf); break;
			}
		}
	}
}
