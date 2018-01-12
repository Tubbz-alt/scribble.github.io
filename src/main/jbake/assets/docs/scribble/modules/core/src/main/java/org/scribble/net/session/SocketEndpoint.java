package org.scribble.net.session;

import java.io.DataInputStream;
import java.io.IOException;

import org.scribble.net.ScribMessage;
import org.scribble.sesstype.name.Role;

// Read/write Objects on the binary connection to an endpoint in the session
@Deprecated
public class SocketEndpoint
{
	private final MPSTEndpoint<?, ?> ep;
	//private final Role peer;
	private final SocketWrapper sw;
	
	public SocketWrapper getSocketWrapper()
	{
		return this.sw;
	}

	private final ReceiverThread receiver;

	public SocketEndpoint(MPSTEndpoint<?, ?> ep, Role peer, SocketWrapper sw) //throws IOException
	{
		this.ep = ep;
		//this.peer = peer;
		this.sw = sw;

		this.receiver = new ReceiverThread(ep, peer, sw.dis);
		this.receiver.start();
	}

	/*// For SelfSocketEndpoint only
	protected SocketEndpoint(Role src, SessionInputQueue inputq)
	{
		this.src = src;
		this.inputq = inputq;
		this.sw = null;
		this.recthread = null;
	}*/

	public void writeMessage(ScribMessage msg) throws IOException
	{
		this.ep.smf.writeMessage(this.sw.dos, msg);
	}

	public void flush() throws IOException
	{
		this.sw.dos.flush();
	}

	public void writeMessageAndFlush(ScribMessage msg) throws IOException
	{
		writeMessage(msg);
		flush();
	}

	public void close() throws IOException
	{
		this.sw.close();
	}

	@Override
	public String toString()
	{
		return this.sw.sock.getInetAddress().toString() + ":" + this.sw.sock.getPort();
	}
}

// Could move to EndpointInputQueues
class ReceiverThread extends Thread
{
	/*private final MPSTEndpoint<?, ?> ep;
	private final Role peer;
	private final DataInputStream dis;

	private Throwable fail;*/

	public ReceiverThread(MPSTEndpoint<?, ?> ep, Role peer, DataInputStream dis)
	{
		/*this.ep = ep;
		this.peer = peer;
		this.dis = dis;*/

		//ep.getInputQueues().register(peer);
	}

	public void run()
	{
		/*EndpointInputQueues queues = null;//this.ep.getInputQueues();
		try
		{
			while (true)
			{
				ScribMessage m = this.ep.smf.readMessage(this.dis);
				queues.enqueue(this.peer, m);  // FIXME: bounded buffer? (endpoint queue property?)
			}
		}
		catch (IOException e)
		{
			this.fail = e;
		}
		finally
		{
			try
			{
				dis.close();  // Maybe just rely on main SocketEndpoint.close
			}
			catch (IOException e)
			{
				if (this.fail == null)
				{
					this.fail = e;
				}
			}
			finally
			{
				if (this.fail != null)
				{
					queues.interrupt(this.peer, this.fail);
				}
			}
		}*/
	}
}
