package org.scribble.visit;

import org.scribble.ast.ProtocolDecl;
import org.scribble.main.Job;
import org.scribble.visit.env.DummyEnv;

public abstract class NoEnvUnfoldingVisitor extends UnfoldingVisitor<DummyEnv>
{
	public NoEnvUnfoldingVisitor(Job job)
	{
		super(job);
	}

	@Override
	protected final DummyEnv makeRootProtocolDeclEnv(ProtocolDecl<?> pd)
	{
		return DummyEnv.DUMMY;
	}
}
