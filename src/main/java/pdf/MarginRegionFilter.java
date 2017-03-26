package pdf;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.parser.RegionTextRenderFilter;

public class MarginRegionFilter {
	
	public static RegionTextRenderFilter create(Rectangle pageSizeRectange, int marginTop, int marginBottom) {
		Rectangle contentRectangle = pageSizeRectange;
		System.out.println("Page Size: " + contentRectangle);
		contentRectangle.setTop(contentRectangle.getTop() - marginTop);
		contentRectangle.setBottom(marginBottom);
		System.out.println("RegionFilter for rectangle: " + contentRectangle);
		return new RegionTextRenderFilter(contentRectangle);
	}
}
