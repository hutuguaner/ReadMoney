package htg;

import java.util.Arrays;
import java.util.List;

import htg.dftt.DfttTaskThread;
import htg.htt.HttTaskThread;
import htg.sb.SbTaskThread;
import htg.shzx.ShzxTaskThread;

public class Commons {

	// ³ÌÐò°üÃû
	public static final List<String> PACKAGENAMES = Arrays.asList(HttTaskThread.PACKAGENAME, DfttTaskThread.PACKAGENAME,
			SbTaskThread.PACKAGENAME, ShzxTaskThread.PACKAGENAME);

}
