package bettybook.math.sock;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class SockMathC
{
	public static void main(String[] args) throws Exception
	{
		try (Socket s = new Socket("localhost", 8888);
				 ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
				 ObjectInputStream ois = new ObjectInputStream(s.getInputStream()))
		{
			int i = 5, res = i;
			while (i > 1)
			{
				oos.writeObject(new Val(i));
				oos.writeObject(new Add(-1));
				oos.flush();
				i = ((Sum) ois.readObject()).val;
				oos.writeObject(new Val(res));
				oos.writeObject(new Mult(i));
				oos.flush();
				res = ((Prod) ois.readObject()).val;
			}
			oos.writeObject(new Bye());
			oos.flush();

			System.out.println("Facto: " + res);
		}
	}
}

abstract class NumMsg implements Serializable
{
	private static final long serialVersionUID = 1L;
	public int val;

	public NumMsg(int val)
	{
		this.val = val;
	}
}

class Val extends NumMsg
{
	private static final long serialVersionUID = 1L;

	public Val(int x)
	{
		super(x);
	}
}

class Add extends NumMsg
{
	private static final long serialVersionUID = 1L;

	public Add(int val)
	{
		super(val);
	}
}

class Sum extends NumMsg
{
	private static final long serialVersionUID = 1L;

	public Sum(int val)
	{
		super(val);
	}
}

class Mult extends NumMsg
{
	private static final long serialVersionUID = 1L;

	public Mult(int val)
	{
		super(val);
	}
}

class Prod extends NumMsg
{
	private static final long serialVersionUID = 1L;

	public Prod(int val)
	{
		super(val);
	}
}

class Bye implements Serializable
{
	private static final long serialVersionUID = 1L;
}
