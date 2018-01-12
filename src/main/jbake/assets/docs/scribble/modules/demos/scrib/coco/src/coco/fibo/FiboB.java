package coco.fibo;

import static coco.fibo.Fibo.Fibonacci.Fibonacci.A;
import static coco.fibo.Fibo.Fibonacci.Fibonacci.B;
import static coco.fibo.Fibo.Fibonacci.Fibonacci.fibonacci;
import static coco.fibo.Fibo.Fibonacci.Fibonacci.stop;

import org.scribble.net.Buf;
import org.scribble.net.ObjectStreamFormatter;
import org.scribble.net.scribsock.ScribServerSocket;
import org.scribble.net.scribsock.SocketChannelServer;
import org.scribble.net.session.MPSTEndpoint;

import coco.fibo.Fibo.Fibonacci.Fibonacci;
import coco.fibo.Fibo.Fibonacci.channels.B.EndSocket;
import coco.fibo.Fibo.Fibonacci.channels.B.Fibonacci_B_1;
import coco.fibo.Fibo.Fibonacci.channels.B.Fibonacci_B_1_Cases;
import coco.fibo.Fibo.Fibonacci.roles.B;

public class FiboB extends Thread
{
	private final Fibonacci fib;
	private Buf<Long> b = new Buf<>(1L);
	
	public FiboB(Fibonacci fib)
	{
		this.fib = fib;
	}
	
	@Override
	public void run()
	{
		try (
			ScribServerSocket ss = new SocketChannelServer(8888);
			MPSTEndpoint<Fibonacci, B> se = new MPSTEndpoint<>(this.fib, B, new ObjectStreamFormatter()))
		{
			se.accept(ss, A);
			run(new Fibonacci_B_1(se));
		}
		catch (Exception x)
		{
			x.printStackTrace();
		}
	}
	
	private EndSocket run(Fibonacci_B_1 s) throws Exception
	{
		/*
		... s.branch(A);
		switch (...)  // Value-switch
		{
			...
		}
	}
}
/*/
		Fibonacci_B_1_Cases c = s.branch(A);
		switch (c.op)
		{
			case fibonacci:
				long prev = this.b.val;
				return run(c.receive(fibonacci, this.b).send(A, fibonacci, this.b.val += prev));
			case stop:
				return c.receive(stop);
			default:
				throw new RuntimeException("Will never get here");
		}
	}
}
//*/
