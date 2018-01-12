package scribtest;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.scribble.cli.CommandLine;
import org.scribble.cli.CommandLineArgParser;
import org.scribble.cli.CommandLineException;
import org.scribble.main.ScribbleException;
import org.scribble.util.ScribParserException;

/**
 * Runs all tests under good and bad root directories in Scribble.
 */
//@RunWith(value = Parameterized.class)
@RunWith(Parameterized.class)
public class AllTest
{
	protected static final boolean GOOD_TEST = false;
	protected static final boolean BAD_TEST = !GOOD_TEST;

	protected final String example;
	protected final boolean isBadTest;

	// relative to cli/src/test/resources (or target/test-classes/)
	protected static final String ALL_ROOT = ".";
	protected static final String GOOD_ROOT = "good";
	protected static final String BAD_ROOT = "bad";

	public AllTest(String example, boolean isBadTest)
	{
		this.example = example;
		this.isBadTest = isBadTest;
	}

	@Parameters(name = "{0}")
	public static Collection<Object[]> data()
	{
		// Test all tests under good and bad root directories
		String dir_good = ClassLoader.getSystemResource(GoodTest.GOOD_ROOT).getFile();
		String dir_bad = ClassLoader.getSystemResource(BadTest.BAD_ROOT).getFile();
		List<Object[]> result = new LinkedList<>();
		result.addAll(Harness.makeTests(GOOD_TEST, dir_good));
		result.addAll(Harness.makeTests(BAD_TEST, dir_bad));
		return result;
	}

	@Test
	public void tests() throws IOException, InterruptedException, ExecutionException
	{
		try
		{
			// TODO: For now just locate classpath for resources -- later maybe directly execute job
			/*URL url = ClassLoader.getSystemResource(AllTest.GOOD_ROOT);  // Assume good/bad have same parent
			String dir = url.getFile().substring(0, url.getFile().length() - ("/" + AllTest.GOOD_ROOT).length());*/
			String dir = ClassLoader.getSystemResource(AllTest.ALL_ROOT).getFile();

			if (File.separator.equals("\\")) // HACK: Windows
			{
				dir = dir.substring(1).replace("/", "\\");
			}
			
			// FIXME: read runtime arguments from a config file, e.g. -oldwf, -fair, etc
			// Also need a way to specify expected tool output (e.g. projections/EFSMs for good, errors for bad)
			new CommandLine(this.example, CommandLineArgParser.JUNIT_FLAG, CommandLineArgParser.IMPORT_PATH_FLAG, dir).run();
					// Added JUNIT flag -- but for some reason only bad DoArgList01.scr was breaking without it...
			Assert.assertFalse("Expecting exception", this.isBadTest);
		}
		catch (ScribbleException e)
		{
			Assert.assertTrue("Unexpected exception '" + e.getMessage() + "'", this.isBadTest);
		}
		catch (ScribParserException | CommandLineException e)
		{
			throw new RuntimeException(e);
		}
	}
}
