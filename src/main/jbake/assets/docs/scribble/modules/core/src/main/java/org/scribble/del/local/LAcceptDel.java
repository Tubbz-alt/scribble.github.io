package org.scribble.del.local;

import org.scribble.ast.MessageSigNode;
import org.scribble.ast.ScribNode;
import org.scribble.ast.local.LAccept;
import org.scribble.main.ScribbleException;
import org.scribble.model.endpoint.actions.EAccept;
import org.scribble.sesstype.Payload;
import org.scribble.sesstype.name.MessageId;
import org.scribble.sesstype.name.Role;
import org.scribble.visit.context.EGraphBuilder;
import org.scribble.visit.context.ProjectedChoiceSubjectFixer;
import org.scribble.visit.context.UnguardedChoiceDoProjectionChecker;
import org.scribble.visit.context.env.UnguardedChoiceDoEnv;
import org.scribble.visit.wf.ExplicitCorrelationChecker;
import org.scribble.visit.wf.env.ExplicitCorrelationEnv;

public class LAcceptDel extends LConnectionActionDel implements LSimpleInteractionNodeDel
{
	@Override
	public LAccept leaveEGraphBuilding(ScribNode parent, ScribNode child, EGraphBuilder builder, ScribNode visited) throws ScribbleException
	{
		LAccept la = (LAccept) visited;
		Role peer = la.src.toName();
		MessageId<?> mid = la.msg.toMessage().getId();
		Payload payload = la.msg.isMessageSigNode()  // Hacky?
					? ((MessageSigNode) la.msg).payloads.toPayload()
					: Payload.EMPTY_PAYLOAD;
		builder.util.addEdge(builder.util.getEntry(), new EAccept(peer, mid, payload), builder.util.getExit());
		//builder.builder.addEdge(builder.builder.getEntry(), new Accept(peer), builder.builder.getExit());
		////builder.builder.addEdge(builder.builder.getEntry(), Receive.get(peer, mid, payload), builder.builder.getExit());
		return (LAccept) super.leaveEGraphBuilding(parent, child, builder, la);
	}

	@Override
	public void enterProjectedChoiceSubjectFixing(ScribNode parent, ScribNode child, ProjectedChoiceSubjectFixer fixer)
	{
		fixer.setChoiceSubject(((LAccept) child).src.toName());
	}

	@Override
	public void enterUnguardedChoiceDoProjectionCheck(ScribNode parent, ScribNode child, UnguardedChoiceDoProjectionChecker checker) throws ScribbleException
	{
		super.enterUnguardedChoiceDoProjectionCheck(parent, child, checker);
		LAccept la = (LAccept) child;
		UnguardedChoiceDoEnv env = checker.popEnv();
		env = env.setChoiceSubject(la.src.toName());
		checker.pushEnv(env);
	}

	@Override
	public LAccept leaveExplicitCorrelationCheck(ScribNode parent, ScribNode child, ExplicitCorrelationChecker checker, ScribNode visited) throws ScribbleException
	{
		LAccept la = (LAccept) visited;
		ExplicitCorrelationEnv env = checker.popEnv();
		if (!env.canAccept())
		{
			//throw new ScribbleException("Invalid accept action: " + la);
			checker.job.warningPrintln("Session correlation warning for: " + la);
		}
		checker.pushEnv(env.disableAccept());
		return la;
	}
}
