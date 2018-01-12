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
import betty16.lec2.adder.Adder.Adder.channels.C.EndSocket;
import betty16.lec2.adder.Adder.Adder.roles.C;

public class FibC1 {

	public static void main(String[] args) throws Exception {
		Adder adder = new Adder();
		try (MPSTEndpoint<Adder, C> client = new MPSTEndpoint<>(adder, C, new ObjectStreamFormatter())) {	
			client.connect(S, SocketChannelEndpoint::new, "localhost", 8888);

			Buf<Integer> i1 = new Buf<>(0);
			Buf<Integer> i2 = new Buf<>(1);
			int N = 10;
			new FibC1().fibo(new Adder_C_1(client), i1, i2, N);
			System.out.println(N + ": " + i1.val);
		}
	}
	
	// Post: i1.val is the i-th Fibonacci number
	private EndSocket fibo(Adder_C_1 c1, Buf<Integer> i1, Buf<Integer> i2, int i) throws Exception {
		return (i > 0)
				? fibo(c1.send(S, Add, i1.val, i1.val=i2.val).receive(S, Res, i2), i1, i2, i-1)
				: c1.send(S, Bye).receive(S, Bye);
	}
}
