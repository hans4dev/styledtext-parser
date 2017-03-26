/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2012 1T3XT BVBA
 * Authors: Kevin Day, Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */
package pdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.RegionTextRenderFilter;
import com.itextpdf.text.pdf.parser.RenderFilter;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextRenderInfo;


/**
 * A simple text extraction renderer.
 * 
 * This renderer keeps track of the current Y position of each string.  If it detects
 * that the y position has changed, it inserts a line break into the output.  If the
 * PDF renders text in a non-top-to-bottom fashion, this will result in the text not
 * being a true representation of how it appears in the PDF.
 * 
 * This renderer also uses a simple strategy based on the font metrics to determine if
 * a blank space should be inserted into the output.
 * 
 * @since	2.1.5
 */
public class StyledTextExtractionStrategy implements TextExtractionStrategy {
    
    /** used to store the resulting String. */
    private final StringBuffer result = new StringBuffer();
    private WhiteSpaceTextFilter whiteSpaceFilter;
    
    private RenderFilter renderFilter;
    protected StyledText currentStyledText;
    protected List<StyledText> styledTexts;

    /**
     * Creates a new text extraction renderer.
     */
    public StyledTextExtractionStrategy() {
    	whiteSpaceFilter = new WhiteSpaceTextFilter();
    	currentStyledText = new StyledText();
    	styledTexts = new ArrayList<StyledText>();
    }

    public StyledTextExtractionStrategy(RenderFilter renderFilter) {
		this();
    	this.renderFilter = renderFilter;
	}

	/**
     * @since 5.0.1
     */
    public void beginTextBlock() {
    }

    /**
     * @since 5.0.1
     */
    public void endTextBlock() {
    	endTextLine();
    }
    
    /**
     * Returns the result so far.
     * @return	a String with the resulting text.
     */
    public String getResultantText(){
        return result.toString();
    }

    private List<StyledText> getStyledTexts() {		
    	return styledTexts;
	}

	/**
     * Captures text using a simplified algorithm for inserting hard returns and spaces
     * @param	renderInfo	render info
     */
    public void renderText(TextRenderInfo renderInfo) {
    	// if renderFilter not passed return immediately
    	if (renderFilter != null && !renderFilter.allowText(renderInfo)) {
    		return;
    	}
    	
    	// if next is newLine call endTextLine
        whiteSpaceFilter.updateRenderInfo( renderInfo );
        if (whiteSpaceFilter.isHardReturn()) {
        	endTextLine();
        }
        
        // if next style is not same to previous style call endTextStyle
    	if (!currentStyledText.getStyle().equals(new TextStyle(renderInfo))) {
    		endTextStyle();
    	}
    	
    	
    	currentStyledText.getStyle().updateRenderInfo( renderInfo );
    	currentStyledText.getStyle().setHardReturn( whiteSpaceFilter.isHardReturn() );        
        
        if (whiteSpaceFilter.isHardReturn()){
            result.append('\n');
            startTextLine();
        } else if (!isFirstRender()){ 
        	// we only insert a blank space if
        	// * the trailing character of the previous string wasn't a space
        	// * the leading character of the current string isn't a space
            if (!isPreviousSpace() && !whiteSpaceFilter.isExplicitSpace()){ 
                if (whiteSpaceFilter.isImpliedSpacing()){
                    //System.out.println("Inserting implied space before '" + renderInfo.getText() + "'");
                    result.append(' ');
                    currentStyledText.appendText(' ');
                } // if implicit spacing detected
            } // if no previous or current explicit space
        } else {
            //System.out.println("Displaying first string of content '" + text + "' :: x1 = " + x1);
        }
        
        //System.out.println("[" + renderInfo.getStartPoint() + "]->[" + renderInfo.getEndPoint() + "] " + renderInfo.getText());
        result.append(renderInfo.getText());
        currentStyledText.appendText(renderInfo.getText());        
    }
    
	private boolean isFirstRender() {
    	return result.length() == 0;
    }
    
    private boolean isPreviousSpace() {
    	return result.length() > 0 && result.charAt(result.length()-1) == ' ';
    }
    

    /**
     * no-op method - this renderer isn't interested in image events
     * @see com.itextpdf.text.pdf.parser.RenderListener#renderImage(com.itextpdf.text.pdf.parser.ImageRenderInfo)
     * @since 5.0.1
     */
    public void renderImage(ImageRenderInfo renderInfo) {
        // do nothing - we aren't tracking images in this renderer
    }
    
    
	
    protected void startTextStyle() {
	}

	protected void endTextStyle() {
		//styledText.println(System.out);
		if (currentStyledText.getText().length() > 0) {
			styledTexts.add(currentStyledText);
			currentStyledText = new StyledText();
		}
	}

	protected void startTextLine() {
	}

	protected void endTextLine() {
		//styledText.println(System.out);
		if (currentStyledText.getText().length() > 0) {
			styledTexts.add(currentStyledText);
			currentStyledText = new StyledText();
		}
	}


	public static void main(String[] args) throws IOException {
		String pdfPath = "src/main/resources/Change_Log_2_3.pdf";
		int pageNum = 1;
		PdfReader reader = new PdfReader( pdfPath );
		PdfReaderContentParser parser = new PdfReaderContentParser(reader);
		RegionTextRenderFilter regionTextRenderFilter = MarginRegionFilter.create(reader.getPageSize(1), 95, 65);
		
		StyledTextExtractionStrategy extractionStrategy = new StyledTextExtractionStrategy(regionTextRenderFilter);
		extractionStrategy = parser.processContent(pageNum, extractionStrategy);
		List<StyledText> styledTexts = extractionStrategy.getStyledTexts();
		
		System.out.println("Found texts: " + styledTexts.size());
		for (StyledText t : styledTexts) {
			t.println(System.out);
		}
		
	}

}