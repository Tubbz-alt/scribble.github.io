package betty16.lec2.adder;

import static betty16.lec2.adder.Adder.Adder.Adder.Add;
import static betty16.lec2.adder.Adder.Adder.Adder.Bye;
import static betty16.lec2.adder.Adder.Adder.Adder.C;
import static betty16.lec2.adder.Adder.Adder.Adder.Res;
import static betty16.lec2.adder.Adder.Adder.Adder.S;

import org.scribble.net.Buf;
import org.scribble.net.ObjectStreamFormatter;
import org.scribble.net.session.MPSTEndpoint;
import org.scribble.net.session.SocketChannelEndpoint;

import betty16.lec2.adder.Adder.Adder.Adder;
import betty16.lec2.adder.Adder.Adder.channels.C.Adder_C_1;
import betty16.lec2.adder.Adder.Adder.roles.C;

public class MyAdderC {

	public static void main(String[] args) throws Exception {
		Adder adder = new Adder();
		try (MPSTEndpoint<Adder, C> client = new MPSTEndpoint<>(adder, C, new ObjectStreamFormatter())) {	
			client.connect(S, SocketChannelEndpoint::new, "localhost", 8888);
			System.out.println("C: " + new MyAdderC().run(client));
		}
	}
	
	private int run(MPSTEndpoint<Adder, C> client) throws Exception {
		Buf<Integer> i = new Buf<>(1);

		Adder_C_1 c1 = new Adder_C_1(client);

		// TODO
		
		return i.val;
	}
}







