package org.scribble.ast.local;

import java.util.List;
import java.util.Set;

import org.antlr.runtime.tree.CommonTree;
import org.scribble.ast.MessageNode;
import org.scribble.ast.name.simple.RoleNode;
import org.scribble.main.ScribbleException;
import org.scribble.sesstype.Message;

public class LCatches extends LInterrupt
{
	protected LCatches(CommonTree source, RoleNode src, List<MessageNode> msgs)
	{
		super(source, src, msgs);
	}

	@Override
	public LInteractionNode merge(LInteractionNode ln) throws ScribbleException
	{
		throw new RuntimeException("TODO: " + this);
	}

	@Override
	public boolean canMerge(LInteractionNode ln)
	{
		throw new RuntimeException("TODO: " + this);
	}

	@Override
	public Set<Message> getEnabling()
	{
		throw new RuntimeException("TODO: " + this);
	}

	/*public LocalCatches(CommonTree ct, RoleNode src, List<MessageNode> msgs, List<RoleNode> dests)
	{
		this(ct, src, msgs, dests, null, null);
	}

	protected LocalCatches(CommonTree ct, RoleNode src, List<MessageNode> msgs, List<RoleNode> dests, GlobalInterruptContext gicontext, Env env)
	{
		super(ct, src, msgs, dests, gicontext, env);
	}

	@Override
	protected LocalThrows reconstruct(CommonTree ct, RoleNode src, List<MessageNode> msgs, List<RoleNode> dests, GlobalInterruptContext icontext, Env env)
	{
		return new LocalThrows(ct, src, msgs, dests, icontext, env);
	}
	
	/*@Override
	public LocalCatches visitChildren(NodeVisitor nv) throws ScribbleException
	{
		LocalInterrupt interr = super.visitChildren(nv);
		return new LocalCatches(interr.ct, interr.src, interr.msgs, interr.dests, (GlobalInterruptContext) interr.getContext());
	}* /
	
	@Override
	public String toString()
	{
		String s = AntlrConstants.CATCHES_KW + " " + this.msgs.get(0).toString();
		for (MessageNode msg : this.msgs.subList(1, this.msgs.size()))
		{
			s += ", " + msg;
		}
		return s + " " + AntlrConstants.FROM_KW + " " + this.src.toName() + ";";
	}

	/*@Override
	public void toGraph(GraphBuilder gb)
	{
		throw new RuntimeException("Shouldn't get in here.");
	}*/
}
