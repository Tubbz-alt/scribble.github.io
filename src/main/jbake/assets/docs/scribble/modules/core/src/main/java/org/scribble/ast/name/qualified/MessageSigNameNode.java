package org.scribble.ast.name.qualified;

import org.antlr.runtime.tree.CommonTree;
import org.scribble.ast.AstFactoryImpl;
import org.scribble.ast.MessageNode;
import org.scribble.sesstype.kind.SigKind;
import org.scribble.sesstype.name.MessageSigName;

public class MessageSigNameNode extends MemberNameNode<SigKind> implements MessageNode
{
	public MessageSigNameNode(CommonTree source, String... elems)
	{
		super(source, elems);
	}
	
	@Override
	public MessageNode project()
	{
		return this;
	}

	@Override
	protected MessageSigNameNode copy()
	{
		return new MessageSigNameNode(this.source, this.elems);
	}
	
	@Override
	public MessageSigNameNode clone()
	{
		return (MessageSigNameNode) AstFactoryImpl.FACTORY.QualifiedNameNode(this.source, SigKind.KIND, this.elems);
	}
	
	@Override
	public MessageSigName toName()
	{
		MessageSigName membname = new MessageSigName(getLastElement());
		return isPrefixed()
				? new MessageSigName(getModuleNamePrefix(), membname)
				: membname;
	}

	@Override
	public boolean isMessageSigNameNode()
	{
		return true;
	}

	@Override
	public MessageSigName toMessage()  // Difference between toName and toMessage is scope? does that make sense?
	{
		return toName();
	}

	@Override
	public MessageSigName toArg()
	{
		return toMessage();
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (!(o instanceof MessageSigNameNode))
		{
			return false;
		}
		return ((MessageSigNameNode) o).canEqual(this) && super.equals(o);
	}
	
	@Override
	public boolean canEqual(Object o)
	{
		return o instanceof MessageSigNameNode;
	}
	
	@Override
	public int hashCode()
	{
		int hash = 421;
		hash = 31 * hash + this.elems.hashCode();
		return hash;
	}
}
