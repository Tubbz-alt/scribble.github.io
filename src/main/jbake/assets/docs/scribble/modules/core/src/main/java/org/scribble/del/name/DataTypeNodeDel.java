package org.scribble.del.name;

import org.scribble.ast.AstFactoryImpl;
import org.scribble.ast.DataTypeDecl;
import org.scribble.ast.ScribNode;
import org.scribble.ast.context.ModuleContext;
import org.scribble.ast.name.qualified.DataTypeNode;
import org.scribble.del.ScribDelBase;
import org.scribble.main.ScribbleException;
import org.scribble.sesstype.kind.DataTypeKind;
import org.scribble.sesstype.name.DataType;
import org.scribble.visit.wf.NameDisambiguator;

public class DataTypeNodeDel extends ScribDelBase
{
	public DataTypeNodeDel()
	{

	}

	// Is this needed?  Or DataTypeNodes always created from AmbigNameNode? (in this same pass)
	@Override
	public ScribNode leaveDisambiguation(ScribNode parent, ScribNode child, NameDisambiguator disamb, ScribNode visited)
			throws ScribbleException
	{
		if (parent instanceof DataTypeDecl)  // Hacky? don't want to do for decl simplenames (generally, don't do if parent is namedeclnode)
		{
			return visited;
		}
		ModuleContext mc = disamb.getModuleContext();
		DataTypeNode dtn = (DataTypeNode) visited;
		DataType dt = dtn.toName();
		if (!mc.isVisibleDataType(dt))
		{
			throw new ScribbleException(dtn.getSource(), "Data type not visible: " + dt);
		}
		DataType fullname = mc.getVisibleDataTypeFullName(dt);
		return (DataTypeNode)
				AstFactoryImpl.FACTORY.QualifiedNameNode(dtn.getSource(), DataTypeKind.KIND, fullname.getElements());  // Didn't keep original del
	}
}
