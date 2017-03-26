package pdf;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.parser.TextRenderInfo;
import com.itextpdf.text.pdf.parser.Vector;

public class TextRenderUtil {

	public static boolean isHardReturn(Vector start, Vector lastStart, Vector lastEnd) {
		Vector x0 = start;
		Vector x1 = lastStart;
		Vector x2 = lastEnd;
		
		// see http://mathworld.wolfram.com/Point-LineDistance2-Dimensional.html
		float dist = (x2.subtract(x1)).cross((x1.subtract(x0))).lengthSquared() / x2.subtract(x1).lengthSquared();

		float sameLineThreshold = 1f; // we should probably base this on the current font metrics, but 1 pt seems to be sufficient for the time being
		if (dist > sameLineThreshold)
		    return true;
		
		// Note:  Technically, we should check both the start and end positions, in case the angle of the text changed without any displacement
		// but this sort of thing probably doesn't happen much in reality, so we'll leave it alone for now
		return false;
	}
    
	public static float getFontSize(TextRenderInfo renderInfo) {
		float ascentStartPoint = renderInfo.getAscentLine().getStartPoint().get(1);
		float descentStartPoint = renderInfo.getDescentLine().getStartPoint().get(1);
		float fontSize = (ascentStartPoint - descentStartPoint);
		
		return fontSize;
	}
	
	public static float getFontSizeRectangular(TextRenderInfo renderInfo) {
		Vector curBaseline = renderInfo.getBaseline().getStartPoint();
		Vector topRight = renderInfo.getAscentLine().getEndPoint();

		Rectangle rect = new Rectangle(curBaseline.get(0), curBaseline.get(1), topRight.get(0), topRight.get(1));
		float fontSize = rect.getHeight();
	
		return fontSize;
}
}
