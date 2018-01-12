package org.scribble.ast.global;

import java.util.Collections;

import org.antlr.runtime.tree.CommonTree;
import org.scribble.ast.AstFactoryImpl;
import org.scribble.ast.ConnectionAction;
import org.scribble.ast.Constants;
import org.scribble.ast.MessageNode;
import org.scribble.ast.MessageSigNode;
import org.scribble.ast.local.LNode;
import org.scribble.ast.name.simple.OpNode;
import org.scribble.ast.name.simple.RoleNode;
import org.scribble.del.ScribDel;
import org.scribble.sesstype.kind.Global;
import org.scribble.sesstype.kind.OpKind;
import org.scribble.sesstype.kind.RoleKind;
import org.scribble.sesstype.name.Op;
import org.scribble.sesstype.name.Role;

public class GWrap extends ConnectionAction<Global> implements GSimpleInteractionNode
{
  // FIXME: inconsistent wrt. this.source -- it is essentially parsed (in the sense of *omitted* syntax), but not recorded
	public static final MessageSigNode UNIT_MESSAGE_SIG_NODE =  // Hacky?  // FIXME: factor out with GDisconnect
			AstFactoryImpl.FACTORY.MessageSigNode(null,
				(OpNode) AstFactoryImpl.FACTORY.SimpleNameNode(null, OpKind.KIND, Op.EMPTY_OPERATOR.toString()),
				AstFactoryImpl.FACTORY.PayloadElemList(null, Collections.emptyList()));
	
	public GWrap(CommonTree source, RoleNode src, RoleNode dest)
	{
		super(source, src, UNIT_MESSAGE_SIG_NODE, dest);
	}

	public LNode project(Role self)
	{
		Role srcrole = this.src.toName();
		Role destrole = this.dest.toName();
		LNode projection = null;
		if (srcrole.equals(self) || destrole.equals(self))
		{
			RoleNode src = (RoleNode) AstFactoryImpl.FACTORY.SimpleNameNode(this.src.getSource(), RoleKind.KIND, this.src.toName().toString());  // clone?
			RoleNode dest = (RoleNode) AstFactoryImpl.FACTORY.SimpleNameNode(this.dest.getSource(), RoleKind.KIND, this.dest.toName().toString());
			if (srcrole.equals(self))
			{
				projection = AstFactoryImpl.FACTORY.LWrapClient(this.source, src, dest);  // src and dest (not self and peer)
			}
			if (destrole.equals(self))
			{
				projection = AstFactoryImpl.FACTORY.LWrapServer(this.source, src, dest);
			}
		}
		return projection;
	}

	@Override
	protected GWrap copy()
	{
		return new GWrap(this.source, this.src, this.dest);
	}
	
	@Override
	public GWrap clone()
	{
		RoleNode src = this.src.clone();
		RoleNode dest = this.dest.clone();
		return AstFactoryImpl.FACTORY.GWrap(this.source, src, dest);
	}

	@Override
	public GWrap reconstruct(RoleNode src, MessageNode msg, RoleNode dest)
	//public GWrap reconstruct(RoleNode src, RoleNode dest)
	{
		ScribDel del = del();
		GWrap gc = new GWrap(this.source, src, dest);  //this.msg);
		gc = (GWrap) gc.del(del);
		return gc;
	}

	// FIXME: shouldn't be needed, but here due to Eclipse bug https://bugs.eclipse.org/bugs/show_bug.cgi?id=436350
	@Override
	public Global getKind()
	{
		return GSimpleInteractionNode.super.getKind();
	}

	@Override
	public String toString()
	{
		return Constants.WRAP_KW + " " + this.src + " " + Constants.TO_KW + " " + this.dest + ";";
	}
}
