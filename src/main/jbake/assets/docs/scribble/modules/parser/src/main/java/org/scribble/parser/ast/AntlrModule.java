package org.scribble.parser.ast;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.antlr.runtime.tree.CommonTree;
import org.scribble.ast.AstFactoryImpl;
import org.scribble.ast.ImportDecl;
import org.scribble.ast.Module;
import org.scribble.ast.ModuleDecl;
import org.scribble.ast.NonProtocolDecl;
import org.scribble.ast.ProtocolDecl;
import org.scribble.parser.AntlrConstants.AntlrNodeType;
import org.scribble.parser.ScribParser;
import org.scribble.parser.util.ScribParserUtil;
import org.scribble.util.ScribParserException;

public class AntlrModule
{
	public static final int MODULEDECL_CHILD_INDEX = 0;

	public static Module parseModule(ScribParser parser, CommonTree ct) throws ScribParserException
	{
		ModuleDecl md = (ModuleDecl) parser.parse(getModuleDeclChild(ct));
		List<ImportDecl<?>> ids = new LinkedList<>();
		List<NonProtocolDecl<?>> ptds = new LinkedList<>();
		List<ProtocolDecl<?>> pds = new LinkedList<>();
		for (CommonTree id : getImportDeclChildren(ct))
		{
			ImportDecl<?> tmp = (ImportDecl<?>) parser.parse(id);
			ids.add(tmp);
		}
		for (CommonTree ptd : getDataTypeDeclChildren(ct))
		{
			NonProtocolDecl<?> tmp = (NonProtocolDecl<?>) parser.parse(ptd);
			ptds.add(tmp);
		}
		for (CommonTree pd : getProtocolDeclChildren(ct))
		{
			ProtocolDecl<?> tmp = (ProtocolDecl<?>) parser.parse(pd);
			pds.add(tmp);
		}
		return AstFactoryImpl.FACTORY.Module(ct, md, ids, ptds, pds);
	}

	public static CommonTree getModuleDeclChild(CommonTree ct)
	{
		return (CommonTree) ct.getChild(MODULEDECL_CHILD_INDEX);
	}
	
	public static List<CommonTree> getImportDeclChildren(CommonTree ct)
	{
		return filterChildren(ct, AntlrNodeType.IMPORTMODULE, AntlrNodeType.IMPORTMEMBER);
	}

	public static List<CommonTree> getDataTypeDeclChildren(CommonTree ct)
	{
		return filterChildren(ct, AntlrNodeType.PAYLOADTYPEDECL, AntlrNodeType.MESSAGESIGNATUREDECL);
	}
	
	public static List<CommonTree> getProtocolDeclChildren(CommonTree ct)
	{
		return filterChildren(ct, AntlrNodeType.GLOBALPROTOCOLDECL, AntlrNodeType.LOCALPROTOCOLDECL);
	}

	private static List<CommonTree> filterChildren(CommonTree ct, AntlrNodeType... types)
	{
		List<AntlrNodeType> tmp = Arrays.asList(types);
		List<CommonTree> children = ScribParserUtil.toCommonTreeList(ct.getChildren());
		return children.subList(1, children.size()).stream()
				.filter((c) -> tmp.contains(ScribParserUtil.getAntlrNodeType(c))).collect(Collectors.toList());
	}
}
