package org.scribble.del.global;

import org.antlr.runtime.tree.CommonTree;
import org.scribble.ast.AstFactoryImpl;
import org.scribble.ast.ScribNode;
import org.scribble.ast.context.ModuleContext;
import org.scribble.ast.global.GContinue;
import org.scribble.ast.global.GDo;
import org.scribble.ast.global.GInteractionSeq;
import org.scribble.ast.global.GProtocolBlock;
import org.scribble.ast.global.GRecursion;
import org.scribble.ast.local.LDo;
import org.scribble.ast.name.qualified.LProtocolNameNode;
import org.scribble.ast.name.simple.RecVarNode;
import org.scribble.del.DoDel;
import org.scribble.main.ScribbleException;
import org.scribble.sesstype.SubprotocolSig;
import org.scribble.sesstype.kind.RecVarKind;
import org.scribble.sesstype.name.GProtocolName;
import org.scribble.sesstype.name.ProtocolName;
import org.scribble.sesstype.name.Role;
import org.scribble.visit.ProtocolDefInliner;
import org.scribble.visit.context.Projector;
import org.scribble.visit.context.ProtocolDeclContextBuilder;
import org.scribble.visit.env.InlineProtocolEnv;

public class GDoDel extends DoDel implements GSimpleInteractionNodeDel
{
	// Part of context building
	@Override
	protected void addProtocolDependency(ProtocolDeclContextBuilder builder, Role self, ProtocolName<?> proto, Role target)
	{
		builder.addGlobalProtocolDependency(self, (GProtocolName) proto, target);
	}

	// Only called if cycle
	public GDo visitForSubprotocolInlining(ProtocolDefInliner builder, GDo child)
	{
		CommonTree blame = child.getSource();
		SubprotocolSig subsig = builder.peekStack();
		RecVarNode recvar = (RecVarNode) AstFactoryImpl.FACTORY.SimpleNameNode(blame,
				RecVarKind.KIND, builder.getSubprotocolRecVar(subsig).toString());
		GContinue inlined = AstFactoryImpl.FACTORY.GContinue(blame, recvar);
		builder.pushEnv(builder.popEnv().setTranslation(inlined));
		return child;
	}
	
	@Override
	public GDo leaveProtocolInlining(ScribNode parent, ScribNode child, ProtocolDefInliner inl, ScribNode visited) throws ScribbleException
	{
		CommonTree blame = visited.getSource();
		SubprotocolSig subsig = inl.peekStack();
		if (!inl.isCycle())
		{
			RecVarNode recvar = (RecVarNode) AstFactoryImpl.FACTORY.SimpleNameNode(blame,
					RecVarKind.KIND, inl.getSubprotocolRecVar(subsig).toString());
			GInteractionSeq gis = (GInteractionSeq) (((InlineProtocolEnv) inl.peekEnv()).getTranslation());
			GProtocolBlock gb = AstFactoryImpl.FACTORY.GProtocolBlock(blame, gis);
			GRecursion inlined = AstFactoryImpl.FACTORY.GRecursion(blame, recvar, gb);
			inl.pushEnv(inl.popEnv().setTranslation(inlined));
			inl.removeSubprotocolRecVar(subsig);
		}	
		return (GDo) super.leaveProtocolInlining(parent, child, inl, visited);
	}

	@Override
	public void enterProjection(ScribNode parent, ScribNode child, Projector proj) throws ScribbleException
	{
		GSimpleInteractionNodeDel.super.enterProjection(parent, child, proj);

		GDo gd = (GDo) child;
		Role self = proj.peekSelf();
		if (gd.roles.getRoles().contains(self))
		{
			// For correct name mangling, need to use the parameter corresponding to the self argument
			// N.B. -- this depends on Projector not following the Subprotocol pattern, otherwise self is wrong
			Role param = gd.getTargetRoleParameter(proj.job.getContext(), proj.getModuleContext(), self);
			proj.pushSelf(param);
		}
		else
		{
			proj.pushSelf(self);  // Dummy: just to make pop in leave work
		}
	}
	
	@Override
	public GDo leaveProjection(ScribNode parent, ScribNode child, Projector proj, ScribNode visited) throws ScribbleException //throws ScribbleException
	{
		GDo gd = (GDo) visited;
		Role popped = proj.popSelf();
		Role self = proj.peekSelf();
		LDo projection = null;
		if (gd.roles.getRoles().contains(self))
		{
			ModuleContext mc = proj.getModuleContext();
			LProtocolNameNode target = Projector.makeProjectedFullNameNode(gd.proto.getSource(), gd.getTargetProtocolDeclFullName(mc), popped);
			projection = gd.project(self, target);
			
			// FIXME: do guarded recursive subprotocol checking (i.e. role is used during chain) in reachability checking? -- required role-usage makes local choice subject inference easier, but is restrictive (e.g. proto(A, B, C) { choice at A {A->B.do Proto(A,B,C)} or {A->B.B->C} }))
		}
		proj.pushEnv(proj.popEnv().setProjection(projection));
		return (GDo) GSimpleInteractionNodeDel.super.leaveProjection(parent, child, proj, gd);
	}
}
