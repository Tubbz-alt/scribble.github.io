package org.scribble.ast.global;

import java.util.List;
import java.util.stream.Collectors;

import org.antlr.runtime.tree.CommonTree;
import org.scribble.ast.Parallel;
import org.scribble.ast.ProtocolBlock;
import org.scribble.ast.ScribNodeBase;
import org.scribble.del.ScribDel;
import org.scribble.sesstype.kind.Global;

public class GParallel extends Parallel<Global> implements GCompoundInteractionNode
{
	public GParallel(CommonTree source, List<GProtocolBlock> blocks)
	{
		super(source, blocks);
	}

	@Override
	protected ScribNodeBase copy()
	{
		return new GParallel(this.source, getBlocks());
	}
	
	@Override
	public GParallel clone()
	{
		//List<GProtocolBlock> blocks = ScribUtil.cloneList(getBlocks());
		throw new RuntimeException("TODO: " + this);
	}
	
	@Override
	public GParallel reconstruct(List<? extends ProtocolBlock<Global>> blocks)
	{
		ScribDel del = del();
		GParallel gp = new GParallel(this.source, castBlocks(blocks));
		gp = (GParallel) gp.del(del);
		return gp;
	}

	@Override
	public List<GProtocolBlock> getBlocks()
	{
		return castBlocks(super.getBlocks());
	}
	
	// FIXME: shouldn't be needed, but here due to Eclipse bug https://bugs.eclipse.org/bugs/show_bug.cgi?id=436350
	@Override
	public Global getKind()
	{
		return GCompoundInteractionNode.super.getKind();
	}
	
	private static List<GProtocolBlock> castBlocks(List<? extends ProtocolBlock<Global>> blocks)
	{
		return blocks.stream().map((b) -> (GProtocolBlock) b).collect(Collectors.toList());
	}
}
