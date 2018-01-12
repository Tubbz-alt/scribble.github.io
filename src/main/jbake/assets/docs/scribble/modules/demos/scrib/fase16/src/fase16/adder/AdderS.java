package fase16.adder;

import static fase16.adder.Adder.Adder.Adder.Add;
import static fase16.adder.Adder.Adder.Adder.Bye;
import static fase16.adder.Adder.Adder.Adder.C;
import static fase16.adder.Adder.Adder.Adder.Res;
import static fase16.adder.Adder.Adder.Adder.S;

import java.io.IOException;

import org.scribble.main.ScribbleRuntimeException;
import org.scribble.net.Buf;
import org.scribble.net.ObjectStreamFormatter;
import org.scribble.net.scribsock.ScribServerSocket;
import org.scribble.net.scribsock.SocketChannelServer;
import org.scribble.net.session.MPSTEndpoint;

import fase16.adder.Adder.Adder.Adder;
import fase16.adder.Adder.Adder.channels.S.Adder_S_1;
import fase16.adder.Adder.Adder.channels.S.Adder_S_1_Cases;
import fase16.adder.Adder.Adder.channels.S.Adder_S_3;
import fase16.adder.Adder.Adder.roles.S;


public class AdderS
{
	public static void main(String[] args) throws Exception
	{
		try (ScribServerSocket ss = new SocketChannelServer(8888))
		{
			Buf<Integer> i1 = new Buf<>();
			Buf<Integer> i2 = new Buf<>();

			while (true)
			{
				Adder adder = new Adder();
				try (MPSTEndpoint<Adder, S> se = new MPSTEndpoint<>(adder, S, new ObjectStreamFormatter()))
				{
					se.accept(ss, C);

					add(new Adder_S_1(se), i1, i2).send(C, Bye);
				}
				catch (ScribbleRuntimeException | IOException | ClassNotFoundException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	private static Adder_S_3 add(Adder_S_1 s1, Buf<Integer> i1, Buf<Integer> i2) throws Exception
	{
		Adder_S_1_Cases cases = s1.branch(C);
		switch (cases.op)
		{
			case Add:
				return add(
						cases.receive(Add, i1, i2)
						     .send(C, Res, i1.val+i2.val)
						, i1, i2);
			case Bye:
				return cases.receive(Bye);
			default:
				throw new RuntimeException("Will never get here.");
		}
	} 	
}
