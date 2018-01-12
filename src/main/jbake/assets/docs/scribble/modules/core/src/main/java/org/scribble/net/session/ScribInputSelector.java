package org.scribble.net.session;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.util.Set;

import org.scribble.sesstype.name.Role;

public class ScribInputSelector extends Thread
{
	//private MPSTEndpoint<?, ?> se;  // FIXME
	private SessionEndpoint<?, ?> se;  // FIXME
	private final Selector sel;

	private volatile boolean paused = false;
	private volatile boolean closed = false;

	//public ScribInputSelector(MPSTEndpoint<?, ?> se) throws IOException
	public ScribInputSelector(SessionEndpoint<?, ?> se) throws IOException
	{
		this.se = se;
		this.sel = Selector.open();
	}

	@Override
	public void run()
	{
		try
		{
			//waitForInit();  
					//..FIXME: reg serversocketchannel with sel -- midsession reg needs wakeup and re-select?
					//..here: do java smtp version using nio

			while (!this.closed)
			{
				this.sel.select();
				if (this.closed)
				{
					return;
				}
				synchronized (this)  // sync'd with pause, unpause
				{
					while (paused)
					{
						wait();
					}
						
					Set<SelectionKey> keys = this.sel.selectedKeys();
					for (SelectionKey key : keys)
					{
						//SocketChannel s = (SocketChannel) key.channel();
						if (key.isReadable())
						{
							Role peer = (Role) key.attachment();
							this.se.chans.get(peer).readAndEnqueueMessages();  // Read as many message as possible as selector only woken up by actual I/O
						}
						else
						{
							throw new RuntimeException("TODO: " + key);
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			// FIXME: throw to user -- e.g. MessageFormatter.fromBytes exception
			e.printStackTrace();  // FIXME? java.nio.channels.CancelledKeyException 
		}
		finally
		{
			try
			{
				this.sel.close();
			}
			catch (IOException e)
			{
				// FIXME
				e.printStackTrace();
			}
		}
	}
	
	protected Selector getSelector()
	{
		return this.sel;
	}
	
	// synchronize?
	protected SelectionKey register(AbstractSelectableChannel c) throws ClosedChannelException
	{
		return c.register(this.sel, SelectionKey.OP_READ);
	}
	
	protected void deregister(SelectionKey key)  // FIXME: refactor to internalise key inside here?
	{
		key.cancel();
	}
	
	// process all keys and keep doing until all pending futures have completed -- i.e. all reads done up to this send state (currently wrap assumed to in send state only)
	protected synchronized void clear()
	{
		
	}
	
	protected synchronized void pause()	
	{
		this.paused = true;
		this.sel.wakeup();
	}
	
	protected synchronized void unpause()	
	{
		this.paused = false;
		notify();
	}
	
	protected synchronized void close()
	{
		this.closed = true;
		this.paused = false;
		//for (BinaryChannelEndpoint c : this.se.chans.values())
		for (Role peer : this.se.getPeers())
		{
			try
			{
				BinaryChannelEndpoint c = this.se.getChannelEndpoint(peer);
				c.close();  // dereg from sel?
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}		
		this.sel.wakeup();
	}
}
