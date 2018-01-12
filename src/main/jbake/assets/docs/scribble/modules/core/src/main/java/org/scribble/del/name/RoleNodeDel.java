package org.scribble.del.name;

import org.scribble.ast.ScribNode;
import org.scribble.ast.name.simple.RoleNode;
import org.scribble.del.ScribDelBase;
import org.scribble.main.ScribbleException;
import org.scribble.visit.wf.NameDisambiguator;

public class RoleNodeDel extends ScribDelBase
{
	public RoleNodeDel()
	{

	}

	@Override
	public RoleNode leaveDisambiguation(ScribNode parent, ScribNode child, NameDisambiguator disamb, ScribNode visited) throws ScribbleException
	{
		RoleNode rn = (RoleNode) visited;
		if (!disamb.isBoundRole(rn.toName()))
		{
			throw new ScribbleException(rn.getSource(), "Role not bound: " + rn);
		}
		return rn;
	}
}
