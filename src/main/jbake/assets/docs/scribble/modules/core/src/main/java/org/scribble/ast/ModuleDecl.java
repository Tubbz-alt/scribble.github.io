package org.scribble.ast;

import org.antlr.runtime.tree.CommonTree;
import org.scribble.ast.name.qualified.ModuleNameNode;
import org.scribble.main.ScribbleException;
import org.scribble.sesstype.kind.ModuleKind;
import org.scribble.sesstype.name.ModuleName;
import org.scribble.visit.AstVisitor;

public class ModuleDecl extends NameDeclNode<ModuleKind>
{
	public ModuleDecl(CommonTree source, ModuleNameNode fullmodname)
	{
		super(source, fullmodname);
	}

	@Override
	protected ModuleDecl copy()
	{
		return new ModuleDecl(this.source, (ModuleNameNode) this.name);
	}
	
	@Override
	public ModuleDecl clone()
	{
		ModuleNameNode modname = (ModuleNameNode) this.name.clone();
		return AstFactoryImpl.FACTORY.ModuleDecl(this.source, modname);
	}

	@Override
	public ModuleDecl visitChildren(AstVisitor nv) throws ScribbleException
	{
		ModuleNameNode fullmodname = (ModuleNameNode) visitChild(this.name, nv);
		return AstFactoryImpl.FACTORY.ModuleDecl(this.source, fullmodname);  // cf., reconstruct
	}

	@Override
	public String toString()
	{
		return Constants.MODULE_KW + " " + this.name + ";";
	}

	@Override
	public ModuleName getDeclName()
	{
		//return (ModuleName) super.getDeclName();  // Would return full name
		return ((ModuleName) super.getDeclName()).getSimpleName();  // Uniform with other NameDeclNodes wrt. returning simple name
	}

	public ModuleName getFullModuleName()
	{
		return (ModuleName) this.name.toName();
	}
}
