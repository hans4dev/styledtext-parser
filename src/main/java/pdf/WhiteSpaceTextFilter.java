package pdf;

import com.itextpdf.text.pdf.parser.LineSegment;
import com.itextpdf.text.pdf.parser.TextRenderInfo;
import com.itextpdf.text.pdf.parser.Vector;

public class WhiteSpaceTextFilter {
	private Vector start;
	private Vector end;
	private Vector lastStart;
    private Vector lastEnd;
	private float halfSpaceWidth;
	private String text;

    public WhiteSpaceTextFilter() {
		// TODO Auto-generated constructor stub
	}
    
    public WhiteSpaceTextFilter(TextRenderInfo renderInfo) {
    	updateRenderInfo(renderInfo);
    }
    
    public boolean isHardReturn() {
        if (lastStart != null && lastEnd != null) {
        	return TextRenderUtil.isHardReturn(start, lastStart, lastEnd);
        }
        
        return false;
    }

	public boolean isImpliedSpacing() {
        if (lastEnd == null || start == null) {
        	return false;
        }
        
		float spacing = lastEnd.subtract(start).length();
		
        return (spacing > halfSpaceWidth);
	}

    public boolean isExplicitSpace() {
		return text.length() > 0 && text.charAt(0) == ' ';
	}

	public Vector getStart() {
		return start;
	}

	public void setStart(Vector start) {
		this.start = start;
	}

	public Vector getEnd() {
		return end;
	}

	public void setEnd(Vector end) {
		this.end = end;
	}

	public Vector getLastStart() {
		return lastStart;
	}

	public void setLastStart(Vector lastStart) {
		this.lastStart = lastStart;
	}

	public Vector getLastEnd() {
		return lastEnd;
	}

	public void setLastEnd(Vector lastEnd) {
		this.lastEnd = lastEnd;
	}

	public void updateRenderInfo(TextRenderInfo renderInfo) {
		lastStart = start;
		lastEnd = end;

		LineSegment segment = renderInfo.getBaseline();
        start = segment.getStartPoint();
        end = segment.getEndPoint();
        
        halfSpaceWidth = renderInfo.getSingleSpaceWidth()/2f;
        text = renderInfo.getText();
	}
    
}
