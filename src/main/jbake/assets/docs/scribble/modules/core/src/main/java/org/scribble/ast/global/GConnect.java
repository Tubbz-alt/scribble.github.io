package org.scribble.ast.global;

import org.antlr.runtime.tree.CommonTree;
import org.scribble.ast.AstFactoryImpl;
import org.scribble.ast.ConnectionAction;
import org.scribble.ast.Constants;
import org.scribble.ast.MessageNode;
import org.scribble.ast.local.LNode;
import org.scribble.ast.name.simple.RoleNode;
import org.scribble.del.ScribDel;
import org.scribble.sesstype.kind.Global;
import org.scribble.sesstype.kind.RoleKind;
import org.scribble.sesstype.name.Role;

public class GConnect extends ConnectionAction<Global> implements GSimpleInteractionNode
{
	public GConnect(CommonTree source, RoleNode src, MessageNode msg, RoleNode dest)
	//public GConnect(RoleNode src, RoleNode dest)
	{
		super(source, src, msg, dest);
		//super(src, dest);
	}

	public LNode project(Role self)
	{
		Role srcrole = this.src.toName();
		Role destrole = this.dest.toName();
		LNode projection = null;
		if (srcrole.equals(self) || destrole.equals(self))
		{
			RoleNode src = (RoleNode) AstFactoryImpl.FACTORY.SimpleNameNode(this.src.getSource(), RoleKind.KIND, this.src.toName().toString());  // clone?
			MessageNode msg = (MessageNode) this.msg;  // FIXME: need namespace prefix update?
			RoleNode dest = (RoleNode) AstFactoryImpl.FACTORY.SimpleNameNode(this.dest.getSource(), RoleKind.KIND, this.dest.toName().toString());
			if (srcrole.equals(self))
			{
				projection = AstFactoryImpl.FACTORY.LConnect(this.source, src, msg, dest);
				//projection = AstFactoryImpl.FACTORY.LConnect(src, dest);  // src and dest (not self and peer)
			}
			if (destrole.equals(self))
			{
				projection = AstFactoryImpl.FACTORY.LAccept(this.source, src, msg, dest);
				//projection = AstFactoryImpl.FACTORY.LAccept(src, dest);
			}
		}
		return projection;
	}

	@Override
	protected GConnect copy()
	{
		return new GConnect(this.source, this.src, this.msg, this.dest);
		//return new GConnect(this.src, this.dest);
	}
	
	@Override
	public GConnect clone()
	{
		RoleNode src = this.src.clone();
		MessageNode msg = this.msg.clone();
		RoleNode dest = this.dest.clone();
		return AstFactoryImpl.FACTORY.GConnect(this.source, src, msg, dest);
		//return AstFactoryImpl.FACTORY.GConnect(src, dest);
	}

	@Override
	public GConnect reconstruct(RoleNode src, MessageNode msg, RoleNode dest)
	//public GConnect reconstruct(RoleNode src, RoleNode dest)
	{
		ScribDel del = del();
		GConnect gc = new GConnect(this.source, src, msg, dest);
		//GConnect gc = new GConnect(src, dest);
		gc = (GConnect) gc.del(del);
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
		return (isUnitMessage() ? "" : this.msg + " ") + Constants.CONNECT_KW + " " + this.src + " " + Constants.TO_KW + " " + this.dest + ";";
		//return Constants.CONNECT_KW + " " + this.src + " " + Constants.TO_KW + " " + this.dest + ";";
	}
}
