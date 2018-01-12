package org.scribble.ast.global;

import org.antlr.runtime.tree.CommonTree;
import org.scribble.ast.AstFactoryImpl;
import org.scribble.ast.InteractionSeq;
import org.scribble.ast.ProtocolBlock;
import org.scribble.ast.local.LInteractionSeq;
import org.scribble.ast.local.LProtocolBlock;
import org.scribble.del.ScribDel;
import org.scribble.sesstype.kind.Global;
import org.scribble.sesstype.name.Role;

public class GProtocolBlock extends ProtocolBlock<Global> implements GNode
{
	public GProtocolBlock(CommonTree source, GInteractionSeq seq)
	{
		super(source, seq);
	}

	public LProtocolBlock project(Role self, LInteractionSeq seq)
	{
		LProtocolBlock projection = AstFactoryImpl.FACTORY.LProtocolBlock(this.source, seq);
		return projection;
	}

	@Override
	protected GProtocolBlock copy()
	{
		return new GProtocolBlock(this.source, getInteractionSeq());
	}
	
	@Override
	public GProtocolBlock clone()
	{
		GInteractionSeq gis = getInteractionSeq().clone();
		return AstFactoryImpl.FACTORY.GProtocolBlock(this.source, gis);
	}

	@Override
	public GProtocolBlock reconstruct(InteractionSeq<Global> seq)
	{
		ScribDel del = del();
		GProtocolBlock gpb = new GProtocolBlock(this.source, (GInteractionSeq) seq);
		gpb = (GProtocolBlock) gpb.del(del);
		return gpb;
	}

	@Override
	public GInteractionSeq getInteractionSeq()
	{
		return (GInteractionSeq) this.seq;
	}

	// FIXME: shouldn't be needed, but here due to Eclipse bug https://bugs.eclipse.org/bugs/show_bug.cgi?id=436350
	@Override
	public Global getKind()
	{
		return GNode.super.getKind();
	}
}
