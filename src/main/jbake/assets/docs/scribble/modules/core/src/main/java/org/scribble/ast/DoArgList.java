package org.scribble.ast;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.antlr.runtime.tree.CommonTree;
import org.scribble.main.ScribbleException;
import org.scribble.sesstype.name.Role;
import org.scribble.visit.AstVisitor;

// Cf. HeaderParameterDeclList -- but not kinded, because cannot determine Arg kind directly from node syntax itself (kinding for ModelNodes is to supplement syntactic information, not "typing" work)
// DoArgList (NonRoleArgList) can be of mixed kinds, so DoArg (NonRoleArg) used as "wildcard" wrapper
// "? extends InstantiationNode" not enforced here (e.g. can put "? extends ModelNode"), because ultimately any instantiation of this class needs an actual instance of "Instantiation" which has to have a parameter that extends "InstantiationNode"
public abstract class DoArgList<T extends DoArg<?>> extends ScribNodeBase  
{
	private final List<T> args;

	public DoArgList(CommonTree source, List<T> is)
	{
		super(source);
		this.args = new LinkedList<>(is);
	}
	
	public abstract DoArgList<T> reconstruct(List<T> instans);
	
	public abstract DoArgList<T> project(Role self);
	
	@Override
	public DoArgList<T> visitChildren(AstVisitor nv) throws ScribbleException
	{
		List<T> nds = visitChildListWithClassEqualityCheck(this, this.args, nv);
		return reconstruct(nds);
	}
	
	public List<T> getDoArgs()
	{
		return Collections.unmodifiableList(this.args);
	}

	public int length()
	{
		return this.args.size();
	}
	
	// Like HeaderParamDeclList, without enclosing braces -- added by subclasses
	@Override
	public String toString()
	{
		return this.args.stream().map((a) -> a.toString()).collect(Collectors.joining(", "));
	}
}
