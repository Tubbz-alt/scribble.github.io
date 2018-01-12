package org.scribble.parser.util;

import java.util.List;
import java.util.stream.Collectors;

import org.antlr.runtime.tree.CommonTree;
import org.scribble.parser.AntlrConstants;
import org.scribble.parser.AntlrConstants.AntlrNodeType;

public class ScribParserUtil
{
	public static List<CommonTree> toCommonTreeList(List<?> list)
	{
		return list.stream().map((x) -> (CommonTree) x).collect(Collectors.toList());
	}
	
	public static AntlrNodeType getAntlrNodeType(CommonTree n)
	{
		String type = n.getToken().getText();
		switch (type)
		{
			//case AntlrConstants.EMPTY_PARAMETERDECLLST_NODE_TYPE: return AntlrNodeType.EMPTY_PARAMETERDECLLST;
			case AntlrConstants.AMBIGUOUSNAME_NODE_TYPE: return AntlrNodeType.AMBIGUOUSNAME;
			case AntlrConstants.QUALIFIEDNAME_NODE_TYPE: return AntlrNodeType.QUALIFIEDNAME;

			case AntlrConstants.MESSAGESIGNATURE_NODE_TYPE: return AntlrNodeType.MESSAGESIGNATURE;
			case AntlrConstants.PAYLOAD_NODE_TYPE: return AntlrNodeType.PAYLOAD;
			//case AntlrConstants.PAYLOADELEMENT_NODE_TYPE: return AntlrNodeType.PAYLOADELEMENT;
			case AntlrConstants.DELEGATION_NODE_TYPE: return AntlrNodeType.DELEGATION;

			case AntlrConstants.MODULE_NODE_TYPE: return AntlrNodeType.MODULE;
			case AntlrConstants.MODULEDECL_NODE_TYPE: return AntlrNodeType.MODULEDECL;
			case AntlrConstants.IMPORTMODULE_NODE_TYPE: return AntlrNodeType.IMPORTMODULE;
			case AntlrConstants.IMPORTMEMBER_NODE_TYPE: return AntlrNodeType.IMPORTMEMBER;
			case AntlrConstants.PAYLOADTYPEDECL_NODE_TYPE: return AntlrNodeType.PAYLOADTYPEDECL;
			case AntlrConstants.MESSAGESIGNATUREDECL_NODE_TYPE: return AntlrNodeType.MESSAGESIGNATUREDECL;
			case AntlrConstants.ROLEDECLLIST_NODE_TYPE: return AntlrNodeType.ROLEDECLLIST;
			case AntlrConstants.ROLEDECL_NODE_TYPE: return AntlrNodeType.ROLEDECL;
			case AntlrConstants.PARAMETERDECLLIST_NODE_TYPE: return AntlrNodeType.PARAMETERDECLLIST; 
			case AntlrConstants.PARAMETERDECL_NODE_TYPE: return AntlrNodeType.PARAMETERDECL;
			case AntlrConstants.ROLEINSTANTIATIONLIST_NODE_TYPE: return AntlrNodeType.ROLEINSTANTIATIONLIST;
			case AntlrConstants.ROLEINSTANTIATION_NODE_TYPE: return AntlrNodeType.ROLEINSTANTIATION;
			case AntlrConstants.ARGUMENTINSTANTIATIONLIST_NODE_TYPE: return AntlrNodeType.ARGUMENTINSTANTIATIONLIST;
			case AntlrConstants.ARGUMENTINSTANTIATION_NODE_TYPE: return AntlrNodeType.ARGUMENTINSTANTIATION;

			case AntlrConstants.GLOBALPROTOCOLDECL_NODE_TYPE: return AntlrNodeType.GLOBALPROTOCOLDECL;
			case AntlrConstants.GLOBALPROTOCOLHEADER_NODE_TYPE: return AntlrNodeType.GLOBALPROTOCOLHEADER;
			case AntlrConstants.GLOBALPROTOCOLDEF_NODE_TYPE: return AntlrNodeType.GLOBALPROTOCOLDEF;
			case AntlrConstants.GLOBALPROTOCOLBLOCK_NODE_TYPE: return AntlrNodeType.GLOBALPROTOCOLBLOCK;
			case AntlrConstants.GLOBALINTERACTIONSEQUENCE_NODE_TYPE: return AntlrNodeType.GLOBALINTERACTIONSEQUENCE;
			case AntlrConstants.GLOBALMESSAGETRANSFER_NODE_TYPE: return AntlrNodeType.GLOBALMESSAGETRANSFER;
			case AntlrConstants.GLOBALCONNECT_NODE_TYPE: return AntlrNodeType.GLOBALCONNECT;
			case AntlrConstants.GLOBALWRAP_NODE_TYPE: return AntlrNodeType.GLOBALWRAP;
			case AntlrConstants.GLOBALDISCONNECT_NODE_TYPE: return AntlrNodeType.GLOBALDISCONNECT;
			case AntlrConstants.GLOBALCHOICE_NODE_TYPE: return AntlrNodeType.GLOBALCHOICE;
			case AntlrConstants.GLOBALRECURSION_NODE_TYPE: return AntlrNodeType.GLOBALRECURSION;
			case AntlrConstants.GLOBALCONTINUE_NODE_TYPE: return AntlrNodeType.GLOBALCONTINUE;
			case AntlrConstants.GLOBALPARALLEL_NODE_TYPE: return AntlrNodeType.GLOBALPARALLEL;
			case AntlrConstants.GLOBALINTERRUPTIBLE_NODE_TYPE: return AntlrNodeType.GLOBALINTERRUPTIBLE;
			case AntlrConstants.GLOBALINTERRUPT_NODE_TYPE: return AntlrNodeType.GLOBALINTERRUPT;
			case AntlrConstants.GLOBALDO_NODE_TYPE: return AntlrNodeType.GLOBALDO;

			case AntlrConstants.LOCALPROTOCOLDECL_NODE_TYPE: return AntlrNodeType.LOCALPROTOCOLDECL; 
			case AntlrConstants.LOCALROLEDECLLIST_NODE_TYPE: return AntlrNodeType.ROLEDECLLIST;
			case AntlrConstants.LOCALROLEDECL_NODE_TYPE: return AntlrNodeType.ROLEDECL;
			case AntlrConstants.SELFDECL_NODE_TYPE: return AntlrNodeType.SELFDECL;
			case AntlrConstants.LOCALPROTOCOLDEF_NODE_TYPE: return AntlrNodeType.LOCALPROTOCOLDEF;
			case AntlrConstants.LOCALPROTOCOLBLOCK_NODE_TYPE: return AntlrNodeType.LOCALPROTOCOLBLOCK;
			case AntlrConstants.LOCALINTERACTIONSEQUENCE_NODE_TYPE: return AntlrNodeType.LOCALINTERACTIONSEQUENCE;
			case AntlrConstants.LOCALSEND_NODE_TYPE: return AntlrNodeType.LOCALSEND;
			case AntlrConstants.LOCALRECEIVE_NODE_TYPE: return AntlrNodeType.LOCALRECEIVE;
			case AntlrConstants.LOCALCHOICE_NODE_TYPE: return AntlrNodeType.LOCALCHOICE;
			case AntlrConstants.LOCALRECURSION_NODE_TYPE: return AntlrNodeType.LOCALRECURSION;
			case AntlrConstants.LOCALCONTINUE_NODE_TYPE: return AntlrNodeType.LOCALCONTINUE;
			case AntlrConstants.LOCALPARALLEL_NODE_TYPE: return AntlrNodeType.LOCALPARALLEL;
			case AntlrConstants.LOCALINTERRUPTIBLE_NODE_TYPE: return AntlrNodeType.LOCALINTERRUPTIBLE;
			case AntlrConstants.LOCALTHROWS_NODE_TYPE: return AntlrNodeType.LOCALTHROWS;
			case AntlrConstants.LOCALCATCHES_NODE_TYPE: return AntlrNodeType.LOCALCATCHES;
			case AntlrConstants.LOCALDO_NODE_TYPE: return AntlrNodeType.LOCALDO;
			default:
			{
				// Nodes without a "node type", e.g. parameter names, fall in here
				throw new RuntimeException("Unknown ANTLR node type label: " + type);
			}
		}
	}
}
