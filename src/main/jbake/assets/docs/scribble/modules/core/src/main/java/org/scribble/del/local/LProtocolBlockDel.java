package org.scribble.del.local;

import org.scribble.ast.AstFactoryImpl;
import org.scribble.ast.ScribNode;
import org.scribble.ast.local.LInteractionSeq;
import org.scribble.ast.local.LProtocolBlock;
import org.scribble.del.ProtocolBlockDel;
import org.scribble.del.ScribDelBase;
import org.scribble.main.ScribbleException;
import org.scribble.visit.ProtocolDefInliner;
import org.scribble.visit.env.InlineProtocolEnv;
import org.scribble.visit.wf.ReachabilityChecker;

public class LProtocolBlockDel extends ProtocolBlockDel
{
	@Override
	public ScribNode leaveProtocolInlining(ScribNode parent, ScribNode child, ProtocolDefInliner inl, ScribNode visited) throws ScribbleException
	{
		LProtocolBlock lpb = (LProtocolBlock) visited;
		LInteractionSeq seq = (LInteractionSeq) ((InlineProtocolEnv) lpb.seq.del().env()).getTranslation();	
		LProtocolBlock inlined = AstFactoryImpl.FACTORY.LProtocolBlock(lpb.getSource(), seq);
		inl.pushEnv(inl.popEnv().setTranslation(inlined));
		return (LProtocolBlock) ScribDelBase.popAndSetVisitorEnv(this, inl, lpb);
	}

	@Override
	public void enterReachabilityCheck(ScribNode parent, ScribNode child, ReachabilityChecker checker) throws ScribbleException
	{
		ScribDelBase.pushVisitorEnv(this, checker);
	}

	@Override
	public LProtocolBlock leaveReachabilityCheck(ScribNode parent, ScribNode child, ReachabilityChecker checker, ScribNode visited) throws ScribbleException
	{
		return (LProtocolBlock) ScribDelBase.popAndSetVisitorEnv(this, checker, visited);  // records the current checker Env to the current del; also pops and merges that env into the parent env
	}
}
