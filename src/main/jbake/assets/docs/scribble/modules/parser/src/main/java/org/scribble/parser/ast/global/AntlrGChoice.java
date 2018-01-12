package org.scribble.parser.ast.global;

import java.util.LinkedList;
import java.util.List;

import org.antlr.runtime.tree.CommonTree;
import org.scribble.ast.AstFactoryImpl;
import org.scribble.ast.global.GChoice;
import org.scribble.ast.global.GProtocolBlock;
import org.scribble.ast.name.simple.RoleNode;
import org.scribble.parser.ScribParser;
import org.scribble.parser.ast.name.AntlrSimpleName;
import org.scribble.parser.util.ScribParserUtil;
import org.scribble.util.ScribParserException;

public class AntlrGChoice
{
	public static final int SUBJECT_CHILD_INDEX = 0;
	public static final int BLOCK_CHILDREN_START_INDEX = 1;
	
	public static GChoice parseGChoice(ScribParser parser, CommonTree ct) throws ScribParserException
	{
		RoleNode subj = AntlrSimpleName.toRoleNode(getSubjectChild(ct));
		/*List<GProtocolBlock> blocks =
				getBlockChildren(ct).stream().map((b) -> (GProtocolBlock) parser.parse(b)).collect(Collectors.toList());*/
		List<GProtocolBlock> blocks = new LinkedList<>();
		for (CommonTree b : getBlockChildren(ct))
		{
			blocks.add((GProtocolBlock) parser.parse(b));
		}
		return AstFactoryImpl.FACTORY.GChoice(ct, subj, blocks);
	}

	public static CommonTree getSubjectChild(CommonTree ct)
	{
		return (CommonTree) ct.getChild(SUBJECT_CHILD_INDEX);
	}

	public static List<CommonTree> getBlockChildren(CommonTree ct)
	{
		List<?> children = ct.getChildren();
		return ScribParserUtil.toCommonTreeList(children.subList(BLOCK_CHILDREN_START_INDEX, children.size()));
	}
}
