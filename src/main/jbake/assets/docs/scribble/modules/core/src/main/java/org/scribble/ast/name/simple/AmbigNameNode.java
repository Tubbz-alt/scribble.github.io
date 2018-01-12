package org.scribble.ast.name.simple;

import org.antlr.runtime.tree.CommonTree;
import org.scribble.ast.AstFactoryImpl;
import org.scribble.ast.MessageNode;
import org.scribble.ast.name.PayloadElemNameNode;
import org.scribble.sesstype.Arg;
import org.scribble.sesstype.Message;
import org.scribble.sesstype.kind.AmbigKind;
import org.scribble.sesstype.kind.DataTypeKind;
import org.scribble.sesstype.kind.NonRoleArgKind;
import org.scribble.sesstype.name.AmbigName;
import org.scribble.sesstype.name.PayloadType;

// Primitive payload type, MessageSigName or parameter names only: if name is parsed as a CompoundNameNodes, it must be a payload type (not ambiguous in this case)
// No counterpart needed for MessageNode because MessageSignature values can be syntactically distinguished from sig parameters
//public class AmbigNameNode extends SimpleNameNode<AmbigKind> implements MessageNode, PayloadElemNameNode
//public class AmbigNameNode extends SimpleNameNode<AmbigKind> implements MessageNode, PayloadElemNameNode<PayloadTypeKind>
public class AmbigNameNode extends SimpleNameNode<AmbigKind> implements MessageNode, PayloadElemNameNode<DataTypeKind>  // Currently hardcoded to DataTypeKind for payload elems
{
	public AmbigNameNode(CommonTree source, String identifier)
	{
		super(source, identifier);
	}
	
	@Override
	public MessageNode project()
	{
		throw new RuntimeException("Shouldn't get in here: " + this);
	}

	@Override
	protected AmbigNameNode copy()
	{
		return new AmbigNameNode(this.source, getIdentifier());
	}
	
	@Override
	public AmbigNameNode clone()
	{
		return (AmbigNameNode) AstFactoryImpl.FACTORY.AmbiguousNameNode(this.source, getIdentifier());
	}
	
	@Override
	public Arg<? extends NonRoleArgKind> toArg()
	{
		throw new RuntimeException("Ambiguous name node not disambiguated: " + this);
	}

	@Override
	public Message toMessage()
	{
		throw new RuntimeException("Ambiguous name node not disambiguated: " + this);
	}

	@Override
	//public PayloadType<AmbigKind> toPayloadType()
	public PayloadType<DataTypeKind> toPayloadType()  // As a payload elem, currently hardcoded to expect only DataTypeKind (protocol payloads not supported)
	//public PayloadType<PayloadTypeKind> toPayloadType()
	{
		throw new RuntimeException("Ambiguous name node not disambiguated: " + this);
	}

	@Override
	public AmbigName toName()
	{
		return new AmbigName(getIdentifier());
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (!(o instanceof AmbigNameNode))
		{
			return false;
		}
		return ((AmbigNameNode) o).canEqual(this) && super.equals(o);
	}
	
	@Override
	public boolean canEqual(Object o)
	{
		return o instanceof AmbigNameNode;
	}
	
	@Override
	public int hashCode()
	{
		int hash = 331;
		hash = 31 * super.hashCode();
		return hash;
	}
}
