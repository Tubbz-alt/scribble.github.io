package org.scribble.del.global;

import org.scribble.ast.ScribNode;
import org.scribble.ast.global.GDisconnect;
import org.scribble.ast.local.LNode;
import org.scribble.del.ConnectionActionDel;
import org.scribble.main.ScribbleException;
import org.scribble.sesstype.name.Role;
import org.scribble.visit.context.Projector;
import org.scribble.visit.wf.NameDisambiguator;
import org.scribble.visit.wf.WFChoiceChecker;
import org.scribble.visit.wf.env.WFChoiceEnv;

// FIXME: make DisconnectDel (cf., G/LMessageTransferDel)
public class GDisconnectDel extends ConnectionActionDel implements GSimpleInteractionNodeDel
{
	public GDisconnectDel()
	{
		
	}

	@Override
	public ScribNode leaveDisambiguation(ScribNode parent, ScribNode child, NameDisambiguator disamb, ScribNode visited) throws ScribbleException
	{
		GDisconnect gc = (GDisconnect) visited;
		/*Role src = gc.src.toName();
		Role dest = gc.dest.toName();*/
		return gc;
	}

	@Override
	public GDisconnect leaveInlinedWFChoiceCheck(ScribNode parent, ScribNode child, WFChoiceChecker checker, ScribNode visited) throws ScribbleException
	{
		GDisconnect gd = (GDisconnect) visited;
		
		Role src = gd.src.toName();
		if (!checker.peekEnv().isEnabled(src))
		{
			throw new ScribbleException(gd.src.getSource(), "Role not enabled: " + src);
		}
		//Message msg = gd.msg.toMessage();  //  Unit message 
		WFChoiceEnv env = checker.popEnv();
		//for (Role dest : gc.getDestinationRoles())
		Role dest = gd.dest.toName();
		if (!env.isEnabled(dest))
		{
			throw new ScribbleException(gd.dest.getSource(), "Role not enabled: " + dest);
		}
		{
			if (src.equals(dest))
			{
				throw new ScribbleException(gd.getSource(), "[TODO] Self connections not supported: " + gd);  // Would currently be subsumed by the below
			}
			if (!env.isConnected(src, dest))
			{
				throw new ScribbleException(gd.getSource(), "Roles not (necessarily) connected: " + src + ", " + dest);
			}

			env = env.disconnect(src, dest);//.removeMessage(src, dest, ...);  // Is remove really needed?
			/*env = env
					.disconnect(src, dest)
					.removeMessage(src, dest, new MessageSig(Op.EMPTY_OPERATOR, Payload.EMPTY_PAYLOAD));  // FIXME: factor out dummy connection message with GConnect etc.*/
		}
		checker.pushEnv(env);
		return gd;
	}

	@Override
	public ScribNode leaveProjection(ScribNode parent, ScribNode child, Projector proj, ScribNode visited) throws ScribbleException //throws ScribbleException
	{
		GDisconnect gd = (GDisconnect) visited;
		Role self = proj.peekSelf();
		LNode projection = gd.project(self);
		proj.pushEnv(proj.popEnv().setProjection(projection));
		return (GDisconnect) GSimpleInteractionNodeDel.super.leaveProjection(parent, child, proj, gd);
	}
}
